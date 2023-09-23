package webmvc.org.springframework.web.servlet.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.ApplicationContextAware;

public class ViewResolverComposite extends ApplicationContextAware {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverComposite() {
        this.viewResolvers = new ArrayList<>();
    }

    public void initialize() {
        final List<? extends ViewResolver> objects = getApplicationContext().getBeansOfType(ViewResolver.class);
        final List<ViewResolver> viewResolvers = objects.stream()
            .map(ViewResolver.class::cast)
            .collect(Collectors.toList());
        this.viewResolvers.addAll(viewResolvers);
    }

    public View resolveViewName(final String viewName) throws Exception {
        for (final ViewResolver viewResolver : viewResolvers) {
            final View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        throw new IllegalArgumentException("지원하는 View 를 찾을 수 없습니다.");
    }
}
