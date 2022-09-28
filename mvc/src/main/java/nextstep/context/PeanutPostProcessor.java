package nextstep.context;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.sf.cglib.proxy.Enhancer;
import nextstep.web.annotation.Service;
import nextstep.web.annotation.Toransactional;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PeanutPostProcessor {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(PeanutPostProcessor.class);

    private Reflections reflections;

    public void init(final String path) {
        try {
            initInternal(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initInternal(final String path) {
        reflections = new Reflections(path);
        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        for (final Class<?> service : services) {
            final List<Method> toransactionals = findToransactionalAnnotations(service);
            if (toransactionals.size() > 0) {
                final Object target = PeanutBox.INSTANCE.getPeanut(service);
                final Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(target.getClass());
                enhancer.setCallback(new ToransactionalMethod(target));
                final Object proxy = enhancer.create();
                log.info("target class = {}", target.getClass());
                log.info("proxy class = {}", proxy.getClass());
                PeanutBox.INSTANCE.changePeanut(service, proxy);
            }
        }
    }

    private List<Method> findToransactionalAnnotations(final Class<?> service) {
        return Arrays.stream(service.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Toransactional.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
