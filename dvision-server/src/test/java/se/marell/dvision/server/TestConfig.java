/*
 * Created by Daniel Marell 14-12-16 20:50
 */
package se.marell.dvision.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import se.marell.dcommons.time.TimeSource;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
@Component
@PropertySource("classpath:/test.properties")
public class TestConfig {
    private long time;

    @Bean
    TimeSource timeSource() {
        setTime("2015-06-20T12:00:00");
        return new TimeSource() {
            @Override
            public long currentTimeMillis() {
                return time;
            }

            @Override
            public long nanoTime() {
                return time * 1000000;
            }
        };
    }

    public void setTime(String dateTime) {
        setTime(LocalDateTime.parse(dateTime));
    }

    public void setTime(LocalDateTime dateTime) {
        this.time = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
