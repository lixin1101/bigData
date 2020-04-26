package lx.udf2;

import java.io.UnsupportedEncodingException;

/**
 * url转码、解码
 *
 */
public class UrlUtil {

	private final static String ENCODE = "UTF-8";

	/**
	 * URL 解码
	 */
	public static String getURLDecoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * URL 转码
	 *
	 */
	/*
	 * public static String getURLEncoderString(String str) { String result =
	 * ""; if (null == str) { return ""; } try { result =
	 * java.net.URLEncoder.encode(str, ENCODE); } catch
	 * (UnsupportedEncodingException e) { e.printStackTrace(); } return result;
	 * }
	 */

	public static void main(String[] args) {
		String str = "%E5%8F%B6%E4%B8%80%E8%8D%A3%EF%BC%88iPhone%E8%B4%B5%EF%BC%89";
		System.out.println(getURLDecoderString(str));
	}
}
