package edu.autonomic.beta.controller.mapekImp;

import edu.autonomic.beta.controller.mapek.Producer;

/** 
* @author Jordi Marco
*/

public class ProducerTask<Item> implements Runnable {
	private SyncronizedBuffer<Item> buffer;
	private Producer<Item> producer;

	public ProducerTask(SyncronizedBuffer<Item> buffer, Producer<Item> producer) {
		super();
		this.buffer = buffer;
		this.producer = producer;
	}

	@Override
	public void run() {
		while(true) {
			Item item = this.producer.produce();
			this.buffer.put(item);
		}		
	}

}
