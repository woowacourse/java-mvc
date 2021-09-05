package nextstep.mvc.view;

import static nextstep.web.support.ContentType.HTML;
import static nextstep.web.support.ContentType.contentTypeKey;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.FileViewUtils;
import nextstep.web.support.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";
    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
        if (response.getStatus() == 0) {
            response.setStatus(StatusCode.OK.statusNumber());
        }
        FileViewUtils.render(request, response)
            .path(viewName)
            .header(contentTypeKey(), HTML.contentType())
            .statusCode(StatusCode.OK)
            .flush();
    }
}
