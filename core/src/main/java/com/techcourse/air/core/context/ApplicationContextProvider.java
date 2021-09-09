package com.techcourse.air.core.context;

import com.techcourse.air.core.context.ApplicationContext;

public class ApplicationContextProvider {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        APPLICATION_CONTEXT = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
