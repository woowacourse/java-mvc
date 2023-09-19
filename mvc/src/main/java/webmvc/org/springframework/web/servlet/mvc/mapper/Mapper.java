package webmvc.org.springframework.web.servlet.mvc.mapper;

import jakarta.servlet.http.HttpServletRequest;

public interface Mapper {

    void initialize();


    Object getHandler(final HttpServletRequest request);
}
