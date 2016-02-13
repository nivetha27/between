package aggregator.hadoop;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.elasticbeanstalk.model.SystemStatus;
import dataprovider.DataProvider;
import dataprovider.dynamodb.DynamoDBDataProvider;
import dataprovider.dynamodb.DynamoDBDataProviderFactory;
import dataprovider.dynamodb.client.LocalDynamoDBClientProvider;
import dataprovider.dynamodb.model.Topic;
import dataprovider.dynamodb.model.Vote;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TopicsInputFormat implements InputFormat {

  private final AmazonDynamoDBClient dynamoDBClient = new LocalDynamoDBClientProvider().getClient();
  private final DataProvider dataProvider = new DynamoDBDataProvider(new LocalDynamoDBClientProvider());
  private final DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDBClient);

  @Override
  public InputSplit[] getSplits(JobConf jobConf, int i) throws IOException {
    List<TopicsSplit> topicsSplits = new ArrayList<>();

    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
        .withProjectionExpression("topicId");

    ScanResultPage scanResultPage = null;

    while (scanResultPage == null || scanResultPage.getLastEvaluatedKey() != null) {
      List<String> topicIds = new ArrayList<>();
      if (scanResultPage != null && scanResultPage.getLastEvaluatedKey() != null) {
        scanExpression.setExclusiveStartKey(scanResultPage.getLastEvaluatedKey());
      }

      scanResultPage = mapper.scanPage(Topic.class, scanExpression);

      List<Object> results = scanResultPage.getResults();
      if (results != null && !results.isEmpty()) {
        for (Object result : results) {
          Topic topic = (Topic) result;
          topicIds.add(topic.getTopicId());
        }
      }
      TopicsSplit topicsSplit = new TopicsSplit(topicIds);
      topicsSplits.add(topicsSplit);
    }
    System.out.println("split size: " + topicsSplits.size());
    return topicsSplits.toArray(new TopicsSplit[topicsSplits.size()]);
  }

  @Override
  public RecordReader getRecordReader(InputSplit inputSplit, JobConf jobConf, Reporter reporter) throws IOException {
    return new TopicsRecordReader((TopicsSplit) inputSplit);
  }
}
