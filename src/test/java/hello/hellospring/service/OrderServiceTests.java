package hello.hellospring.service;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
[만들고자 하는 기능 정의]
- 주문 생성
    - 사용자가 어떤 상품(상품명, 가격)을 주문한다.

- 사용자의 등급 별 할인이 적용
    - VIP 등급: 현재 적용된 할인 정책에 따라 주문이 생성되었는지 확인한다.
    - 일반 등급: 주문된 가격 그대로 주문이 생성되었는지 확인한다.

- 할인 적용 조건 검증
    - 10000원 이상으로 구매한 경우에만 할인 정책을 적용한다.
*/

public class OrderServiceTests {
    @Autowired MemberService memberService;
    @Autowired OrderService orderService;

    @Test
    public void createOrder() {
        Member member = new Member();
        member.setName("member1");
        member.setGrade(Grade.BASIC);

        Product product = new Product("product1", 9000);

        Order order = orderService.orderItem(member, product);

        Assertions.assertThat(mem)
        order.getBuyerName()


    }
}
