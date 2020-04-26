package zyzs.data;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegLog extends UDF {
    public String evaluate(String log) throws Exception {
        if (Strings.isNullOrEmpty(log))
            return log;
        // 正则表达式
        String reg = "^([0-9.]+) - - \\[(.*)\\] .*(GET|POST) (.+) (HTTP)\\S+ (\\d+) (.*) \\\"([A-Za-z0-9./]+) .*$";
        // 获取pattern对象
        Pattern p = Pattern.compile(reg);
        // 获取matcher对象
        Matcher m = p.matcher(log);
        // 定义一个stringbuffer，用来拼接字符串；
        StringBuffer sbf = null;
        if (m.find()) {
            sbf = new StringBuffer();
            String ip = m.group(1);
            sbf.append(ip + "|");
            /**
             *
             * 31/Jan/2012:00:07:54 +0800 20120131 0002032
             */
            String timeDate = m.group(2);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy:HH:mm:ss Z",
                    Locale.ENGLISH);
            Date date = sdf.parse(timeDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date_time = sdf2.format(date);
            sbf.append(date_time + "|");

            String get_post = m.group(3);
            sbf.append(get_post + "|");
            String url = m.group(4);
            sbf.append(url + "|");
            String http = m.group(5);
            sbf.append(http + "|");
            String status = m.group(6);
            sbf.append(status + "|");
            String url1 = m.group(7).trim();
            int i=url1.indexOf("\"");
            String u = url1.substring(i);
            sbf.append(u);

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
                (new RegLog().evaluate("58.144.155.147 - - [06/Sep/2019:14:14:51 +0800] \"POST /api/zsnum/get_rool_images?htmlName=query_code HTTP/1.1\" 200 419 \"http://vdl.zyzsbj.com.cn/product?openid=okzizs2oqTy_6XcvKrGiqQVG4hh8&zsNum=21001047100000033163570607&trackId=okzizs2oqTy_6XcvKrGiqQVG4hh81567750490874&longitude=118.09063&latitude=36.795902&addressCode=370300&sideId=gh_b6bc98decfd4&appId=wx9c43dca9c4c265f0&status=101002\" \"Mozilla/5.0 (Linux; Android 9; Note9 Build/PKQ1.181203.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/6977 MicroMessenger/7.0.6.1480(0x2700063B) Process/tools NetType/4G Language/zh_CN\""));


        System.out.println
                (new RegLog().evaluate("111.10.34.19 - - [06/Sep/2019:14:14:51 +0800] \"GET /author/index?appid=wx9c43dca9c4c265f0,KcPzKUPzOUAqKUMzKUMzGYMaKhM2KcA5GUA=&code=061RQNb82LGeGK0y4j982GM4c82RQNbT&state=STATE HTTP/1.1\" 200 1496 \"-\" \"Mozilla/5.0 (Linux; Android 7.1.1; OPPO R11s Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/8046 MicroMessenger/7.0.6.1460(0x27000634) Process/tools NetType/4G Language/zh_CN\""));


        System.out.println
                (new RegLog().evaluate("220.194.106.94 - - [06/Sep/2019:14:16:45 +0800] \"GET /21000773110000064755742940 HTTP/1.1\" 301 185 \"-\" \"Mozilla/5.0 (Linux; Android 7.0; HUAWEI CAZ-AL10 Build/HUAWEICAZ-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044904 Mobile Safari/537.36 MMWEBID/2989 MicroMessenger/7.0.6.1480(0x2700063C) Process/tools NetType/4G Language/zh_CN\""));



    }
}
