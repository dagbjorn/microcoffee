package study.microcoffee.order.rest;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import study.microcoffee.order.domain.Order;
import study.microcoffee.order.exception.OrderNotFoundException;
import study.microcoffee.order.repository.OrderRepository;

@CrossOrigin(exposedHeaders = { HttpHeaders.LOCATION })
@RestController
@RequestMapping(path = "/coffeeshop", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderRestService {

    private Logger logger = LoggerFactory.getLogger(OrderRestService.class);

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping(path = "/{coffeeShopId}/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Order> saveOrder(@PathVariable("coffeeShopId") long coffeeShopId, @RequestBody Order order) {
        logger.debug("REST service called: POST /{}/order body={}", coffeeShopId, order);

        order.setCoffeeShopId(coffeeShopId);

        order = orderRepository.save(order);

        // Create URL of Location header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{orderId}").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(location).body(order);
    }

    @GetMapping(path = "/{coffeeShopId}/order/{orderId}")
    public Object getOrder(@PathVariable("coffeeShopId") long coffeeShopId, @PathVariable("orderId") String orderId) {
        logger.debug("REST service called: GET /{}/order/{}", coffeeShopId, orderId);

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        return order;
    }
}
