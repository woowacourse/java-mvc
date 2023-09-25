package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
