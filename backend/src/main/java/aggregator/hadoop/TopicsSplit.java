package aggregator.hadoop;

import com.google.common.primitives.Bytes;
import org.apache.hadoop.mapred.InputSplit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TopicsSplit implements InputSplit {

  private List<String> topicIds;

  public TopicsSplit() {
    topicIds = new ArrayList<>();
  }

  public TopicsSplit(List<String> topicIds) {
    this.topicIds = topicIds;
  }

  public List<String> getTopicIds() {
    return new ArrayList<String>(topicIds);
  }

  @Override
  public long getLength() throws IOException {
    return 0;
  }

  @Override
  public String[] getLocations() throws IOException {
    return new String[0];
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(out);
    oos.writeObject(topicIds);
    oos.close();
    dataOutput.write(out.toByteArray());
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    List<Byte> bytes = new ArrayList<>();
    while (true) {
      try {
        bytes.add(dataInput.readByte());
      } catch (EOFException e) {
        break;
      }
    }
    ByteArrayInputStream in;
    in = new ByteArrayInputStream(Bytes.toArray(bytes));
    try {
      this.topicIds = (List<String>)  (new ObjectInputStream(in).readObject());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
