package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerMappingException extends HandleException {

    private HandlerMappingException(final String message) {
        super(message);
    }

    public static class NotFoundException extends HandlerMappingException {

        public NotFoundException() {
            super("요청을 처리할 수 있는 Handler 가 없습니다.");
        }
    }
}
