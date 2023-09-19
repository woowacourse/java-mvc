package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface Mapper {

    void initialize();


    Object getHandler(final HttpServletRequest request);
}
