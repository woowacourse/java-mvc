package com.interface21.webmvc.servlet.mvc.tobe;

public interface ApplicationContext {

    <T> T getBean(Class<T> clazz);
}
