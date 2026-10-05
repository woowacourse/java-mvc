package com.interface21.webmvc.servlet.view.resolver;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import java.util.ArrayList;
import java.util.List;

public class ViewResolverRegistry {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverRegistry() {
        this.viewResolvers = new ArrayList<>();
    }

    public void addViewResolver(final ViewResolver viewResolver) {
        viewResolvers.add(viewResolver);
    }

    public View resolveView(final ModelAndView modelAndView) {
        if (modelAndView.hasView()) {
            return modelAndView.getView();
        }
        String viewName = modelAndView.getViewName();
        for (ViewResolver resolver : viewResolvers) {
            View view = resolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        throw new IllegalStateException("viewName에 맞는 View를 찾을 수 없습니다: " + viewName);
    }
}
