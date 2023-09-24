package webmvc.org.springframework.web.servlet.exception;

public class JsonException extends RuntimeException {

    final String message;

    public JsonException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
