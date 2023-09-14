package webmvc.org.springframework.web.servlet.mvc.exception;

public class GetInstanceException extends RuntimeException {

    public GetInstanceException() {
        super("컨트롤러의 인스턴스를 생성할 수 없습니다.");
    }
}
