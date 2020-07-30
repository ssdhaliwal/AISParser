package elsu.ais.parser.sentence;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			 * if (sentence != null) { try { AISSentence.appendString(message,
			 * sentence); } catch (IncompleteFragmentException ife) {
			 * notifyError(ife, sentence); } } else { sentence =
			 * AISSentence.fromString(message); }
			 * 
			 * // if complete sentence notify complete if (sentence.isComplete()
			 * && sentence.isValid()) { notifyComplete(sentence); }
			 */
		} catch (Exception ex) {
			notifyError(ex, sentence, message);
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
		if (tagBlock == null) {
			if (Pattern.matches("^\\$..VSI.*", message)) {
				if (lastSentence != null) {
					VDLSignalInformation vsi = VDLSignalInformation.fromString(message);
					lastSentence.setVDLInfo(vsi);

					notifyUpdate(lastSentence);
					return;
				} else {
					throw new Exception("$..VSI messages with no VDO/VDM");
				}
			}

			if (sentence != null) {
				if (sentence.isComplete() && sentence.isValid()) {
					notifyComplete(sentence);
				}
			}

			if (sentence == null) {
				sentence = AISSentence.fromString(message);
			} else {
				try {
					sentence = AISSentence.appendString(message, sentence);
				} catch (IncompleteFragmentException ife) {
					notifyError(ife, sentence, message);
				}
			}

			if (sentence.isComplete() && sentence.isValid()) {
				notifyComplete(sentence);
			}
		}
		// tag block parsing and validation
		else {
			// if prior tag exists and sequence is invalid or code is invalid;
			// report error

			// update the tag block
			if (getTagBlock() == null) {
				setTagBlock(tagBlock);
			} else {
				if ((getTagBlock().getSentenceGroup().getTotallines() != tagBlock.getSentenceGroup().getTotallines())
						|| (!getTagBlock().getSentenceGroup().getCode().equals(tagBlock.getSentenceGroup().getCode()))
						|| ((getTagBlock().getSentenceGroup().getLinenumber() + 1) != tagBlock.getSentenceGroup()
								.getLinenumber())) {
					notifyError(new Exception("tag mis-match"), sentence, message);

					if (tagBlock.getSentenceGroup().getLinenumber() == 1) {
						setTagBlock(tagBlock);
					} else {
						throw new Exception("new tag sequence invalid");
					}
				}

				getTagBlock().getSentenceGroup().setLinenumber(tagBlock.getSentenceGroup().getLinenumber());
			}

			// see if there is a message as part of the tag
			// -- extract message and process it
			hMatch = AISBase.messageVDOPattern.matcher(message);
			while (hMatch.find()) {
				tags = hMatch.group(0);

				if (sentence == null) {
					sentence = AISSentence.fromString(message);
				} else {
					try {
						sentence = AISSentence.appendString(message, sentence);
					} catch (IncompleteFragmentException ife) {
						notifyError(ife, sentence, message);
					}
				}
			}

			// see if the tag is complete
			// -- send complete
			if (sentence.isComplete() && sentence.isValid() && (getTagBlock().getSentenceGroup()
					.getLinenumber() == getTagBlock().getSentenceGroup().getTotallines())) {
				notifyComplete(sentence);
			}
		}
	}

	public void addEventListener(IEventListener listener) {
		listeners.add(listener);
	}

	public void removeEventListener(IEventListener listener) {
		listeners.remove(listener);
	}

	public void notifyError(Exception ex, Object o, String message) {
		for (IEventListener listener : listeners) {
			listener.onError(ex, o, message);
		}

		// clear the existing sentence
		lastSentence = null;
		sentence = null;
		tagBlock = null;
		vdlInfo = null;
	}

	public void notifyComplete(Object o) {
		for (IEventListener listener : listeners) {
			listener.onComplete(o);
		}

		// clear the existing sentence
		lastSentence = sentence;
		sentence = null;
		tagBlock = null;
		vdlInfo = null;
	}

	public void notifyUpdate(Object o) {
		for (IEventListener listener : listeners) {
			listener.onUpdate(o);
		}

		// clear the existing sentence
		lastSentence = null;
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

	private AISSentence lastSentence = null;
	private AISSentence sentence = null;
	private SentenceTagBlock tagBlock = null;
	private VDLSignalInformation vdlInfo = null;

	private final Set<IEventListener> listeners = new CopyOnWriteArraySet<>();
}
