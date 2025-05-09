package ru.doc.memento;

import java.io.Serializable;
import java.util.Map;

public interface DocumentMemento extends Serializable {
    Map<String, Object> getState();
}