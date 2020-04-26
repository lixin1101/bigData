package lx.udf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg3{

	public String evaluate(String context) {
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
		System.out.println(new Reg3().evaluate("CN648046646"));
		System.out.println(new Reg3().evaluate("CNa000648046646"));
		System.out.println(new Reg3().evaluate("CNb64000646"));
		System.out.println(new Reg3().evaluate("aaaaaa648457646"));
		System.out.println(new Reg3().evaluate("00648457646"));
	}
}
