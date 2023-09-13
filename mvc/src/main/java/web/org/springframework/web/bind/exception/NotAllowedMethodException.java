package web.org.springframework.web.bind.exception;

public class NotAllowedMethodException extends RuntimeException {

    public NotAllowedMethodException() {
        super("허용되지 않은 Http Method입니다.");
    }
}
