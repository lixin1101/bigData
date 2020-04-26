package zyzs.data;

import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetDate extends UDF {
    public String evaluate(String date) throws Exception {

        if (Strings.isNullOrEmpty(date))
            return "无日期";
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
        Date date1 = sDateFormat.parse(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date1);
        calendar.add(calendar.DATE, -1);
        date1 = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date1);
        return dateString;
    }

    public static void main(String[] args) throws Exception {
        String date = new GetDate().evaluate("2019-08-01 12:34:56");
        System.out.println(date);

    }
}
