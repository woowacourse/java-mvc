package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface StaticResourceHandler {

    void handleResource(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
