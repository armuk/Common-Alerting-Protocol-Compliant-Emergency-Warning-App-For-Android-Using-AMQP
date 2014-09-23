package in.ac.iitd.cse.iuatc.ews;

public interface IRabbitMQConsumerCallback {
	public void messageReceived(byte bytes[]);
}
