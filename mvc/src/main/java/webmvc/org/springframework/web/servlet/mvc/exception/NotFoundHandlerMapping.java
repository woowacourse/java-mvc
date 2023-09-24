package webmvc.org.springframework.web.servlet.mvc.exception;

public class NotFoundHandlerMapping extends IllegalArgumentException {

    public NotFoundHandlerMapping(final String message) {
        super(message);
    }
}
