package webmvc.org.springframework.web.servlet.exception;

public class HandlerExecutionNotInitializeException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "HandlerExecution 이 초기화에 실패했습니다.";

    public HandlerExecutionNotInitializeException() {
        super(EXCEPTION_MESSAGE);
    }
}
