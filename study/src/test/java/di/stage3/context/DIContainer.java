package di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    /**
     * 외부에서 Bean으로 등록될 DAO와 Service 객체를 생성자 인자로 받는다.
     */
    public DIContainer(final Set<Class<?>> classes) {
        this.beans = initBeans(classes);
    }

    private Set<Object> initBeans(final Set<Class<?>> classes) {
        // 의존성 없는 빈 먼저 생성
        final Set<Object> notHaveDependenciesBeans = initNotHaveDependenciesBeans(classes);
        final Set<Object> haveDependenciesBeans = initHaveDependenciesBeans(classes, notHaveDependenciesBeans);

        final HashSet<Object> beans = new HashSet<>();
        beans.addAll(notHaveDependenciesBeans);
        beans.addAll(haveDependenciesBeans);

        return beans;
    }

    private Set<Object> initNotHaveDependenciesBeans(final Set<Class<?>> classes) {
        final Set<Class<?>> notHaveDependenciesClasses = classes.stream()
                .filter(this::isNotHaveDependencies)
                .collect(Collectors.toSet());

        return notHaveDependenciesClasses.stream()
                .map(this::createBean)
                .collect(Collectors.toSet());
    }

    private boolean isNotHaveDependencies(final Class<?> clazz) {
        final Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        final Class<?>[] parameterTypes = constructor.getParameterTypes();

        return parameterTypes.length == 0;
    }

    private Object createBean(final Class<?> clazz) {
        final Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        return convertInstance(constructor);
    }

    private Object convertInstance(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (final Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Set<Object> initHaveDependenciesBeans(final Set<Class<?>> classes, final Set<Object> dependencies) {
        final Set<Class<?>> haveDependenciesClasses = classes.stream()
                .filter(clazz -> !isNotHaveDependencies(clazz))
                .collect(Collectors.toSet());

        return haveDependenciesClasses.stream()
                .map(clazz -> createBean(clazz, dependencies))
                .collect(Collectors.toSet());
    }

    private Object createBean(final Class<?> clazz, final Set<Object> dependencies) {
        final Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        final Class<?>[] dependencyTypes = constructor.getParameterTypes();
        final Object[] needDependencies = findNeedDependencies(dependencyTypes, dependencies);
        return convertInstance(constructor, needDependencies);
    }

    private Object[] findNeedDependencies(final Class<?>[] dependencyTypes, final Set<Object> dependencies) {
        final List<Object> findDependencies = Arrays.stream(dependencyTypes)
                .map(type -> findDependency(type, dependencies))
                .toList();

        return findDependencies.toArray();
    }

    private Object findDependency(final Class<?> dependencyType, final Set<Object> dependencies) {
        return dependencies.stream()
                .filter(dependency -> dependencyType.isAssignableFrom(dependency.getClass()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("일치하는 의존성이 존재하지 않습니다."));
    }

    private Object convertInstance(final Constructor<?> constructor, final Object[] needDependencies) {
        try {
            return constructor.newInstance(needDependencies);
        } catch (final Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * @SuppressWarnings("unchecked"), 이건 뭘까?
     * 설명 : 컴파일러가 발생시키는 경고를 무시하도록 설정하는 어노테이션
     * 이 어노테이션의 ("unchecked") 파라미터는 unchecked type casting(검증되지 않은 타입 캐스팅) 경고를 무시한다는 의미
     * 원활한 학습에 집중하기 위해 사용하신듯.
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {

        /**
         * isAssignableFrom 메서드는 두 클래스가 상속 관계 또는 인터페이스 구현 관계에 있을 때 true를 반환
         */
        final Optional<Object> target = beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findAny();
        return target.map(bean -> (T) bean)
                .orElseThrow(() -> new NoSuchElementException("해당되는 Bean이 존재하지 않습니다."));
    }
}
