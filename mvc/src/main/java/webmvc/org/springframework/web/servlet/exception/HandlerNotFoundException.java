package webmvc.org.springframework.web.servlet.exception;

public class HandlerNotFoundException extends DispatcherServletException {
    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
