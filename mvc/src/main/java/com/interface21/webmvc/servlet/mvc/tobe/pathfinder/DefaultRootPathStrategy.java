package com.interface21.webmvc.servlet.mvc.tobe.pathfinder;

import org.reflections.Reflections;

import java.util.Set;

public class DefaultRootPathStrategy implements RootPathStrategy {

    private static final int ORDINAL_APPLICATION_SIZE = 1;
    private static final String ROOT_PATH_REGEX = "\\.";
    
    private final Reflections reflections;
    private String rootPath;

    public DefaultRootPathStrategy() {
        this.reflections = new Reflections();
    }

    @Override
    public String search() {
        if (rootPath == null) {
            initRootPath();
            return rootPath;
        }
        return rootPath;
    }

    private void initRootPath() {
        Set<Class<?>> applications = reflections.getTypesAnnotatedWith(FrameApplication.class);
        if (applications.size() != ORDINAL_APPLICATION_SIZE) {
            throw new IllegalStateException("시작할 수 없는 Application 입니다.");
        }

        Class<?> application = applications.iterator().next();
        rootPath = extractPath(application);
    }

    private String extractPath(Class<?> application) {
        Package currentPackage = application.getPackage();
        String packageName = currentPackage.getName();

        return packageName.split(ROOT_PATH_REGEX)[0];
    }
}
