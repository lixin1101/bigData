package lx.mr.flowsum;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

// 1 实现writable接口
public class FlowBean implements Writable {

    private long upFlow;
    private long downFlow;
    private long sumFlow;

    //2  反序列化时，需要反射调用空参构造函数，所以必须有
    public FlowBean() {
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow +"\t" + sumFlow;
    }

    public void set(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + downFlow;
    }

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }

    /**
     * 序列化方法
     * @param out 框架给我们提供的数据出口
     * @throws IOException
     */

    public void write(DataOutput out) throws IOException {
        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    /**
     * 反序列化方法
     * 反序列化方法读顺序必须和写序列化方法的写顺序必须一致
     * @param in 框架提供的数据来源
     * @throws IOException
     */
    public void readFields(DataInput in) throws IOException {
        upFlow = in.readLong();
        downFlow = in.readLong();
        sumFlow = in.readLong();
    }
}
