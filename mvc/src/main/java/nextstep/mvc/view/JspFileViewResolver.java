package nextstep.mvc.view;

public class JspFileViewResolver extends FileViewResolver {

    public JspFileViewResolver(String pathName) {
        super(".jsp", pathName);
    }

    @Override
    public View resolve(String name) {
        return new JspView(name);
    }
}
