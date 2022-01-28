package hello.hellospring.factory;

import hello.hellospring.repository.order.*;
import hello.hellospring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Configuration
public class OrderFactory {
    @PersistenceContext
    EntityManager em;

    @Autowired  DataSource dataSource;

    @Bean
    public OrderService orderService() {
        return new OrderService(discountPolicy(), orderRepository());
    }

    @Bean
    /*
    역할이라 함은
    역할은 추상적인 것으로써 어떤 로미오라는 역할이 있으나 구체적으로 어떤 배우가 이 로미오의 역을 맡아 연기를 해줄지에 대해서는 아직 모르는 것.
    이 설정 클래스에선 이 각각의 역할에 구체적인 구현체를 생성해 인자로 넣어준다.
    * */

    @Primary
    @Qualifier("fixDiscountPolicy")
    public DiscountPolicy discountPolicy() {
        // 언제든 변경 가능한 할인 정책에 대해 유연하게 대응하기 위체
        // 구체적인 구현
        return new FixDiscountPolicy();
    }

    @Bean
    @Qualifier("rateDiscountPolicy")
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }

    @Bean
    // 역할
    public OrderRepository orderRepository() {
        // 구체적인 구현체
//        return new JdbcOrderRepository(dataSource);
        return new JpaOrderRepository(em);
    }


}
