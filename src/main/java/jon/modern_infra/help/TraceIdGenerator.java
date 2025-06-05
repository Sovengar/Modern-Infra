package jon.modern_infra.help;

import org.slf4j.MDC;

import java.util.Random;

public class TraceIdGenerator {
    private TraceIdGenerator() {
    }

    public static void generateTraceId() {
        int length = 14;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        var traceId = sb.toString();
        MDC.put("traceId", traceId);
    }

}