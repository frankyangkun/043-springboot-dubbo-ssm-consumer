package com.frank.springboot.rabbitmq_workqueues;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 2021-12-06 11:27:01
 * 工作队列模型--消费者2
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("work",true,false,false,null);
        channel.basicConsume("work",true, new DefaultConsumer(channel){//快捷键：Ctrl+O
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2：" + new String(body));
            }
        });
//      不需要关闭通道和链接，因为需要监听消息队列
//      channel.close();
//      connection.close();
    }
}
