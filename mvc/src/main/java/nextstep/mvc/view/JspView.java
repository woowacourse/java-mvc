package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.support.FileNameHandlerUtils;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    public static boolean isJspFile(String fileName) {
        return FileNameHandlerUtils.isExtension(fileName, "jsp") || fileName.startsWith(REDIRECT_PREFIX);
    }

    @Override
    public void render(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.TEXT_HTML_UTF8_VALUE);

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.getAttribute(key));
            request.setAttribute(key, model.getAttribute(key));
        });

        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
