package lx.mybatisDemo.dao;

import lx.mybatisDemo.util.Util;
import org.apache.ibatis.session.SqlSession;

/**
 * 模板类
 */
public class DaoTemplate {
    /**
     * 执行
     */
    public static Object execute(MybatisCallback cb){
        SqlSession s = null;
        try {
            s = Util.openSession();
            Object ret = cb.doInMybatis(s);
            s.commit();
            return ret ;
        } catch (Exception e) {
            Util.rollbackTx(s);
        } finally {
            Util.closeSession(s);
        }
        return null ;
    }
}
