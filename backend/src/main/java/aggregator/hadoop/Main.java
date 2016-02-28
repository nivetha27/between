package aggregator.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

import java.util.UUID;

public class Main {
  public static void main(String[] args) {
    try {
      JobConf conf = new JobConf(Main.class);
      conf.setJobName("VoteAggregator");
      conf.setMapperClass(MyMapper.class);
      conf.setInputFormat(TopicsInputFormat.class);
      FileOutputFormat.setOutputPath(conf, new Path("user/vishnu/" + UUID.randomUUID().toString().replace('-', '0')));
      JobClient.runJob(conf);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
