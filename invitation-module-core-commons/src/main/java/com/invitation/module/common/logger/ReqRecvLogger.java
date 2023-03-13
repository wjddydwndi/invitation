package com.invitation.module.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReqRecvLogger extends AbstractDefaultLogger {

    private static final Logger errorLogger   = LoggerFactory.getLogger(LoggerType.DETAIL_LOGGER_ERROR.name());
    private static final Logger warnLogger    = LoggerFactory.getLogger(LoggerType.DETAIL_LOGGER_WARN.name());
    private static final Logger infoLogger    = LoggerFactory.getLogger(LoggerType.DETAIL_LOGGER_INFO.name());
    private static final Logger debugLogger   = LoggerFactory.getLogger(LoggerType.DETAIL_LOGGER_DEBUG.name());
    private static final Logger traceLogger   = LoggerFactory.getLogger(LoggerType.DETAIL_LOGGER_TRACE.name());


    public static void error(String pattern, Object... args){Object[] params = params(args);    errorLogger.error("[{}] [ERROR]" + pattern, params);}

    public static void warn(String pattern, Object... args) {Object[] params = params(args);    warnLogger. warn("[{}] [WARN]" + pattern, params);}

    public static void info(String pattern, Object... args) {Object[] params = params(args);    infoLogger.info("[{}] [INFO]" + pattern, params);}

    public static void debug(String pattern, Object... args) {Object[] params = params(args);   debugLogger.debug("[{}] [DEBUG]" + pattern, params);}

    public static void trace(String pattern, Object... args) {Object[] params = params(args);   traceLogger.trace("[{}] [TRACE]" + pattern, params);}

}
