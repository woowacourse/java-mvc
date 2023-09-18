package webmvc.org.springframework.web.servlet.exception;

public class RequestMethodNotValidException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "유효하지 않는 HTTP 메서드입니다.";

    public RequestMethodNotValidException() {
        super(EXCEPTION_MESSAGE);
    }
}
