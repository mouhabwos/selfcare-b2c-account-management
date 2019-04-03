package sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.security.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mbbsow
 */
@Component
@Aspect
public class AuditInterceptor {

    private static final Logger LOGGER = Logger.getLogger ( AuditInterceptor.class.getName () );


    @Autowired
    ILogger fileLogger;

    public AuditInterceptor() {
        super ();
    }

    @Around("execution(* *(..)) && @annotation(sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable)")
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] params = joinPoint.getArgs ();

        final String methodName = joinPoint.getSignature ().getName ();
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature ();
        Method method = methodSignature.getMethod ();
        method = joinPoint.getTarget ().getClass ()
            .getDeclaredMethod ( methodName, method.getParameterTypes () );
        Auditable annotation = method.getAnnotation ( Auditable.class );
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes ()).getRequest ();
        String ipAddress = "";
        String browser = "";

        if (request != null) {
            browser = request.getHeader ( "User-Agent" );
            ipAddress = request.getHeader ( "X-FORWARDED-FOR" );
            if (ipAddress == null || ipAddress.trim ().isEmpty ()) {
                ipAddress = request.getRemoteAddr ();
                ipAddress = ipAddress.contains ( "," ) ? ipAddress.split ( "," )[0] : ipAddress;
            }
        }

        SelfcareLog log = new SelfcareLog ();
        log.setDescription ( "Debut : " + annotation.description () );
        log.setIp ( ipAddress );
        log.setParams ( params );
        log.setBrowser ( browser );
        log.setUserName ( SecurityUtils.getCurrentUserLogin ().toString ().replace ( "Optional", "" ) );
        log.setPerimetre ( "Selfcare B2C Account-Management" );
        log.setTypeAction ( "Audit" );
        fileLogger.log ( log );


        Object result = joinPoint.proceed ();
        try {
           /* final String methodName = joinPoint.getSignature().getName();
            final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            method = joinPoint.getTarget().getClass()
                    .getDeclaredMethod(methodName, method.getParameterTypes());
            Auditable annotation = method.getAnnotation(Auditable.class);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String ipAddress = "";
             String browser = "";*/
            if (request != null) {
                browser = request.getHeader ( "User-Agent" );
                ipAddress = request.getHeader ( "X-FORWARDED-FOR" );
                if (ipAddress == null || ipAddress.trim ().isEmpty ()) {
                    ipAddress = request.getRemoteAddr ();
                    ipAddress = ipAddress.contains ( "," ) ? ipAddress.split ( "," )[0] : ipAddress;
                }
            }

            SelfcareLog logss = new SelfcareLog ();
            logss.setDescription ( " Fin : " + annotation.description () );
            logss.setIp ( ipAddress );
            logss.setParams ( params );
            logss.setBrowser ( browser );
            logss.setUserName ( SecurityUtils.getCurrentUserLogin ().toString ().replace ( "Optional", "" ) );
            logss.setPerimetre ( "Selfcare B2C Account-Management" );
            logss.setTypeAction ( "Audit" );
            fileLogger.log ( logss );
        } catch (IllegalStateException e) {
            LOGGER.log ( Level.INFO, "---------{ advice audit Exception }---------{0}", e );
        }
        return result;
    }

    @AfterThrowing(pointcut = " @annotation(sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable)", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {


        Object[] params = joinPoint.getArgs ();

        try {
            final String methodName = joinPoint.getSignature ().getName ();
            final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature ();
            Method method = methodSignature.getMethod ();
            method = joinPoint.getTarget ().getClass ()
                .getDeclaredMethod ( methodName, method.getParameterTypes () );
            Auditable annotation = method.getAnnotation ( Auditable.class );
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes ()).getRequest ();
            String ipAddress = "";
            String browser = "";
            if (request != null) {
                browser = request.getHeader ( "User-Agent" );
                ipAddress = request.getHeader ( "X-FORWARDED-FOR" );
                if (ipAddress == null || ipAddress.trim ().isEmpty ()) {
                    ipAddress = request.getRemoteAddr ();
                    ipAddress = ipAddress.contains ( "," ) ? ipAddress.split ( "," )[0] : ipAddress;
                }
            }

            SelfcareLogException selfcareLogException = new SelfcareLogException ();
            selfcareLogException.setCause ( e.getCause () != null ? e.getCause ().toString () : "NULL" );
            selfcareLogException.setMessage ( e.getMessage () );
            selfcareLogException.setNomMethode ( joinPoint.getSignature ().getName () );
            selfcareLogException.setType ( joinPoint.getSignature ().getDeclaringTypeName () );

            SelfcareLog logs = new SelfcareLog ();
            logs.setDescription ( annotation.description () );
            logs.setIp ( ipAddress );
            logs.setParams ( params );
            logs.setBrowser ( browser );
            logs.setUserName ( SecurityUtils.getCurrentUserLogin ().toString ().replace ( "Optional", "" ) );
            fileLogger.log ( logs );
        } catch (Exception ex) {
            LOGGER.log ( Level.INFO, "---------{ advice audit Exception }---------{0}", ex );
        }

    }
}
