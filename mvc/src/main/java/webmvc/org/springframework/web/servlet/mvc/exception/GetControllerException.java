package webmvc.org.springframework.web.servlet.mvc.exception;

public class GetControllerException extends RuntimeException {

    public GetControllerException() {
        super("컨트롤러의 인스턴스를 생성할 수 없습니다.");
    }
}
