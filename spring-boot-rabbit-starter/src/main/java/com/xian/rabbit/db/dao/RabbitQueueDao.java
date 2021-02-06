package com.xian.rabbit.db.dao;

import com.xian.rabbit.db.dao.base.BaseDao;
import com.xian.rabbit.db.entity.RabbitQueueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * User Dao
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-10-15 11:15
 */
@Repository
public class RabbitQueueDao extends BaseDao<RabbitQueueEntity, Long> {

    @Autowired
    public RabbitQueueDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * 保存用户
     *
     * @param user 用户对象
     * @return 操作影响行数
     */
    public Integer insert(RabbitQueueEntity user) {
        return super.insert(user, true);
    }

    /**
     * 根据主键删除用户
     *
     * @param id 主键id
     * @return 操作影响行数
     */
    public Integer delete(Long id) {
        return super.deleteById(id);
    }

    /**
     * 更新用户
     *
     * @param user 用户对象
     * @param id   主键id
     * @return 操作影响行数
     */
    public Integer update(RabbitQueueEntity user, Long id) {
        return super.updateById(user, id, true);
    }

    /**
     * 根据主键获取用户
     *
     * @param id 主键id
     * @return id对应的用户
     */
    public RabbitQueueEntity selectById(Long id) {
        return super.findOneById(id);
    }

    /**
     * 根据查询条件获取用户列表
     *
     * @param user 用户查询条件
     * @return 用户列表
     */
    public List<RabbitQueueEntity> selectUserList(RabbitQueueEntity user) {
        return super.findByExample(user);
    }

}
