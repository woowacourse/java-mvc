package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class VoidView implements View {

    public static VoidView CACHE = new VoidView();

    private VoidView() {
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request,
                       HttpServletResponse response) {
        throw new UnsupportedOperationException("정해지지 않은 뷰 형식입니다.");
    }
}
