package com.mininglamp.currencySys.common.connpool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBPool {  
    private static DBPool dbPool;  
    private ComboPooledDataSource dataSource;  
  
    static {  
        dbPool = new DBPool();  
    }  
  
    public DBPool() {  
        try {  
            dataSource = new ComboPooledDataSource();  
            dataSource.setUser("root");  
            dataSource.setPassword("1234");  
            dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/game?user=root&password=1234&useUnicode=true");  
            dataSource.setDriverClass("com.mysql.jdbc.Driver");  
            // 设置初始连接池的大小！  
            dataSource.setInitialPoolSize(2);  
            // 设置连接池的最小值！   
            dataSource.setMinPoolSize(1);  
            // 设置连接池的最大值！   
            dataSource.setMaxPoolSize(10);  
            // 设置连接池中的最大Statements数量！  
            dataSource.setMaxStatements(50);  
            // 设置连接池的最大空闲时间！  
            dataSource.setMaxIdleTime(60);  
        } catch ( Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    public final static DBPool getInstance() {  
        return dbPool;  
    }  
  
    public final Connection getConnection() {  
        try {  
            return dataSource.getConnection();  
        } catch (SQLException e) {  
            throw new RuntimeException("无法从数据源获取连接 ", e);  
        }  
    }  
  
    public static void main(String[] args) throws SQLException {  
        Connection con = null;  
        try {  
            con = DBPool.getInstance().getConnection();  
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM LESOGO_USER");  
            while (rs.next()) {  
                System.out.println(rs.getObject(1));  
                System.out.println(rs.getObject(2));  
                System.out.println(rs.getObject(3));  
            }  
        } catch (Exception e) {
        	e.printStackTrace();
			throw new RuntimeException(e);
        } finally {  
            if (con != null)  
                con.close();  
        }  
    }  
}  