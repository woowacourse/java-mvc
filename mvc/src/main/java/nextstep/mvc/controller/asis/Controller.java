package nextstep.mvc.controller.asis;

import java.lang.annotation.Annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {
    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}
