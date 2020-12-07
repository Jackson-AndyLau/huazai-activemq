# huazai-activemq
---

## ActiveMQ 的使用
### 1）、队列（Queue）的使用

Producer 创建步骤（负责生产消息）：

1. 创建 ConnectionFactory 对象，并指定 ActiveMQ 服务IP及端口号（需要指定TCP通信；
2. 使用 ConnectionFactory 对象创建一个 Connection 对象；
3. 开启连接，调用 Connection 对象的 start() 方法；
4. 使用 Connection 对象创建一个 Session 对象；
5. 使用 Session 对象创建一个 Destination 对象（topic、queue），此处创建一个 Queue 对象；
6. 使用 Session 对象构建一个 Producer 对象；
7. 创建 TextMessage 对象，并封装消息；
8. 使用 Producer 对象发送消息；
9. 释放连接资源；

示例代码如下：
```Java
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
```


Consumer 创建步骤（负责消费消息）：

1. 创建 ConnectionFactory 对象，并指定 ActiveMQ 服务IP及端口号（需要指定TCP通信）；
2. 使用 ConnectionFactory 对象创建一个 Connection 对象；
3. 开启连接，调用 Connection 对象的 start() 方法；
4. 使用 Connection 对象创建一个 Session 对象；
5. 使用 Session 对象创建一个 Destination 对象（topic、queue），此处创建一个 Queue 对象；
6. 使用 Session 对象构建一个 Consumer 对象；
7. 接收消息，并解析输出；
8. 关闭资源；

示例代码如下：
```Java
package com.huazai.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
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
 * @description ActiveMQ的Queue的Consumer测试单元
 *              </ul>
 * @className ActiveMQTest
 * @package com.huazai.activemq.queue
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class TQueueConsumer {
	
	@Test
	public void recieve() throws Exception {
		// 1、创建 ConnectionFactory 对象，并指定 ActiveMQ 服务IP及端口号（需要指定TCP通信）
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.194.131:61616");
		// 2、使用 ConnectionFactory 对象创建一个 Connection 对象
		Connection connection = factory.createConnection();
		// 3、开启连接，调用 Connection 对象的 start() 方法
		connection.start();
		// 4、使用 Connection 对象创建一个 Session 对象
		// 第一个参数：表示是否开启分布式事务（JTA） 一般就是false :表示不开启。 只有设置了false ,第二个参数才有意义。
		// 第二个参数：表示设置应答模式 自动应答和手动应答 。使用的是自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用 Session 对象创建一个 Destination 对象（topic、queue），此处创建一个 Queue 对象
		Queue query = session.createQueue("queue-001");
		// 6、使用 Session 对象构建一个 Consumer 对象
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
		
		// 8、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
```


### 2）、队列（Queue）的使用

Producer 创建步骤（负责生产消息）：

**同上。。。**



示例代码如下：
```Java
package com.huazai.activemq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
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
 * @description ActiveMQ的Topic的Producer测试单元
 *              </ul>
 * @className TopicProducer
 * @package com.huazai.activemq.topic
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class TopicProducer {

	@Test
	public void send() throws Exception {
		// 1、创建 ConnectionFactory 对象，并指定 ActiveMQ 服务 IP 及端口号（需要指定TCP通信）
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.194.131:61616");
		// 2、使用 ConnectionFactory 对象创建一个 Connection 对象
		Connection connection = factory.createConnection();
		// 3、开启连接，调用 Connection 对象的start方法
		connection.start();
		// 4、使用 Connection 对象创建一个 Session 对象
		// 第一个参数：表示是否开启分布式事务（JTA） 一般就是false :表示不开启。 只有设置了false ,第二个参数才有意义。
		// 第二个参数：表示设置应答模式 自动应答和手动应答 。使用的是自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用 session 创建目的地（destination）topic
		Topic topic = session.createTopic("topic-001");
		// 6、使用 Session 对象创建一个 Producer 对象
		MessageProducer producer = session.createProducer(topic);
		// 7、创建 TextMessage 对象，并封装消息
		TextMessage message = session.createTextMessage("Hello test ActiveMQ topic!!!");
		// 8、使用 Producer 对象发送消息
		producer.send(message);
		// 9、释放连接资源
		session.close();
		producer.close();
		connection.close();
	}
}
```


Consumer 创建步骤（负责消费消息）：

**创建步骤同上。。。**



示例代码如下：

订阅者1，

```Java

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
 * @description ActiveMQ的Topic的Consumer1测试单元
 *              </ul>
 * @className TopicProducer
 * @package com.huazai.activemq.topic
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class TopicCustomer1 {

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
```

