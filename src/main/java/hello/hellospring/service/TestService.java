package hello.hellospring.service;

import hello.hellospring.repository.order.DiscountPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public
class TestService {
    private final DiscountPolicy discountPolicy;

    public TestService(@Qualifier("rateDiscountPolicy") DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}
