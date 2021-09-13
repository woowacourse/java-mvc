package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);

    boolean canHandle(HttpServletRequest request);

    void handle(HttpServletRequest request, HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException, IOException, ServletException;
}
