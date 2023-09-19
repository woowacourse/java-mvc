package webmvc.org.springframework.web.servlet.mvc.adapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class HandlerAdapter {

    final List<Adapter> adapters;

    public HandlerAdapter(final List<Adapter> adapters) {
        this.adapters = adapters;
    }

    public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        for(Adapter adapter : adapters) {
            if(adapter.isPossibleToHandle(handler)) {
                return adapter.handle(handler, request, response);
            }
        }
        throw new ServletException("핸들러를 처리할 수 있는 어댑터가 존재하지 않습니다");
    }
}
