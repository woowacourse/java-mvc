package nextstep.mvc.view.resolver;

import java.util.Objects;
import nextstep.mvc.support.ViewNameHandlerUtils;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class ViewResolverImpl implements ViewResolver {

    @Override
    public View resolve(String fileName) {
        String fileExtension = ViewNameHandlerUtils.getExtension(fileName);
        if (!Objects.isNull(fileExtension) && fileExtension.equalsIgnoreCase("jsp")) {
            return new JspView(fileName);
        }
        return new JsonView();
    }
}
