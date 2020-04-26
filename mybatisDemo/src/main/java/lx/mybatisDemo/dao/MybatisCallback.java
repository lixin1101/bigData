package lx.mybatisDemo.dao;

import org.apache.ibatis.session.SqlSession;

/**
 * 回调接口
 */
public interface MybatisCallback {
    public Object doInMybatis(SqlSession s);
}
