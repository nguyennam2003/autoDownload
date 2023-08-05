package asia.tdt.parameterdownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Configuration
@EnableScheduling
public class ParameterDownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParameterDownloadApplication.class, args);
	}

}
