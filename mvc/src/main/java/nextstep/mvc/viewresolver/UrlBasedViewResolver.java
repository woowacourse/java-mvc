package nextstep.mvc.viewresolver;

import nextstep.mvc.view.RedirectView;
import nextstep.mvc.view.View;

import static nextstep.mvc.view.RedirectView.REDIRECT_PREFIX;

public class UrlBasedViewResolver implements ViewResolver {

    @Override
    public boolean supports(String viewName) {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    @Override
    public View resolveViewName(String viewName) {
        return new RedirectView(viewName);
    }
}
