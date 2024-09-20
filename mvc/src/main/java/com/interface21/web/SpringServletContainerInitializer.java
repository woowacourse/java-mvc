package com.interface21.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import com.interface21.core.util.ReflectionUtils;

@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(final Set<Class<?>> webAppInitializerClasses, final ServletContext servletContext)
            throws ServletException {
        final List<WebApplicationInitializer> initializers = new ArrayList<>();

        if (webAppInitializerClasses != null) {
            for (Class<?> waiClass : webAppInitializerClasses) {
                try {
                    initializers.add((WebApplicationInitializer)
                            ReflectionUtils.accessibleConstructor(waiClass).newInstance());
                } catch (final Exception ex) {
                    throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
                }
            }
        }

        if (initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
            return;
        }

        for (final WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
