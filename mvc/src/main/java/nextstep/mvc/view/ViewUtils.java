package nextstep.mvc.view;

public class ViewUtils {

    public static String removeRedirect(String viewName) {
        String redirectString = "redirect:";
        if (viewName.startsWith(redirectString)) {
            return viewName.substring(redirectString.length());
        }
        return viewName;
    }
}
