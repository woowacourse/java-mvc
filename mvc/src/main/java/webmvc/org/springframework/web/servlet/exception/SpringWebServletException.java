package webmvc.org.springframework.web.servlet.exception;

public abstract class SpringWebServletException extends RuntimeException {

    protected SpringWebServletException(final String message) {
        super(message);
    }
}
