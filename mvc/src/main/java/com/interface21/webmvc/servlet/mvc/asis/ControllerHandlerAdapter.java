package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
이 어탭터는 아래 흐름 담당
Controller
   ↓ execute()
String viewName
   ↓ 변환
ModelAndView
 */
public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        // Legacy Controller만 이 Adapter가 처리한다.
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {

        final Controller controller = (Controller) handler;

        // Legacy Controller는 String viewName을 반환한다.
        final String viewName =
                controller.execute(request, response);

        // 이후 흐름을 통일하기 위해 ModelAndView로 변환한다.
        return new ModelAndView(
                new JspView(viewName)
        );
    }
}