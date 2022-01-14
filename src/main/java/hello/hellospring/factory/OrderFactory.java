package hello.hellospring.factory;

import hello.hellospring.repository.order.DiscountPolicy;
import hello.hellospring.repository.order.FixDiscountPolicy;
import hello.hellospring.repository.order.JdbcOrderRepository;
import hello.hellospring.repository.order.OrderRepository;
import hello.hellospring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class OrderFactory {
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
    public DiscountPolicy discountPolicy() {
        // 언제든 변경 가능한 할인 정책에 대해 유연하게 대응하기 위체
        // 구체적인 구현
        return new FixDiscountPolicy();
    }

    @Bean
    // 역할
    public OrderRepository orderRepository() {
        // 구체적인 구현체
        return new JdbcOrderRepository(dataSource);
    }


}
