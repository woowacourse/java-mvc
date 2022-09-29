package nextstep.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.GiveMePeanut;
import nextstep.web.annotation.ImPeanut;
import nextstep.web.annotation.PeanutConfiguration;
import nextstep.web.annotation.Repository;
import nextstep.web.annotation.Service;
import nextstep.web.annotation.ThisIsPeanut;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PeanutBox {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(PeanutBox.class);

    private final Set<Object> peanutsCache = new HashSet<>();
    private Reflections reflections;

    public void init(final String path) {
        try {
            initInternal(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getPeanut(final Class<T> clazz) {
        return (T) peanutsCache.stream()
                .filter(peanut -> clazz.isAssignableFrom(peanut.getClass()))
                .findAny()
                .orElse(null);
    }

    public void clear() {
        peanutsCache.clear();
    }

    public void changePeanut(final Class<?> oldPeanutType, final Object newPeanut) {
        final Object beforePeanut = getPeanut(oldPeanutType);
        if (beforePeanut == null) {
            throw new RuntimeException("제거할 peanut이 존재하지 않습니다.");
        }
        peanutsCache.remove(beforePeanut);
        log.info("remove peanut = {}", beforePeanut.getClass().getSimpleName());
        peanutsCache.add(newPeanut);
        log.info("add new peanut = {}", newPeanut.getClass().getSimpleName());
    }

    private void initInternal(final String path) throws Exception {
        reflections = new Reflections(path);
        scanManualPeanuts(reflections);
        scanAutoPeanuts(reflections);
    }

    private void scanManualPeanuts(final Reflections reflections) throws Exception {
        final Set<Class<?>> peanutConfigs = reflections.getTypesAnnotatedWith(PeanutConfiguration.class);
        for (final Class<?> peanutConfig : peanutConfigs) {
            final Object newConfigInstance = peanutConfig.getConstructor().newInstance();
            for (final Method peanutMethod : peanutConfig.getDeclaredMethods()) {
                if (peanutMethod.isAnnotationPresent(ThisIsPeanut.class)) {
                    final Object peanut = peanutMethod.invoke(newConfigInstance);
                    peanutsCache.add(peanut);
                }
            }
        }
    }

    private void scanAutoPeanuts(final Reflections reflections) throws Exception {
        final Set<Class<?>> peanutTypes = findPeanutAnnotatedTypes(reflections);
        for (final Class<?> peanutType : peanutTypes) {
            if (isNotCreationCase(peanutType)) {
                continue;
            }
            final Object newInstance = dfs_types(peanutType);
            peanutsCache.add(newInstance);
        }
    }

    private Object dfs_types(final Class<?> type) throws Exception {
        if (isSimpleConcreteClass(type)) {
            return dfs_args(type);
        } else if (isConcreteClassHasInterface(type)) {
            return dfs_args(type);
        } else if (isInterface(type)) {
            return dfs_args(getSubType(type));
        }
        throw new RuntimeException("예측하지 못한 타입입니다: + " + type);
    }

    private Class<?> getSubType(final Class<?> type) {
        return (Class<?>) reflections.getSubTypesOf(type).toArray()[0];
    }

    private Object dfs_args(final Class<?> type) throws Exception {
        if (isAlreadyExistPeanut(type)) {
            return getPeanut(type);
        }

        /** public 기본 생성자가 있는 경우 */
        else if (hasDefaultConstructor(type)) {
            final Constructor<?> defaultConstructor = getDefaultConstructor(type);
            return defaultConstructor.newInstance();
        }

        /** 필드 주입의 경우 */
        else if (isFieldInjection(type)) {
            final Constructor<?> hiddenDefaultConstructor = findHiddenDefaultConstructor(type);
            final Object newInstance = hiddenDefaultConstructor.newInstance();
            final Field[] fields = type.getDeclaredFields();
            for (final Field field : fields) {
                if (field.isAnnotationPresent(GiveMePeanut.class)) {
                    field.setAccessible(true);
                    field.set(newInstance, dfs_types(field.getType()));
                }
            }
            return newInstance;
        }

        /** 여러 args인 경우 */
        else {
            final Constructor<?>[] constructors = type.getDeclaredConstructors();
            validateConstructorIsUnique(constructors);
            final Constructor<?> constructor = constructors[0];
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            final Object[] parameterInstances = findAndCacheParameters(parameterTypes);
            return constructor.newInstance(parameterInstances);
        }
    }

    private boolean isInterface(Class<?> type) {
        return type.isInterface();
    }

    private boolean isSimpleConcreteClass(final Class<?> type) {
        // 자신은 구체 클래스이며, 인터페이스를 하나라도 가지고 있다면!
        return !type.isInterface() && type.getInterfaces().length == 0;
    }

    private boolean isConcreteClassHasInterface(final Class<?> type) {
        return !type.isInterface() && type.getInterfaces().length > 0;
    }

    /**
     * 1. 기본 생성자가 있다 (public 유무는 상관 없음) 2. Inject로 된 필드가 하나라도 있다
     */
    private boolean isFieldInjection(final Class<?> type) {
        return existAnyDefaultConstructor(type) && existFieldAnnotation(type);
    }

    private boolean existAnyDefaultConstructor(final Class<?> type) {
        try {
            type.getDeclaredConstructor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean existPublicDefaultConstructor(final Class<?> type) {
        try {
            type.getConstructor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean existFieldAnnotation(final Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .anyMatch(field -> field.isAnnotationPresent(GiveMePeanut.class));
    }

    private Constructor<?> findHiddenDefaultConstructor(final Class<?> type) throws NoSuchMethodException {
        final Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor;
    }

    private Set<Class<?>> findPeanutAnnotatedTypes(final Reflections reflections) {
        final Set<Class<?>> peanuts = reflections.getTypesAnnotatedWith(ImPeanut.class);
        peanuts.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        peanuts.addAll(reflections.getTypesAnnotatedWith(Service.class));
        peanuts.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        return peanuts;
    }

    private Object[] findAndCacheParameters(final Class<?>[] parameterTypes) throws Exception {
        final Object[] parameterInstances = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            final Object parameterInstance = dfs_types(parameterType);
            peanutsCache.add(parameterInstance);
            parameterInstances[i] = parameterInstance;
        }
        return parameterInstances;
    }

    private void validateConstructorIsUnique(final Constructor<?>[] constructors) {
        if (constructors.length > 1) {
            throw new RuntimeException("Peanut은 하나의 생성자만을 가져야 합니다.");
        }
    }

    /**
     * 1. public 인 기본 생성자가 반드시 있어야 하고
     * 2. @GiveMePeanut 어노테이션이 단 한나라도 있으면 아니 된다
     */
    private boolean hasDefaultConstructor(final Class<?> type) {
        return existPublicDefaultConstructor(type) && !existFieldAnnotation(type);
    }

    private Constructor<?> getDefaultConstructor(final Class<?> peanut) {
        try {
            return peanut.getConstructor();
        } catch (NoSuchMethodException e) {
            return null; // 메서드가 존재하지 않는 경우를 허용해야 함!
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAlreadyExistPeanut(final Class<?> peanut) {
        return getPeanut(peanut) != null;
    }

    private boolean isNotCreationCase(final Class<?> type) {
        /**
         * 어노테이션이거나
         * 인터페이스가 존재한다면(#1)
         * 처리하지 않는다
         *
         * #1의 경우 인터페이스에 따른 생성방식을 따로 처리하고 있는 로직이 있기 때문에 이곳에서 처리하지 않는다
         */
        return type.isAnnotation() && type.getInterfaces().length > 0;
    }
}
