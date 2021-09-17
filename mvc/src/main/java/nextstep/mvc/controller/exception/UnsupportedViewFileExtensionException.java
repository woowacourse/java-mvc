package nextstep.mvc.controller.exception;

public class UnsupportedViewFileExtensionException extends RuntimeException {
    private static final String MESSAGE = "지원되지 않는 View의 확장자입니다.";

    public UnsupportedViewFileExtensionException() {
        super(MESSAGE);
    }
}
