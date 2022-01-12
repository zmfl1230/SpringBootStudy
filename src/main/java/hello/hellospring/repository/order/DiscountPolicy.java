package hello.hellospring.repository.order;

import hello.hellospring.domain.Member;

public interface DiscountPolicy {
    void validateMemberGrade(Member member);
    void validatePurchasePrice(int price);
    int discount(int price);
}
