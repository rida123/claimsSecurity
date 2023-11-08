package claims.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/demo")
public class DemoController {

    @GetMapping("/greeting")
    public String helloEndpoint() {
        return "Hello";
    }
}

