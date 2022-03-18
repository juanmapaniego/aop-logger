package com.jmpaniego.aoplogger.around;

import com.jmpaniego.aoplogger.Level;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogAround {
  Level level() default Level.DEFAULT;
  Level errorLevel() default Level.ERROR;
  String enteringMessage() default "";
  String exitedMessage() default "";
  String elapsedMessage() default "";
}
