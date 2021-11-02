package com.example.common.myAspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LogAop {

//    @Around("@annotation(com.example.common.annotation.JBasicLog)")
    public Object around(ProceedingJoinPoint joinPoint)throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("{}调用方法:"+signature.getName(), "JLog");
        return joinPoint.proceed();
    }
}
