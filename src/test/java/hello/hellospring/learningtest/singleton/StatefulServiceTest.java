package hello.hellospring.learningtest.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 상태를 갖는 클래스를 싱글톤 오브젝트로 만들었을 때의 위험을 테스트
 * */

public class StatefulServiceTest {
    ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonLearningTestNotUsingConfigurationConfig.class);

    @Test
    @DisplayName("싱글톤 오브젝트 내에 상태를 가졌을 때 해당 오브젝트에 접근하는 여러 클라이언트에 의해 상태 값이 덮어 씌어진다")
    public void StatefulServiceSingleton() {
        StatefulService client1 = ac.getBean(StatefulService.class);
        StatefulService client2 = ac.getBean(StatefulService.class);

        int CLIENT1_ORDER_PRICE = 10000;
        int CLIENT2_ORDER_PRICE = 8000;

        // client1 이 10000원짜리 상품을 주문
        client1.order("product1", CLIENT1_ORDER_PRICE);

        // client2 가 8000원짜리 상품을 주문
        client2.order("product2", CLIENT2_ORDER_PRICE);

        //client 1이 주문한 가격을 가져와 보고 싶음 -> 일치하지 않음 (price는 공유되는 필드임으로 매 클라이언트 주문건마다 덮어 씌어짐)
        Assertions.assertThat(CLIENT1_ORDER_PRICE).isNotEqualTo(client1.getPrice());
        System.out.println("order price = " + client1.getPrice());

    }
}
