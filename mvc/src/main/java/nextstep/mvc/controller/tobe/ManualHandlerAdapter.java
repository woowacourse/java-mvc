package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerAdapter.class);

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        final String viewName = ((Controller) handler).execute(request, response);
        ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
        addAttributes(request, modelAndView);
        return new ModelAndView(new JspView(viewName));
    }

    private void addAttributes(final HttpServletRequest request, final ModelAndView modelAndView) {
        final Enumeration<String> attributes = request.getAttributeNames();
        if (attributes == null) {
            return;
        }

        while (attributes.hasMoreElements()) {
            final String key = attributes.nextElement();
            final Object value = request.getAttribute(key);
            modelAndView.addObject(key, value);
            log.info("key = {}, value = {}", key, value);
        }
    }
}
