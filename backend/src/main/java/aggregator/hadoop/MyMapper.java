package aggregator.hadoop;

import aggregator.VoteAggregator;
import com.google.common.base.Strings;
import dataprovider.dynamodb.client.LocalDynamoDBClientProvider;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

public class MyMapper implements Mapper<Text, NullWritable, NullWritable, NullWritable> {

  private final VoteAggregator voteAggregator = new VoteAggregator(new LocalDynamoDBClientProvider());

  @Override
  public void close() throws IOException {
  }

  @Override
  public void configure(JobConf jobConf) {
  }

  @Override
  public void map(Text text, NullWritable nullWritable, OutputCollector<NullWritable, NullWritable> outputCollector,
                  Reporter reporter) throws IOException {
    String topicId = text.toString();
    if (!Strings.isNullOrEmpty(topicId)) {
      voteAggregator.removeDuplicateVotes(topicId);
      voteAggregator.aggregateVotes(topicId);
    }
  }
}
