package com.jujing.telehook_2.util;

public class AbstractCrashReportHandler {

    public AbstractCrashReportHandler() {

        CrashHandler crashHandler = new CrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

    }
}
