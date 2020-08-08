package elsu.sentence;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import elsu.ais.base.IEventListener;
import elsu.ais.exceptions.IncompleteFragmentException;
import elsu.nmea.messages.VSISignalInformation;
import elsu.sentence.tags.SentenceTagBlock;

public class SentenceFactory {

	public SentenceFactory() {
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
		// if message has tag blocks
		String tags = "";
		SentenceTagBlock tagBlock = null;

		Matcher hMatch = SentenceBase.headerPattern.matcher(message);
		while (hMatch.find()) {
			tags = hMatch.group(0).replaceAll("\\\\", "");
			tagBlock = SentenceTagBlock.fromString(tags);
		}

		// if non-supported sentence format; return error
		if (!SentenceBase.supportedFormatsPattern.matcher(message).matches()) {
			throw new Exception("unsupported format; " + message);
		}

		// if message has no tag blocks
		if (tagBlock == null) {
			if (SentenceBase.messageVSIShortPattern.matcher(message).matches()) {
				if (lastSentence != null) {
					VSISignalInformation vsi = VSISignalInformation.fromString(message);
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
				sentence = Sentence.fromString(message);
			} else {
				try {
					sentence = Sentence.appendString(message, sentence);
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
			hMatch = SentenceBase.messageVDOPattern.matcher(message);
			while (hMatch.find()) {
				tags = hMatch.group(0);

				if (sentence == null) {
					sentence = Sentence.fromString(tags);
				} else {
					try {
						sentence = Sentence.appendString(tags, sentence);
					} catch (IncompleteFragmentException ife) {
						notifyError(ife, sentence, message);
					}
				}

				sentence.setTagBlock(tagBlock);
			}

			hMatch = SentenceBase.messageVSIPattern.matcher(message);
			while (hMatch.find()) {
				tags = hMatch.group(0);

				if (sentence == null) {
					throw new Exception("$..VSI messages with no VDO/VDM");
				}

				VSISignalInformation vsi = VSISignalInformation.fromString(tags);
				sentence.setVDLInfo(vsi);
			}

			// -- send complete
			if (sentence.isComplete() && sentence.isValid() && (getTagBlock().getSentenceGroup()
					.getLinenumber() == getTagBlock().getSentenceGroup().getTotallines())) {
				notifyComplete(sentence);
			}
		}
	}

	private void doCleanup() {
		// clear the existing sentence
		sentence = null;
		tagBlock = null;
		vdlInfo = null;
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

		this.doCleanup();
	}

	public void notifyComplete(Object o) {
		for (IEventListener listener : listeners) {
			listener.onComplete(o);
		}

		lastSentence = sentence;
		this.doCleanup();
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

	public VSISignalInformation getVDLInfo() {
		return this.vdlInfo;
	}

	private Sentence lastSentence = null;
	private Sentence sentence = null;
	private SentenceTagBlock tagBlock = null;
	private VSISignalInformation vdlInfo = null;

	private final Set<IEventListener> listeners = new CopyOnWriteArraySet<>();
}
