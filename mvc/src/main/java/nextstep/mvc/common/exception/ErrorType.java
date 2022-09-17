package nextstep.mvc.common.exception;

public enum ErrorType {

    FAIL_CONTROLLER_MAPPING("컨트롤러 매핑에 실패했습니다."),
    INVALID_REQUEST_METHOD("잘못된 요청 메서드 입니다.");

    private final String message;

    ErrorType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
