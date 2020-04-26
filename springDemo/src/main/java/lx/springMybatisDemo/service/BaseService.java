package lx.springMybatisDemo.service;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */
public interface BaseService<T> {
    public void insert(T t);

    public void update(T t);

    public void delete(Integer id);

    public T selectOne(Integer id);

    public List<T> selectAll();
}
