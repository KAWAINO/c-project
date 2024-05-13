package iwm_ko.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {
	
	private final static String mSalt = "apms";

	public String encodineSha256(String str) {
		String result = "";

		byte[] strByte = str.getBytes();
		byte[] salt = mSalt.getBytes();
		byte[] bytes = new byte[strByte.length + salt.length];

		System.arraycopy(strByte, 0, bytes, 0, strByte.length);
		System.arraycopy(salt, 0, bytes, strByte.length, salt.length);

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// md.update((str).getBytes());
			md.update(bytes);

			byte[] bytedata = md.digest();
			StringBuffer sb = new StringBuffer();

			for(byte b : bytedata) {
				sb.append(String.format("%02x", b));
			}
			result = sb.toString();

		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
//	public class SHA256Hashing {
//		public static String encodeSha256(String input) {
//			try {
//				MessageDigest md = MessageDigest.getInstance("SHA-256");
//				byte[] hashedBytes = md.digest(input.getBytes("UTF-8"));
//
//				// 바이트 배열을 16진수 문자열로 변환
//				StringBuilder hexString = new StringBuilder();
//				for (byte b : hashedBytes) {
//					String hex = Integer.toHexString(0xff & b);
//					if (hex.length() == 1) {
//						hexString.append('0');
//					}
//					hexString.append(hex);
//				}
//				return hexString.toString();
//			} catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}
}
