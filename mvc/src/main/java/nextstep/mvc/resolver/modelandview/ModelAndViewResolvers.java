package nextstep.mvc.resolver.modelandview;

import java.util.Arrays;
import java.util.List;
import nextstep.mvc.view.ModelAndView;

public class ModelAndViewResolvers {

    private List<ModelAndViewResolver> modelAndViewResolvers;

    public ModelAndViewResolvers(
        ModelAndViewResolver... modelAndViewResolvers) {
        this.modelAndViewResolvers = Arrays.asList(modelAndViewResolvers);
    }

    public ModelAndView resolveProperModelAndView(Object handleResult) {
        return modelAndViewResolvers.stream()
            .filter(modelAndViewResolver -> modelAndViewResolver.isSupport(handleResult.getClass()))
            .map(
                modelAndViewResolver -> modelAndViewResolver.chooseProperModelAndView(handleResult))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("처리할 수 없는 형태의 return type입니다."));

    }
}
