package webmvc.org.springframework.web.servlet.mvc.handlermapping;

public class HandlerReflectionException extends RuntimeException {

    private static final String REFLECTION_EXCEPTION_MESSAGE = "리플렉션에서 예외가 발생했습니다.";

    public HandlerReflectionException(String exception) {
        super(REFLECTION_EXCEPTION_MESSAGE + ": " + exception);
    }
}
