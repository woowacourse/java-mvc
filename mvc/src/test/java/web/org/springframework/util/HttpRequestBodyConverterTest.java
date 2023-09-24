package web.org.springframework.util;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HttpRequestBodyConverterTest {

    @Test
    void JSON_형식의_데이터를_받아서_객체로_변환한다() throws Exception {
        // given
        final String json = "{\"username\":\"test1234\",\"password\":\"test1234!\"}";

        // when
        final TestDto serialized = (TestDto) HttpRequestBodyConverter.serialize(json, TestDto.class);

        // then
        assertSoftly(softly -> {
            softly.assertThat(serialized.getUsername()).isEqualTo("test1234");
            softly.assertThat(serialized.getPassword()).isEqualTo("test1234!");
        });
    }

    static class TestDto {

        private String username;
        private String password;

        public TestDto() {
        }

        public TestDto(final String username, final String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
