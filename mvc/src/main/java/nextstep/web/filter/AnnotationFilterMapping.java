package nextstep.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.annotation.WebFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.mvc.support.annotation.AnnotationHandleUtils;
import nextstep.mvc.support.BeanNameParserUtils;

public class AnnotationFilterMapping implements FilterMapping {

    private final String[] basePackages;
    private final List<FilterChain> filterChains = new ArrayList<>();

    public AnnotationFilterMapping(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void initialize() {
        List<Class<?>> filterClasses = getFilterClasses();
        registerAsChain(filterClasses);
    }

    @Override
    public List<FilterChain> filters() {
        if (filterChains.isEmpty()) {
            initialize();
        }
        return Collections.unmodifiableList(filterChains);
    }

    private List<Class<?>> getFilterClasses() {
        return Arrays.stream(basePackages)
                .map(basePackage -> (String) basePackage)
                .flatMap(path -> AnnotationHandleUtils.getClassesAnnotatedWith(path, WebFilter.class).stream())
                .collect(Collectors.toList());
    }

    private void registerAsChain(List<Class<?>> filterClasses) {
        try {
            for (Class<?> klass : filterClasses) {
                String originName = klass.getSimpleName();
                String name = BeanNameParserUtils.toLowerFirstChar(originName);

                String[] paths = klass.getAnnotation(WebFilter.class).urlPatterns();
                Filter instance = (Filter) klass.getConstructor().newInstance();

                filterChains.add(new FilterChain(name, paths, instance));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Annotation filter mapping 오류입니다.");
        }
    }
}
