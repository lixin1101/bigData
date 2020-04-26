package lx.udf2;

import java.io.UnsupportedEncodingException;

import org.apache.hadoop.hive.ql.exec.UDF;

public class URLDecoderUDF extends UDF {
	public URLDecoderUDF() {
	}

	public String evaluate(String nickname) {
		String ENCODE = "UTF-8";
		String result = "";
		if (null == nickname) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(nickname, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args) {
		//%E5%89%91%E5%AE%A2
		//%E6%B2%99%E6%BC%A0%E8%BF%B7%E8%B7%AF%E7%9A%84%E9%AA%86%E9%A9%BC
		String str = "%E6%B2%99%E6%BC%A0%E8%BF%B7%E8%B7%AF%E7%9A%84%E9%AA%86%E9%A9%BC";
		//String str = "%E6%B2%99%E6%BC%A0%E8%BF%B7%E8%B7%AF%E7%9A%84%E9%AA%86%E9%A9%BC";
		System.out.println(new URLDecoderUDF().evaluate(str));
	}

}
