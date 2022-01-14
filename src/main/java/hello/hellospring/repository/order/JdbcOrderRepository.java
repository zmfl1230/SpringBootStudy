package hello.hellospring.repository.order;

import hello.hellospring.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcOrderRepository implements OrderRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Optional<Order> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void deleteAll() {

    }
}
