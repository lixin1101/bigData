package collection.jihe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JiheDemo2 {

	public static void main(String[] args) {
		testMap();
		System.out.println("-----------");
		testArray();

	}

	public static void testArray(){
		int []array={25,21,46,33,338,2,78};
		Arrays.sort(array);
		System.out.println(Arrays.toString(array));
		int i=Arrays.binarySearch(array, 46);
		System.out.println(i);
	}

	public static void testMap(){
		Map<String, String> map=new HashMap<String, String>();
		for (int i = 0; i < 26; i++) {
			map.put("宋喆"+i, "王宝强"+i);
		}
		Set<String> keyset=map.keySet();
		Iterator<String> it=keyset.iterator();
		while(it.hasNext()){
			String key=it.next();
			String value=map.get(key);
			System.out.println(key+"\t"+value);//无序，改成treemap变成有序（字典顺序）
		}

	}
}
