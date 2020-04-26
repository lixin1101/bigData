package lx.hbase.mr_mysql.tool;

import lx.hbase.mr_mysql.mapper.CacheData;
import lx.hbase.mr_mysql.mapper.ScanHbaseMapper;
import lx.hbase.mr_mysql.reduce.Hbase2MysqlReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.util.Tool;

import java.util.HashMap;
import java.util.Map;

public class Table2MysqlTool implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance();
        job.setJarByClass(Table2MysqlTool.class);

        Map<String, String> paramMap = new HashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            String paramName = args[i];
            String paramValue = args[i + 1];
            if(!paramName.startsWith("--")){
                throw new RuntimeException("参数传递不正确");
            }
            if(paramValue.startsWith("--")){
                throw new RuntimeException("参数传递不正确");
            }
            paramMap.put(paramName,paramValue);
        }

        Scan scan=new Scan();
        if(paramMap.get("--hbase-family")!=null){
            scan.addFamily(Bytes.toBytes(paramMap.get("--hbase-family")));
        }
        //mapper
        TableMapReduceUtil.initTableMapperJob(
                paramMap.get("--hbase-table"),
                //"ns1.student",
                scan,
                ScanHbaseMapper.class,
                Text.class,
                CacheData.class,
                job
        );

        //reduce
        job.setReducerClass(Hbase2MysqlReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(CacheData.class);

        job.setOutputFormatClass(MysqlOutputFormat.class);


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
