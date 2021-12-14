package com.frank.springboot.rabbitmq_routing_topic;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 2021-12-14 11:18:01
 * 第五种模型：Routing路由之Topic（动态路由）
 */
public class Consumer1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("topics","topic");//声明交换机及其类型 参数1：交换机名，参数2：交换机类型
        String queueName = channel.queueDeclare().getQueue();//创建临时队列
        channel.queueBind(queueName,"topics","user.*");//绑定队列和交换机，动态通配符形式route key

        //消费消息
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1：" + new String(body));
            }
        });
    }
}
