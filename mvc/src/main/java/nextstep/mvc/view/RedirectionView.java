package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RedirectionView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String path;

    public RedirectionView(String path) {
        this.path = path;
    }

    public static boolean isRedirection(String path){
        return path.startsWith(REDIRECT_PREFIX);
    }

    @Override
    public void render(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(path.substring(REDIRECT_PREFIX.length()));
    }
}
