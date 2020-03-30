package lx;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

import java.util.HashMap;
import java.util.Map;

public class MySource extends AbstractSource implements Configurable, PollableSource {

    //定义需要从配置中读取的字段

    //两条数据之间的间隔
    private long delay;
    private String field;


    public Status process() throws EventDeliveryException {
        try {
            Map<String, String> header = new HashMap<>();
            SimpleEvent event = new SimpleEvent();
            //拿到数据
            for (int i = 0; i < 5; i++) {
                event.setHeaders(header);
                event.setBody((field + i).getBytes());
                getChannelProcessor().processEvent(event);
                Thread.sleep(delay);
            }
        } catch (Exception e) {
            return Status.BACKOFF;
        }
        return Status.READY;
    }

    public long getBackOffSleepIncrement() {
        return 0;
    }

    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    public void configure(Context context) {
        delay = context.getLong("delay", 2000l);
        field = context.getString("field", "lixin");
    }
}
