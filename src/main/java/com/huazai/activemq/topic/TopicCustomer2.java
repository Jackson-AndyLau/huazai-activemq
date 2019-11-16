package com.huazai.activemq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

/**
 * 
 * @author HuaZai
 * @contact who.seek.me@java98k.vip
 *          <ul>
 * @description ActiveMQ的Topic的Consumer2测试单元
 *              </ul>
 * @className TopicProducer
 * @package com.huazai.activemq.topic
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class TopicCustomer2 {
	
	@Test
	public void reieve() throws Exception {
		// 1、创建ConnectionFactory对象，并指定ActiveMQ服务ip及端口号（需要指定TCP通信）
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.194.131:61616");
		// 2、使用ConnectionFactory对象创建一个Connection对象
		Connection connection = factory.createConnection();
		// 3、开启连接，调用Connection对象的start方法
		connection.start();
		// 4、使用Connection对象创建一个Session对象
		// 第一个参数：表示是否开启分布式事务（JTA） 一般就是false :表示不开启。 只有设置了false ,第二个参数才有意义。
		// 第二个参数：表示设置应答模式 自动应答和手动应答 。使用的是自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用session创建目的地（destination）topic
		Topic topic = session.createTopic("topic-001");
		// 6、使用Session对象创建一个Producer对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 7.接收消息

		// 设置消息监听器
		System.out.println("start");
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				if (message instanceof TextMessage) {
					TextMessage message2 = (TextMessage) message;
					String text = "";
					try {
						text = message2.getText();
					} catch (JMSException e) {
						e.printStackTrace();
					}
					// 获取消息的内容
					System.out.println(text);
				}
			}
		});
		System.out.println("end");
		// 睡眠
		Thread.sleep(10000000);

		// 9.、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
