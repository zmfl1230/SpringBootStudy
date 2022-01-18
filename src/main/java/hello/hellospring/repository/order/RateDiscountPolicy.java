package hello.hellospring.repository.order;

import hello.hellospring.CommonConstant;
import hello.hellospring.domain.Member;

public class RateDiscountPolicy implements DiscountPolicy {
    private final float PAYMENT_AMOUNT_ON_RATE_DISCOUNT = (1 - CommonConstant.RATE_DISCOUNT);
    @Override
    public int discount(int price) {
        return (int) (price * PAYMENT_AMOUNT_ON_RATE_DISCOUNT);
    }
}
