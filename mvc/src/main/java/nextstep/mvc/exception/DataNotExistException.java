package nextstep.mvc.exception;

public class DataNotExistException extends RuntimeException {

    public DataNotExistException() {
        super("데이터가 존재하지 않습니다.");
    }
}
