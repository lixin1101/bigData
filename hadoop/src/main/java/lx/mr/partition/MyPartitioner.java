package lx.mr.partition;


import lx.mr.flowsum.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class MyPartitioner extends Partitioner<Text, FlowBean> {
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
      /*  String phone = text.toString();

        switch (phone.substring(0, 3)) {
            case "136":
                return 0;
            case "137":
                return 1;
            case "138":
                return 2;
            default:
                return 4;
        }*/
        // 1 获取电话号码的前三位
        String preNum = text.toString().substring(0, 3);
        int partition = 4;
        // 2 判断是哪个省
        if ("136".equals(preNum)) {
            partition = 0;
        }else if ("137".equals(preNum)) {
            partition = 1;
        }else if ("138".equals(preNum)) {
            partition = 2;
        }else if ("139".equals(preNum)) {
            partition = 3;
        }else if ("135".equals(preNum)) {
            partition = 4;
        }
        return partition;
    }
}
