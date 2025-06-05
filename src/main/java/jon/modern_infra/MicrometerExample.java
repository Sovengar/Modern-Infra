package jon.modern_infra;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/micrometer")
@RequiredArgsConstructor
@Slf4j
class MicrometerExample {
    private final ObservationRegistry registry;

    @GetMapping(path = "/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");

        //Fine-grained Observation instead of @Observed
        String prefix = accountNumber.split("-")[0];
        var observation = Observation
                .createNotStarted("get-balance", registry)
                .lowCardinalityKeyValue("accountPrefix", prefix)
                .contextualName("getBalance");

        log.info("BEGIN getBalance for accountNumber: {}", accountNumber);
        var balance = observation.observe(() -> getBalance());
        log.info("END getBalance for accountNumber: {}", accountNumber);

        return ResponseEntity.ok(balance);
    }

    @Observed(name = "getBalanceStartYear")
    @GetMapping(path = "/{accountNumber}/balance/start-year")
    public ResponseEntity<BigDecimal> getBalanceAtTheStartOfTheYear(@PathVariable String accountNumber) {
        Assert.state(StringUtils.hasText(accountNumber), "Account number is required");

        log.info("BEGIN getBalance for accountNumber: {}", accountNumber);
        var balance = getBalance();
        log.info("END getBalance for accountNumber: {}", accountNumber);

        return ResponseEntity.ok(balance);
    }

    BigDecimal getBalance(){
        return BigDecimal.ZERO;
    }
}

/*
@Slf4j
class LoggingObservationHandler implements ObservationHandler<Observation.Context> {

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true; // Maneja todos los contextos
    }

    @Override
    public void onStart(Observation.Context context) {
        log.info("Observation started: {}", context.getName());
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("Observation stopped: {}", context.getName());
    }

    @Override
    public void onError(Observation.Context context) {
        log.error("Observation error: {}", context.getName(), context.getError());
    }
}
*/
