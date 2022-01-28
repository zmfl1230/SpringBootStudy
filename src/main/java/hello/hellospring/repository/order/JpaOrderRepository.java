package hello.hellospring.repository.order;

import hello.hellospring.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepository implements OrderRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Order save(Order order) {
        em.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findById(long id) {
        return Optional.of(em.find(Order.class, id));
    }

    @Override
    public List<Order> findAll() {
        return em.createQuery("select o from orders o", Order.class)
                .getResultList();
    }

    @Override
    public void deleteById(long id) {
        em.createQuery("delete from orders o where o.id=:id",Order.class)
                .setParameter("id", id);
    }

    @Override
    public void deleteAll() {
        em.createQuery("delete from orders o", Order.class);
    }
}
