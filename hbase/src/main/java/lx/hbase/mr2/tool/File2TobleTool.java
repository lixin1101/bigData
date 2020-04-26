package lx.hbase.mr2.tool;

import lx.hbase.mr2.mapper.ReadFileMapper;
import lx.hbase.mr2.reduce.InsertDataReduce;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;

public class File2TobleTool implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance();
        job.setJarByClass(File2TobleTool.class);
        //mapper

        Path path = new Path("hdfs://s101:9000/data/student.csv");
        FileInputFormat.addInputPath(job, path);

        job.setMapperClass(ReadFileMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);

        //reduce
        TableMapReduceUtil.initTableReducerJob(
                "ns1.user",
                InsertDataReduce.class,
                job
        );

        //执行作业
        boolean flg = job.waitForCompletion(true);
        return flg ? JobStatus.State.SUCCEEDED.getValue() : JobStatus.State.FAILED.getValue();
    }

    @Override
    public void setConf(Configuration conf) {

    }

    @Override
    public Configuration getConf() {
        return null;
    }
}
