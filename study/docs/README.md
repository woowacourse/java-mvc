## 메서드 반환
- `getDeclaredMethods()`
  - 클래스 또는 인터페이스의 모든 선언된 메소드를 반환
  - 접근 제한자에 상관없이 모든 메소드를 반환한다.
  - 상속받은 메소드는 포함하지 않는다.
- `getMethods()`
  - 클래스 또는 인터페이스의 모든 `public` 메소드를 반환
  - 상위 클래스나 인터페이스로부터 상속된 `public` 메소드도 포함한다.

## 이름 반환
- `getSimpleName()`
  - 클래스의 단순 이름 반환
  - 패키지를 제외한 클래스 이름만 반환
```java
Class<?> clazz = java.util.ArrayList.class;
System.out.println(clazz.getSimpleName()); // "ArrayList"
```
- `getName()`
  - 전체 이름 반환
  - 패키지를 포함한 전체 경로 반환
  - 배열 클래스의 경우 배열의 구성을 나타내는 문자가 포함된다. 
    - 예를 들어, int[]는 [I로 표시된다.
```java
Class<?> clazz = java.util.ArrayList.class;
System.out.println(clazz.getName()); // "java.util.ArrayList"
```
- `getCanonicalName()`
  - 정규화된 이름 반환
  - 클래스가 실제로 존재하고, 법적으로 사용할 수 있는 이름을 반환
  - 익명 클래스나 지역 클래스를 다룰 때 null을 반환할 수 있습니다.
  - 배열의 경우, 배열 타입을 표현하는 정규화된 이름을 반환한다. 
    - 예를 들어, int[]는 int[]로 표시된다.
```java
Class<?> clazz = java.util.ArrayList.class;
System.out.println(clazz.getCanonicalName()); // "java.util.ArrayList"
```

### `getName()`의 배열 반환 방식
#### 배열 이름의 구성 방식
1. 차원
   - 배열의 차원(1차원, 2차원 등)에 따라 대괄호([)가 사용된다.
   - 배열의 차원이 늘어날수록 대괄호의 개수가 증가한다.
2. 타입
   - 원시 타입의 경우 한 글자 기호로 나타난다.
   - 참조 타입(객체)의 경우 `L`로 시작하고, 그 뒤에 패키지 이름과 클래스 이름이 붙으며 마지막에 세미콜론(;)이 추가된다.
#### 타입을 나타내는 기호
- 원시 타입
   - Z : boolean
   - B : byte
   - C : char
   - D : double
   - F : float
   - I : int
   - J : long
   - S : short
- 참조 타입(객체)
  - `L<패키지 이름>.<클래스 이름>;`로 표현된다.

#### 예시:
```java
int[] intArray = new int[5];
System.out.println(intArray.getClass().getName()); // [I

int[][] twoDIntArray = new int[5][5];
System.out.println(twoDIntArray.getClass().getName()); // [[I

String[] stringArray = new String[5];
System.out.println(stringArray.getClass().getName()); // [Ljava.lang.String;

String[][] twoDStringArray = new String[5][5];
System.out.println(twoDStringArray.getClass().getName()); // [[Ljava.lang.String;
```

## 생성자 반환
- `getConstructors()`
  - 해당 클래스의 `public` 생성자만 반환
  - 상속된 클래스나 다른 접근 제어자의 생성자는 반환하지 않음
- `getDeclaredConstructors()`
  - 모든 생성자 반환
  - 상속된 클래스의 생성자는 제외

## 필드 반환
- `getFields()`
  - 클래스에 선언된 `public` 필드만 반환
  - 상위 클래스에서 상속된 `public` 필드도 포함
- `getDeclaredFields()`
  - 클래스에 선언된 모든 필드 반환
  - 상위 클래스에서 상속된 필드는 포함하지 않는다.
