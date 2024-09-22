package com.interface21.context;

public class BeanCreationException extends RuntimeException {

    public BeanCreationException(String beanName, Throwable cause) {
        super("Error creating bean with name '%s'".formatted(beanName), cause);
    }
}
