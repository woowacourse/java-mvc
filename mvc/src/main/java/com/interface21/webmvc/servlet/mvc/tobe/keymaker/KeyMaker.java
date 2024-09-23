package com.interface21.webmvc.servlet.mvc.tobe.keymaker;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;

import java.lang.reflect.Method;

public interface KeyMaker {

    boolean hasAnnotation(Method method);

    HandlerKey[] makeKeys(Method method);
}
