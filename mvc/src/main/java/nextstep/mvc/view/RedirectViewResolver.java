package nextstep.mvc.view;

import static nextstep.mvc.view.RedirectView.REDIRECT_PREFIX;

public class RedirectViewResolver implements ViewResolver {

    @Override
    public boolean supports(String viewName) {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    @Override
    public View resolveViewName(String viewName) {
        return new RedirectView(viewName);
    }
}
