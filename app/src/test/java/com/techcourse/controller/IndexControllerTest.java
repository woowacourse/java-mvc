package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("인덱스 컨트롤러 테스트")
class IndexControllerTest {

    @DisplayName("GET 요청 시")
    @Nested
    class IndexGet{

        @DisplayName("index.jsp를 반환하는지 확인")
        @Test
        void indexDestinationTest() {
            //given
            IndexController indexController = new IndexController();
            String expectedResult = "/index.jsp";
            //when
            String destination = indexController.goHome();
            //then
            assertThat(destination).isEqualTo(expectedResult);
        }
    }
}