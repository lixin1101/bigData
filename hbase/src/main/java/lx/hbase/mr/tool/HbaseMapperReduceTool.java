package lx.hbase.mr.tool;

import lx.hbase.mr.mapper.ScanDataMapper;
import lx.hbase.mr.reduce.InsertDataReduce;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.util.Tool;

public class HbaseMapperReduceTool implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance();
        job.setJarByClass(HbaseMapperReduceTool.class);
        //mapper
        TableMapReduceUtil.initTableMapperJob(
                "ns1.student",
                new Scan(),
                ScanDataMapper.class,
                ImmutableBytesWritable.class,
                Put.class,
                job
        );

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
