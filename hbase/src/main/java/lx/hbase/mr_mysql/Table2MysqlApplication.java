package lx.hbase.mr_mysql;

import lx.hbase.mr_mysql.tool.Table2MysqlTool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 打包方法  artfict ==>
 */
public class Table2MysqlApplication {
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Table2MysqlTool(),args);
    }
}
