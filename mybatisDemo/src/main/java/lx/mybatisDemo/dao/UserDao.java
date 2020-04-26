package lx.mybatisDemo.dao;


import lx.mybatisDemo.domain.User;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * UserDao
 */
public class UserDao {

    /**
     * 插入操作
     */
    public void insert(final User user){
        DaoTemplate.execute(new MybatisCallback() {
            public Object doInMybatis(SqlSession s) {
                s.insert("users.insert",user);
                return null ;
            }
        });
    }

    /**
     * 插入操作
     */
    public void update(final User user){
        DaoTemplate.execute(new MybatisCallback() {
            public Object doInMybatis(SqlSession s) {
                s.update("users.update", user);
                return null ;
            }
        });
    }

    public User selctOne(final Integer id){
        return (User)DaoTemplate.execute(new MybatisCallback() {
            public Object doInMybatis(SqlSession s) {
                return s.selectOne("users.selectOne",id);
            }
        });
    }

    public List<User> selctAll(){
        return (List<User>)DaoTemplate.execute(new MybatisCallback() {
            public Object doInMybatis(SqlSession s) {
                return s.selectList("users.selectAll");
            }
        });
    }
}
