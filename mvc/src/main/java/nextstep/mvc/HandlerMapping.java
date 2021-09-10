package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;

public interface HandlerMapping {

    void initialize();

    HandlerExecution getHandler(HttpServletRequest request);
}
