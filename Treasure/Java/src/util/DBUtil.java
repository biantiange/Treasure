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

	public static int executeUpdate(String sql, Object[] params) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = getCon();
			pstm = con.prepareStatement(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++)
					pstm.setObject(i + 1, params[i]);
			}
			return pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			close(null, pstm, con);
		}
	}
	
	public static List<Map<String, Object>> findAll(String sql) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = getCon();
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>(metaData.getColumnCount());
				for (int i = 0; i < metaData.getColumnCount(); i++) {
					map.put(metaData.getColumnLabel(i + 1), rs.getObject(i + 1));
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(rs, pstm, con);
		}
	}
	
	public static Object findById(Class cls, Object id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = getCon();
			pstm = con.prepareStatement("select * from " + cls.getSimpleName() + " where id=?");
			pstm.setObject(1, id);
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			Object obj = cls.newInstance();
			if (rs.next()) {
				for (int i = 0; i < metaData.getColumnCount(); i++) {
					Field field = cls.getDeclaredField(metaData.getColumnLabel(i + 1));
					field.setAccessible(true);
					field.set(obj, rs.getObject(i + 1));
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(rs, pstm, con);
		}
	}
	
	public static Object findByIdForParent(Class cls, Object id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = getCon();
			pstm = con.prepareStatement("select * from " + "tbl_parent" + " where id=?");
			pstm.setObject(1, id);
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			Object obj = cls.newInstance();
			if (rs.next()) {
				for (int i = 0; i < metaData.getColumnCount(); i++) {
					Field field = cls.getDeclaredField(metaData.getColumnLabel(i + 1));
					field.setAccessible(true);
					field.set(obj, rs.getObject(i + 1));
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(rs, pstm, con);
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
