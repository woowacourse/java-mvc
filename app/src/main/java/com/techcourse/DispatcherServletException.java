package com.techcourse;

import jakarta.servlet.ServletException;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapterException;

public class DispatcherServletException extends ServletException {

    public DispatcherServletException(String message) {
        super(message);
    }

    public static class NotFoundHandlerAdapterException extends HandlerAdapterException {

        private static final String NOT_FOUND_HANDLER_ADAPTER_MESSAGE = "해당 컨트롤러의 어댑터를 찾을 수 없습니다.: ";

        public NotFoundHandlerAdapterException(String controllerName) {
            super(NOT_FOUND_HANDLER_ADAPTER_MESSAGE + controllerName);
        }
    }

    public static class CannotHandleException extends ControllerException {

        private static final String CANNOT_HANDLE_CONTROLLER_MESSAGE = "컨트롤러를 동작할 수 없습니다.: ";

        public CannotHandleException(String controllerName) {
            super(CANNOT_HANDLE_CONTROLLER_MESSAGE + controllerName);
        }
    }
}
