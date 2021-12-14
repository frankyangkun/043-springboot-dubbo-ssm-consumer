package com.frank.springboot.rabbitmq_routing_direct;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 2021-12-13 17:20:21
 * 第四种模型：Routing路由之Direct直连
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "logs_direct";//交换机名

        channel.exchangeDeclare(exchangeName,"direct");//通道声明交换机及其类型
        String queueName = channel.queueDeclare().getQueue();//获取临时队列
        channel.queueBind(queueName,exchangeName,"info");//基于route key绑定队列和交换机
        channel.queueBind(queueName,exchangeName,"error");
        channel.queueBind(queueName,exchangeName,"warning");

        //消费消息
        channel.basicConsume(queueName,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2：" + new String(body));
            }
        });
    }
}
