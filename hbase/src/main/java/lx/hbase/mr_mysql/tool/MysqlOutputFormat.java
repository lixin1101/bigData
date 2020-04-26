package lx.hbase.mr_mysql.tool;


import lx.hbase.mr_mysql.mapper.CacheData;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;

import java.io.IOException;
;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlOutputFormat extends OutputFormat<Text, CacheData> {

    class MysqlRecordWriter extends RecordWriter<Text, CacheData> {
        private static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
        private static final String MYSQL_URL = "jdbc:mysql://s101:3306/test?useUnicode=true&characterEncoding=UTF-8";
        private static final String MYSQL_USERNAME = "root";
        private static final String MYSQL_PASSWORD = "root";
        private Connection connection;

        public MysqlRecordWriter() {
            try {
                Class.forName(MYSQL_DRIVER_CLASS);
                connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void write(Text key, CacheData data) throws IOException, InterruptedException {

            String sql = "insert into statresult(name,sumcnt) values (?,?)";
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, key.toString());
                preparedStatement.setObject(2, data.getCount());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    @Override
    public RecordWriter<Text, CacheData> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        MysqlRecordWriter myMysqlRecordWriter = new MysqlRecordWriter();
        return  myMysqlRecordWriter;
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {

    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return null;
    }
}
