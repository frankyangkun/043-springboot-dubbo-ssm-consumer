package com.frank.springboot.rabbitmq_workqueues;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * 2021-12-06 11:27:01
 * 工作队列模型--消费者1
 * 1、默认情况（消息平均分配给多个消费者）；
 * 2、实现『能者多劳』，处理快消费者消费更多的消息，避免消息在通道堆积
 */
public class Consumer1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);//每次只能消费1个消息
        channel.queueDeclare("work",true,false,false,null);
        channel.basicConsume("work",false, new DefaultConsumer(channel){//快捷键：Ctrl+O，参数2false，不自动确认
            @SneakyThrows //根据提示添加的lombok的注解，抑制异常。也可用try catch处理Thread.sleep的异常
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1：" + new String(body));
                Thread.sleep(2000);//模拟处理时间较长的消费者
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
//      不需要关闭通道和链接，因为需要监听消息队列
//      channel.close();
//      connection.close();
    }
}
