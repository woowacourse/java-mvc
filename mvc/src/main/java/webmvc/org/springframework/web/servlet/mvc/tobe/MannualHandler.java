package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class MannualHandler implements Handler {

    private final Controller controller;

    public MannualHandler(final Controller controller) {
        this.controller = controller;
    }

    // TODO: 2023/09/18 테스트 작성
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String viewName = controller.execute(request, response);
        move(viewName, request, response);
        return new ModelAndView(new JspView(viewName));
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
