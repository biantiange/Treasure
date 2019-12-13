package mine;


	import java.io.InputStream;
	import java.lang.reflect.Field;
	import java.lang.reflect.Method;
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

import com.sun.org.apache.regexp.internal.recompile;
	public class MineDBUtil {

		private static Properties dbProps = new Properties();

		/**
		 * ��ȡ�����ļ����������ݿ�����
		 */
		static {
			try {
				InputStream is = MineDBUtil.class.getResourceAsStream("/dbinfo.properties");
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

		/**
		 * ִ����ɾ��
		 * 
		 * @param sql
		 * @param params
		 * @return
		 */
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

		/**
		 * ����������ѯ��������
		 * 
		 * @param cls
		 * @param id
		 * @return
		 */
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

		/**
		 * ��ѯĳ��ȫ������
		 * 
		 * @param sql
		 * @return
		 */
		
		
		public static List<Map<String, Object>> findAll(String sql,Object[] params) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = getCon();
				pstm = con.prepareStatement(sql);
				pstm.setObject(1, params[0]);
				rs = pstm.executeQuery();
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					Map<String, Object> map = new LinkedHashMap<String, Object>(metaData.getColumnCount());
					for (int i = 0; i < metaData.getColumnCount(); i++) {
						//if(rs.getObject(i + 1)!=null){
						map.put(metaData.getColumnLabel(i + 1), rs.getObject(i + 1));
						//}else{
						//	map.put(metaData.getColumnLabel(i + 1), null);
						//}
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

		/**
		 * ����������ѯ������List���ϣ������д洢���Ӧ�Ķ���
		 * @param cls
		 * @param sql
		 * @param params
		 * @return
		 */
		public static List find(Class cls, String sql, Object[] params) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = getCon();
				pstm = con.prepareStatement(sql);
				if (params != null && params.length > 0) {
					for (int i = 0; i < params.length; i++)
						pstm.setObject(i + 1, params[i]);
				}
				rs = pstm.executeQuery();
				List list = new ArrayList();
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					Object obj = cls.newInstance();
					for (int i = 0; i < metaData.getColumnCount(); i++) {
						String columnLabel = metaData.getColumnLabel(i+1);//获取字段名
	                    // 动态拼接成该属性对应实体中的setter方法的方法名（=set字符串拼接首字母大写
	                    // 的属性名）。如：setName(Stringname)的方法名为setName
	                    String name = "set" + StringUtil.toUpper(columnLabel);
	                    // 获取实体中所有声明（私有+公有）的属性
	                    Field field = cls.getDeclaredField(columnLabel);
	                    // 获取实体中所有声明（私有+公有）的形参为field.getType()类型，方法名为
	                    // name的方法
	                    Method method = cls.getDeclaredMethod(name, field.getType());
	                    // 通过结果集获取字段名为fieldName（与实体中的对应属性名完全相同）的值
	                    Object realParam = rs.getObject(columnLabel);
	                    // 执行obj对象中的method方法，传入的实参为realParam
	                    method.invoke(obj, realParam);
						/*Field field = cls.getDeclaredField(metaData.getColumnLabel(i + 1));
						field.setAccessible(true);
						field.set(obj, rs.getObject(i + 1));*/
					}
					list.add(obj);
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				close(rs, pstm, con);
			}
		}

	}