订阅者2，

```Java
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
 * @description ActiveMQ的Topic的Consumer1测试单元
 *              </ul>
 * @className TopicProducer
 * @package com.huazai.activemq.topic
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class TopicCustomer1 {

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
		// 7、接收消息，并输出内容

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

		// 9、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
```

注意：

**Topic 默认是不存在于MQ服务器中的，一旦发送之后，如果没有订阅，就没了。**
**Queue 默认是存在于MQ的服务器中的，发送消息之后，随时取。但是一定是一个消费者取完就没了。**



### 3)、ActiveMQ 整合 Spring

配置pom.xml，内容如下：

```HTML
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.huazai</groupId>
		<artifactId>huazai-parent</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</parent>
	<artifactId>huazai-activemq</artifactId>
	<dependencies>
		<!-- Spring -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-beans</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aspects</artifactId>
		</dependency>
		<!-- activemq 相关-->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jms</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context-support</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.activemq</groupId>
			<artifactId>activemq-all</artifactId>
		</dependency>
	</dependencies>
</project>
```

在上面配置中以来的父级包，地址：【[GtiHub_Parent](https://github.com/Jackson-AndyLau/huazai-parent)】


新增 Spring 配置文件（applicationContext-activemq.xml）：

```HTML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.2.xsd
	http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.2.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.2.xsd
	http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.2.xsd">

	<!-- 1、配置ActiveMQ的连接工厂 -->
	<bean id="targetConnection"
		class="org.apache.activemq.ActiveMQConnectionFactory">
		<property name="brokerURL"
			value="tcp://192.168.194.131:61616"></property>
	</bean>

	<!-- 2、通用的connectionFactory 指定真正使用的连接工厂 -->
	<bean id="connectionFactory"
		class="org.springframework.jms.connection.SingleConnectionFactory">
		<property name="targetConnectionFactory"
			ref="targetConnection"></property>
	</bean>

	<!-- 3、接收和发送消息时使用的类 -->
	<bean class="org.springframework.jms.core.JmsTemplate">
		<property name="connectionFactory" ref="connectionFactory"></property>
	</bean>

	<!-- 4、配置Queue或者Topic -->
<!-- 	<bean id="queueDestination"
		class="org.apache.activemq.command.ActiveMQQueue">
		<constructor-arg name="name" value="spring-queue-001"></constructor-arg>
	</bean> -->
	<bean id="topicDestination"
		class="org.apache.activemq.command.ActiveMQTopic">
		<constructor-arg name="name" value="spring-topic-001"></constructor-arg>
	</bean>

	<!-- 5、配置ActiveMQ监听容器 -->
	<bean id="activeMQMessageListener"
		class="com.huazai.activemq.spring.ActiveMQMessageListener"></bean>
	<bean
		class="org.springframework.jms.listener.DefaultMessageListenerContainer">
		<property name="connectionFactory" ref="connectionFactory"></property>
		<property name="destination" ref="topicDestination"></property>
		<property name="messageListener"
			ref="activeMQMessageListener"></property>
	</bean>
</beans>
```


创建消息生产者，示例代码如下：

```Java
package com.huazai.activemq.spring;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 
 * @author HuaZai
 * @contact who.seek.me@java98k.vip
 *          <ul>
 * @description ActiveMQ的Producer测试单元
 *              </ul>
 * @className TopicProducer
 * @package com.huazai.activemq.spring
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class Producer {

	private ApplicationContext context;

	@Test
	public void send() throws Exception {
		// 1、初始化Spring容器
		context = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq.xml");
		// 2、获取JmsTemplate对象
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		// 3、获取Destination对象
		Destination destination = (Destination) context.getBean(Destination.class);
		// 4、发送消息
		jmsTemplate.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("Hello test Spring With ActiveMQ Send Message!!!");
			}
		});
		Thread.sleep(100000);
	}
}
```


创建消息监听对象（MessageListener），示例代码如下：

```Java
package com.huazai.activemq.spring;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 
 * @author HuaZai
 * @contact who.seek.me@java98k.vip
 *          <ul>
 * @description ActiveMQ的MessageListener
 *              </ul>
 * @className TopicProducer
 * @package com.huazai.activemq.spring
 * @createdTime 2017年06月18日
 *
 * @version V1.0.0
 */
public class ActiveMQMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 接收消息
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String text;
			try {
				text = textMessage.getText();
				// 输出消息内容
				System.out.println(text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
```
