package com.techcourse;

import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import java.util.List;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.assembler.ComponentAssembler;
import nextstep.web.WebApplicationInitializer;
import nextstep.web.filter.AnnotationFilterMapping;
import nextstep.web.filter.FilterChain;
import nextstep.web.filter.FilterMapping;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final String[] FILTER_BASE_PACKAGE_PATHS = new String[]{"com/techcourse/support/web/filter"};

    private static final ComponentAssembler COMPONENT_ASSEMBLER = new ComponentAssembler();

    public AppWebApplicationInitializer() {
        COMPONENT_ASSEMBLER.scan("com/techcourse");
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        final List<FilterChain> filters = findFilters();
        addFilters(servletContext, filters);

        final DispatcherServlet dispatcherServlet = (DispatcherServlet) COMPONENT_ASSEMBLER
                .getBeanByType(DispatcherServlet.class);

        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private List<FilterChain> findFilters() {
        FilterMapping filterMapping = new AnnotationFilterMapping(FILTER_BASE_PACKAGE_PATHS);
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
