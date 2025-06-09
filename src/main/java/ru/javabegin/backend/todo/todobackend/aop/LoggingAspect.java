package ru.javabegin.backend.todo.todobackend.aop;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Log


public class LoggingAspect {


    @Around("execution(* ru.javabegin.backend.todo.controller..*(..)))")
    public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {


        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();


        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        log.info("-------- Executing "+ className + "." + methodName + "   ----------- ");

        StopWatch countdown = new StopWatch();


        countdown.start();
        Object result = proceedingJoinPoint.proceed(); // выполняем сам метод
        countdown.stop();

        log.info("-------- Execution time of " + className + "." + methodName + " :: " + countdown.getTotalTimeMillis() + " ms");

        return result;
    }

}
