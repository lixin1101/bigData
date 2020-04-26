package zyzs.data;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegLog2 extends UDF {
    public String evaluate(String log) throws Exception {
        if (Strings.isNullOrEmpty(log))
            return log;
        // 正则表达式
        String reg = "^([0-9.]+) - - \\[(.*)\\] .*(GET|POST) (.+) (HTTP).*$";
        // 获取pattern对象
        Pattern p = Pattern.compile(reg);
        // 获取matcher对象
        Matcher m = p.matcher(log);
        // 定义一个stringbuffer，用来拼接字符串；
        StringBuffer sbf = null;
        if (m.find()) {
            sbf = new StringBuffer();
            String ip = m.group(1);
            sbf.append(ip + ",");
            String timeDate = m.group(2);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy:HH:mm:ss Z", Locale.ENGLISH);
            Date date = sdf.parse(timeDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date_time = sdf2.format(date);
            sbf.append(date_time + ",");
            String get_post = m.group(3);
            sbf.append(get_post + ",");
            String url = m.group(4);
            sbf.append(url + ",");
            String http = m.group(5);
            sbf.append(http);
        }

        if (sbf != null) {
            //IP	日期/时间	请求方式	请求URL	网络协议	状态码
            return sbf.toString();
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println
                (new RegLog2().evaluate("223.166.222.103 - - [05/Sep/2019:00:00:54 +0800] \"POST /sd/zyzsSd/wechat/valid?signature=0aaf481878aa815bb3e24930387103b46ed4c2d8&timestamp=1567612854&nonce=1541781677&openid=okzizs1ui9snfV95UVsIswqSxvFU HTTP/1.1\" 200 0 \"-\" \"Mozilla/4.0\""));

    }
}
