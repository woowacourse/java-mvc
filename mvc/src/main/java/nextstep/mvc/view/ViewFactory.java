package nextstep.mvc.view;

public class ViewFactory {

    private static final String JSP = ".jsp";

    public View createView(final String viewName) {
        if (isDefaultView(viewName)) {
            return new DefaultView(viewName);
        }
        if (viewName.contains(JSP)) {
            return new JspView(viewName);
        }
        return new JsonView();
    }

    private boolean isDefaultView(final String viewName) {
        return viewName.equals(DefaultView.DEFAULT_VIEW_PREFIX) || viewName.equals(DefaultView.DEFAULT_VIEW_REDIRECT);
    }
}
