package lx.udf2;

import org.apache.hadoop.hive.ql.exec.UDF;

public class StringSub extends UDF {
	
	public StringSub() {}

	public String evaluate(String col) {
		if(col == null || col.trim().equals("")) {
			return col;
		}
		String c1=col.substring(0, 9);
        String c2 = c1.substring(c1.length() - 1, c1.length());

        return c2;
	}
	
	public static void main(String[] args) {
		System.out.println(new StringSub().evaluate("21000701800000007190477742"));
	}

}
