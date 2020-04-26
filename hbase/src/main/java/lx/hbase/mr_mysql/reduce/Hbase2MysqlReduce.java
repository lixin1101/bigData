package lx.hbase.mr_mysql.reduce;


import lx.hbase.mr_mysql.mapper.CacheData;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Hbase2MysqlReduce extends Reducer<Text, CacheData,Text,CacheData> {

    @Override
    protected void reduce(Text key, Iterable<CacheData> values, Context context) throws IOException, InterruptedException {
        int sum=0;
        for (CacheData data : values) {
            sum=sum+data.getCount();
        }
        CacheData sumData=new CacheData();
        sumData.setName(key.toString());
        sumData.setCount(sum);

        context.write(key,sumData);
    }
}
