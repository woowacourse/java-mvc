package nextstep.mvc.exception;

public class ModelAttributeEmptyException extends RuntimeException {

    public ModelAttributeEmptyException() {
        super("Model이 비어있습니다.");
    }
}
