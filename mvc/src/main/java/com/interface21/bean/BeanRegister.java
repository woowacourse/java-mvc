package com.interface21.bean;

import com.interface21.bean.container.BeanContainer;
import com.interface21.bean.scanner.BeanScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import java.util.List;

public class BeanRegister {

    private BeanRegister() {
    }

    public static void run(Class<?> app) {
        BeanContainer beanContainer = BeanContainer.getInstance();
        registerInternalBean(beanContainer);
        registerExternalBean(beanContainer, app);
    }

    private static void registerInternalBean(BeanContainer beanContainer) {
        register(beanContainer, HandlerAdaptor.class);
        register(beanContainer, HandlerMapping.class);
    }

    private static void register(BeanContainer beanContainer, Class<?> clazz) {
        List<Object> beans = BeanScanner.subTypeScan(clazz, clazz.getPackageName());
        beanContainer.registerBeans(beans);
    }

    private static void registerExternalBean(BeanContainer beanContainer, Class<?> clazz) {
        String packageName = clazz.getPackageName();
        List<Object> beans = BeanScanner.componentScan(packageName);
        beanContainer.registerBeans(beans);
    }
}
