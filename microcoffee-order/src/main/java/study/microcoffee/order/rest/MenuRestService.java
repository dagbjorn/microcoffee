package study.microcoffee.order.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import study.microcoffee.order.repository.MenuRepository;

@CrossOrigin
@RestController
@RequestMapping(path = "/coffeeshop", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MenuRestService {

    private Logger logger = LoggerFactory.getLogger(MenuRestService.class);

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping(path = "/menu")
    public Object getCoffeeMenu() {
        logger.debug("REST service called: /menu");

        return menuRepository.getCoffeeMenu();
    }
}
