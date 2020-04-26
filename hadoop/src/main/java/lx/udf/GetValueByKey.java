package lx.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 根据key找到value 如：sex=1&hight=178&weight=130&sal=25000
 * @author Administrator
 *
 */
public class GetValueByKey extends UDF {

	public GetValueByKey(){}
	
	public String evaluate(String context,String key){
		
		String result_value=null;
		try {
			context = context.replace("&", ",");
			context = context.replace("=", ":");
			context = "{"+context+"}";
			JSONObject json=new JSONObject(context);
			//System.out.println(json);
			if(key==null || key==""){
				return null;
			}
			result_value = json.getString(key);

		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return result_value;
		
	}
	
	public static void main(String[] args) {
		String con = new GetValueByKey().evaluate("sex=1&hight=178&weight=130&sal=25000", "weights");
		System.out.println(con);
	}
}
