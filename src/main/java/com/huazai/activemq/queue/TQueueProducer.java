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
 * @description ActiveMQ的Queue的Producer测试单元
 *              </ul>
 * @className ActiveMQTest
 * @package com.huazai.activemq.queue
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class TQueueProducer
{

	@Test
	public void test() throws Exception
	{
		// 1、创建 ConnectionFactory 对象，并指定 ActiveMQ 服务IP及端口号（需要指定TCP通信）
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.194.131:61616");
		// 2、使用 ConnectionFactory 对象创建一个 Connection 对象
		Connection connection = factory.createConnection();
		// 3、开启连接，调用 Connection 对象的 start() 方法
		connection.start();
		// 4、使用 Connection 对象创建一个 Session 对象
		// 第一个参数：表示是否开启分布式事务（JTA） 一般就是false :表示不开启。 只有设置了false ,第二个参数才有意义。
		// 第二个参数：表示设置应答模式 自动应答和手动应答 。一般使用的是自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用 Session 对象创建一个 Destination 对象（topic、queue），此处创建一个 Queue 对象
		Queue query = session.createQueue("queue-001");
		// 6、使用 Session 对象构建一个 Producer 对象
		MessageProducer producer = session.createProducer(query);
		// 7、创建 TextMessage 对象，并封装消息
		TextMessage message = session.createTextMessage("Hello test ActiveMQ queue!!!");
		// 8、使用 Producer 对象发送消息
		producer.send(message);
		// 9、释放连接资源
		session.close();
		producer.close();
		connection.close();
	}
}
