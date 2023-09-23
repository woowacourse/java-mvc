package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerNotFoundException extends IllegalArgumentException {
    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
