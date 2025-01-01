package com.tyut.judge.rabbitmq;

import com.rabbitmq.client.Channel;
import com.tyut.judge.JudgeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private JudgeService judgeService;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"question_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("receiveMessage message = {}", message);
            judgeService.doJudge(Long.parseLong(message));
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            channel.basicNack(deliveryTag, false, false);
        }
    }

}