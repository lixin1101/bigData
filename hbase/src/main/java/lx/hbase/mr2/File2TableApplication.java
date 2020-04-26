package lx.hbase.mr2;

import lx.hbase.mr2.tool.File2TobleTool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 打包方法  artfict ==>
 */
public class File2TableApplication {
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new File2TobleTool(),args);
    }
}
