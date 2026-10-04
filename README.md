# 만들면서 배우는 스프링

## @MVC 구현하기

### 학습목표
- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 시작 가이드
1. 미션을 시작하기 전에 학습 테스트를 먼저 진행합니다.
    - [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
    - 나머지 학습 테스트는 강의 시간에 풀어봅시다.
2. 학습 테스트를 완료하면 LMS의 1단계 미션부터 진행합니다.

## 학습 테스트
1. [Reflection API](study/src/test/java/reflection)
2. [Servlet](study/src/test/java/servlet)

----

## 공부한 내용


### forward와 Redirect
```
requestDispatcher.forward(request, response); // 서버 내부 처리 위임해서 기존 요청으로 응답
response.sendRedirect(jspPath); // 리다이렉트 응답 → 브라우저가 새 요청
```

jsp.render()에서는 리다이렉트 또는 포워딩으로 동작한다.
requestDispatcher.forward()는 새로운 요청 호출이 아니라 기존 요청에서 JSP로 처리를 넘겨 모델 데이터로 HTML응답을 만든다.
response.sendRedirect()는 리다이렉트 응답을 보내서, 브라우저가 새로운 요청을 호출하도록 한다.

### ReflectiveOperationException
getDeclaredConstructor()는 NoSuchMethodException을 던지고, 
newInstance()는 InvocationTargetException, InstantiationException, IllegalAccessException을 던진다. 

모두 ReflectiveOperationException에 묶어서 처리할 수 있다. 

### AnnotationHandlerMapping

1. AnnotationHandlerMapping에서 @Controller 가 붙은 클래스 탐색
2. 찾은 클래스에서 @RequestMapping이 붙은 메서드 탐색
3. RequestMethod(HttpMethod)와 url을 기준으로 HandlerKey를 생성
   - RequestMethod가 지정되지 않았다면 모든 메서드를 허용
4. Controller와 Method로 HandlerExecution 생성
5. key: HandlerKey, value: HandlerExecution으로 된 handlerMapping에 추가

url과 HttpMethod를 기준으로 HandlerExecution을 매핑하여 초기화한다.