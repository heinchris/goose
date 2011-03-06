package com.obtiva.goose.acceptance.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

public class JmsUtils {

	public static final String TOPIC_KEY = "auctions";
	private static final List<SimpleMessageListenerContainer> containers = new ArrayList<SimpleMessageListenerContainer>();

	public InitialContext getContext() {
		try {
			return new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public ConnectionFactory getConnectionFactory() {
		try {
			String factoryName = "ConnectionFactory";
			return (ConnectionFactory) getContext().lookup(factoryName);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public Session getSession() {
		try {
			return getConnectionFactory().createConnection().createSession(
					false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public Destination getDestination(String destinationName) {
		try {
			// look up the ConnectionFactory
			return (Destination) getContext().lookup(destinationName);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public JmsTemplate getJmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(getConnectionFactory());
		jmsTemplate.setDefaultDestination(getDestination(TOPIC_KEY));
		return jmsTemplate;
	}

	public SimpleMessageListenerContainer getMessageListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(getConnectionFactory());
		container.setDestination(getDestination(TOPIC_KEY));
		container.start();

		containers.add(container);
		return container;
	}

	public void addMessageListener(MessageListener listener) {
		getMessageListenerContainer().setMessageListener(listener);
	}
	
	public void removeMessageListener(MessageListener listener) {
		SimpleMessageListenerContainer matchingContainer = findContainerFor(listener);
		matchingContainer.stop();
		containers.remove(matchingContainer);
	}

	private SimpleMessageListenerContainer findContainerFor(MessageListener listener) {
		for (SimpleMessageListenerContainer container : containers) {
			if (listener == container.getMessageListener()) {
				return container;
			}
		}
		// Null Object
		return new SimpleMessageListenerContainer() {
			@Override
			public void stop() throws JmsException {
				// NoOp
			}
		};
	}
	
	public void sendMessage(final String contents) {
		sendMessage(contents, new HashMap<String, Object>());
	}
	
	public void sendMessage(final String contents, final Map<String, Object> properties) {
		getJmsTemplate().send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage();
				for (String key : properties.keySet()) {
					message.setObjectProperty(key, properties.get(key));
				}
				message.setText(contents);
				return message;
			}
		});
	}

}
