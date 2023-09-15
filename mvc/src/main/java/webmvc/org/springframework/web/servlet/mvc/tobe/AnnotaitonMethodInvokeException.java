package webmvc.org.springframework.web.servlet.mvc.tobe;

public class AnnotaitonMethodInvokeException extends RuntimeException {

    public AnnotaitonMethodInvokeException(String message, ReflectiveOperationException cause) {
        super(message, cause);
    }
}
