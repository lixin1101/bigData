package collection.jihe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JiheDemo {
	public void testList(){
		List<String> list=new ArrayList<String>();
		list.add("aaa");
		list.add("bbb");
		list.add("ccc");
		list.add("aaa");
		System.out.println(list.size());
		Iterator<String> it=list.iterator();
		while(it.hasNext()){
			String str=it.next();
			System.out.println(str);
		}
	}
	public void testset(){
		Set<String> s=new HashSet<String>();
		s.add("aaa");
		s.add("bbb");
		s.add("ccc");
		s.add("aaa");
		System.out.println(s.size());
		Iterator<String> it=s.iterator();
		while(it.hasNext()){
			String str=it.next();
			System.out.println(str);
		}
	}
	public void testset1(){
		Set<String> s=new TreeSet<String>();
		for (int i = 0; i < 26; i++) {
			System.out.print('a'+i+"\t");
		}
		Iterator<String> it=s.iterator();
		while(it.hasNext()){
			String str=it.next();
			System.out.println(str);
		}
	}
	public static void main(String[] args) {
		new JiheDemo().testList();//说明list有序，可重复
		System.out.println("----------------");
		new JiheDemo().testset();//说明set无序，不可重复，后面的把前面的覆盖了
		System.out.println("------------");
		new JiheDemo().testset1();//treeSet有序，字典顺序
	}



}

