package com.mininglamp.currencySys.util;

import java.beans.PropertyVetoException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
//import com.mininglamp.mws.hbpost.controller.CustomerInfoController;
//import com.mininglamp.mws.hbpost.service.CustomerInfoService;

public class Test {
	static PreparedStatement ps = null;
    static ResultSet rs = null;
    public static void main(String[] args) {
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
//    	CustomerInfoController c = applicationContext.getBean("customerInfo",CustomerInfoController.class);
//    	System.out.println(c);
	}
	public static void main1(String[] args) throws PropertyVetoException, SQLException {
		@SuppressWarnings("unused")
		long start = System.currentTimeMillis();
		
		DruidDataSource dataSourceImpala = new DruidDataSource();  
//		SingleConnectionDataSource dataSourceImpala = new SingleConnectionDataSource();  
		dataSourceImpala.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
		dataSourceImpala.setUrl("jdbc:hive2://21.32.3.138:10000/staff");
		dataSourceImpala.setUsername("hive");
		dataSourceImpala.setPassword("mininglamp@bd");
//		ComboPooledDataSource dataSourceImpala = new ComboPooledDataSource();  
//		dataSourceImpala.setDriverClass("org.apache.hive.jdbc.HiveDriver");
//        dataSourceImpala.setUser("hive");  
//        dataSourceImpala.setPassword("mininglamp@bd");  
//        dataSourceImpala.setJdbcUrl("jdbc:hive2://21.32.3.138:10000/staff");  
//        dataSourceImpala.setDriverClass("org.apache.hive.jdbc.HiveDriver");  
//        // 设置初始连接池的大小！  
//        dataSourceImpala.setInitialPoolSize(10);  
//        // 设置连接池的最小值！   
//        dataSourceImpala.setMinPoolSize(10);  
//        // 设置连接池的最大值！   
//        dataSourceImpala.setMaxPoolSize(80);  
//        // 设置连接池中的最大Statements数量！  
//        dataSourceImpala.setMaxStatements(50);  
//        // 设置连接池的最大空闲时间！  
//        dataSourceImpala.setMaxIdleTime(60);
//        //设置异步进程数
//        dataSourceImpala.setNumHelperThreads(10);
//        dataSourceImpala.setTestConnectionOnCheckin(true);
//        dataSourceImpala.setUnreturnedConnectionTimeout(600);
//        dataSourceImpala.setDebugUnreturnedConnectionStackTraces(true);
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceImpala);
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from cpv.tb_ml_cpv_baseinfo_par where certificate_code = '131126196706153620' and hashval=178");
        System.out.println(list);
//        Connection conn = dataSourceImpala.getConnection();
//        
//        
//        
//        ps = conn.prepareStatement("select * from cpv.tb_ml_cpv_baseinfo_par where certificate_code = '131126196706153620' and hashval=178"); 
//       rs = ps.executeQuery();
//       rs.next();
//       System.out.println(rs.getLong(1));
//       long end = System.currentTimeMillis();
//       System.out.println( end - start );
//   
//       long start2 = System.currentTimeMillis();
//       
//       
//       ps = conn.prepareStatement("select * from cpv.tb_ml_cpv_baseinfo_par where certificate_code = '133029196911274822' and hashval = 98"); 
//      rs = ps.executeQuery();
//      rs.next();
//      System.out.println(rs.getLong(1));
//      long end2 = System.currentTimeMillis();
//      System.out.println( end2 - start2 );
        
	}
}
