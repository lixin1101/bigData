package lx.kylin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestCRUD {
    public static void main(String[] args) throws Exception {
        //Kylin_JDBC 驱动
        String KYLIN_DRIVER = "org.apache.kylin.jdbc.Driver";
        //Kylin_URL
        String KYLIN_URL = "jdbc:kylin://s101:7070/emp_project";
        //Kylin的用户名
        String KYLIN_USER = "admin";
        //Kylin的密码
        String KYLIN_PASSWD = "KYLIN";
        //添加驱动信息
        Class.forName(KYLIN_DRIVER);

        //获取连接
        Connection connection = DriverManager.getConnection(KYLIN_URL, KYLIN_USER, KYLIN_PASSWD);

        //预编译SQL
        PreparedStatement ps = connection.prepareStatement("select dept.dname,sum(emp.sal) from emp join dept on emp.deptno=dept.deptno group by dept.dname;");

        //执行查询
        ResultSet resultSet = ps.executeQuery();

        //遍历打印
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1)+"\t"+resultSet.getDouble(2));
        }

        connection.close();
    }
}
