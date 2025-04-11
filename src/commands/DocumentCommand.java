package commands;

public interface DocumentCommand {
    void execute();
    void undo();
}
