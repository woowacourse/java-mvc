package nextstep.test;

import java.util.HashMap;
import java.util.Map;
import nextstep.web.support.StatusCode;

public class MockMvcResponse {

    private Map<String, String> headers = new HashMap<>();
    private StatusCode statusCode;
    private FakeWriter fakeWriter;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getJsonBody() {
        return fakeWriter.getBody();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addWriter(FakeWriter writer) {
        this.fakeWriter = writer;
    }
}
