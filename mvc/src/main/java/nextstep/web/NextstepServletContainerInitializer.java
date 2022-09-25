package nextstep.web;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@HandlesTypes(WebApplicationInitializer.class)
public class NextstepServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(final Set<Class<?>> webAppInitializerClasses, final ServletContext servletContext)
            throws ServletException {
        final List<WebApplicationInitializer> initializers = new LinkedList<>();

        if (webAppInitializerClasses != null) {
            for (final Class<?> waiClass : webAppInitializerClasses) {
                try {
                    initializers.add((WebApplicationInitializer) waiClass.getDeclaredConstructor().newInstance());
                } catch (final Throwable e) {
                    throw new ServletException("Failed to instantiate WebApplicationInitializer class", e);
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
