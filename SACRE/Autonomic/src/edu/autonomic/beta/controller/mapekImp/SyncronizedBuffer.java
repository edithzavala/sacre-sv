package edu.autonomic.beta.controller.mapekImp;

import java.util.LinkedList;
import java.util.List;

import edu.autonomic.beta.controller.functions.AutonomicOuput;
import edu.autonomic.beta.controller.functions.AutonomicOuput.Type;

/** 
* @author Jordi Marco
*/

public class SyncronizedBuffer<Element> {
	private List<Element> buffer;

	public SyncronizedBuffer() {
		super();
		this.buffer = new LinkedList<Element>();
	}

	public synchronized Element getNext() {
		// Wait until messages is not empty
		while (buffer.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		Element message = buffer.remove(0);
		notifyAll();
		return message;
	}

	public synchronized void put(Element message) {
		if (message != null) {
			buffer.add(message);
			// Notify consumer that status has changed.
			notifyAll();
		}
	}
}
