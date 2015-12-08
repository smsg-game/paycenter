package test;

import java.util.UUID;

public class UUIDGenertor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String key = UUID.randomUUID().toString().toUpperCase();
		System.out.println(key);
	}

}
