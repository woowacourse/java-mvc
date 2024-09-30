package com.interface21.core.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class AnnotationUtils {

    private AnnotationUtils() {
    }

    private static final Set<Class<? extends Annotation>> componentCache = ConcurrentHashMap.newKeySet();
    private static final Set<Class<? extends Annotation>> visitedAnnotations = ConcurrentHashMap.newKeySet();

    public static boolean hasMetaAnnotatedClasses(Class<?> clazz,
                                                  Class<? extends Annotation> targetAnnotation) {
        return Arrays.stream(clazz.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .map(annotation -> hasMetaAnnotatedClasses(annotation, targetAnnotation, visitedAnnotations))
                .anyMatch(Predicate.isEqual(true));
    }

    // Depth-first search to find meta-annotated classes recursively
    private static boolean hasMetaAnnotatedClasses(Class<? extends Annotation> currentAnnotation,
                                                   Class<? extends Annotation> targetAnnotation,
                                                   Set<Class<? extends Annotation>> visited) {
        visited.add(currentAnnotation);
        if (componentCache.contains(currentAnnotation) || currentAnnotation.equals(targetAnnotation)) {
            return true;
        }
        boolean result = Arrays.stream(currentAnnotation.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .filter(annotation -> !visited.contains(annotation))
                .map(annotation -> hasMetaAnnotatedClasses(annotation, targetAnnotation, visited)) // Recursive call
                .anyMatch(Predicate.isEqual(true));
        if (result) {
            componentCache.add(currentAnnotation);
        }
        return result;
    }
}
