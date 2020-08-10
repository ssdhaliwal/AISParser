package elsu.base;

public interface IAISEventListener {
	void onAISError(Exception ex, Object o, String message);
	void onAISComplete(Object o);
	void onAISUpdate(Object o);
}
