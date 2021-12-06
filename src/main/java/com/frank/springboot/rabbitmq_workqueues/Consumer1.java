package com.frank.springboot.rabbitmq_workqueues;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * 2021-12-06 11:27:01
 * 工作队列模型--消费者1
 */
public class Consumer1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("work",true,false,false,null);
        channel.basicConsume("work",true, new DefaultConsumer(channel){//快捷键：Ctrl+O
            @SneakyThrows //根据提示添加的lombok的注解，抑制异常。也可用try catch处理Thread.sleep的异常
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1：" + new String(body));
                Thread.sleep(2000);
            }
        });
//      不需要关闭通道和链接，因为需要监听消息队列
//      channel.close();
//      connection.close();
    }
}
