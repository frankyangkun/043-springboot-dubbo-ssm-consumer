package com.frank.springboot.rabbitmq_workqueues;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 2021-12-06 11:27:01
 * 工作队列模型--消费者2
 * 1、默认情况（消息平均分配给多个消费者）；
 * 2、实现『能者多劳』，处理快消费者消费更多的消息，避免消息在通道堆积
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);//每次只能消费1个消息
        channel.queueDeclare("work",true,false,false,null);
        channel.basicConsume("work",false, new DefaultConsumer(channel){//快捷键：Ctrl+O,参数2false，不自动确认
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2：" + new String(body));
                //手动确认，参数1：手动确认消息标识，确认队列中哪个具体消息，参数2：是否开启多个消息同时确认，false每次确认一个
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
//      不需要关闭通道和链接，因为需要监听消息队列
//      channel.close();
//      connection.close();
    }
}
