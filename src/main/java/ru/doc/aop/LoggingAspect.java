package ru.doc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.doc.logging.LogCollector;
import ru.doc.logging.Loggable;

import java.lang.reflect.Method;

@Aspect
@Component
public class LoggingAspect {

    private final LogCollector logCollector;

    public LoggingAspect(LogCollector logCollector) {
        this.logCollector = logCollector;
    }

    @Around("@annotation(ru.doc.logging.Loggable) || @within(ru.doc.logging.Loggable)")
    public Object aroundLoggableMethod(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        Loggable loggable = method.getAnnotation(Loggable.class);
        if (loggable == null) {
            loggable = method.getDeclaringClass().getAnnotation(Loggable.class);
        }
        if (loggable == null) {
            return pjp.proceed();
        }

        LogCollector.Category category = loggable.category();
        LogCollector.Level level = loggable.level();
        boolean withArgs = loggable.withArgs();
        boolean withResult = loggable.withResult();
        boolean withTime = loggable.withTime();
        String description = loggable.description();

        String methodName = signature.toShortString();
        String message = description.isEmpty() ? methodName : description + " (" + methodName + ")";

        if (withArgs && pjp.getArgs().length > 0) {
            StringBuilder args = new StringBuilder();
            boolean first = true;
            for (Object arg : pjp.getArgs()) {
                if (!first) args.append(", ");
                first = false;
                args.append(arg != null ? arg.toString() : "null");
            }
            message += " args=[" + args + "]";
        }

        logCollector.add(level, category, LogCollector.OperationType.CALL, message);

        long start = System.nanoTime();
        try {
            Object result = pjp.proceed();
            if (withResult && result != null) {
                message += " result=" + result;
            }
            return result;
        } catch (Throwable t) {
            logCollector.add(LogCollector.Level.ERROR, category, LogCollector.OperationType.ERROR,
                    message + " error=" + t.getMessage());
            throw t;
        } finally {
            long elapsed = System.nanoTime() - start;
            if (withTime) {
                message += " time=" + elapsed / 1_000_000 + "ms";
            }
            logCollector.add(level, category, LogCollector.OperationType.RETURN, message);
        }
    }
}
