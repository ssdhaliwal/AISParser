package elsu.base;

public interface IEventListener {
	void onError(Exception ex, Object o, String message);
	void onComplete(Object o);
	void onUpdate(Object o);
}
