package nextstep.web.filter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationFilterMappingTest {

    @DisplayName("@WebFilter 어노테이션을 기반으로 Filter를 검색한다.")
    @Test
    void initialize() {
        String[] basePaths = new String[]{"samples"};
        AnnotationFilterMapping filterMapping = new AnnotationFilterMapping(basePaths);
        filterMapping.initialize();

        List<FilterChain> filters = filterMapping.filters();
        Assertions.assertThat(filters).hasSize(1);
    }

    @DisplayName("검색한 filter를 FilterChain 형태로 가져온다.")
    @Test
    void filters() {
        String[] basePaths = new String[]{"samples"};
        AnnotationFilterMapping filterMapping = new AnnotationFilterMapping(basePaths);
        List<FilterChain> filters = filterMapping.filters();

        Assertions.assertThat(filters).hasSize(1);
        Assertions.assertThat(filters.get(0).name()).isEqualTo("testResourceFilter");
        Assertions.assertThat(filters.get(0).uriPaths()).isEqualTo(new String[]{"/*"});
    }
}