package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.AbstractView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView extends AbstractView {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private static final String REDIRECT_PREFIX = "redirect:";

    public JspView(String viewName) {
        super(viewName);
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
        /*
        requestDispatcher는 서블릿의 스펙으로 요청을 다른 uri에 매핑되는 서블릿으로 처리를 넘길 때 사용한다. 리다이렉트와 달리 클라이언트를 거치지 않고
        내부적으로 요청의 처리를 넘기는 방식이다. 클라이언트로 리다이렉트 하면 request 객체에 저장해둔 여러 정보를 사용할 수 없게 된다.
         */
    }
}
