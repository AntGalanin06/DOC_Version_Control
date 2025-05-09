package ru.doc.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    LogCollector.Category category() default LogCollector.Category.DOCUMENT;
    LogCollector.Level level() default LogCollector.Level.INFO;
    boolean withArgs() default false;
    boolean withResult() default false;
    boolean withTime() default true;
    String description() default "";
}