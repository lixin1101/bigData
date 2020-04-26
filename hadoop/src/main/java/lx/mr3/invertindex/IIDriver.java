package lx.mr3.invertindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class IIDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job1 = Job.getInstance(new Configuration());

        job1.setJarByClass(IIDriver.class);

        job1.setMapperClass(IIMapper1.class);
        job1.setReducerClass(IIReducer1.class);

        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(IntWritable.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job1, new Path("F:\\bigdataSource\\shangguigu\\invertindex"));
        FileOutputFormat.setOutputPath(job1, new Path("F:\\output"));

        boolean b = job1.waitForCompletion(true);
        if (b) {
            Job job2 = Job.getInstance(new Configuration());

            job2.setJarByClass(IIDriver.class);

            job2.setMapperClass(IIMapper2.class);
            job2.setReducerClass(IIReducer2.class);

            job2.setMapOutputKeyClass(Text.class);
            job2.setMapOutputValueClass(Text.class);
            job2.setOutputKeyClass(Text.class);
            job2.setOutputValueClass(Text.class);

            FileInputFormat.setInputPaths(job2, new Path("F:\\output"));
            FileOutputFormat.setOutputPath(job2, new Path("F:\\output2"));

            boolean b2 = job2.waitForCompletion(true);

            System.exit(b2 ? 0 : 1);
        }
    }
}
