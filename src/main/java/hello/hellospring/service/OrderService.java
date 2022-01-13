package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.domain.Order;
import hello.hellospring.domain.Product;
import hello.hellospring.repository.order.DiscountPolicy;
import hello.hellospring.exception.NotPermissionToDiscount;
import hello.hellospring.repository.order.OrderRepository;
import hello.hellospring.validator.DiscountValidator;

public class OrderService {
    DiscountPolicy discountPolicy;
    OrderRepository orderRepository;

    public OrderService(DiscountPolicy discountPolicy, OrderRepository orderRepository) {
        this.discountPolicy = discountPolicy;
        this.orderRepository = orderRepository;
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
            DiscountValidator.validateMemberGrade(buyer);
            DiscountValidator.validatePurchasePrice(product.getPrice());
            return discountPolicy.discount(product.getPrice());
        } catch (NotPermissionToDiscount e) {
            return product.getPrice();
        }
    }

}
