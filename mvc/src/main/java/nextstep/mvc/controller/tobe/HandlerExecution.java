package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        log.info("handler Execution");
        return null;
    }
}
