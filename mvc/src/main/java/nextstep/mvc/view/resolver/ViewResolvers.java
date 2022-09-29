package nextstep.mvc.view.resolver;

import java.util.List;
import java.util.Objects;
import nextstep.mvc.view.View;

public class ViewResolvers {

    private final List<ViewResolver> viewResolvers;

    public ViewResolvers() {
        this.viewResolvers = List.of(new JspViewResolver(), new JsonViewResolver());
    }

    public View resolve(final Object view) {
        return this.viewResolvers
                .stream()
                .map(viewResolver -> viewResolver.resolve(view))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("No Support View for view : %s", view)));
    }
}
