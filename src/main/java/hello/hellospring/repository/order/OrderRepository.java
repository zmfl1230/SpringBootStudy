package hello.hellospring.repository.order;

import hello.hellospring.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save (Order order);
    Optional<Order> findById(long id);
    List<Order> findAll();
    void deleteById(long id);
    void deleteAll();

}
