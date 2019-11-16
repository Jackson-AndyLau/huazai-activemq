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
