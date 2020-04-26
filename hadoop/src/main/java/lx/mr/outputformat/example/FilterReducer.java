package lx.mr.outputformat.example;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FilterReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
    Text k = new Text();

    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {

        // 1 获取一行
        String line = key.toString();

        // 2 拼接
        line = line + "\r\n";

        // 3 设置key
        k.set(line);

        // 4 输出
        context.write(k, NullWritable.get());
    }
}
