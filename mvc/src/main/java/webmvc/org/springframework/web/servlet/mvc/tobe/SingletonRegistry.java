package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SingletonRegistry {

    private static final Logger log = LoggerFactory.getLogger(SingletonRegistry.class);
    private static final Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    private SingletonRegistry() {
    }

    public static Object getInstance(final Class<?> controller) {
        if (!singletonObjects.containsKey(controller)) {
            throw new IllegalArgumentException("[ERROR] 아직 인스턴스가 등록되지 않은 클래스입니다");
        }

        return singletonObjects.get(controller);
    }

    public static void registerInstance(final Class<?> controller) {
        try {
            final Object controllerInstance = controller.getDeclaredConstructor()
                    .newInstance();
            singletonObjects.put(controller, controllerInstance);
        } catch (final Exception e) {
            log.error("", e);
            throw new IllegalArgumentException("[ERROR] 해당 클래스의 인스턴스를 생성할 수 없습니다");
        }
    }
}
