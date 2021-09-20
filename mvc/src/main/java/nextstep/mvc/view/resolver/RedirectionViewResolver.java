package nextstep.mvc.view.resolver;

import nextstep.mvc.view.RedirectionView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewName;

public class RedirectionViewResolver implements ViewResolver {

    @Override
    public boolean supports(ViewName viewName) {
        return !viewName.isEmpty() && RedirectionView.isRedirection(viewName.value());
    }

    @Override
    public View resolve(ViewName viewName) {
        return new RedirectionView(viewName.value());
    }
}
