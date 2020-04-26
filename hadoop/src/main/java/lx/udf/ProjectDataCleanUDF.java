package lx.udf;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * 27.19.74.143 - - [2017-03-26 14:44:01]
 * GET /static/js/common_extra.js?y7a HTTP/1.1 500 鬼吹灯之精绝古城 欧美 悬疑
 * ^([0-9.]+) - - \[(.*)\] (GET|POST) (.+) (HTTP)\S+ (\d+) (.*) (.*) (.*)$
 */
public class ProjectDataCleanUDF extends UDF {

    public ProjectDataCleanUDF() {
    }

    public String evaluate(String log) {
        if (Strings.isNullOrEmpty(log)) {
            return "NULL";

        }
        String reg = "^([0-9.]+) - - \\[(.*)\\] (GET|POST) (.+) (HTTP)\\S+ (\\d+) (.*) (.*) (.*)$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(log);
        StringBuffer sb = null;

        if (m.find()) {
            sb = new StringBuffer();
            String IP = m.group(1);//IP
            sb.append(IP).append("\t");

            String dateTime = m.group(2);//时间和日期
            String[] date_time = dateTime.split(" ");

            sb.append(date_time[0].split("-")[0] + date_time[0].split("-")[1] + date_time[0].split("-")[2]).append("\t");
            sb.append(date_time[1]).append("\t");

            String get_post = m.group(3);//请求方式
            sb.append(get_post).append("\t");

            String url = m.group(4);//url
            sb.append(url).append("\t");

            String http = m.group(5);//http
            sb.append(http).append("\t");

            String status = m.group(6);//状态码
            sb.append(status).append("\t");

            String playname = m.group(7);//据名
            sb.append(playname).append("\t");

            String playlocal = m.group(8);//产地
            sb.append(playlocal).append("\t");

            String playtype = m.group(9);//type
            sb.append(playtype);

        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new ProjectDataCleanUDF().evaluate("27.19.74.143 - - [2017-03-26 14:44:01] GET /static/js/common_extra.js?y7a HTTP/1.1 500 鬼吹灯之精绝古城 欧美 悬疑"));
        String str = "2018";
        String five = str.replace(str, "2010");
        System.out.println(five);

    }
}
