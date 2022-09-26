package nextstep.mvc.exception;

public class AttributeNotFoundException extends RuntimeException {

    public AttributeNotFoundException() {
        super("Model에 attribute 값이 들어있지 않습니다.");
    }
}
