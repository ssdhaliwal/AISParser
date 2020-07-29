package elsu.ais.parser.sentence;

public interface IEventListener {
	void onError(Exception ex, Object o);
	void onComplete(Object o);
}
