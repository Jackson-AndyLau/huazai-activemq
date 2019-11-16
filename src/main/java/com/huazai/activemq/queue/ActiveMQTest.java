package com.huazai.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

/**
 * 
 * @author HuaZai
 * @contact who.seek.me@java98k.vip
 *          <ul>
 * @description TODO
 *              </ul>
 * @className ActiveMQTest
 * @package com.huazai.activemq.queue
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class ActiveMQTest {

	@Test
	public void test() throws Exception {
		// 1、创建ConnectionFactory对象，并指定ActiveMQ服务ip及端口号（需要指定TCP通信）
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.194.131:61616");
		// 2、使用ConnectionFactory对象创建一个Connection对象
		Connection connection = factory.createConnection();
		// 3、开启连接，调用Connection对象的start方法
		connection.start();
		// 4、使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象
		Queue query = session.createQueue("test-001");
		// 6、使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(query);
		// 7、创建TextMessage对象，并封装消息
		TextMessage message = session.createTextMessage("Hello ActiveMQ!!!");
		// 8、使用Producer对象发送消息
		producer.send(message);
		// 9、释放连接资源
		session.close();
		producer.close();
		connection.close();
	}
}