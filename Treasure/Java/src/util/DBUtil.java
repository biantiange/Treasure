package util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DBUtil {

	private static Properties dbProps = new Properties();

	/**
	 * ��ȡ�����ļ����������ݿ�����
	 */
	static {
		try {
			InputStream is = DBUtil.class.getResourceAsStream("/dbinfo.properties");
			dbProps.load(is);
			Class.forName(dbProps.getProperty("db.driver"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ���ݿ�����
	 * 
	 * @return
	 */
	public static Connection getCon() {
		try {
			return DriverManager.getConnection(dbProps.getProperty("db.connectUrl"), dbProps.getProperty("db.user"),
					dbProps.getProperty("db.pwd"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �ر����ݿ����ӵȶ���
	 * 
	 * @param rs
	 * @param pstm
	 * @param con
	 */
	public static void close(ResultSet rs, PreparedStatement pstm, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (pstm != null)
				pstm.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
