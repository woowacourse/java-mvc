package com.interface21.webmvc.servlet.mvc.handler;

public interface ApplicationContext {

    <T> T getBean(Class<T> clazz);
}
