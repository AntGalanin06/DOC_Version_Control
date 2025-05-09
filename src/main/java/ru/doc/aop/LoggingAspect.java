package ru.doc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

    @Pointcut("execution(* ru.doc.document..*(..)) || execution(* ru.doc.factory..*(..)) || execution(* ru.doc.commands..*(..))")
    public void applicationPointcut() {}

    @Pointcut("@annotation(ru.doc.logging.Loggable)")
    public void loggableMethod() {}

    @Pointcut("@within(ru.doc.logging.Loggable)")
    public void loggableClass() {}

    @Around("loggableMethod() || loggableClass()")
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
                if (!first) {
                    args.append(", ");
                }
                first = false;
                try {
                    args.append(arg != null ? arg.toString() : "null");
                } catch (Exception e) {
                    args.append("[Error getting argument]");
                }
            }
            message += " Args: [" + args + "]";
        }

        logCollector.add(level, category, LogCollector.OperationType.CALL, message);
        long startTime = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;

            String returnMessage = message;
            if (withTime) {
                returnMessage += " Time: " + elapsedTime + "ms";
            }

            if (withResult && result != null) {
                try {
                    returnMessage += " Result: [" + result + "]";
                } catch (Exception e) {
                    returnMessage += " Result: [Error getting result]";
                }
            }

            logCollector.add(level, category, LogCollector.OperationType.RETURN, returnMessage);
            return result;

        } catch (Throwable ex) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            String errorMessage = message + " Exception: " + ex.getClass().getSimpleName();

            if (withTime) {
                errorMessage += " Time: " + elapsedTime + "ms";
            }

            logCollector.add(LogCollector.Level.ERROR, category, LogCollector.OperationType.ERROR, errorMessage);
            throw ex;
        }
    }

    @Around("applicationPointcut() && !(loggableMethod() || loggableClass())")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {
        String sig = pjp.getSignature().toShortString();
        logCollector.add(LogCollector.Level.INFO, "CALL " + sig);

        try {
            Object res = pjp.proceed();
            logCollector.add(LogCollector.Level.INFO, "RET  " + sig);
            return res;
        } catch (Throwable ex) {
            logCollector.add(LogCollector.Level.ERROR, "THRW " + sig + " " + ex.getClass().getSimpleName());
            throw ex;
        }
    }
}