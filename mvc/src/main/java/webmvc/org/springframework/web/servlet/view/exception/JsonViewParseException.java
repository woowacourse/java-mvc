package webmvc.org.springframework.web.servlet.view.exception;

public class JsonViewParseException extends RuntimeException {

    public JsonViewParseException() {
        super("JsonView 를 올바르게 생성할 수 없습니다.");
    }
}
