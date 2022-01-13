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
    @Autowired DataSource dataSource;

    @Bean
    public OrderService orderService() {
        return new OrderService(discountPolicy(), orderRepository());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
    // 언제든 변경 가능한 할인 정책에 대해 유연하게 대응하기 위함
        return new FixDiscountPolicy();
    }

    @Bean
    public OrderRepository orderRepository() {
        return new JdbcOrderRepository(dataSource);
    }


}
