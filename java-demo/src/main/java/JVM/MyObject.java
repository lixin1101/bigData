package JVM;

class Person{
    private int age;
    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class MyObject {
    public static void m1() {
        m1();
    }

    public void changeValue1(int age) {
        age = 30;
    }

    public void changeValue2(Person p) {
        p.setName("xxx");
    }
    public void changeValue3(String s) {
        s="xxx";
    }

    public static void main(String[] args) {
       /* Object obj =new Object();
        System.out.println(obj.getClass().getClassLoader());//null
        //System.out.println(obj.getClass().getClassLoader().getParent().getParent());//报错
        //System.out.println(obj.getClass().getClassLoader().getParent());//报错

        MyObject myObject=new MyObject();
        System.out.println(myObject.getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(myObject.getClass().getClassLoader().getParent().getParent());//null
        System.out.println(myObject.getClass().getClassLoader().getParent());//sun.misc.Launcher$ExtClassLoader@4554617c*/
        MyObject myObject = new MyObject();
        int age = 20;
        myObject.changeValue1(age);
        System.out.println("age=" + age);

        Person p =new Person("abc");
        myObject.changeValue2(p);
        System.out.println("pname="+p.getName());

        String str="str";
        myObject.changeValue3(str);
        System.out.println("str="+str);



    }
}
