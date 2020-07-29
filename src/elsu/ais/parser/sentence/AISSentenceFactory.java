package elsu.ais.parser.sentence;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import elsu.ais.parser.exceptions.IncompleteFragmentException;

public class AISSentenceFactory {

	public AISSentenceFactory() {
	}

	public void parseSentence(String message) {
		try {
			if (sentence != null) {
				try {
					AISSentence.appendString(message, sentence);
				} catch (IncompleteFragmentException ife) {
					notifyError(ife, sentence);
				}
			} else {
				sentence = AISSentence.fromString(message);
			}
			
			// if complete sentence notify complete
			if (sentence.isComplete() && sentence.isValid()) {
				notifyComplete(sentence);
			}
		} catch (Exception ex) {
			notifyError(ex, sentence);
		}
	}

	public void addEventListener(IEventListener listener) {
		listeners.add(listener);
	}

	public void removeEventListener(IEventListener listener) {
		listeners.remove(listener);
	}

	public void notifyError(Exception ex, Object o) {
		for (IEventListener listener : listeners) {
			listener.onError(ex, o);
		}

		// clear the existing sentence
		sentence = null;
	}

	public void notifyComplete(Object o) {
		for (IEventListener listener : listeners) {
			listener.onComplete(o);
		}

		// clear the existing sentence
		sentence = null;
	}

	private AISSentence sentence = null;
	private final Set<IEventListener> listeners = new CopyOnWriteArraySet<>();
}
