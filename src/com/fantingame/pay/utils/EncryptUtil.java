package com.fantingame.pay.utils;

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryptUtil {
	private static final Log LOGGER = LogFactory.getLog(EncryptUtil.class);

	public static String getMD5(String str) {
		return encode(str, "MD5");
	}

	public static String getDES(String str) {
		return encode(str, "DES");
	}

	public static String getSHA1(String str) {
		return encode(str, "SHA-1");
	}

	public static String getSHA256(String str) {
		return encode(str, "SHA-256");
	}
	
	private static String encode(String str, String type) {
		try {
			MessageDigest alga = MessageDigest.getInstance(type);
			alga.update(str.getBytes());
			byte digesta[] = alga.digest();
			String hex = byte2hex(digesta);
			// String hex2 = byte2hex2(digesta);
			// if (!hex.equals(hex2)) {
			// System.out.println("str:" + str);
			// }
			return hex;
		}
		catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return "";
	}

	public static String uuid(String strs[]) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			for (int i = 0; i < strs.length; i++) {
				if (strs[i] != null) {
					md.update(strs[i].getBytes());
				}
			}

			byte bs[] = md.digest();
			return byte2hex(bs);
		}
		catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return null;
	}

	private static String byte2hex(byte b[]) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < b.length; n++) {
			String stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1) {
				//				hs = hs + "0" + stmp;
				sb.append("0");
			}
			sb.append(stmp);
			//			else {
			////				hs = hs + stmp;
			//			}
		}

		return sb.toString().toUpperCase();
	}

	// private static byte[] hex2byte(byte b[]) {
	// if (b.length % 2 != 0) {
	// throw new
	// IllegalArgumentException("\u957F\u5EA6\u4E0D\u662F\u5076\u6570");
	// }
	// byte b2[] = new byte[b.length / 2];
	// for (int n = 0; n < b.length; n += 2) {
	// String item = new String(b, n, 2);
	// b2[n / 2] = (byte) Integer.parseInt(item, 16);
	// }
	//
	// return b2;
	// }
}
