package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("레지스터 뷰 컨트롤러 테스트")
class RegisterViewControllerTest {

    @DisplayName("GET 요청 시")
    @Nested
    class RegisterView{

        @DisplayName("register.jsp를 출력하는지 확인")
        @Test
        void registerViewTest() {
            //given
            RegisterViewController registerViewController = new RegisterViewController();
            String expectedResult = "/register.jsp";
            //when
            String destination = registerViewController.registerView();
            //then
            assertThat(destination).isEqualTo(expectedResult);
        }
    }
}