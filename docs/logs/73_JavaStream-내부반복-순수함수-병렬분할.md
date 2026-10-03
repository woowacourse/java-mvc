# 73. Java Stream — 내부 반복, 순수한 변환, 병렬 분할

**날짜**: 2026-10-03  
**프로젝트**: java-mvc ([2단계 PR #1157](https://github.com/woowacourse/java-mvc/pull/1157))  
**재방문**: #70 어노테이션 라우트 등록, #67 스레드 실행 상태  
**분류**: Java·동시성 / java-mvc 미러 대상

## 출발점 — 등록 작업을 Stream으로 쓰는 게 맞나

AnnotationHandlerMapping에서 HTTP 메서드마다 HandlerKey와 HandlerExecution을 만들어 필드의 Map에 등록했다. Stream으로 바꾼 코드는 임시 Map을 collect(toMap)으로 만든 다음 forEach(this::put)으로 실제 등록을 했다. 책에서 “Stream에는 순수 함수를 쓰고, 외부 Map에 put하는 패턴을 피하라”는 조언을 본 기억 때문에 의문이 생겼다.

## Stream이 해결하려던 문제

보통의 for문은 요소를 어떻게 순회할지와 요소로 무엇을 할지를 함께 쓴다. Stream은 데이터 출처 → filter/map 같은 중간 연산 → sum/collect 같은 최종 연산으로 **원하는 결과**를 기술하고, 순회는 라이브러리에 맡긴다. 중간 연산은 지연되어 최종 연산에서 실행된다. 상태 없는 단계들은 중간 목록 없이 한 번의 통과로 이어질 수 있다. [OpenJDK 설계 설명](https://cr.openjdk.org/~briangoetz/lambda/lambda-libraries-final.html) · [Java Stream 문서](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html)

Stream이라는 문법 자체가 순수성을 보장하지는 않는다. 각 요소를 처리하는 함수가 공유 상태에 간섭하지 않아야 결과와 실행 순서를 이해하기 쉽고 병렬화에도 안전하다.

## 이번 코드에서 순수한 경계와 상태 변경의 경계

~~~java
requestMethod -> new HandlerKey(uri, requestMethod)
~~~

위 람다는 호출 시 정해진 불변 String uri를 포착한다. 같은 uri와 RequestMethod에 대해 값 동등성 기준으로 같은 키를 만들고 외부 상태를 바꾸지 않는다. 다른 enrollHandler 호출은 다른 uri를 포착한 별도의 함수가 된다. 매번 새 객체를 만들어 참조 동일성은 다르지만, HandlerKey의 의미는 URL과 HTTP 메서드 값이다.

반면 put(new HandlerKey(...), execution)은 handlerExecutions 필드를 변경한다. 따라서 enrollHandler 전체는 순수 함수가 아니다. collect(toMap)으로 임시 Map을 만든 뒤 forEach(this::put)을 해도 마지막 외부 상태 변경은 남는다. 이 메서드의 목적은 “결과 컬렉션 만들기”가 아니라 “기존 레지스트리에 등록하기”이므로 for문이 의도를 가장 직접적으로 드러낸다. collect 내부가 결과 컨테이너를 변경하는 것과 람다가 공유 필드를 변경하는 것은 경계가 다르다.

## 병렬 Stream의 실제 흐름

stream()은 기본적으로 순차 실행이다. parallelStream()에서는 Spliterator가 입력을 덩어리로 나누고 Fork/Join 작업이 각 덩어리를 처리한 뒤 부분 결과를 합친다. 요소마다 스레드를 하나씩 만드는 방식이 아니다. 여러 작업을 임의 순서로 처리할 수 있으므로 상태 없는 함수가 유리하고, 합치는 연산도 병렬로 결합 가능한 규칙을 가져야 한다. [OpenJDK 병렬 처리 설계](https://cr.openjdk.org/~briangoetz/lambda/lambda-libraries-final.html) · [Oracle 병렬 Stream](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html)

순수성은 병렬 안전성을 얻는 쉬운 방법이지 API의 절대 조건은 아니다. 항목 수가 많아도 계산이 아주 가볍거나 분할·결합 비용이 크면 병렬화가 느릴 수 있다. 현재 컨트롤러 매핑은 항목이 적고 공유 Map에 등록하므로 병렬 Stream의 대상이 아니다. 병렬 Stream의 ForkJoinPool은 플랫폼 스레드로 CPU 계산을 나눈다. 가상 스레드는 I/O 대기가 많은 작업의 캐리어 점유를 줄이는 다른 문제를 푼다. [JEP 444](https://openjdk.org/jeps/444)

## 한 문장 봉인

> Stream은 순회를 라이브러리에 맡겨 값의 선택·변환·집계를 표현한다. 각 요소의 처리를 독립적으로 만들면 순차 실행도 읽기 쉽고 병렬 분할도 안전해지지만, 기존 레지스트리를 변경하는 작업은 for문으로 쓰는 편이 명확하다.

## 학습 방식 회고

- 처음에는 “Stream으로 가능하다”와 “이 코드에 Stream이 적합하다”를 섞어 설명했다. 책의 put 반례를 현재 코드에 대입해 보면서 둘을 분리했다. 다음에는 API 허용 여부와 설계상 권장 여부를 따로 말한다.
- 학습자가 uri 포착을 보고 “다른 처리에서는 uri가 바뀌지 않나?”라고 물은 뒤, 호출마다 별도의 함수가 생성된다는 연결을 스스로 만들었다. 포착한 값과 함수 호출의 범위를 실제 코드에 붙여 설명한 것이 도움이 됐다.

## 재방문 질문과 다음 씨앗

- collect(toMap) 뒤 forEach(this::put)이 순수한 등록 작업이 아닌 이유는?
- 같은 RequestMethod라도 uri가 다른 두 HandlerKey 생성 람다가 각각 어떤 값을 기억하는가?
- 다음 씨앗: Spliterator.trySplit의 분할 품질과 ForkJoinPool의 work stealing. 현재 BACKLOG의 hot arc는 컴퓨터 구조 순회이므로 새 항목은 추가하지 않는다.
