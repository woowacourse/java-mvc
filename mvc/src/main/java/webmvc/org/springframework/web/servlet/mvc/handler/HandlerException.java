package webmvc.org.springframework.web.servlet.mvc.handler;

public class HandlerException extends RuntimeException {

    public HandlerException() {
    }

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
