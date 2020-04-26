package lx.mr.writablecomparable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

    private FlowBean flow = new FlowBean();
    private Text phone = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        phone.set(fields[0]);
        flow.setUpFlow(Long.parseLong(fields[1]));
        flow.setDownFlow(Long.parseLong(fields[2]));
        flow.setSumFlow(Long.parseLong(fields[3]));

        context.write(flow, phone);
    }
}
