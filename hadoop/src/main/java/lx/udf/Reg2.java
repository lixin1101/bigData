package lx.udf;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg2 extends UDF{

	public Reg2 () {}
	public String evaluate(String context) {
		if(Strings.isNullOrEmpty(context)) {
			return context;
		}
		String reg="^[^0-9]*(\\d+).*$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(context);
		if(m.find()) {
			//m.group(0);//整行内容
			context = m.group(1);// group(1)表示第一()域里面的内容
		}
		return context;
		
	}

	public static void main(String[] args) {
		System.out.println(new Reg2().evaluate("http:123ww.com"));
	}
}
