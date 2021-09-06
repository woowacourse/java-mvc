package nextstep.web.filter;

import jakarta.servlet.Filter;

public class FilterChain {

    private final String name;
    private final String[] uriPaths;
    private final Filter filter;

    public FilterChain(String name, String[] uriPaths, Filter filter) {
        this.name = name;
        this.uriPaths = uriPaths;
        this.filter = filter;
    }

    public String name() {
        return name;
    }

    public String[] uriPaths() {
        return uriPaths;
    }

    public Filter filter() {
        return filter;
    }
}
