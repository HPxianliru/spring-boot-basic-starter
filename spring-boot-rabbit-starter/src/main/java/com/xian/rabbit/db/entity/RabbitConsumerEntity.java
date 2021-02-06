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
@Table(name = "rabbit_consumer")
public class RabbitConsumerEntity implements Serializable {

	/**
	 * 
	 */
	private Long id;
	/**
	 * 每次拉取数量
	 */
	private Integer prefetchSize;
	/**
	 * 消费者数量
	 */
	private Integer concurrent;
	/**
	 * 是否手动确认 0 手动确认 1 自动
	 */
	private Integer acknowledgeMode;
	/**
	 * 队列
	 */
	private Long queueId;
	@Ignore
	private RabbitQueueEntity queueEntity;

}
