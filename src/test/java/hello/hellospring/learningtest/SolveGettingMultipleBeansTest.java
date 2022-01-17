package hello.hellospring.learningtest;

import hello.hellospring.repository.order.RateDiscountPolicy;
import hello.hellospring.service.TestService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


/**
 * 어노테이션을 이용한 자동 DI 시, 지정한 타입과 관련된 복수의 빈 오브젝트가 존재할 때 주입할 오브젝트 명시 방법
 * 1. @Autowired 필드명 매칭
 * 2. @Qulifier(@String: matchingName)
 * 3. @Primary 사용
 *
 * 해결 방법의 우선순위 테스트 시나리오 (가정, 구체성이 강항 @Qulifier 에서 ->  @Primary 순으로 처리된다.)
 * 1. DiscountPolice 클래스 소속인 FixDiscountPolicy, RateDiscountPolicy 모두 빈으로 등록해둔다.
 * 2. 이때 @Primary를 FixDiscountPolicy 호출 메서드에 붙여놓는다.
 * 3. FixDiscountPolicy, RateDiscountPolicy 모두 @Qulifier(@String)를 지정해 둔다.
 * 4. TestService 생성자에 파라미터에 @Qualifier("RateDiscountPolicy")로 주입되도록 한다.
 * 5. TestService 내부 필드에 저장된 값을 가져와서 RateDiscountPolicy와 맞는지 확인한다.
 *
 * */

@SpringBootTest
public class SolveGettingMultipleBeansTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("중복 빈 조회 시, @Qulifier보다 @Primary의 우선순위가 낮다")
    public void checkPriority() {
        TestService testService = applicationContext.getBean("testService", TestService.class);
        RateDiscountPolicy rateDiscountPolicy = applicationContext.getBean("rateDiscountPolicy", RateDiscountPolicy.class);

        Assertions.assertThat(testService.getDiscountPolicy()).isSameAs(rateDiscountPolicy);

    }
}

