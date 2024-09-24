package com.interface21.webmvc.servlet.mvc.tobe;

public interface HandlerMapping<T, R> {

    void initialize();

    boolean hasHandler(T request);

    R getHandler(T request);
}
