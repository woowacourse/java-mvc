package nextstep.mvc.controller;

import nextstep.web.support.RequestMethod;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + requestMethod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HandlerKey that = (HandlerKey) o;

        return url.equals(that.url) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + requestMethod.hashCode();
        return result;
    }
}
