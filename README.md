# MVC 프레임워크 구현하기

## Annotation Based MVC 구현

## ISSUE

- MockHttpServletRequest/MockHttpServletResponse

ServletContainer에서 설정되는 HttpServletRequest/Response를 테스트에 사용하기 위해서는 fake 객체가 필요. 모든 메소드들을 직업 다 설정하는
방법 vs Mockito.mock 사용.
fake 객체의 경우 안에 인수로 넘겨줬을 때, 내부적인 로직이 수정(null체크와 같은)된다면, 이 테스트의 영향을 줄 수가 있음.
-> Mock을 이용하여 진행

- 어떻게 Legacy와 동시에 작업을 해나갈 것인가?

~~url path를 다르게 두어서 별도로 테스트를 진행할 수 있도록.~~
test를 두어 진행.
