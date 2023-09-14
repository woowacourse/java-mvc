package web.org.springframework.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

  @Test
  @DisplayName("find() : String으로 받은 HTTP Method 를 ENUM RequestHttpMethod 찾을 수 있다.")
  void test_find() throws Exception {
    //given
    final List<String> methods = List.of("GET", "get", "Get", "GEt");

    //when & then
    for (String method : methods) {
      assertEquals(RequestMethod.GET, RequestMethod.find(method));
    }
  }

  @Test
  @DisplayName("find() : RequestMethod에 없는 String method일 경우 IllegalArgumentException이 반환될 수 있다.")
  void test_find_IllegalArgumentException() throws Exception {
    //given
    final String method = "POSTING";

    //when & then
    assertThatThrownBy(() -> RequestMethod.find(method))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
