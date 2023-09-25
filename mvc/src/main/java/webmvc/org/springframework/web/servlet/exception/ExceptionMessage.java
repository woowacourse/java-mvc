package webmvc.org.springframework.web.servlet.exception;

public class ExceptionMessage {

    private final String message;

    public ExceptionMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
