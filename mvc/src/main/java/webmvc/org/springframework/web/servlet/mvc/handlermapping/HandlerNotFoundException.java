package webmvc.org.springframework.web.servlet.mvc.handlermapping;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
