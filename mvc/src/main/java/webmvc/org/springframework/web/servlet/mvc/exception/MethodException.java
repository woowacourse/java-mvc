package webmvc.org.springframework.web.servlet.mvc.exception;

public class MethodException extends RuntimeException{

    public MethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
