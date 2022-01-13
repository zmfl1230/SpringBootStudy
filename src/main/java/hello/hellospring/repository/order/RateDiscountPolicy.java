package hello.hellospring.repository.order;

import hello.hellospring.domain.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int price) {
        return 0;
    }
}
