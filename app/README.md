# 미션 요구사항 

## 1단계 요구사항
어노테이션 기반으로 URL을 컨트롤러에 매핑한다.
- [x] 컨트롤러 어노테이션이 있는 메서드를 모두 조회하여 URL과 Method를 비교한다.
- [x] URL과 Method에 매핑된 컨트롤러 메서드가 실행되도록 만든다.
- [x] Method가 지정되어있지 않다면 모든 Method에 대해 매핑한다.
- [x] 지원하지 않는 Method에 대한 요청의 경우 예외를 반환한다.

## 2단계 요구사항

 `@Controller` 어노테이션 기반의 컨트롤러 등록도 지원해주는 프레임워크를 만든다.
- [x] DispatcherServlet이 AnnotationHandlerMapping을 통해 ModelAndView를 받도록 구현
- [x] 핸들러 매핑을 추상화하여 어노테이션 기반과 인터페이스 기반의 매핑을 모두 지원하도록 수정
