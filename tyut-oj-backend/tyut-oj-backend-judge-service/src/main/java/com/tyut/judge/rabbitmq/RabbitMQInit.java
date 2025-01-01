package com.tyut.judge.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @version v1.0
 * @author OldGj 2025/1/1
 * @apiNote TODO(一句话给出该类描述)
 */
@Slf4j
public class RabbitMQInit {
    public static void doInitRabbitMQ() {
        try {
            // 1）建立连接
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            // 创建通道
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "question_message";
            // 绑定交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = "question_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingKey");
            log.info("消息队列创建成功");
        } catch (Exception e) {
            log.error("消息队列创建失败");
        }

    }
}
