package com.interface21.context;

public interface ApplicationContext {

    <T> T getBean(Class<T> clazz);
}
