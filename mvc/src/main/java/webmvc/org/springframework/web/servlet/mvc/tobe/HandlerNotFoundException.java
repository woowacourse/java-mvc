package webmvc.org.springframework.web.servlet.mvc.tobe;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
