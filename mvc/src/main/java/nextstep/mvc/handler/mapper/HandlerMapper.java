package nextstep.mvc.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapper {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
