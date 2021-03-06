package study.microcoffee.order.rest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import study.microcoffee.order.domain.DrinkType;
import study.microcoffee.order.domain.Order;
import study.microcoffee.order.repository.OrderRepository;
import study.microcoffee.order.rest.OrderRestService;

@RunWith(SpringRunner.class)
@WebMvcTest
@TestPropertySource(properties = { "logging.level.study.microcoffee=DEBUG" })
public class OrderRestServiceTest {

    private static final String POST_SERVICE_PATH = "/coffeeshop/{coffeeShopId}/order";
    private static final String GET_SERVICE_PATH = "/coffeeshop/{coffeeShopId}/order/{orderId}";

    private static final int COFFEE_SHOP_ID = 10;

    @MockBean
    private OrderRepository orderRepositoryMock;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void saveOrderShouldReturnCreated() throws Exception {
        final String accessControlAllowOrigin = "http://localhost:8080";
        final String accessControlExposeHeaders = "Location";

        Order newOrder = new Order.Builder() //
            .type(new DrinkType("Latte", "Coffee")) //
            .size("Small") //
            .drinker("Dagbjørn") //
            .selectedOption("skimmed milk") //
            .build();

        Order savedOrder = new Order.Builder() //
            .order(newOrder) //
            .id("1234567890abcdf") //
            .build();

        given(orderRepositoryMock.save(any(Order.class))).willReturn(savedOrder);

        mockMvc
            .perform(post(POST_SERVICE_PATH, COFFEE_SHOP_ID) //
                .header(HttpHeaders.ORIGIN, accessControlAllowOrigin) //
                .content(toJson(newOrder)) //
                .contentType(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(status().isCreated()) //
            .andExpect(header().string(HttpHeaders.LOCATION, Matchers.endsWith(savedOrder.getId())))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, Matchers.equalTo(accessControlAllowOrigin))) //
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, Matchers.equalTo(accessControlExposeHeaders))) //
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(content().json(toJson(savedOrder)));
    }

    @Test
    public void getOrderShouldReturnOrder() throws Exception {
        Order expectedOrder = new Order.Builder() //
            .id("1234567890abcdef") //
            .type(new DrinkType("Americano", "Coffee")) //
            .size("Large") //
            .drinker("Dagbjørn") //
            .selectedOption("soy milk") //
            .build();

        given(orderRepositoryMock.findById(eq(expectedOrder.getId()))).willReturn(expectedOrder);

        mockMvc
            .perform(get(GET_SERVICE_PATH, COFFEE_SHOP_ID, expectedOrder.getId()) //
                .accept(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(status().isOk()) //
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(content().json(toJson(expectedOrder)));
    }

    @Test
    public void getOrderWhenNoOrderShouldReturnNoContent() throws Exception {
        String orderId = "1111111111111111";

        given(orderRepositoryMock.findById(eq(orderId))).willReturn(null);

        mockMvc
            .perform(get(GET_SERVICE_PATH, COFFEE_SHOP_ID, orderId) //
                .accept(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(status().isNoContent());
    }

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    @Configuration
    @Import(OrderRestService.class)
    static class Config {
    }
}
