package nextstep.mvc.view;

public class ModelAndView {

    private final ViewName viewName;
    private final Model model;

    public ModelAndView(Model model, ViewName viewName) {
        this.model = model;
        this.viewName = viewName;
    }

    public ModelAndView(String viewName) {
        this(new Model(), ViewName.of(viewName));
    }

    public ModelAndView() {
        this(new Model(), ViewName.EMPTY);
    }

    public void addObject(String attributeName, Object attributeValue) {
        model.addAttribute(attributeName, attributeValue);
    }

    public Object getObject(String attributeName) {
        return model.getAttribute(attributeName);
    }

    public Model getModel() {
        return model;
    }

    public ViewName getViewName() {
        return viewName;
    }
}
