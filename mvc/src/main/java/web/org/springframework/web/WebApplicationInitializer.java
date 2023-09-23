package web.org.springframework.web;

import jakarta.servlet.ServletContext;

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext);
}
