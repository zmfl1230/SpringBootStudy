package hello.hellospring.repository.order;

import hello.hellospring.CommonConstant;

public class FixDiscountPolicy implements DiscountPolicy {
    private final int PAYMENT_AMOUNT_ON_FIX_DISCOUNT = CommonConstant.FIX_DISCOUNT;

    @Override
    public int discount(int price) {
        return price - PAYMENT_AMOUNT_ON_FIX_DISCOUNT;
    }
}
