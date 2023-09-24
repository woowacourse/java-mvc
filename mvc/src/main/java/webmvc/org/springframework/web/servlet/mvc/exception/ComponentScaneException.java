package webmvc.org.springframework.web.servlet.mvc.exception;

public class ComponentScaneException extends RuntimeException {

    public ComponentScaneException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentScaneException(String message) {
        super(message);
    }
}
