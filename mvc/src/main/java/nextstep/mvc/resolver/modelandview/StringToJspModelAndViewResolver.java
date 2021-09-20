package nextstep.mvc.resolver.modelandview;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class StringToJspModelAndViewResolver implements ModelAndViewResolver {

    @Override
    public boolean isSupport(Class<?> returnType) {
        return returnType.equals(String.class);
    }

    @Override
    public ModelAndView chooseProperModelAndView(Object obj) {
        String urlPath = (String) obj;
        return new ModelAndView(new JspView(urlPath));
    }
}
