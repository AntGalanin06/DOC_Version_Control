package core.commands;

public interface DocumentCommand {
    void execute();
    void undo();
}