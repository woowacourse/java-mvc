package com.interface21.webmvc.servlet.mvc.tobe;

public class ControllerConstructorNotFoundException extends RuntimeException {

    public ControllerConstructorNotFoundException(Class<?> clazz, Throwable cause) {
        super(getMessage(clazz), cause);
    }

    private static String getMessage(Class<?> clazz) {
        return String.format("컨트롤러의 기본 생성자를 찾을 수 없습니다. - controllerName: %s", clazz.getName());
    }
}
