package com.interface21.webmvc.servlet.mvc.tobe.handleradaptor;

import com.interface21.container.BeanContainer;
import com.interface21.scanner.BeanScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import java.util.List;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptors() {
        String packageName = getClass().getPackageName();
        List<Object> beans = BeanScanner.subTypeScan(HandlerAdaptor.class, packageName);
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.registerBean(beans);
        this.handlerAdaptors = beanContainer.getSubTypeBeansOf(HandlerAdaptor.class);
    }

    public HandlerAdaptor findHandlerAdaptor(Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.support(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 handlerAdaptor 가 없습니다."));
    }
}
