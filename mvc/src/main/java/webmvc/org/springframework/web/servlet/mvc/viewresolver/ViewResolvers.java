package webmvc.org.springframework.web.servlet.mvc.viewresolver;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

public class ViewResolvers {

    private final List<ViewResolver> viewResolvers;

    public ViewResolvers(final List<ViewResolver> viewResolvers) {
        this.viewResolvers = new ArrayList<>(viewResolvers);
    }

    public View getView(final ModelAndView modelAndView) {
        final Object view = modelAndView.getView();
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver.supports(view))
                .findFirst()
                .map(viewResolver -> viewResolver.resolve(view))
                .orElseThrow(() -> new ViewResolverException("해당 요청에 대한 view를 처리할 수 없습니다."));
    }
}
