package lx.springMybatisDemo.service.impl;


import lx.springMybatisDemo.dao.BaseDao;
import lx.springMybatisDemo.domain.Item;
import lx.springMybatisDemo.service.ItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service("itemService")
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

    @Resource(name="itemDao")
    public void setDao(BaseDao<Item> dao) {
        super.setDao(dao);
    }
}