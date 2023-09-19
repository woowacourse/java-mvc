package web.org.springframework.web.bind.annotation;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod find(final String name) {
        return Arrays.stream(values())
                .filter(method -> method.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("cannot find request method : " + name));
    }
}
