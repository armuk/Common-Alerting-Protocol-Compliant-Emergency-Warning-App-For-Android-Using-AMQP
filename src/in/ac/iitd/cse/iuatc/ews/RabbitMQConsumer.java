package in.ac.iitd.cse.iuatc.ews;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Handler;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitMQConsumer {
	private Connection connection;
	private Channel channel;
	private QueueingConsumer queueingConsumer;
	private String queue;
	private String consumerTag;
	private boolean running;
	private byte[] message;
	private IRabbitMQConsumerCallback callback;
	private ExecutorService executorService;
	private Handler messageRecieveHandler;

	private static final String TAG = "Rabbit";

	public RabbitMQConsumer(String queue, IRabbitMQConsumerCallback callback) {
		this.running = false;
		this.callback = callback;
		this.queue = queue;
		this.consumerTag = queue;
		this.executorService = Executors.newSingleThreadExecutor();
		this.messageRecieveHandler = new Handler();
	}

	public void start() {
		this.running = true;
		Thread thread = new Thread() {
			@Override
			public void run() {
				connectToRabbitMQ();
			}
		};
		thread.start();
	}

	private void connectToRabbitMQ() {
		while (this.running) {
			try {

				ConnectionFactory connectionFactory = new ConnectionFactory();
				connectionFactory.setUri(BrokerParameters.BROKER_URI);

				this.connection = connectionFactory.newConnection();
				this.channel = this.connection.createChannel();

				this.channel.queueDeclare(this.queue, true, false, false, null);
				this.queueingConsumer = new QueueingConsumer(this.channel);
				this.channel.basicConsume(this.queue, false, this.consumerTag,
						this.queueingConsumer);

				
				this.channel.queueBind(this.queue, "emergency_exchange.topic",
						"India");


				Log.v(TAG, "connected to broker");

				Callable<Integer> callable = new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						return consume();
					}
				};

				Future<Integer> future = this.executorService.submit(callable);

				Log.v(TAG, String.format("%d messages were recieved",
						future.get()));
			} catch (Exception e) {
				Log.e(TAG, "\n\n***** unable to reach broker *****\n\n", e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException exx) {
				}
			}
		}

	}

	int consume() throws Exception {
		int count = 0;
		while (this.running) {
			QueueingConsumer.Delivery delivery;
			Log.v(TAG, "waiting for message...");
			delivery = this.queueingConsumer.nextDelivery();
			this.message = delivery.getBody();
			count++;
			Log.v(TAG, String.format("message recieved\t[%d]", count));
			this.messageRecieveHandler.post(new Runnable() {
				@Override
				public void run() {
					callback.messageReceived(RabbitMQConsumer.this.message);
				}
			});
			this.channel.basicAck(delivery.getEnvelope().getDeliveryTag(),
					false);
			Log.v(TAG, "ack sent");
		}
		return count;
	}

	public void stop() {
		this.running = false;
		this.connection.abort();
	}

}
