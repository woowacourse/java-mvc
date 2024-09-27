package com.interface21.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class MainClassPackageNameFinder {
    public static String getMainClassPackageName() {
        List<StackTraceElement> allStackTraceElements = getAllStackTraceElements();
        return allStackTraceElements.stream()
                .filter(stackTraceElement -> stackTraceElement.getMethodName().equals("main")) // "main" 메서드가 있는지 필터링
                .map(StackTraceElement::getClassName) // 클래스 이름으로 매핑
                .map(MainClassPackageNameFinder::getClass) // 클래스 객체로 변환
                .filter(MainClassPackageNameFinder::isMainClass) // main 클래스인지 확인
                .map(Class::getPackageName) // 패키지 이름으로 변환
                .findFirst() // 첫 번째 일치하는 값 찾기
                .orElseThrow(() -> new RuntimeException("main 메서드가 있는 클래스를 찾을 수 없습니다.")); // 값이 없을 때 예외 발생
    }

    private static List<StackTraceElement> getAllStackTraceElements() {
        return Thread.getAllStackTraces().keySet().stream()
                .flatMap(thread -> Arrays.stream(thread.getStackTrace()))
                .toList();
    }

    private static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isMainClass(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .anyMatch(MainClassPackageNameFinder::isMainMethod);
    }

    private static boolean isMainMethod(Method method) {
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        boolean isMain = method.getName().equals("main");
        boolean isVoid = method.getReturnType().getName().equals("void");
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> parameterType = parameterTypes[0];
        boolean equals = parameterType.equals(String[].class);
        return isStatic && isMain && isVoid && equals;
    }
}
