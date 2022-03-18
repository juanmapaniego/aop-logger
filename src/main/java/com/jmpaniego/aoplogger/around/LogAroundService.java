package com.jmpaniego.aoplogger.around;

import com.jmpaniego.aoplogger.Level;
import com.jmpaniego.aoplogger.util.LoggerUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.Duration;

public class LogAroundService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAroundService.class);

  public Object logAround(final ProceedingJoinPoint joinPoint, final LogAround logAround)
          throws Throwable {
    final long enteringTime = System.nanoTime();

    final Logger logger = LoggerUtil.getLogger(joinPoint);
    logEnteringMessage(joinPoint, logAround, logger);
    try {
      final long beforeProceedTime = System.nanoTime();
      final Object returnValue = joinPoint.proceed();

      final long proceedElapsedTime = System.nanoTime() - beforeProceedTime;
      logExitedMessage(joinPoint, logAround, logger, returnValue);
      //logElapsedTime(joinPoint, logAround, logger, proceedElapsedTime);

      return returnValue;
    }catch (Throwable e){
      logExitedAbnormallyMessage(joinPoint, logAround, logger, e);
      throw e;
    }
  }

  private void logExitedAbnormallyMessage(ProceedingJoinPoint joinPoint, LogAround logAround, Logger logger, Throwable e) {
    final Level exitedAbnormallyLevel = logAround.errorLevel();
    if (isLoggerLevelDisabled(logger, exitedAbnormallyLevel)) {
      return;
    }
    LoggerUtil.log(logger, exitedAbnormallyLevel, e.getMessage());
  }

  private void logElapsed(final long enteringTime, final long proceedElapsedTime) {
    LOGGER.debug(
            "[logAround] elapsed [{}]",
            Duration.ofNanos(System.nanoTime() - enteringTime - proceedElapsedTime));
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LoggerUtil.isEnabled(logger, level) == false;
  }

  private boolean isIgnoredException(
          final Throwable exception, final Class<? extends Throwable>[] ignoredExceptions) {
    if (exception == null) {
      return true;
    }

    return matchesIgnoreExceptions(exception, ignoredExceptions);
    //|| matchesIgnoreExceptions(exception, aopLoggerProperties.getIgnoreExceptions());
  }

  private boolean matchesIgnoreExceptions(
          final Throwable exception, final Class<? extends Throwable>[] ignoredExceptions) {
    if (ignoredExceptions == null || ignoredExceptions.length == 0) {
      return false;
    }
    for (Class<? extends Throwable> ignoredException : ignoredExceptions) {
      if (ignoredException == null) {
        continue;
      }
      if (ignoredException.isInstance(exception)) {
        return true;
      }
    }
    return false;
  }

  private void logEnteringMessage(
          final ProceedingJoinPoint joinPoint,
          final LogAround annotation,
          final Logger logger) {
    final Level enteringLevel = annotation.level();
    //getLevel(annotation.level(), aopLoggersProperties.getEnteringLevel());

    if (isLoggerLevelDisabled(logger, enteringLevel)) {
      return;
    }

    //joinPointStringSupplierRegistrar.register(stringLookup, joinPoint);

    /*final String enteringMessage =
        stringSubstitutor.substitute(
            getMessage(annotation.enteringMessage(), aopLoggersProperties.getEnteringMessage()),
            stringLookup);*/
    LoggerUtil.log(logger, enteringLevel, annotation.enteringMessage() + " " + methodParameters(joinPoint));
  }

  private void logElapsedTime(
          final ProceedingJoinPoint joinPoint,
          final LogAround annotation,
          final Logger logger,
          final long elapsedTime) {
    final Level elapsedLevel = annotation.level();// getLevel(annotation.level(), aopLoggersProperties.getElapsedLevel());
    if (isLoggerLevelDisabled(logger, elapsedLevel)) {
      return;
    }


    /*final String elapsedMessage =
        stringSubstitutor.substitute(
            getMessage(annotation.elapsedMessage(), aopLoggersProperties.getElapsedMessage()),
            stringLookup);*/
    LoggerUtil.log(logger, elapsedLevel, annotation.elapsedMessage());
  }

  /*private void logElapsedWarning(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final StringSupplierLookup stringLookup,
      final long elapsedTime) {
    final Level elapsedWarningLevel =
        getLevel(annotation.elapsedWarningLevel(), aopLoggersProperties.getElapsedWarningLevel());
    if (isLoggerLevelDisabled(logger, elapsedWarningLevel)) {
      return;
    }

    if (annotation.elapsedTimeLimit() == 0) {
      return;
    }
    final Duration elapsedTimeLimit =
        Duration.of(annotation.elapsedTimeLimit(), annotation.elapsedTimeUnit());
    if (elapsedTimeLimit.minusNanos(elapsedTime).isNegative() == false) {
      return;
    }

    elapsedStringSupplierRegistrar.register(stringLookup, elapsedTime);
    elapsedTimeLimitStringSupplierRegistrar.register(stringLookup, elapsedTimeLimit);

    final String elapsedWarningMessage =
        stringSubstitutor.substitute(
            getMessage(
                annotation.elapsedWarningMessage(),
                aopLoggersProperties.getElapsedWarningMessage()),
            stringLookup);
    LoggerUtil.log(logger, elapsedWarningLevel, elapsedWarningMessage);
  }*/

  private void logExitedMessage(
          final ProceedingJoinPoint joinPoint,
          final LogAround annotation,
          final Logger logger,
          final Object returnValue) {
    final Level exitedLevel = annotation.level();//getLevel(annotation.level(), aopLoggersProperties.getExitedLevel());
    if (isLoggerLevelDisabled(logger, exitedLevel)) {
      return;
    }

    //returnValueStringSupplierRegistrar.register(stringLookup, joinPoint, returnValue);

    /*final String exitedMessage =
        stringSubstitutor.substitute(
            getMessage(annotation.exitedMessage(), aopLoggersProperties.getExitedMessage()),
            stringLookup);*/
    LoggerUtil.log(logger, exitedLevel, annotation.exitedMessage() + " Retorna " + returnedValue(joinPoint, returnValue));
  }

  /*private void logExitedAbnormallyMessage(
      final ProceedingJoinPoint joinPoint,
      final LogAround annotation,
      final Logger logger,
      final Throwable exception) {
    final Level exitedAbnormallyLevel = annotation.level();
        //getLevel(annotation.exitedAbnormallyLevel(), aopLoggersProperties.getExitedAbnormallyLevel());
    if (isLoggerLevelDisabled(logger, exitedAbnormallyLevel)
        || isIgnoredException(exception, annotation.ignoreExceptions())) {
      return;
    }

    //exceptionStringSupplierRegistrar.register(stringLookup, exception);

    final String exitedAbnormallyMessage =
        stringSubstitutor.substitute(
            getMessage(
                annotation.exitedAbnormallyMessage(),
                aopLoggersProperties.getExitedAbnormallyMessage()),
            stringLookup);

    if (annotation.printStackTrace()) {
      LoggerUtil.logException(logger, exitedAbnormallyLevel, exitedAbnormallyMessage, exception);
    } else {
      LoggerUtil.log(logger, exitedAbnormallyLevel, exitedAbnormallyMessage);
    }
  }

  private Level getLevel(final Level level, final Level defaultLevel) {
    return level == Level.DEFAULT ? defaultLevel : level;
  }

  private String getMessage(final String message, final String defaultMessage) {
    return message.length() > 0 ? message : defaultMessage;
  }*/


  private String method(final JoinPoint joinPoint) {
    final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    return method.getReturnType().getSimpleName()
            + " "
            + method.getName()
            + "("
            + methodParameters(joinPoint)
            + ")";
  }

  private String methodParameters(final JoinPoint joinPoint) {
    final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    final String methodName = methodSignature.getMethod().getName();
    final Method method = methodSignature.getMethod();
    final int parameterCount = method.getParameterCount() - 1;

    final Object[] parameterValues = joinPoint.getArgs();
    final StringBuilder builder = new StringBuilder();
    builder.append(methodName);
    builder.append("(");
    if (parameterCount == -1) {
      builder.append("none");
    }else {
      for (int index = 0; index <= parameterCount; index++) {
        builder.append(String.valueOf(parameterValues[index]));
        builder.append(", ");
      }
    }
    builder.append(")");
    return builder.toString();
  }

  private String returnedValue(final JoinPoint joinPoint, final Object returnValue) {
    final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    if (void.class.equals(methodSignature.getReturnType())) {
      return "none";
    }

    return String.valueOf(returnValue);
  }
}

