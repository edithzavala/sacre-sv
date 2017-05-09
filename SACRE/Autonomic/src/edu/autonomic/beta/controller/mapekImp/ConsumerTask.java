package edu.autonomic.beta.controller.mapekImp;

import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;
import edu.autonomic.beta.controller.mapek.Consumer;

/** 
* @author Jordi Marco
*/

public class ConsumerTask<Item> implements Runnable{
	private SyncronizedBuffer<Item> buffer;
	private Consumer<Item> consumer;
	
	public ConsumerTask(SyncronizedBuffer<Item> buffer, Consumer<Item> consumer) {
		super();
		this.buffer = buffer;
		this.consumer = consumer;
	}
	
	@Override
	public void run() {
		while(true) {
			Item item = this.buffer.getNext();
			this.consumer.consume(item);
		}
	}

}
