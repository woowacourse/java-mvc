package nextstep.web;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@HandlesTypes(WebApplicationInitializer.class)
public class NextstepServletContainerInitializer implements ServletContainerInitializer {

    private static final Logger log = LoggerFactory.getLogger(NextstepServletContainerInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
            throws ServletException {
        final List<WebApplicationInitializer> initializers = new LinkedList<>();
        log.info("call NextstepServletContainerInitializer.onStartUp()");
        if (webAppInitializerClasses != null) {
            for (Class<?> waiClass : webAppInitializerClasses) {
                try {
                    log.info("className : " + waiClass.getName());
                    initializers.add((WebApplicationInitializer) waiClass.getDeclaredConstructor().newInstance());
                } catch (Throwable e) {
                    throw new ServletException("Failed to instantiate WebApplicationInitializer class", e);
                }
            }
        }

        if (initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
            return;
        }

        for (WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
