package com.mininglamp.currencySys.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 通用获取spring bean工具类
 * @author czy
 * 2015年12月22日15:45:38
 */
public class GetBeanUtil implements ApplicationContextAware{
	private GetBeanUtil() {
	}
	private static ApplicationContext applicationContext;
	/**
	 * 获取spring管理的bean对象，可获取所有dao
	 * beanName参考配置文件applicationContext-dao.xml
	 * @param <T>
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getBean(String beanName){
		if(beanName==null){
			return null;
		}
		T bean=(T) applicationContext.getBean(beanName);
		return bean;
	}
	/**
	 * 用于单元测试，获取action、biz、dao,其他场景不适用
	 * @param <T>
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static <T>T getBeanTest(String beanName){
		if(beanName==null){
			return null;
		}
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
	    		"applicationContext.xml","applicationContext-dao.xml","applicationContext-biz.xml","applicationContext-action.xml"});
		T bean=(T) applicationContext.getBean(beanName);
		return bean;
	}
	/**
	 * 读取例子
	 * @return
	 */
//	public static List getAttachs(){
//		JdbcTemplate jdbcTemplate = getBean("jdbcTemplate");
//		final LobHandler lobHandler = getBean("lobHandler");
//		String sql = "select * from RESULTT t where column1='123' ";
//		return jdbcTemplate.query(sql, new RowMapper() {
//			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
//				BufferedReader reader = new BufferedReader(rs.getClob(2)
//						.getCharacterStream());
//				System.out.println(112313123);
//				String line;
//				StringBuffer sb = new StringBuffer();
//				try {
//					while ((line = reader.readLine()) != null) {
//						System.out.println(line);
//						sb.append(line);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				System.out.println("**********************************");
//				System.out.println("**" + sb.toString());
//				return null;
//			}
//		});
//	}
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}
}
