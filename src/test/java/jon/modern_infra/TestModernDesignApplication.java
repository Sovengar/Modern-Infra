package jon.modern_infra;

import org.springframework.boot.SpringApplication;

public class TestModernDesignApplication {

	public static void main(String[] args) {
		SpringApplication.from(AppRunner::main).with(TestcontainersConfiguration.class).run(args);
	}

}
