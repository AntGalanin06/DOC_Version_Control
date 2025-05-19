package concurrency.server;

import concurrency.model.Request;
import concurrency.model.RequestType;
import concurrency.queue.RequestQueue;
import concurrency.monitor.StatsAggregator;
import core.commands.*;
import core.commands.DocumentCommand;
import core.documents.Document;
import core.memento.DocumentHistoryLogger;

public class Worker implements Runnable {

    private final int id;
    private final RequestQueue q;
    private final Document doc;
    private final DocumentHistoryLogger hist;
    private final StatsAggregator agg;

    public Worker(int id, RequestQueue q, Document doc,
                  DocumentHistoryLogger h, StatsAggregator a) {
        this.id = id;
        this.q = q;
        this.doc = doc;
        this.hist = h;
        this.agg = a;
    }

    @Override
    public void run() {
        var stat = agg.worker(id);
        boolean shutdownReceived = false;
        while (true) {
            try {
                Request r = q.take();
                stat.markBusy();
                if (r.getType() == RequestType.SHUTDOWN) {
                    shutdownReceived = true;
                    if (q.size() == 0) break;
                    else continue;
                }
                switch (r.getType()) {
                    case CHANGE_TEXT -> new ChangeTextCommand(doc, r.getContent()).execute();
                    case UNDO        -> new UndoCommand(doc, hist).execute();
                    case REDO        -> new RedoCommand(doc, hist).execute();
                    case REVERT      -> new RevertCommand(doc, hist).execute();
                }
                stat.incConsumed();
                agg.incConsumedTotal();
                if (shutdownReceived && q.size() == 0) break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}