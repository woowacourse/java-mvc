package nextstep.mvc;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {

    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private BeanScanner() {
    }

    public static List<Object> getBeansWithAnnotation(Object[] basePackage, Class<? extends Annotation> annotation) {
        log.info("Scan Beans ! package: {}, annotation: {}", basePackage, annotation);
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> beanClasses = reflections.getTypesAnnotatedWith(annotation);
        List<BeanDefinition> beanDefinitions = getBeanDefinitions(beanClasses);
        return beanDefinitions.stream()
                .map(BeanDefinition::getBean)
                .collect(Collectors.toList());
    }

    private static List<BeanDefinition> getBeanDefinitions(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .map(BeanDefinition::new)
                .collect(Collectors.toList());
    }
}
