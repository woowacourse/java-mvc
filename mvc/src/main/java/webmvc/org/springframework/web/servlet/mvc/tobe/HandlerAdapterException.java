package webmvc.org.springframework.web.servlet.mvc.tobe;

public class HandlerAdapterException extends RuntimeException{

    public HandlerAdapterException() {
    }

    public HandlerAdapterException(String message) {
        super(message);
    }

    public HandlerAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
