package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.JspViewResolver;
import nextstep.mvc.ViewResolver;

public class ViewResolverRegistry {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverRegistry() {
        this.viewResolvers = new ArrayList<>();
    }

    public void addViewResolver(final ViewResolver viewResolver) {
        viewResolvers.add(viewResolver);
    }

    public ViewResolver getJspViewResolver() {
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver instanceof JspViewResolver)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Jsp를 지원하는 뷰리졸버가 존재하지 않습니다."));
    }
}
