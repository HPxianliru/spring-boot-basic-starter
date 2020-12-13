# spring-boot-rabbit-starter
二次封装-基础组件


主要针对rabbit 简化使用封装 starter

  1 复杂的高级特性。
  
  2 枚举类 简化重复性的queue、exchange、bing
  
  3 枚举类 简化重复性消费者注册。
  
    1 规范性使用队列监听。统一接口，
    2 根据枚举选择是否手动、自动确认
    3 队列消费者监听注册，需要根据 项目名称、消费实现类、消费枚举 三个维度控制需要监听队列
  4 消费者动态调整 
  
    1 动态创建队列 基于AmqpAdmin 进行queue、exchange、bing 的动态绑定。暂不支持消费者动态创建
    2 
    RESTful 风格调用
      1 /mqManage/resetConcurrentConsumers 重置指定队列消费者数量
      2 /mqManage/restartMessageListener  重启对消息队列的监听
      3 /mqManage/stopMessageListener  停止对消息队列的监听
      4 /mqManage/statAllMessageQueueDetail 获取所有消息队列对应的消费者
      5 /mqManage/queueNotListening 未被监听的队列
  
  5 生产者使用统一 AmqTemplateUtil工具类 静态方式调用发送。不需要全局多次依赖注入 RabbitTemplate，统一复用。
  
  6 可以作为公司基础组件使用ApplicationEnums 用于程序的区分。需要在配置公司项目做声明
  
  7 目前支持的高级特性
  
    1 延迟队列
    2 死信队列
    3 优先级
    4 备份交换机
    
   
  
  项目使用 所需要的jar 都是spring-boot内的。如需使用。只需要替换 pom spring-boot-starter-parent 的版本
  
  
