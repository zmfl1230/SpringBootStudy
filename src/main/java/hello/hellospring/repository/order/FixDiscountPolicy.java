package hello.hellospring.repository.order;

import hello.hellospring.domain.Member;

public class FixDiscountPolicy implements DiscountPolicy {
    @Override
    public void validateMemberGrade(Member member) {

    }

    @Override
    public void validatePurchasePrice(int price) {

    }

    @Override
    public int discount(int price) {
        return 0;
    }
}
