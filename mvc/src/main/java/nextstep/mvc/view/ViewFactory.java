package nextstep.mvc.view;

public class ViewFactory {

    private static final String JSP = ".jsp";

    public View createView(final String viewName) {
        if (viewName.contains(JSP)) {
            return new JspView(viewName);
        }
        return new JsonView();
    }
}
