package nextstep.mvc.view;

public class ModelAndView {

    private final String viewName;
    private final Model model;

    public ModelAndView(Model model, String viewName) {
        this.model = model;
        this.viewName = viewName;
    }

    public ModelAndView(String viewName) {
        this(new Model(), viewName);
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

    public String getViewName() {
        return viewName;
    }
}
