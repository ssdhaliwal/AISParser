package elsu.ais.resource;

import java.security.*;
import javax.crypto.*;

public abstract class AISCryptoSupport {

	private static String key = "";
	private static String sByte = "";
	private static String date = "";
	
	public static String getKey() {
		return key;
	}
	
	public static String getKeyBytes() {
		return sByte;
	}
}
