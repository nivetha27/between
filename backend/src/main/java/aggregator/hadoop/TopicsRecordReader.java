package aggregator.hadoop;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.net.SyslogAppender;

import java.io.IOException;

public class TopicsRecordReader implements RecordReader<Text, NullWritable> {

  private final TopicsSplit topicsSplit;
  private Integer currentPos;
  private NullWritable nullWritable = NullWritable.get();
  private Text text;

  public TopicsRecordReader(TopicsSplit topicsSplit) {
    this.topicsSplit = topicsSplit;
    this.currentPos = 0;
  }

  @Override
  public boolean next(Text text, NullWritable nullWritable) throws IOException {
    if (currentPos < topicsSplit.getTopicIds().size()) {
      this.text = new Text(topicsSplit.getTopicIds().get(currentPos));
      currentPos++;
      return true;
    }
    return false;
  }

  @Override
  public Text createKey() {
    return new Text(topicsSplit.getTopicIds().get(currentPos));
  }

  @Override
  public NullWritable createValue() {
    return nullWritable;
  }

  @Override
  public long getPos() throws IOException {
    return currentPos;
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public float getProgress() throws IOException {
    if (topicsSplit.getTopicIds().size() != 0) {
      return Math.min(1.0f, currentPos / topicsSplit.getTopicIds().size());
    }
    return 0;
  }
}
