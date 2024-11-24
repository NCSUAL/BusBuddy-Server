package Java2Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Java2ProjectApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Java2ProjectApplication.class);
		application.setAdditionalProfiles("main"); // 메인 환경
		application.run(args);
	}

}
