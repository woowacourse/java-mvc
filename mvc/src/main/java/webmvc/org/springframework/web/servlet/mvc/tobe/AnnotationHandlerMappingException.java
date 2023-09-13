package webmvc.org.springframework.web.servlet.mvc.tobe;

public class AnnotationHandlerMappingException extends RuntimeException{

    private static final String REFLECTION_EXCEPTION_MESSAGE = "리플렉션에서 예외가 발생했습니다.";

    public AnnotationHandlerMappingException(String exception) {
        super(REFLECTION_EXCEPTION_MESSAGE + ": " + exception);
    }
}
