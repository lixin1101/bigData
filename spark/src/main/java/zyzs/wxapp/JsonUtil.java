package zyzs.wxapp;

import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonUtil {
    private static final Logger logger = LogManager.getLogger(JsonUtil.class);

    public static String readJsonFile(File jsonFile) {

        String jsonStr = "";

        logger.info("————开始读取" + jsonFile.getPath() + "文件————");

        try {

            //File jsonFile = new File(fileName);

            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");

            int ch = 0;

            StringBuffer sb = new StringBuffer();

            while ((ch = reader.read()) != -1) {

                sb.append((char) ch);

            }

            fileReader.close();

            reader.close();

            jsonStr = sb.toString();

            logger.info("————读取" + jsonFile.getPath() + "文件结束!————");

            return jsonStr;

        } catch (Exception e) {

            logger.info("————读取" + jsonFile.getPath() + "文件出现异常，读取失败!————");

            e.printStackTrace();

            return null;

        }

    }

    public static void main(String[] args) {
        /*String jsonStr = JsonUtil.readJsonFile(new File("C:\\Users\\W541\\Documents\\WeChat Files\\lx781836132\\FileStorage\\File\\2019-11\\a.txt"));
        Object parse = JSONArray.parse(jsonStr);*/

       /* Map jsonMap = (Map) JSON.parse(jsonStr);
        Set set = jsonMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println(next);

        }*/

       String a="{\n" +
               "    \"code\": 4000,\n" +
               "    \"msg\": \"成功\",\n" +
               "    \"data\": {\n" +
               "        \"ranks\": [\n" +
               "            {\n" +
               "                \"openId\": \"ozyqv4jIrGgFZQ_MjA-PojkGyKAY\",\n" +
               "                \"aid\": \"f16f60c85952444cb2878362fa644116\",\n" +
               "                \"periods\": \"cyh01\",\n" +
               "                \"author\": {\n" +
               "                    \"headimg\": \"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKqSwPrp34qsTSQa4LZJL3JusOF3BDQECy6eS8YOicbBvGjicF5WAlLfc8sf1Xef9v4tfUPlxSDamiag/132\",\n" +
               "                    \"nickname\": \"正大烟酒15030553908\"\n" +
               "                },\n" +
               "                \"rank\": 1,\n" +
               "                \"prizeName\": \"五粮52一瓶\",\n" +
               "                \"type\": \"week\",\n" +
               "                \"createTime\": \"2019-11-18 11:36:36\"\n" +
               "            },\n" +
               "            {\n" +
               "                \"openId\": \"ozyqv4gBl6_97dG-JqP67--LSNus\",\n" +
               "                \"aid\": \"8d9a27ae1b4e446ea77182b0b9475a1f\",\n" +
               "                \"periods\": \"cyh01\",\n" +
               "                \"author\": {\n" +
               "                    \"headimg\": \"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTL1ZpHKmWicMhzbHx0fAglqnvOib5YbuhAsfJh4AsEpmZfXH5sTFUXqtToWdjtcHJeMwrhp2ks3iccibg/132\",\n" +
               "                    \"nickname\": \"激情四射\"\n" +
               "                },\n" +
               "                \"rank\": 50,\n" +
               "                \"prizeName\": \"五粮52一瓶\",\n" +
               "                \"type\": \"week\",\n" +
               "                \"createTime\": \"2019-11-18 11:36:37\"\n" +
               "            }\n" +
               "        ]\n" +
               "    }\n" +
               "}";

        String replace1 = a.replace("\n", "");
        String replace2 = replace1.replace("\t", " ");
        System.out.println(replace2);
    }
}
