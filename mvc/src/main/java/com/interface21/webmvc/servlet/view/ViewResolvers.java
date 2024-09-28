package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.NotFoundView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.ViewResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewResolvers {

    private final List<ViewResolver> viewResolvers;

    public ViewResolvers() {
        this.viewResolvers = new ArrayList<>();
    }

    public void addViewResolvers(ViewResolver viewResolver) {
        if (viewResolver == null) {
            throw new NullPointerException("viewResolver가 존재하지 않습니다.");
        }
        viewResolvers.add(viewResolver);
    }

    public View resolveViewName(String viewName) {
        return viewResolvers.stream()
                .map(viewResolver -> viewResolver.resolveViewName(viewName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NotFoundView("viewName : %s 에 해당하는 뷰가 존재하지 않습니다.".formatted(viewName)));
    }
}
