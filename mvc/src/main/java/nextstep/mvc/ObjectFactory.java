package nextstep.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectFactory<T> {

    private static final Logger log = LoggerFactory.getLogger(ObjectFactory.class);

    private final Class<T> clazz;

    public ObjectFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T newInstance() {
        final Object object;
        try {
            object = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("Handler Reflection 중 예외 발생", e);

            throw new IllegalArgumentException("Handler 생성 중 오류가 발생했습니다.");
        }

        return clazz.cast(object);
    }
}
