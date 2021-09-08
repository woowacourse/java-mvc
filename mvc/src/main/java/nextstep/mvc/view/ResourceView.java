package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Map;

public class ResourceView implements View {

    private static final String CHARSET = "UTF-8";

    private final String url;

    public ResourceView(final String url) {
        this.url = url;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final File resource = new File(url);
        response.setContentType(Files.probeContentType(resource.toPath()));
        response.setCharacterEncoding(CHARSET);

        final PrintWriter outputStream = response.getWriter();
        outputStream.print(Files.readString(resource.toPath()));
    }
}
