package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerException;
import webmvc.org.springframework.web.servlet.mvc.handleradapter.HandlerAdapterException;

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

    public static class NotFoundHandlerException extends HandlerException {

        private static final String CANNOT_FIND_NOT_FOUND_HANDLER_MESSAGE = "핸들러를 찾을 수 없습니다.";

        public NotFoundHandlerException() {
            super(CANNOT_FIND_NOT_FOUND_HANDLER_MESSAGE);
        }
    }
}
