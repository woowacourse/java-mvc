package nextstep.mvc.controller.tobe.exception;

public class ClassInstantiateException extends RuntimeException {

    public ClassInstantiateException() {
        super("클래스의 인스턴스를 생성할 수 없습니다.");
    }
}
