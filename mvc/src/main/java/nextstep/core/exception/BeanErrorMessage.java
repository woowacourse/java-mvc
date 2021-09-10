package nextstep.core.exception;

public enum BeanErrorMessage {
    NOT_FOUND("해당하는 빈을 찾을 수 없습니다."),
    ;

    private final String message;

    BeanErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
