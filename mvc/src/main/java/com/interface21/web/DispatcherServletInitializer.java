package com.interface21.web;

import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import jakarta.servlet.ServletContext;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        String basePackages = findMvcApplicationPackage();
        final var dispatcherServlet = new DispatcherServlet(basePackages);

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }


    private String findMvcApplicationPackage() {
        try {
            // 전체 클래스패스를 스캔하여 @MvcApplication 어노테이션 찾기
            Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forClassLoader()) // 전체 클래스패스 스캔
                    .setScanners(Scanners.TypesAnnotated)
            );

            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(MvcApplication.class);

            Class<?> mvcApplicationClass = annotatedClasses.stream()
                .sorted(Comparator.comparing(Class::getName))
                .findFirst()
                .get();

            return mvcApplicationClass.getPackageName();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
