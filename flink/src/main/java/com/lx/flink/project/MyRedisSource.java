package com.lx.flink.project;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.Map;

/**
 * redis中进行数据初始化
 * <p>
 * hset areas AREA_US US
 * hset areas AREA_CT TW,HK
 * hset areas AREA_AR PK,KW,SA
 * hset areas AREA_IN IN
 * 需要把大区和国家的对应关系组装成java的hashmap
 */
public class MyRedisSource implements SourceFunction<HashMap<String, String>> {

    private Logger logger = LoggerFactory.getLogger(MyRedisSource.class);
    private final long SLEEP_MILLION = 60000;
    private boolean isRunning = true;
    private Jedis jedis = null;

    public void run(SourceContext<HashMap<String, String>> ctx) throws Exception {

        this.jedis = new Jedis("127.0.0.1", 6379);
        HashMap<String, String> keyValuemap = new HashMap<String, String>();
        while (isRunning) {
            try {
                keyValuemap.clear();
                Map<String, String> areas = jedis.hgetAll("areas");
                for (Map.Entry<String, String> entry : areas.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String[] splits = value.split(",");
                    for (String split : splits) {
                        keyValuemap.put(split, key);
                    }
                }
                if (keyValuemap.size() > 0) {
                    ctx.collect(keyValuemap);
                } else {
                    logger.warn("从redis中获取的数据为空");
                }
                Thread.sleep(SLEEP_MILLION);
            } catch (JedisConnectionException e) {
                logger.error("redis连接异常，重新连接" + e.getCause());
                jedis = new Jedis("127.0.0.1", 6379);
            } catch (Exception e) {
                logger.error("source异常" + e.getCause());
            }
        }
    }

    public void cancel() {

        isRunning = false;
        if (jedis != null) {
            jedis.close();
        }
    }
}
