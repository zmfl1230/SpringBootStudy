package hello.hellospring.repository.order;

public interface DiscountPolicy {
    int discount(int price);
}
