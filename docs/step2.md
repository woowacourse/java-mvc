# Step 2 - 점진적인 리팩터링

## Legacy MVC와 @MVC 통합하기

- [ ] Legacy MVC와 Annotation MVC가 함께 동작한다.
    - [ ] 기존 Controller 인터페이스 기반 컨트롤러는 변경 없이 계속 동작한다.
    - [ ] @Controller + @RequestMapping 기반 컨트롤러도 동작한다.
    - [ ] 두 방식이 같은 애플리케이션에서 동시에 동작한다.
    - [ ] 요청에 대응하는 핸들러를 찾을 수 있다.
    - [ ] 핸들러 종류에 맞는 실행 방식을 선택할 수 있다.
    - [ ] 신규 방식 도입 때문에 기존 Legacy 컨트롤러 전체를 수정하지 않는다.
