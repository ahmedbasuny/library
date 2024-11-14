package com.library.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        String user = getLoggedInUser();

        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String arguments = Arrays.stream(joinPoint.getArgs()).map(
                Object::toString).collect(Collectors.joining(", "));

        log.info("User: [{}] - Starting method: {} with arguments: [{}]", user, methodName, arguments);

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("User: [{}] - Method: {} executed in {} ms with result: {}", user, methodName, elapsedTime, result);
            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("User: [{}] Exception in method: {} after: {} ms {}", user, methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }

    @AfterReturning(pointcut = "applicationServiceMethods()", returning = "result")
    public void logAfterReturning(Object result) {
        String user = getLoggedInUser();
        log.info("User: [{}] - Method completed successfully with result: {}", user, result);
    }

    @AfterThrowing(pointcut = "applicationServiceMethods()", throwing = "ex")
    public void logException(Exception ex) {
        String user = getLoggedInUser();
        log.error("User: [{}] - Exception thrown: {} ", user, ex.getMessage(), ex);
    }

    private String getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Anonymous";
    }
}
