package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.Objects;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerKey {

    private static final Logger log = LoggerFactory.getLogger(HandlerKey.class);

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static HandlerKey from(Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final String url = annotation.value();
        final RequestMethod requestMethod = annotation.method()[0];
        log.info("url : {} , method: {}, requestMethod: {}", url, method.getName(), requestMethod.name());
        return new HandlerKey(url, requestMethod);
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + requestMethod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HandlerKey)) {
            return false;
        }
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
