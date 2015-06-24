package se.marell.dvision.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import se.marell.dvesta.system.BuildInfo;
import se.marell.dvesta.system.LogbackLoggerInitializer;
import se.marell.dvesta.system.RunEnvironment;
import se.marell.dvesta.system.SystemSpringConfig;

@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableWebSecurity
@EnableAsync
@EnableScheduling
@Import(SystemSpringConfig.class)
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        if (System.getProperty("spring.profiles.active") == null) {
            app.setAdditionalProfiles("local");
        }
        app.addListeners(new ApplicationListener<ApplicationEnvironmentPreparedEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
                LogbackLoggerInitializer.init(event.getEnvironment());
                logger.info("AppVersion: " + BuildInfo.getAppVersion());
                logger.info("RunEnvironment: " + RunEnvironment.getCurrentEnvironment(event.getEnvironment()));
            }
        });
        app.run(args);
    }
}

