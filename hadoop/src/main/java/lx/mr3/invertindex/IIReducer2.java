package lx.mr3.invertindex;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class IIReducer2 extends Reducer<Text, Text, Text, Text> {

    private Text v = new Text();

    private StringBuilder sb = new StringBuilder();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        sb.delete(0, sb.length());
        for (Text value : values) {
            sb.append(value.toString()).append(" ");
        }
        v.set(sb.toString());
        context.write(key, v);
    }
}
