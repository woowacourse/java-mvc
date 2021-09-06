package com.techcourse;

import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import java.util.List;
import nextstep.mvc.DispatcherServlet;
import nextstep.web.WebApplicationInitializer;
import nextstep.web.filter.AnnotationFilterMapping;
import nextstep.web.filter.FilterChain;
import nextstep.web.filter.FilterMapping;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final String[] FILTER_BASE_PACKAGE_PATHS = new String[]{"com/techcourse/support/web/filter"};

    @Override
    public void onStartup(ServletContext servletContext) {

        final List<FilterChain> filters = findFilters(FILTER_BASE_PACKAGE_PATHS);
        addFilters(servletContext, filters);

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());

        // TODO :: AnnotationHandlerMapping 추가
//        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping());

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private List<FilterChain> findFilters(String[] basePackagePath) {
        FilterMapping filterMapping = new AnnotationFilterMapping(basePackagePath);
        return filterMapping.filters();
    }

    private void addFilters(ServletContext servletContext, List<FilterChain> filters) {
        for (FilterChain filterChain : filters) {
            Dynamic resourceFilter = servletContext.addFilter(filterChain.name(), filterChain.filter());
            resourceFilter.setAsyncSupported(true);
            resourceFilter.addMappingForUrlPatterns(null, false, filterChain.uriPaths());
        }
    }
}
