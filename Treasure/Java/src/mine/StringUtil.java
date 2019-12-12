package mine;

public class StringUtil {


	    /**
	     * 将字符串首字母转为大写
	     * @param str 要转换的字符串
	     * @return 返回转换后的首字母大写的字符串
	     */
	    public static String toUpper(String str){
	        return str.substring(0, 1).toUpperCase() + str.substring(1);
	    }

	}
	
