package study.microcoffee.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import study.microcoffee.order.domain.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

    Order findById(String orderId);
}
