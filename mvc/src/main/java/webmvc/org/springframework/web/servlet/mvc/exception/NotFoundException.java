package webmvc.org.springframework.web.servlet.mvc.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}
