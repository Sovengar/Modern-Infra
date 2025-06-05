package jon.modern_infra;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
class Test {

    @GetMapping
    public String test() {
        return "Hello World!";
    }
}
