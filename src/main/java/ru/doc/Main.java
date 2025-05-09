package ru.doc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.doc.config.AppConfig;
import ru.doc.gui.DocumentEditorGUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        Runtime.getRuntime().addShutdownHook(new Thread(ctx::close));
        SwingUtilities.invokeLater(() -> ctx.getBean(DocumentEditorGUI.class));
    }
}
