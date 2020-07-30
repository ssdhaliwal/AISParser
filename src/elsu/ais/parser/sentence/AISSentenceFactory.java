package elsu.ais.parser.sentence;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;

import elsu.ais.parser.AISBase;
import elsu.ais.parser.exceptions.IncompleteFragmentException;
import elsu.ais.parser.sentence.tags.SentenceTagBlock;
import elsu.ais.parser.sentence.tags.VDLSignalInformation;

public class AISSentenceFactory {

	public AISSentenceFactory() {
	}

	public void parseSentence(String message) {
		try {
			validateSentence(message);
			/*
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
			*/
		} catch (Exception ex) {
			notifyError(ex, sentence);
		}
	}

	private void validateSentence(String message) throws Exception {
		System.out.println(message);
		
		// if message has tag blocks
		String tags = "";
		SentenceTagBlock tagBlock = null;
		
		Matcher hMatch = AISBase.headerPattern.matcher(message);
		while (hMatch.find()) {
			tags = hMatch.group(0).replaceAll("\\\\", "");
			tagBlock = SentenceTagBlock.fromString(tags);
		}
		
		// if message has no tag blocks
		if (sentence == null) {
			if (tagBlock == null) {
				sentence = AISSentence.fromString(message);
			
				if (sentence.isComplete() && sentence.isValid()) {
					notifyComplete(sentence);
				}
			} else {
				sentence = new AISSentence();
				sentence.setTagBlock(tagBlock);
			}
		} else {
			if ((tagBlock == null) && (sentence.getTagBlock() == null)) {
				try {
					sentence = AISSentence.appendString(message, sentence);
				} catch (IncompleteFragmentException ife) {
					notifyError(ife, sentence);
				}
				
				if ((sentence != null) && sentence.isComplete() && sentence.isValid()) {
					notifyComplete(sentence);
				} else {
					sentence = AISSentence.fromString(message);
					if (sentence.isComplete() && sentence.isValid()) {
						notifyComplete(sentence);
					}
				}
			}
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
	
	public SentenceTagBlock getTagBlock() {
		return this.tagBlock;
	}

	public void setTagBlock(SentenceTagBlock tagBlock) {
		this.tagBlock = tagBlock;
	}

	public VDLSignalInformation getVDLInfo() {
		return this.vdlInfo;
	}

	private AISSentence sentence = null;
	private SentenceTagBlock tagBlock = null;
	private VDLSignalInformation vdlInfo = null;

	private final Set<IEventListener> listeners = new CopyOnWriteArraySet<>();
}
