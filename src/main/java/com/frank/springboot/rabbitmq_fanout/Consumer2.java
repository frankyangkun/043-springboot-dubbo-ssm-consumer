package com.frank.springboot.rabbitmq_fanout;

import com.frank.springboot.mqutils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 2021-12-10 16:28:30
 * 第三种模型：广播（或发布/订阅）
 * 消费者2
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        //通道绑定交换机，可以用exchangeBind也可以用exchangeDeclare直接声明交换机
        channel.exchangeDeclare("testExchange","fanout");

        //交换机还要绑定队列（临时队列，队列可能会很多，所以不持久化）
        String queueName = channel.queueDeclare().getQueue();//获取临时队列
        channel.queueBind(queueName,"testExchange","");//交换机和队列绑定，参数3：路由key，广播模型下没用

        //消费消息
        channel.basicConsume(queueName,true, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2：" + new String(body));
            }
        });
    }
}
