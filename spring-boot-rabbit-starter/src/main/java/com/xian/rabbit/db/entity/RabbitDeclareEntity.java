package com.xian.rabbit.db.entity;

import com.xian.rabbit.db.annotation.Ignore;
import com.xian.rabbit.db.annotation.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2021-02-05 22:48:45
 */
@Data
@Table(name = "rabbit_declare")
public class RabbitDeclareEntity implements Serializable {

	/**
	 * 
	 */
	private Long id;
	/**
	 * 交换机类型 fan-out、topic、headers、system、direct
	 */
	private String exchangeType;
	/**
	 * 是否持久化队列 0 是 1 否
	 */
	private Integer durable;
	/**
	 * 设置队列最大优先级
	 */
	private Integer priority;
	/**
	 * 延迟时间 毫秒
	 */
	private Long ttl;
	/**
	 * 交换机持久 默认 0 true 1 false 
	 */
	private Integer exchangeDurable;
	/**
	 * 备份交换机 queue_id
	 */
	private Long alternateId;
	@Ignore
	private RabbitQueueEntity queueEntity;
	/**
	 * 死信队列
	 */
	private String deadRoutingKey;
	/**
	 * 死信交换机
	 */
	private String deadExchange;
	/**
	 * 是否自动删除 0 ture 1 false
	 */
	private Integer autoDelete;

}
