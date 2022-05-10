package cn.lllllan.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }

}
