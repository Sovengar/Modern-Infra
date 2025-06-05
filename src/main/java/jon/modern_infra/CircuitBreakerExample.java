package jon.modern_infra;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/circuitBreaker")
@RequiredArgsConstructor
class CircuitBreakerExample {
    private final PaymentService paymentService;

    @GetMapping("/processPayment")
    @CircuitBreaker(name = "processPayment", fallbackMethod = "fallbackMethod")
    public String processPayment() throws Exception {
        return paymentService.processPayment();
    }

    public String fallbackMethod(Throwable throwable) {
        //Worst example of fallback, a good fallback would be using an alternative payment API
        return "Lo sentimos, actualmente estamos experimentando dificultades técnicas para procesar pagos en línea. Por favor, inténtalo de nuevo más tarde. Agradecemos tu paciencia y comprensión.";
    }
}

@Component
@RequiredArgsConstructor
class PaymentManager {
    private final RestTemplate restTemplate;

    public String processPayment() throws Exception {
        try {
            restTemplate.getForEntity("https://www.google.com", String.class);

            var random = Math.random();

            if (random > 0.5) {
                throw new Exception("Error en el proceso de pago");
            }

            return "Pago procesado correctamente";
        } catch (Exception ex) {
            throw ex;
        }
    }
}

@Service
@RequiredArgsConstructor
class PaymentService {
    private final PaymentManager paymentManager;

    public String processPayment() throws Exception{
        return paymentManager.processPayment();
    }
}