package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.AppWebApplicationInitializer;
import com.techcourse.JwpApplication;
import nextstep.core.AnnotationConfigApplicationContext;
import nextstep.test.MockMvc;
import nextstep.test.MockMvcResponse;
import nextstep.web.support.ContentType;
import nextstep.web.support.MediaType;
import nextstep.web.support.StatusCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberControllerTest {

    private static MockMvc mvc;

    @BeforeAll
    static void beforeAll() {
        mvc = new MockMvc(new AnnotationConfigApplicationContext(JwpApplication.class));
    }

    @Test
    @DisplayName("members test")
    public void membersTest() throws Exception{
        final MockMvcResponse result =
            mvc.get("/members")
                .result();

        assertThat(result.getHeaders().get(ContentType.headerKey()))
            .isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(result.getStatusCode()).isEqualTo(StatusCode.OK);
        assertThat(result.getJsonBody())
            .isEqualTo("[{\"id\":1,\"account\":\"gugu\",\"password\":\"password\",\"email\":\"hkkang@woowahan.com\"}]");
    }
}
