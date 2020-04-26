package lx.udf2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class IpToAreaUDF extends UDF {
	public IpToAreaUDF() {
	}

	public String evaluate(String ip) {
		// 解析ip
		String src = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "text/html; charset=utf-8");
			connection.setRequestMethod("GET");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = JSON.parseObject(sb.toString().trim());
			if (jsonObject.getString("code").equals("0")) {
				JSONObject obj = JSON.parseObject(jsonObject.getString("data"));
				if (StringUtils.isNotBlank(obj.getString("country")) && StringUtils.isNotBlank(obj.getString("region"))
						&& StringUtils.isNotBlank(obj.getString("city"))) {
					return obj.getString("country") + "," + obj.getString("region") + "," + obj.getString("city");
				}
				if (StringUtils.isNotBlank(obj.getString("country"))
						&& StringUtils.isNotBlank(obj.getString("region"))) {
					return obj.getString("country") + "," + obj.getString("region");
				}
				if (StringUtils.isNotBlank(obj.getString("country"))) {
					return obj.getString("country");
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) {
		String ip = "58.18.68.62";
		System.out.println(new IpToAreaUDF().evaluate(ip));
	}

}
