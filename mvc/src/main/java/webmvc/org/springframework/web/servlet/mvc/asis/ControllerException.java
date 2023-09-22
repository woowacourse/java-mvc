package webmvc.org.springframework.web.servlet.mvc.asis;

public class ControllerException extends RuntimeException{

    public ControllerException() {
    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
