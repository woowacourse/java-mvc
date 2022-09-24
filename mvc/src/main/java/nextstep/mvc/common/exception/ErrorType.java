package nextstep.mvc.common.exception;

public enum ErrorType {

    FAIL_HANDLER_MAPPING("핸들러 매핑에 실패했습니다."),
    INVALID_REQUEST_METHOD("잘못된 요청 메서드 입니다."),
    NOT_SUPPORTED_HANDLER("지원하지 않는 핸들러입니다."),
    NOT_FOUND_HANDLER_ADAPTER("핸들러 어뎁터를 찾을 수 없습니다."),
    NOT_FOUND_HANDLER_MAPPING("핸들러 매핑을 찾을 수 없습니다."),
    FAIL_INSTANTIATE_CONTROLLER("컨트롤러 객체를 인스턴스화하지 못했습니다.");

    private final String message;

    ErrorType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
