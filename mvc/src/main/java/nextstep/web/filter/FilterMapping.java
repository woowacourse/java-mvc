package nextstep.web.filter;

import java.util.List;

public interface FilterMapping {

    void initialize();

    List<FilterChain> filters();
}
