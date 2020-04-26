package collection.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;


//读取属性文件
public class PropertiesDemo {

	public static void main(String[] args) throws Exception {
		testpropertyfile();
		//ResourceBundle bundle=ResourceBundle.getBundle("lixin.properties.db");
		//包名.类名，再加属性文件名，如果不在包名下，就直接写属性文件名
		//System.out.println(bundle.getString("sclool"));

	}


	public static void testpropertyfile() throws Exception{
		FileInputStream fis=new FileInputStream("F:\\idea2019\\big-data\\java-demo\\src\\main\\java\\collection\\properties\\db.properties");

		Properties pro=new Properties();
		pro.load(fis);
		fis.close();
		pro.setProperty("grade", "1606A");
		//pro.setProperty("user", "小李");
		pro.store(new FileOutputStream("D:\\hello.txt"), "李鑫笔记");
		Set keys=pro.keySet();
		Iterator<String> it=keys.iterator();
		while(it.hasNext()){
			String key=it.next();
			String value=pro.getProperty(key);
			System.out.println(key+"\t"+value);
		}

	}
}

