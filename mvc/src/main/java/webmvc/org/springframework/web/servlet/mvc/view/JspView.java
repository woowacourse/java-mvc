package webmvc.org.springframework.web.servlet.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.View;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";
    public static final String JSP_SUFFIX = ".jsp";

    private final String viewName;

    public JspView(final String viewName) {
        if (viewName == null || viewName.isEmpty() || viewName.isBlank()) {
            throw new IllegalArgumentException("이동할 페이지를 입력해주세요.");
        }
        if (!viewName.endsWith(JSP_SUFFIX)) {
            throw new IllegalArgumentException("올바르지 않은 jsp 이름입니다.");
        }

        this.viewName = viewName.strip();
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        // TODO : 이후 미션 단계에서 구현 예정

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

    }
}
