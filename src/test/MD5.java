/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test;

import java.security.MessageDigest;

/** A Writable for MD5 hash values.
 */
public class MD5 {
  public static final int MD5_LEN = 16;
  
  public static void main(String args[]){
	 System.out.println(digest("ccc"));
  }
  
  
  public static String digest(String src){
	  try {
		     byte[] btInput = src.getBytes("UTF-8");
		     MessageDigest mdInst = MessageDigest.getInstance("MD5");
		     mdInst.update(btInput);
		     byte[] md = mdInst.digest();
		     StringBuffer sb = new StringBuffer();
		     for (int i = 0; i < md.length; i++) {
		       int val = ((int) md[i]) & 0xff;
		       if (val < 16) sb.append("0");
		       sb.append(Integer.toHexString(val));
		     }
		     return sb.toString();
		}catch (Exception e) {
		     e.printStackTrace();
	    }
		return null;
  }
  
  
}
