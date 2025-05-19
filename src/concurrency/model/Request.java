package concurrency.model;

import java.util.concurrent.atomic.AtomicLong;

public class Request {
    private static final AtomicLong SEQ = new AtomicLong();

    private final long id;
    private final RequestType type;
    private final String content;
    private final long createdAt;

    public Request(RequestType type, String content) {
        this.id = SEQ.incrementAndGet();
        this.type = type;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
    }

    public long getId()         { return id; }
    public RequestType getType(){ return type; }
    public String getContent()  { return content; }
    public long getCreatedAt()  { return createdAt; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Request r && r.id == id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}