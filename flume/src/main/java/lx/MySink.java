package lx;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySink extends AbstractSink implements Configurable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSink.class);
    private String prefix = "";
    private String suffix= "";

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;

        Channel channel = getChannel();

        Transaction transaction = channel.getTransaction();

        transaction.begin();

        try {

            Event take;
            while ((take = channel.take()) == null) {
                Thread.sleep(200);
            }

            //到这里我们就拿到数据了
            LOG.info(prefix + new String(take.getBody()) + suffix);

            transaction.commit();
            status = Status.READY;

        } catch (Throwable e) {
            transaction.rollback();
            status = Status.BACKOFF;

            if (e instanceof Error) {
                throw (Error) e;
            }
        } finally {
            transaction.close();
        }

        return status;
    }

    @Override
    public void configure(Context context) {
        prefix = context.getString("prefix", "PRE" );
        suffix = context.getString("suffix","xxx");
    }
}
