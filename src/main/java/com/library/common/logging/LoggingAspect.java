package com.library.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.library.domain.book.BookService.*(..))")
    public void bookServiceMethods() {
    }

    @Pointcut("execution(* com.library.domain.patron.PatronService.*(..))")
    public void patronServiceMethods() {
    }

    @Pointcut("execution(* com.library.domain.borrowing.BorrowingService.*(..))")
    public void borrowingServiceMethods() {
    }

    @Pointcut("bookServiceMethods() || patronServiceMethods() || borrowingServiceMethods()")
    public void applicationServiceMethods() {
    }

    @Around("bookServiceMethods() || patronServiceMethods() || borrowingServiceMethods()")
    public Object logAndMeasurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String arguments = Arrays.stream(joinPoint.getArgs()).map(
                Object::toString).collect(Collectors.joining(", "));

        log.info("Starting method: {} with arguments: [{}]", methodName, arguments);

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("Method: {} executed in {} ms with result: {}", methodName, elapsedTime, result);
            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("Exception in method: {} after: {} ms {}", methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }

    @AfterReturning(pointcut = "applicationServiceMethods()", returning = "result")
    public void logAfterReturning(Object result) {
        log.info("Method completed successfully with result: {}", result);
    }

    @AfterThrowing(pointcut = "applicationServiceMethods()", throwing = "ex")
    public void logException(Exception ex) {
        log.error("Exception thrown: {} ", ex.getMessage(), ex);
    }
}
