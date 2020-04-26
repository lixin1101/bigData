package lx.udf2;

import org.apache.hadoop.hive.ql.exec.UDF;

public class SpiltProvinceUDF extends UDF {

    public SpiltProvinceUDF() {
    }

    public String evaluate(String col) {
        if (col == null || col.trim().equals("")) {
            return col;
        }
        if (col.split(",").length == 1) {
            return col;
        }
        if (col.split(",").length == 2) {
            return col.split(",")[1];
        } else {
            return col.split(",")[1];
        }
    }

    public static void main(String[] args) {
        System.out.println(new SpiltProvinceUDF().evaluate("中国,山东省,枣庄市"));
    }
}
