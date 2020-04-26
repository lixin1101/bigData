package lx.product;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author liubo
 */
public class CustomProducer {

    public static void main(String[] args) throws InterruptedException {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "s101:9092,s102:9092,s103:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        ArrayList<String> interceptors = new ArrayList<String>();
        interceptors.add("lx.interceptor.CounterInterceptor");
        interceptors.add("lx.interceptor.TimeInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);


        //1.创建1个生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        //2.调用send方法
        for (int i = 0; i < 1000000; i++) {
            Thread.sleep(10);
            producer.send(new ProducerRecord<String, String>("myTopic", i + "", "message-" + i));
        }

        //3.关闭生产者
        producer.close();
    }
}
