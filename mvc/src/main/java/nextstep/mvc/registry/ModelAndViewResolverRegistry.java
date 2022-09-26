package nextstep.mvc.registry;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.ModelAndViewResolver;

public class ModelAndViewResolverRegistry {

    private final List<ModelAndViewResolver> modelAndViewResolvers;

    private ModelAndViewResolverRegistry(List<ModelAndViewResolver> modelAndViewResolvers) {
        this.modelAndViewResolvers = modelAndViewResolvers;
    }

    public ModelAndViewResolverRegistry() {
        this(new ArrayList<>());
    }

    public void addModelAndViewResolver(ModelAndViewResolver modelAndViewResolver) {
        this.modelAndViewResolvers.add(modelAndViewResolver);
    }

    public ModelAndViewResolver findModelAndViewResolver(Object object) {
        return modelAndViewResolvers.stream()
                .filter(resolver -> resolver.supports(object))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find viewResolver : " + object));
    }
}
