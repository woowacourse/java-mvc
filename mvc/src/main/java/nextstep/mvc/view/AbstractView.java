package nextstep.mvc.view;

public abstract class AbstractView implements View {
    protected final String viewName;

    protected AbstractView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String getViewName() {
        return viewName;
    }
}
