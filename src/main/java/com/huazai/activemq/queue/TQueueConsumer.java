package com.huazai.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TQueueConsumer {
	
	@Test
	public void recieve() throws Exception {
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
		// 5、使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象
		Queue query = session.createQueue("test-001");
		// 6、使用Session对象创建一个Producer对象
		MessageConsumer consumer = session.createConsumer(query);
		// 7、接收消息，并解析输出
		
		//第一种接收消息.直接接收  只是测试的使用
		/*while(true){
			//设置接收消息的超时时间 单位是毫秒
			Message receive = consumer.receive(3000000);
			
			if(receive==null){
				break;
			}
			
			//取消息
			if(receive instanceof TextMessage){
				TextMessage message = (TextMessage)receive;
				String text = message.getText();//获取消息的内容
				System.out.println(text);
			}
		}*/
		
		//第二种接收消息.设置一个监听器  就是开启了一个新的线程
		System.out.println("start");
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				if(message instanceof TextMessage){
					TextMessage message2 = (TextMessage)message;
					String text="";
					try {
						text = message2.getText();
					} catch (JMSException e) {
						e.printStackTrace();
					}
					//输出消息内容
					System.out.println(text);
				}
			}
		});
		System.out.println("end");
		// 模拟消费者挂起
		Thread.sleep(10000000);
		
		// 9、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
