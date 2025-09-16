package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(final HandlerMappingRegistry handlerMappingRegistry,final HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            // 1. 요청에 맞는 핸들러 찾기
            Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new RuntimeException("핸들러가 없습니다. URI: " + request.getRequestURI()));

            // 2. 핸들러에 맞는 Adapter 가져오기
            HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);

            // 3. 핸들러 실행 → ModelAndView 반환
            ModelAndView mav = adapter.handle(request, response, handler);

            // 4. View 렌더링
            render(mav, request, response);

        } catch (Throwable e) {
            log.error("DispatcherServlet 처리 중 에러: {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (mav == null) return;

        View view = mav.getView();
        if (view instanceof JspView jspView) {
            String viewName = jspView.getViewName();

            if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
                response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
                return;
            }

            mav.getModel().forEach(request::setAttribute);

            request.getRequestDispatcher(viewName).forward(request, response);
        } else {
            throw new RuntimeException("지원하지 않는 View 타입: " + view.getClass());
        }
    }
}