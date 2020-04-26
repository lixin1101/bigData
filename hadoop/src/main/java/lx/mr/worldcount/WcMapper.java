package lx.mr.worldcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WcMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    private Text word = new Text();
    private IntWritable one = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //拿到这一行数据
        String line = value.toString();

        //按照空格切分数据
        String[] words = line.split(" ");

        //遍历数组，把单词变成（word，1）的形式交给框架
        for (String word : words) {
            this.word.set(word);
            context.write(this.word, this.one);
        }
    }
}
