package lx.udf2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.UDF;

public class GetMode extends UDF {
	public GetMode() {

	}

	public Double evaluate(String arr) {
		if ("".equals(arr) || arr == null || arr.length() == 0)  {
			return Double.parseDouble(arr);
		}
		String[] fields = arr.split(",");
		Map<Double, Integer> map = new HashMap<Double, Integer>();
		for (int i = 0; i < fields.length; i++) {
			if (map.containsKey(Double.parseDouble(fields[i]))) {
				map.put(Double.parseDouble(fields[i]), map.get(Double.parseDouble(fields[i])) + 1);
			} else {
				map.put(Double.parseDouble(fields[i]), 1);
			}
		}
		int maxCount = 0;
		double mode = -1;
		Iterator<Double> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			double num = iter.next();
			int count = map.get(num);
			if (count > maxCount) {
				maxCount = count;
				mode = num;
			}
		}
		return mode;
	}

	public static void main(String[] args) {
		String arr = "1,4,5,6,7,4,5,8,1,3";
		Double mode = new GetMode().evaluate(arr);
		System.out.println(mode);
	}
}
