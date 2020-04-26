package lx.udf;

/**
 一、星座与日期
    摩羯座：12.22-1.20 
    水瓶座：1.21-2.19 
    双鱼座：2.20-3.20  
    白羊座：3.21-4.20 
    金牛座：4.21-5.21 
    双子座：5.22-6.21
    巨蟹座：6.22-7.22 
    狮子座：7.23-8.23 
    处女座：8.24-9.23 
    天秤座：9.24-10.23 
    天蝎座：10.24-11.22 
    射手座：11.23-12.21    
二、星座类型：
   "摩羯座","水瓶座","双鱼座","白羊座","金牛座","双子座",
   "巨蟹座","狮子座","处女座","天秤座","天蝎座","射手座"  
三、日期界限：
    20,19,20,20,21,21,22,23,23,23,22,21
 */

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

public class Star extends UDF {
	public Star(){}
	
	public String evaluate(String date){
		
		if (Strings.isNullOrEmpty(date))
			return date;
		// 星座类型素组：
		String[] starArr = { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
				"狮子座", "处女座", "天秤座", "天蝎座", "射手座" };
		// 星座的分界日期：
		int[] dayArr = { 20, 19, 20, 20, 21, 21, 22, 23, 23, 23, 22, 21 };
		// 出生的月份:
		int month = Integer.parseInt(date.split("-")[1]);
		// 获取出生的日期：
		int day = Integer.parseInt(date.split("-")[2]);
		int index = month;
		/**
		 *  1988-3-23   白羊座
			dayArr[2]=20
	
	        day=23
		    dayArr[month-1]=dayArr[2]=20
		 */
		if (dayArr[month - 1] > day) {
			index = month - 1;
		}
		return starArr[index];
		
	}

	public static void main(String[] args) {
		System.out.println(new Star().evaluate("2019-08-01"));
	}

}
