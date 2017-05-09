/*
 * Observer/Observable
 */

package edu.autonomic.beta.controller.mapek;

import edu.autonomic.beta.controller.documents.IDocument;

/** 
* @author Edith Zavala
*/

public interface IComponent {

	public enum State {
		OK, UNSTABLE, UNKOWN, DEAD
	}
	
	// Observable
	public void notifyState();

	public void attachObserver(IComponent observer);

	public void deattachObserver(IComponent observer);

	// Observer
	public void notified(String name, State state, IDocument doc);

}
