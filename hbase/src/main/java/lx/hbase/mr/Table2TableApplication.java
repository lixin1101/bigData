package lx.hbase.mr;

import lx.hbase.mr.tool.HbaseMapperReduceTool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 打包方法  artfict ==>
 */
public class Table2TableApplication {
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new HbaseMapperReduceTool(),args);
    }
}
