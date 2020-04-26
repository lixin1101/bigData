package lx.springMybatisDemo.service;


import lx.springMybatisDemo.domain.User;

/**
 *
 */
public interface UserService extends BaseService<User> {
    public void longTx();

}
