package nextstep.mvc.view.resolver;

import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.support.FileNameHandlerUtils;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class ViewResolverImpl implements ViewResolver {
    private final View JSON_VIEW = new JsonView();
    private final Map<String, View> container = new HashMap<>();

    @Override
    public View resolve(String fileName) {
        if (container.containsKey(fileName)) {
            return container.get(fileName);
        }
        View view = getViewByExtension(fileName);
        container.putIfAbsent(fileName, view);
        return view;
    }

    private View getViewByExtension(String fileName) {
        if (FileNameHandlerUtils.isExtension(fileName, "jsp") || fileName.startsWith(JspView.REDIRECT_PREFIX)) {
            return new JspView(fileName);
        }
        return JSON_VIEW;
    }
}
