package appointmentscheduler.aspects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

@Component
@Aspect
public class LoggingAspect {

    private ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping() {}

    @Before(value = "@within(appointmentscheduler.annotation.LogREST) || @annotation(appointmentscheduler.annotation.LogREST)")
    public void loggerWarn(final JoinPoint jp) throws Throwable {
        final Logger logger = getLoggerForClass(jp);
        logger.warn(buildLogging(jp));
    }


    @Around("requestMapping()")
    public void logApiCall(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        final Logger logger = getLoggerForClass(proceedingJoinPoint);
        logger.debug("{} {} from {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());
    }

    private Logger getLoggerForClass(final JoinPoint jp) {
        return this.iLoggerFactory.getLogger(jp.getSignature()
                .getDeclaringType()
                .getName());
    }

    private String buildLogging(final JoinPoint jp) {
        final StringBuilder message = new StringBuilder();
        message.append("Method Invocation=[")
                .append(jp.getSignature()
                        .getName())
                .append("]")
                .append(" - Args=")
                .append(Arrays.toString(jp.getArgs()));
        return message.toString();
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

    @Pointcut("execution(* *.*(..))")
    protected void allMethod() {
    }

    @Pointcut("execution(public * *(..))")
    protected void loggingPublicOperation() {
    }

    @Pointcut("execution(* *.*(..))")
    protected void loggingAllOperation() {
    }



    //before -> Any resource annotated with @Controller annotation
    //and all method and function taking HttpServletRequest as first parameter
    @Before("controller() && allMethod() && args(..,request)")
    public void logBefore(JoinPoint joinPoint, HttpServletRequest request) {

        log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
        log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        log.debug("Target class : " + joinPoint.getTarget().getClass().getName());

        if (null != request) {
            log.debug("Start Header Section of request ");
            log.debug("Method Type : " + request.getMethod());
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String)headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.debug("Header Name: " + headerName + " Header Value : " + headerValue);
            }
            log.debug("Request Path info :" + request.getServletPath());
            log.debug("End Header Section of request ");
        }
    }
    //After -> All method within resource annotated with @Controller annotation
    // and return a  value
    @AfterReturning(pointcut = "controller() && allMethod()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);
        log.debug("Method Return value : " + returnValue);
    }
    //After -> Any method within resource annotated with @Controller annotation
    // throws an exception ...Log it
    @AfterThrowing(pointcut = "controller() && allMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
        log.error("Cause : " + exception.getCause());
    }
    //Around -> Any method within resource annotated with @Controller annotation
    @Around("controller() && allMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            log.debug("Method " + className + "." + methodName + " ()" + " execution time : "
                    + elapsedTime + " ms");

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }
    private String getValue(Object result) {
        String returnValue = null;
        if (null != result) {
            if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
                returnValue = ReflectionToStringBuilder.toString(result);
            } else {
                returnValue = result.toString();
            }
        }
        return returnValue;
    }

}
