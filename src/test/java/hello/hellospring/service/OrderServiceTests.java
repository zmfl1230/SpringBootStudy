package hello.hellospring.service;

import hello.hellospring.domain.Grade;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
[만들고자 하는 기능 정의]
- 주문 생성
    - 사용자가 어떤 상품(상품명, 가격)을 주문한다.

    할인 정책의 종류
    - FixDiscountPolicy: 정액 할인 정책(1000원 할인)
    - RateDiscountPolicy: 정률 할인 정책(10% 할인)

- 할인 적용 조건 검증
    - 10000원 이상으로 구매한 경우에만 할인 정책을 적용한다.
    - 사용자의 등급 별 할인이 적용
         - VIP 등급: 현재 적용된 할인 정책에 따라 주문이 생성되었는지 확인한다.
         - 일반 등급: 주문된 가격 그대로 주문이 생성되었는지 확인한다.

*/

public class OrderServiceTests {

    @Autowired MemberService memberService;
    @Autowired OrderService orderService;

    private final int STANDARD_PRICE = 10000;

    private final int FIX_DISCOUNT = 1000;
    private final float RATE_DISCOUNT = 0.1f;
    private final int PAYMENT_AMOUNT_ON_FIX_DISCOUNT = STANDARD_PRICE - FIX_DISCOUNT;
    private final float PAYMENT_AMOUNT_ON_RATE_DISCOUNT = STANDARD_PRICE * (1 - RATE_DISCOUNT);

    private Member memberBasic;
    private Member memberVip;
    private Product productHigherStandard;
    private Product productLowerStandard;

    @BeforeEach
    public void setUp() {
        //given
        memberBasic = new Member();
        memberBasic.setName("member basic");
        memberBasic.setGrade(Grade.BASIC);

        memberVip = new Member();
        memberVip.setName("member vip");
        memberVip.setGrade(Grade.VIP);

        productHigherStandard = new Product();
        productHigherStandard.setName("productHigher");
        productHigherStandard.setPrice(12000);

        productLowerStandard = new Product();
        productLowerStandard.setName("productLower");
        productLowerStandard.setPrice(9000);
    }

    @Test
    public void createOrder() {

        //when
        Order order = orderService.orderItem(memberBasic, productHigherStandard);

        //then
        Assertions.assertThat(memberBasic.getName()).isEqualTo(order.getBuyerName());
        Assertions.assertThat(memberBasic.getName()).isEqualTo(order.getProductName());

    }

    @Test
    public void checkDiscountPolicy() {

        //when
        Order orderVip = orderService.orderItem(memberVip, productHigherStandard);

        //then
        Assertions.assertThat(orderService.getPaymentAmoutnOnDiscountPolicy()).isEqualTo(orderBasic.getPaymentAmount());
    }

    @Test
    public void ValidateConditionToApplyDiscount() {
        //when
        Order orderBasicLowerStandard = orderService.orderItem(memberBasic, productLowerStandard);
        Order orderBasicHigherStandard = orderService.orderItem(memberBasic, productHigherStandard);

        Order orderVipLowerStandard = orderService.orderItem(memberVip, productLowerStandard);
        Order orderVipHigherStandard = orderService.orderItem(memberVip, productHigherStandard);

        //then
        // 할인 적용이 불가능한 경우
        Assertions.assertThat(9000).isEqualTo(orderBasicLowerStandard.getPaymentAmount());
        Assertions.assertThat(12000).isEqualTo(orderBasicHigherStandard.getPaymentAmount());

        Assertions.assertThat(9000).isEqualTo(orderVipLowerStandard.getPaymentAmount());

        // 정상적으로 할인이 적용되는 경우 (오로지 등급이 Vip 이며, 10000원 이상 구매했을 경우 할인이 적용된다.)
        Assertions.assertThat(orderService.getPaymentAmoutnOnDiscountPolicy()).isEqualTo(orderVipHigherStandard.getPaymentAmount());
    }


}
