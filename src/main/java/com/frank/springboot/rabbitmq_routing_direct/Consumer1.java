package com.frank.springboot.rabbitmq_routing_direct;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 2021-12-13 17:20:21
 * 第四种模型：Routing路由之Direct直连
 */
public class Consumer1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "logs_direct";//交换机名

        channel.exchangeDeclare(exchangeName,"direct");//通道声明交换机以及交换机类型
        String queueName = channel.queueDeclare().getQueue();//创建一个临时队列
        channel.queueBind(queueName,exchangeName,"error");//基于route key绑定队列和交换机

        //获取消费的消息
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1：" + new String(body));
            }
        });
    }
}
