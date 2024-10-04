package di.stage4.annotations;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ClassPathScanner {

    public static Set<Class<?>> getAnnotationClasses(String packageName, Class<? extends Annotation>... annotations) {
        Set<Class<?>> annotationClasses = new HashSet<>();
        for (Class<?> aClass : getAllClassesInPackage(packageName)) {
            if (doesHaveAnyAnnotations(aClass, annotations)) {
                annotationClasses.add(aClass);
            }
        }
        return annotationClasses;
    }

    private static Set<Class<?>> getAllClassesInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.SubTypes.filterResultsBy(c -> true));
        return reflections.getSubTypesOf(Object.class);
    }

    private static boolean doesHaveAnyAnnotations(Class<?> aClass, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (aClass.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }
}
