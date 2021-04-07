package com.gridone.scraping.utils;

public class Utils {

	public static int toInt(Object obj) {
		try {
			if (obj == null) {
				return 0;
			} else if (obj instanceof String) {
				return Integer.parseInt((String) obj);
			} else if (obj instanceof Integer) {
				return ((Integer) obj).intValue();
			} else {
				String toString = obj.toString();
				if (toString.matches("-?\\d+")) {
					return Integer.parseInt(toString);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
