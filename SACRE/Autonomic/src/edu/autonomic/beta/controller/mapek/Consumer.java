package edu.autonomic.beta.controller.mapek;

/** 
* @author Jordi Marco
*/

public interface Consumer<Item> {
	public void consume(Item item);
}
