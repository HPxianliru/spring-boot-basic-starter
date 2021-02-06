package com.xian.rabbit.db.entity;

import com.xian.rabbit.db.annotation.Column;
import com.xian.rabbit.db.annotation.Ignore;
import com.xian.rabbit.db.annotation.Pk;
import com.xian.rabbit.db.annotation.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2021-02-05 22:48:46
 */
@Data
@Table(name = "rabbit_queue")
public class RabbitQueueEntity implements Serializable {

	/**
	 * 
	 */
	@Pk
	private Long id;
	/**
	 * 队列名字
	 */
	@Column(name = "queue_name")
	private String queueName;
	/**
	 * 
	 */
	@Column(name = "routing_key")
	private String routingKey;
	/**
	 * 交换机
	 */
	private String exchange;
	/**
	 * application_id
	 */
	@Column(name = "app_id")
	private Long appId;
	@Ignore
	private RabbitApplicationEntity applicationEntity;
	/**
	 * 消费表id
	 */
	@Column(name = "consumer_id")
	private Long consumerId;
	@Ignore
	private RabbitConsumerEntity consumerEntity;
	/**
	 * 高级特性
	 */
	@Column(name = "declare_id")
	private Long declareId;
	@Ignore
	private RabbitDeclareEntity declareEntity;
	/**
	 * 描述
	 */
	private String desc;

}
