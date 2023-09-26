package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import web.org.springframework.http.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class JsonViewTest {

    @Test
    void testRenderWhenSingleValue() throws Exception {
        final HttpServletRequest request = null;
        final TestHttpServletResponse response = new TestHttpServletResponse();

        final Map<String, Object> model = new HashMap<>();
        final TestObject testObject = new TestObject(1L, "test@test.com", "password");
        model.put("test", testObject);

        final JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo("{\"id\":1,\"email\":\"test@test.com\",\"password\":\"password\"}");
    }

    @Test
    void testRenderWhenNotSingleValue() throws Exception {
        final HttpServletRequest request = null;
        final TestHttpServletResponse response = new TestHttpServletResponse();

        final Map<String, Object> model = new HashMap<>();
        final TestObject testObject1 = new TestObject(1L, "test@test.com", "password");
        final TestObject testObject2 = new TestObject(2L, "test2@test.com", "password2");
        model.put("test1", testObject1);
        model.put("test2", testObject2);

        final JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(
                "{\"test2\":{\"id\":2,\"email\":\"test2@test.com\",\"password\":\"password2\"}," +
                        "\"test1\":{\"id\":1,\"email\":\"test@test.com\",\"password\":\"password\"}}");
    }

    static class TestObject {
        private final Long id;
        private final String email;
        private final String password;

        public TestObject(final Long id, final String email, final String password) {
            this.id = id;
            this.email = email;
            this.password = password;
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    static class TestHttpServletResponse implements HttpServletResponse {

        private String contentType;
        private final StringWriter stringWriter = new StringWriter();

        @Override
        public void addCookie(final Cookie cookie) {

        }

        @Override
        public boolean containsHeader(final String name) {
            return false;
        }

        @Override
        public String encodeURL(final String url) {
            return null;
        }

        @Override
        public String encodeRedirectURL(final String url) {
            return null;
        }

        @Override
        public void sendError(final int sc, final String msg) throws IOException {

        }

        @Override
        public void sendError(final int sc) throws IOException {

        }

        @Override
        public void sendRedirect(final String location) throws IOException {

        }

        @Override
        public void setDateHeader(final String name, final long date) {

        }

        @Override
        public void addDateHeader(final String name, final long date) {

        }

        @Override
        public void setHeader(final String name, final String value) {

        }

        @Override
        public void addHeader(final String name, final String value) {

        }

        @Override
        public void setIntHeader(final String name, final int value) {

        }

        @Override
        public void addIntHeader(final String name, final int value) {

        }

        @Override
        public void setStatus(final int sc) {

        }

        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public String getHeader(final String name) {
            return null;
        }

        @Override
        public Collection<String> getHeaders(final String name) {
            return null;
        }

        @Override
        public Collection<String> getHeaderNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(stringWriter);
        }

        @Override
        public void setCharacterEncoding(final String charset) {
        }

        @Override
        public void setContentLength(final int len) {

        }

        @Override
        public void setContentLengthLong(final long len) {

        }

        @Override
        public void setContentType(final String type) {
            this.contentType = type;
        }

        @Override
        public void setBufferSize(final int size) {

        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public void flushBuffer() throws IOException {

        }

        @Override
        public void resetBuffer() {

        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {

        }

        @Override
        public void setLocale(final Locale loc) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }

        public String getContentAsString() {
            return stringWriter.toString();
        }
    }
}
