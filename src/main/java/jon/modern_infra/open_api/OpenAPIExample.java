package jon.modern_infra.open_api;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/openAPI")
@RequiredArgsConstructor
@Slf4j
class OpenAPIExample {

    @Operation(description = "Update Account")
    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateAccount(@RequestBody Request request) {
        log.info("BEGIN Updating account with number {} with this JSON: {}", request.accountNumber(), request);
        //...
        log.info("END Account with number {} updated", request.accountNumber());
        return ResponseEntity.ok().build();
    }

    @Schema(description = "Data for updating an account")
    record Request(
            @Schema(description = "Account number", example = "1234567890")
            @NotBlank(message = "Account number is required")
            String accountNumber,

            @Schema(description = "Account balance", example = "1000.50")
            @DecimalMin(value = "0.0", message = "Balance cannot be negative")
            BigDecimal balance,

            @Schema(description = "Account currency", example = "USD")
            @NotBlank(message = "Currency is required")
            String currency,

            @Schema(description = "Account status", example = "ACTIVE")
            String status,

            @Schema(description = "Account type", example = "SAVINGS")
            String accountType,

            @Schema(description = "Date of creation", example = "2022-01-01T00:00:00")
            LocalDateTime dateOfCreation
    ) {
    }
}
