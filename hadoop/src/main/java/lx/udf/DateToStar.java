package lx.udf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

/* 
 *   
 * */  
class MyFrameee extends JFrame implements ItemListener {  
  
    private JComboBox day = new JComboBox(), month = new JComboBox();  
    private JLabel tip = new JLabel("请选择日期");  
    public static final String[] starArr = {"魔羯座","水瓶座", "双鱼座", "牡羊座",   
        "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座" };     
    public static final int[] DayArr = {20,19,20,20,21,21,22,23,23,23,22,21};     
  
    MyFrameee() {  
        super("星座计算");  
        for (int i = 1; i <= 12; i++) {  
            //void addItem(Object anObject)  为项列表添加项。  
            month.addItem(String.valueOf(i));  
        }  
        for (int i = 1; i <= 31; i++) {  
            day.addItem(String.valueOf(i));  
        }  
        //setForeground:设置此组件的前景色。  
        tip.setForeground(Color.blue);  
  
        Container con = getContentPane();  
        con.setLayout(new FlowLayout());  
        con.add(month);  
        con.add(new JLabel("月-"));  
        con.add(day);  
        con.add(new JLabel("日"));  
        con.add(tip);  
        day.addItemListener(this);  
        month.addItemListener(this);  
        setSize(280, 100);  
        setLocation(400, 300);  
        setVisible(true);  
        setResizable(false);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
  
    public static String getConstellation(Calendar time) {     
        int month = time.get(Calendar.MONTH);     
        int day = time.get(Calendar.DAY_OF_MONTH);     
        if (day < DayArr[month-1]) {     
            month = month - 1;     
        }     
        if (month >= 0) {     
            return starArr[month];     
        }     
        return starArr[0];     
    }    
  
    public void itemStateChanged(ItemEvent e) {  
        Calendar date = Calendar.getInstance();  
        date.set(2010, Integer.parseInt((String) month.getSelectedItem()),  
                Integer.parseInt((String) day.getSelectedItem()));  
        tip.setText(getConstellation(date));  
    }  
}

public class DateToStar {  
    public static void main(String[] args) {  
        new MyFrameee();  
        
    }  
}  
