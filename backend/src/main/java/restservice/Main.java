package restservice;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import core.Application;
import dataprovider.dynamodb.DynamoDBDataProvider;
import dataprovider.dynamodb.client.InstanceProfileDynamoDBClientProvider;
import dataprovider.dynamodb.client.LocalDynamoDBClientProvider;
import dataprovider.dynamodb.table.TableCreator;
import dataprovider.dynamodb.table.TableCreatorFactory;
import imageuploader.S3ImageUploader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

  private final static TableCreator tableCreator = TableCreatorFactory.create();

  @Bean
  public Application getApplication() {
    return new Application(new DynamoDBDataProvider(new InstanceProfileDynamoDBClientProvider()),
        new S3ImageUploader(new AmazonS3Client(new InstanceProfileCredentialsProvider()), "between-prod"));
  }

  public static void main(String[] args) {
    tableCreator.createIfNotExists();
    SpringApplication.run(Main.class, args);
  }
}
