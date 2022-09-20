package nextstep.web.support;

import nextstep.web.exception.InvalideRequestFormatException;

public class RequestUrl {

    private final String value;

    public RequestUrl(final String value) {
        validateRequestFormat(value);
        this.value = value;
    }

    private void validateRequestFormat(final String value) {
        if (!value.startsWith("/")) {
            throw new InvalideRequestFormatException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequestUrl that = (RequestUrl) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
