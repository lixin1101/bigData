package lx.mr.partition;


import lx.mr.flowsum.FlowBean;
import lx.mr.flowsum.FlowMapper;
import lx.mr.flowsum.FlowReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class PartitionerDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.获取Job实例
        Job job = Job.getInstance(new Configuration());

        //2.设置类路径
        job.setJarByClass(PartitionerDriver.class);

        //3.设置Mapper和Reducer
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setNumReduceTasks(5);
        job.setPartitionerClass(MyPartitioner.class);

        //4. 设置输入输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //5. 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path("d:\\input"));
        FileOutputFormat.setOutputPath(job, new Path("d:\\output"));

        //6. 提交
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
