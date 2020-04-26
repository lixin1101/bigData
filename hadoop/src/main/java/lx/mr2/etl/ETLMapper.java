package lx.mr2.etl;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class ETLMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(" ");

        if (fields.length > 11) {
            context.write(value, NullWritable.get());
            //计数器
            context.getCounter("ETL", "True").increment(1);
        } else {
            context.getCounter("ETL", "False").increment(1);
        }

    }
}
