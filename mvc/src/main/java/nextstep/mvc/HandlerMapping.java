package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);

    boolean canHandle(HttpServletRequest request);

    void handle(HttpServletRequest request, HttpServletResponse response)  throws Exception;
}
