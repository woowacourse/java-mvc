package di.stage3;

public class BeanNotFoundException extends RuntimeException {

    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException(Class<?> notFoundType) {
        this(notFoundType + "에 해당하는 빈을 찾을 수 없습니다");
    }
}
