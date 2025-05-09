package ru.doc.commands;

public interface DocumentCommand {
    void execute();
    void undo();
}