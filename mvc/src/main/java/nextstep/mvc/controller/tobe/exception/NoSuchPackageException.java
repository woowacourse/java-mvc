package nextstep.mvc.controller.tobe.exception;

public class NoSuchPackageException extends RuntimeException {

    public NoSuchPackageException() {
        super("요청된 경로에 패키지가 존재하지 않습니다.");
    }
}
