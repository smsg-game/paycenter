package test;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignatureData {
	
	private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public void run() {
		try {
			String prikeyvalue = "30820277020100300d06092a864886f70d0101010500048202613082025d0201000281810093dbe94b47911b429ae9cbb7181651492a29532a74caef2f15385334d168d75d236b4752ccf24d29ed7f3fa5d52dc18d106f8f1d7fcafac546f9de5bb96633dc3e7b92cf527221aa736b97669e8a8389587cfe05aeaaad15f3f2409fea47ec00c1b81f6036c4c71bdba66230f67114a1e29ee29c5bb6d3f9b5d3171ad0b11fc9020301000102818100876bcae85fcbcf23d9ae781e61b94f09abcbdd97ed1050b0d6c697aeb07d4d82c885e0f53d304eec2fd17401f695c9c7e15dcbbe40e52f78afe0fa7f17f73b56fbe0b3f2dba57c273ef615b5d919d71dc06517d2ff9b92ad2a00922374cc426799bdd8c29ed894261f56591ee2ee9d85037e5b1bea3e5ebd54c0733696a05d61024100e505552f22a429e9ed06b29e283a3dc7c90b424fe06c6916642ed30d4eae69733ffcdecd47d1c22853c7534fb2c266f470fb28d8810e58058f43d026419b075f024100a546f39adda494da5fd9aa5a77223e7d871b601932de46f6aba619ff142e094f1e4890b81766cf0c37f1a6c22a0038f903c281ef812707a875513f6b40e371d7024100c5ee15f0e6bf7a79f1a0183f18053ddfaca14e6e3a47778b228a555ceae351bf894dc2412810e0fc796b8b5515e96d915513bbf9619044028a0ed1963e9cfd8102407c2a508ad0e67b198f96c3bf50eefe0969fdebf5225d10ecf1c9489074459da2bdba80567ce634b816334a37663c6290d8fd408680fda4682f32c9b536bb6863024051244e4ffd8086182a894cc0c9653bf48df9d3b9e16d4f73f0f1eb966c130ce0abe50405bd018dbc5b55e6d545c6976a6e8e288b825b62b0750b8c958e80a9e4";//这是GenerateKeyPair输出的私钥编码
			PKCS8EncodedKeySpec priPKCS8=new PKCS8EncodedKeySpec(hexStrToBytes(prikeyvalue)); 
			KeyFactory keyf=KeyFactory.getInstance("RSA");
			PrivateKey myprikey=keyf.generatePrivate(priPKCS8);
	
			String myinfo = "orderId=10dkfadsfksdkssdkd&amount=80&orderTime=20060509"; // 要签名的信息
			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(myinfo.getBytes("ISO-8859-1"));
			byte[] signed = signet.sign(); // 对信息的数字签名
	
			System.out.println("signed(签名内容)原值=" + bytesToHexStr(signed));
			System.out.println("info（原值）=" + myinfo);
	
			System.out.println("签名并生成文件成功");
		} catch (java.lang.Exception e) {
		    e.printStackTrace();
		    System.out.println("签名并生成文件失败");
		}
		;

		}
		/**
		* Transform the specified byte into a Hex String form.
		*/
    public static final String bytesToHexStr(byte[] bcd) {
		StringBuffer s = new StringBuffer(bcd.length * 2);
		for (int i = 0; i < bcd.length; i++) {
		  s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
		  s.append(bcdLookup[bcd[i] & 0x0f]);
		}
		return s.toString();
	}

	/**
	* Transform the specified Hex String into a byte array.
	*/
	public static final byte[] hexStrToBytes(String s) {
		byte[] bytes = new byte[s.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
		  bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),16);
		}
		return bytes;
	}

		
	/**
	* @param args
	*/
	public static void main(String[] args) {
		SignatureData s = new SignatureData();
		s.run();

	}
}
