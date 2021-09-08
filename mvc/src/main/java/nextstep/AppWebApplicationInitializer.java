package nextstep;

import air.context.ApplicationContext;
import air.context.ApplicationContextProvider;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import nextstep.mvc.DispatcherServlet;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    private final ApplicationContext context = ApplicationContextProvider.getApplicationContext();

    @Override
    public void onStartup(ServletContext servletContext) {
        DispatcherServlet dispatcherServlet = context.findBeanByType(DispatcherServlet.class);
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
