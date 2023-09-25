package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public void handle(HandlerNotFoundException e, HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("{0} exception handled", e);
            new JspView("/404.jsp")
                .render(Collections.emptyMap(), request, response);
        } catch (Exception ex) {
            throw new IllegalArgumentException("exception handling failed");
        }
    }
}
