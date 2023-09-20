package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerExecutionException extends RuntimeException {

    private HandlerExecutionException(final String message) {
        super(message);
    }

    public static class InitializationException extends HandlerExecutionException {

        public InitializationException() {
            super("HandlerExecution 을 생성할 수 없습니다.");
        }
    }
}
