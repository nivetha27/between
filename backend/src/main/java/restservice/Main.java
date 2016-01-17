package restservice;

import core.Application;
import dataprovider.dynamodb.DynamoDBDataProvider;
import dataprovider.dynamodb.client.LocalDynamoDBClientProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

  @Bean
  public Application getApplication() {
    return new Application(new DynamoDBDataProvider(new LocalDynamoDBClientProvider()));
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
