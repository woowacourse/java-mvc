package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import nextstep.mvc.controller.tobe.HandlerExecution;

public interface HandlerMapping {

    void initialize() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    HandlerExecution getHandler(HttpServletRequest request);
}
