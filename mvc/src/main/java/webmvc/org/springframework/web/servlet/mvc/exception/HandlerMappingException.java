package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerMappingException extends RuntimeException {

    public HandlerMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
