package lx.springMybatisDemo.service.impl;


import lx.springMybatisDemo.dao.BaseDao;
import lx.springMybatisDemo.domain.Item;
import lx.springMybatisDemo.domain.Order;
import lx.springMybatisDemo.domain.User;
import lx.springMybatisDemo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {


    @Resource(name = "itemDao")
    private BaseDao<Item> itemDao ;

    @Resource(name="userDao")
    public void setDao(BaseDao<User> dao) {
        super.setDao(dao);
    }

    /**
     * 长事务测试  长事务回滚，线程本地化  在执行几个语句同时进行，有一个语句执行出错，所有语句都执行不了，事务管理
     */
    public void longTx(){
        //插入item
        Item i = new Item();
        i.setItemName("ttt");

        Order o = new Order();
        o.setId(2);

        //
        itemDao.insert(i);

        this.delete(21);
    }
}