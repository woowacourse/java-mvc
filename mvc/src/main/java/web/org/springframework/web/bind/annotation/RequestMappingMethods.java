package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMappingMethods {

    GET(GetMapping.class, RequestMethod.GET),
    POST(PostMapping.class, RequestMethod.POST),
    PUT(PutMapping.class, RequestMethod.PUT),
    DELETE(DeleteMapping.class, RequestMethod.DELETE),
    PATCH(PatchMapping.class, RequestMethod.PATCH);

    private final Class<?> clazz;
    private final RequestMethod requestMethod;

    RequestMappingMethods(Class<?> clazz, RequestMethod requestMethod) {
        this.clazz = clazz;
        this.requestMethod = requestMethod;
    }

    public static RequestMethod getMethod(Class<?> clazz) {
        return Arrays.stream(values())
                .filter(each -> each.clazz.equals(clazz))
                .findFirst()
                .orElseThrow()
                .requestMethod;
    }
}
