package testsupport;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

public class TestResponse {
    public final HttpServletResponse response = mock(HttpServletResponse.class);
    private final ByteArrayOutputStream body = new ByteArrayOutputStream();
    private String contentType;
    private Charset encoding = StandardCharsets.ISO_8859_1;
    private PrintWriter writer;

    public TestResponse() throws Exception {
        doAnswer(invocation -> {
            contentType = invocation.getArgument(0);
            int index = contentType.indexOf("charset=");
            if (index >= 0 && writer == null) {
                encoding = Charset.forName(contentType.substring(index + "charset=".length()));
            }
            return null;
        }).when(response).setContentType(anyString());
        when(response.getWriter()).thenAnswer(invocation -> {
            if (writer == null) {
                writer = new PrintWriter(new OutputStreamWriter(body, encoding));
            }
            return writer;
        });
    }

    public String getContentType() {
        return contentType;
    }

    public String getCharacterEncoding() {
        return encoding.name();
    }

    public byte[] getContentAsByteArray() {
        if (writer != null) {
            writer.flush();
        }
        return body.toByteArray();
    }

    public String getContentAsString() {
        return new String(getContentAsByteArray(), encoding);
    }
}
