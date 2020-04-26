package lx.mr2.mapjoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

public class MJDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(MJDriver.class);

        job.setMapperClass(MJMapper.class);
        job.setNumReduceTasks(0);

        job.addCacheFile(URI.create("file:///d:/input/pd.txt"));

        FileInputFormat.setInputPaths(job, new Path("d:\\input\\order.txt"));
        FileOutputFormat.setOutputPath(job, new Path("d:\\output"));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
