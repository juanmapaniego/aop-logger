package com.jmpaniego.aoplogger;

import com.jmpaniego.aoplogger.around.LogAroundConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        LogAroundConfiguration.class
})
public class AopLoggerConfiguration {}
