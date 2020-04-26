package lx.udf2;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 说明：根据key找到value 如：sex=1&hight=178&weight=130&sal=25000
 */
public class GetValueByKey extends UDF {

    public String evaluate(String context,String key) {
        //sex=1&hight=178&weight=130&sal=25000
        String result = null;
        try {
            //sex=1&hight=178&weight=130&sal=25000
            context = context.replace('&', ',');
            context = context.replace('=', ':');
            context = "{" + context + "}";
            JSONObject json = new JSONObject(context);
            result = json.getString(key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        String context="sex=1&hight=178&weight=130&sal=25000";
        String key="sex";
        String result = new GetValueByKey().evaluate(context, key);
        System.out.println(result);

    }
}