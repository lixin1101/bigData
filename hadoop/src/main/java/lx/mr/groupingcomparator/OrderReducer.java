package lx.mr.groupingcomparator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class OrderReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {

    @Override
    protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        //取出每个订单的前2名
        Iterator<NullWritable> iterator = values.iterator();
        for (int i = 0; i < 2; i++) {
            if (iterator.hasNext()) {
                context.write(key, iterator.next());
            }
        }
    }
}
