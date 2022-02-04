## `스프링` 및 `스프링 부트`를 학습합니다.

### 다음과 같은 목표를 가지고 있습니다 :)
- `DI`에 기반한 코드 설계에 집중합니다.
- 구체 클래스가 아닌 `추상(인터페이스)`에 의존하도로 설계합니다. 이를 통해 `의존성 역전 원칙(DIP)`과 `개방 폐쇄 원칙(OCP)`을 실현하도록 합니다.
- `전략 패턴`, `템플릿/콜백 패턴`을 따라 `개방 폐쇄 원칙(OCP)` 실현에 집중합니다.
- 기능 구현 이전, 테스트 코드를 먼저 작성한 뒤 기능을 개발해 가는 `테스트 주도 개발`에 집중합니다.
- 스프링관련 `학습 테스트`를 만들어 스프링 사용법에 대해 익힙니다.


</br>


### 아래는 다양한 테스트를 진행하고 고찰한 내용을 담은 파일경로들을 기록합니다 :)

✍🏻 [PrototypeProvider](https://github.com/zmfl1230/springboot-study/blob/0ae589d6977c68e542c830f16b1bc9e7ca622ab3/src/test/java/hello/hellospring/learningtest/provider/PrototypeProviderTest.java)
- 싱글톤 빈과 프로토타입 빈을 동시에 사용할 경우 발생하는 문제 및 해결과정

✍🏻 [값 주입](https://github.com/zmfl1230/springboot-study/blob/0ae589d6977c68e542c830f16b1bc9e7ca622ab3/src/test/java/hello/hellospring/learningtest/ValueInjectionTest.java)
- 값 주입 발생 시점  

✍🏻 [빈 등록 우선순위](https://github.com/zmfl1230/springboot-study/blob/0ae589d6977c68e542c830f16b1bc9e7ca622ab3/src/test/java/hello/hellospring/learningtest/SolveGettingMultipleBeansTest.java)
- 자동 주입 시, 같은 이름의 복수의 빈 오브젝트가 존재할 때 해결방법

✍🏻 [빈 등록 순서](https://github.com/zmfl1230/springboot-study/blob/0ae589d6977c68e542c830f16b1bc9e7ca622ab3/src/test/java/hello/hellospring/learningtest/BeanRegisterOrder.java)
- 빈 등록 시, 오브젝트 생성 과정에서 의존성 주입에 필요한 오브젝트가 아직 빈으로 등록되지 않은 경우

✍🏻 [ApplicationContext1](https://github.com/zmfl1230/springboot-study/blob/0ae589d6977c68e542c830f16b1bc9e7ca622ab3/src/test/java/hello/hellospring/learningtest/ApplicationContextInfoTest.java)

✍🏻 [ApplicationContext2](https://github.com/zmfl1230/springboot-study/blob/0ae589d6977c68e542c830f16b1bc9e7ca622ab3/src/test/java/hello/hellospring/learningtest/ApplicationContextBasicFindTest.java)
