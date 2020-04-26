package lx.udf;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateToAnimal {

	public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };
	 
	public static final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
	 
	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
	 
	/**
	 * 根据日期获取生肖
	 * @return
	 */
	public String getZodica(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    return zodiacArr[cal.get(Calendar.YEAR) % 12];
	}
	 
	/**
	 * 根据日期获取星座
	 * @return
	 */
	public  String getConstellation(Date date) {
	    if (date == null) {
	        return "";
	    }
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    if (day < constellationEdgeDay[month]) {
	        month = month - 1;
	    }
	    if (month >= 0) {
	        return constellationArr[month];
	    }
	    // default to return 魔羯
	    return constellationArr[11];
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		Date date=new Date(994567890);
		String time = sdf.format(new Date());
		System.out.println(time);
		System.out.println(new DateToAnimal().getZodica(new Date()));
		System.out.println(new DateToAnimal().getConstellation(new Date()));
		System.out.println(new DateToAnimal().getZodica(date));
	}
}
