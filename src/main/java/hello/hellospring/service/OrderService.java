package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.domain.Order;
import hello.hellospring.domain.Product;
import hello.hellospring.repository.order.DiscountPolicy;

public class OrderService {
    DiscountPolicy discountPolicy;

    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public Order orderItem(Member buyer, Product product) {
        Order order = new Order();
        order.setBuyer(buyer);
        order.setProduct(product);

        order.setPaymentAmount(getPaymentAmountOnDiscountPolicy(buyer, product));
        // TODO: repository 생성 후, 정의
        // order.save()
        return order;
    }

    public int getPaymentAmountOnDiscountPolicy(Member buyer, Product product) {
        try {
            discountPolicy.validateMemberGrade(buyer);
            discountPolicy.validatePurchasePrice(product.getPrice());
            return discountPolicy.discount(product.getPrice());
            // TODO: exception 세부화
        } catch (Exception e) {
            return product.getPrice();
        }
    }
}
