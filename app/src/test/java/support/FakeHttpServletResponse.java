package support;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class FakeHttpServletResponse implements HttpServletResponse {
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
    public int getStatus() {
        return 0;
    }

    @Override
    public void setStatus(final int sc) {

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
    public void setCharacterEncoding(final String charset) {

    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void setContentType(final String type) {

    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public void setContentLength(final int len) {

    }

    @Override
    public void setContentLengthLong(final long length) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void setBufferSize(final int size) {

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
    public Locale getLocale() {
        return null;
    }

    @Override
    public void setLocale(final Locale loc) {

    }

}
