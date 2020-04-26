package lx.udf;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.regex.Pattern;

public class Reg1 extends UDF{

	public Reg1() {}
	public boolean evaluate(String concat) {
		if(Strings.isNullOrEmpty(concat)) {
			return false;
		}
		String reg="^[A-Za-z]+$";
		boolean bool = Pattern.matches(reg, concat);
		return bool;		
	}
	
	public static void main(String[] args) {
		System.out.println(new Reg1().evaluate("lixin"));
	}
}
