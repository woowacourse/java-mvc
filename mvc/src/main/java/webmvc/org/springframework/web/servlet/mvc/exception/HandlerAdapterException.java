package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerAdapterException extends HandleException {

    private HandlerAdapterException(final String message) {
        super(message);
    }

    public static class NotFoundException extends HandlerAdapterException {

        public NotFoundException() {
            super("요청을 처리할 수 있는 HandlerAdapter 가 없습니다.");
        }
    }
}
