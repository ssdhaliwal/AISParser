package elsu.sentence;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;

import elsu.ais.base.AISMessageBase;
import elsu.ais.exceptions.IncompleteFragmentException;
import elsu.base.IAISEventListener;
import elsu.nmea.messages.NMEAMessage;
//import elsu.nmea.messages.VSISignalInformation;
import elsu.sentence.tags.SentenceTagBlock;

public class SentenceFactory {

	public SentenceFactory() {
		// (default) messages to parse
		SentenceFactory.processMessages = new Boolean[SentenceFactory.max_messages];
		for (int i = 0; i < SentenceFactory.max_messages; i++) {
			SentenceFactory.processMessages[i] = true;
		}
	}

	public SentenceFactory(String messagesToProcess) {
		this();

		String messages = messagesToProcess;
		if (!messages.equals("*")) {
			for (int i = 0; i < getMaxMessages(); i++) {
				getProcessMessages()[i] = false;
			}

			String[] msgs = messages.split(",");
			Integer msgNumber = 0;
			for (String msg : msgs) {
				msgNumber = Integer.valueOf(msg);
				getProcessMessages()[msgNumber] = true;
			}
		}
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

	public void parseSentence(ArrayList<String> messages) {
		for (String message : messages) {
			parseSentence(message);
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
		if (!SentenceBase.supportedTAGFormatsPattern.matcher(message).matches()
				&& !SentenceBase.supportedNMEAFormatsPattern.matcher(message).matches()) {
			throw new Exception("unsupported format; " + message);
		}

		if (SentenceBase.supportedNMEAFormatsPattern.matcher(message).matches()) {
			NMEAMessage nmea = NMEAMessage.fromString(message);
			notifyComplete(nmea);
			return;
		}

		// if message has no tag blocks (TSA preceeds VDO/VDM, VSI follows
		// VDO/VDM)
		if ((tagBlock == null) || (tagBlock.getSentenceGroup() == null)) {
			if (SentenceBase.messageTSAPattern.matcher(message).matches()) {
				if ((tsaInfo != null) || (sentence != null)) {
					notifyError(new Exception("$..TSA message with no VDO/VDM"), tsaInfo, null);
				}

				tsaInfo = NMEAMessage.fromString(message);
				return;
			}

			if (SentenceBase.messageVSIPattern.matcher(message).matches()) {
				if (sentence != null) {
					NMEAMessage nmeaMessage = NMEAMessage.fromString(message);
					sentence.setVSIInfo(nmeaMessage);

					if (sentence.isComplete() && sentence.isValid()) {
						notifyUpdate(sentence);
					} else {
						notifyError(new Exception("incomplete message"), sentence, message);
					}

					sentence = null;
					tsaInfo = null;
					vsiInfo = null;
					return;
				} else {
					throw new Exception("$..VSI message with no VDO/VDM");
				}
			}

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
			}

			// link TSA to the sentence
			if (sentence != null) {
				sentence.setTSAInfo(tsaInfo);
			}

			if (sentence.isComplete() && sentence.isValid()) {
				if (!sentence.isIgnore()) {
					notifyComplete(sentence);
				}

				setTagBlock(null);
				sentence = null;
				tsaInfo = null;
				vsiInfo = null;
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
				try {
					if ((getTagBlock().getSentenceGroup().getTotallines() != tagBlock.getSentenceGroup()
							.getTotallines())
							|| (!getTagBlock().getSentenceGroup().getCode()
									.equals(tagBlock.getSentenceGroup().getCode()))
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
				} catch (Exception exi) {
					throw new Exception("new tag sequence invalid - tag block invalid");
				}
			}

			// see if there is a message as part of the tag
			Boolean processed = false;

			// -- extract message and process it
			hMatch = SentenceBase.messageVDOPattern.matcher(message);
			while (hMatch.find()) {
				tags = hMatch.group(0);
				processed = true;

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

			if (!processed) {
				hMatch = SentenceBase.messageVSIPattern.matcher(message);
				while (hMatch.find()) {
					tags = hMatch.group(0);
					processed = true;

					if (sentence == null) {
						throw new Exception("$..VSI message with no VDO/VDM");
					}

					vsiInfo = NMEAMessage.fromString(tags);
					sentence.setVSIInfo(vsiInfo);
				}
			}

			if (!processed) {
				hMatch = SentenceBase.messageTSAPattern.matcher(message);
				while (hMatch.find()) {
					tags = hMatch.group(0);
					processed = true;

					if (sentence == null) {
						throw new Exception("$..TSA message with no VDO/VDM");
					}

					tsaInfo = NMEAMessage.fromString(tags);
					sentence.setTSAInfo(tsaInfo);
				}
			}

			// -- send complete
			if (sentence.isComplete() && sentence.isValid() && (getTagBlock().getSentenceGroup()
					.getLinenumber() == getTagBlock().getSentenceGroup().getTotallines())) {
				if (!sentence.isIgnore()) {
					notifyComplete(sentence);
				}

				setTagBlock(null);
				sentence = null;
				tsaInfo = null;
				vsiInfo = null;
			}
		}
	}

	public void addEventListener(IAISEventListener listener) {
		listeners.add(listener);
	}

	public void removeEventListener(IAISEventListener listener) {
		listeners.remove(listener);
	}

	public void notifyError(Exception ex, Object o, String message) {
		for (IAISEventListener listener : listeners) {
			listener.onAISError(ex, o, message);
		}
	}

	public void notifyComplete(Object o) {
		if ((o instanceof Sentence) && ((Sentence)o).isIgnore()) {
			return;
		}

		for (IAISEventListener listener : listeners) {
			listener.onAISComplete(o);
		}
	}

	public void notifyUpdate(Object o) {
		if ((o instanceof Sentence) && ((Sentence)o).isIgnore()) {
			return;
		}

		for (IAISEventListener listener : listeners) {
			listener.onAISUpdate(o);
		}
	}

	public SentenceTagBlock getTagBlock() {
		return this.tagBlock;
	}

	public void setTagBlock(SentenceTagBlock tagBlock) {
		this.tagBlock = tagBlock;
	}

	public NMEAMessage getTSAInfo() {
		return this.tsaInfo;
	}

	public static int getMaxMessages() {
		return SentenceFactory.max_messages;
	}

	public static Boolean[] getProcessMessages() {
		return SentenceFactory.processMessages;
	}

	public static Boolean isValidMessage(int msgType) {
		Boolean result = false;

		if ((msgType >= 0) && (msgType < SentenceFactory.getMaxMessages()) && 
				(SentenceFactory.getProcessMessages()[msgType])) {
			result = true;
		}

		return result;
	}

	public static Boolean isValidMessage(AISMessageBase message) {
		Boolean result = false;

		if ((message != null) && SentenceFactory.getProcessMessages()[message.getType()]) {
			result = true;
		}

		return result;
	}

	private static int max_messages = 28; // include zero message type (0-27)
	private static Boolean[] processMessages = null;

	private Sentence sentence = null;
	private SentenceTagBlock tagBlock = null;
	private NMEAMessage tsaInfo = null;
	private NMEAMessage vsiInfo = null;

	private final Set<IAISEventListener> listeners = new CopyOnWriteArraySet<>();
}
