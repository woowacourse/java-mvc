package web.org.springframework.web.bind.exception;

public class InvalidRequestMethodException extends IllegalArgumentException {
    public InvalidRequestMethodException() {
        super("지원하지 않는 Request Method 입니다.");
    }
}
