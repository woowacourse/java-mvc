package webmvc.org.springframework.web.servlet.mvc.tobe.exception;

public class ControllerClassNotFoundByNameException extends RuntimeException {

    public ControllerClassNotFoundByNameException(final String className) {
        super("해당 이름의 클래스를 찾을 수 업습니다. 클래스명 : " + className);
    }
}
