package com.frank.springboot.rabbitmq;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 2021-12-01 17:23:55
 * 消费者
 * 1、与rabbitmq建立连接，获取一个连接对象
 * 2、消费消息
 * 3、后续处理（关闭通道连接）
 */
public class Consumer {
//    @Test //这里不能使用junit，因为不支持多线程模型，执行完就结束了，无法让消费者处于监听状态，要用main函数来执行
//    public void test() throws IOException, TimeoutException {
//        //创建连接工厂
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/testhost");
//        connectionFactory.setUsername("test");
//        connectionFactory.setPassword("test");
//
//        Connection connection = connectionFactory.newConnection();//创建连接对象
//        Channel channel = connection.createChannel();//创建通道
//
//        //通道绑定队列
//        channel.queueDeclare("hellomq",false,false,false,null);//注意生产者消费者对同一个队列定义是一样的，比如都是持久化
//
//        //消费消息
//        //参数1：消费哪个队列的消息 队列名称；参数2：开启消息的自动确认机制；参数3：消费时的回调接口（消费时需要拿到这个消息队列中的消息）
//        channel.basicConsume("hellomq",true, new DefaultConsumer(channel){
//            @Override //参数1：类似于标签；参数2：消息传递过程中的信封；参数3：消息队列中取出的消息
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                super.handleDelivery(consumerTag, envelope, properties, body);
//                System.out.println("消息队列中的消息:" + new String(body));//打印看看消息队列中的消息
//            }
//        });
//
//        //消费者执行完后，如果不关闭通道和链接，会一直监听队列，也就是只要有新消息来，就会消费
////        channel.close();
////        connection.close();
//    }

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/testhost");
//        connectionFactory.setUsername("test");
//        connectionFactory.setPassword("test");
//
//        Connection connection = connectionFactory.newConnection();//创建连接对象

        //通过工具类获取连接对象
        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();//创建通道

        //通道绑定队列
        channel.queueDeclare("hellomq",false,false,false,null);//注意生产者消费者对同一个队列定义是一样的，比如都是持久化

        //消费消息
        //参数1：消费哪个队列的消息 队列名称；参数2：开启消息的自动确认机制；参数3：消费时的回调接口（消费时需要拿到这个消息队列中的消息）
        channel.basicConsume("hellomq",true, new DefaultConsumer(channel){
            @Override //参数1：类似于标签；参数2：消息传递过程中的信封；参数3：消息队列中取出的消息
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("消息队列中的消息:" + new String(body));//打印看看消息队列中的消息
            }
        });
        //消费者执行完后，如果不关闭通道和链接，会一直监听队列，也就是只要有新消息来，就会消费
//        channel.close();
//        connection.close();
    }
}
