package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RequestUrlTest {

    @Test
    void request_url을_생성한다() {
        // given
        String url = "/eden";

        // when
        RequestUrl requestUrl = new RequestUrl(url);

        // then
        assertThat(requestUrl).isEqualTo(new RequestUrl("/eden"));
    }

}
