package com.interface21.webmvc.servlet.mvc;

public class SingletonInstantiationException extends RuntimeException {

    public SingletonInstantiationException(Class<?> singletonClass, String message, Throwable ex) {
        super("Failed to instantiate singleton instance (" + singletonClass.getName() + "): " + message, ex);
    }
}
