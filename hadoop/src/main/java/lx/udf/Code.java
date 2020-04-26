package lx.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import static java.lang.Math.abs;

/**
 * SET @n=_unionid;
 * SET @size = LENGTH(_unionid);
 * SET @pos = 1;
 * SET @hashValue = 0;
 * WHILE @pos<@size+1 DO
 * SET @nCh = SUBSTRING(_unionid,@pos,1);
 * SET @hashValue = @hashValue + ASCII(@nCh);
 * SET @pos = @pos + 1;
 * END WHILE;
 */

public class Code extends UDF {

    public Code() {
    }

    public int evaluate(String context) {

        //2147483647
        /*int i = context.hashCode();
        i = (i & Integer.MAX_VALUE) % 100;
        return i;*/

        int pos = 1;
        int hashValue = 0;
        int size = context.length();
        char[] chars = context.toCharArray();
        while (pos < size + 1) {
            for (char c : chars) {
                int value = c;
                hashValue = hashValue + value;
                pos = pos + 1;
            }
        }

        return abs(hashValue % 100);

    }

    public static void main(String[] args) {
        int evaluate = new Code().evaluate("o1Zv-wa7VEOiy7LVFB1dr1wCno54");
        System.out.println(evaluate);

    }

}
