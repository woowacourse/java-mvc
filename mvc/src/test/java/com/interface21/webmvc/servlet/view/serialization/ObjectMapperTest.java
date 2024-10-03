package com.interface21.webmvc.servlet.view.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("모든 private 필드가 getter로 열려있는 경우 정상적으로 직렬화한다.")
    @Test
    void should_serialize_when_givenAllGetter() throws JsonProcessingException {
        // given
        TargetWithGetter target = new TargetWithGetter(1L, "account", "password", "email");

        // when
        String json = objectMapper.writeValueAsString(target);

        // then
        assertThat(json).isEqualTo("{\"id\":1,\"account\":\"account\",\"password\":\"password\",\"email\":\"email\"}");
    }

    @DisplayName("private 필드가 getter로 열려있지 않은 경우 예외를 반환한다.")
    @Test
    void should_notSerialize_when_notGivenGetter() {
        // given
        TargetWithoutGetter target = new TargetWithoutGetter(1L, "account", "password", "email");

        // when & then
        assertThatThrownBy(() -> objectMapper.writeValueAsString(target))
                .isInstanceOf(InvalidDefinitionException.class)
                .hasMessageContaining("No serializer found")
                .hasMessageContaining("no properties discovered to create BeanSerializer");
    }

    @DisplayName("getter가 선언된 필드만 직렬화한다.")
    @Test
    void should_serializePartially_when_notGivenAllGetter() throws JsonProcessingException {
        // given
        TargetWithPartialGetter target = new TargetWithPartialGetter(1L, "account", "password", "email");

        // when
        String json = objectMapper.writeValueAsString(target);

        // then
        assertThat(json).isEqualTo("{\"id\":1}");
    }
}
