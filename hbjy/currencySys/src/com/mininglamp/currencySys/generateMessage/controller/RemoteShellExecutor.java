package com.mininglamp.currencySys.generateMessage.controller;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 远程连接liunx系统
 * @author Administrator
 *
 */
public class RemoteShellExecutor {
     
     private static Connection conn;
     /** 远程机器IP */
     private static String ip;
     /** 用户名 */
     private static String osUsername;
     /** 密码 */
     private static String password;
     private static String charset = Charset.defaultCharset().toString();

     private static final int TIME_OUT = 1000 * 5 * 60;

     /**
      * 构造函数
      * @param ip
      * @param usr
      * @param pasword
      */
     public RemoteShellExecutor(String ip, String usr, String pasword) {
          this.ip = ip;
         this.osUsername = usr;
         this.password = pasword;
     }


     /**
     * 登录
     * @return
     * @throws IOException
     */
     private static boolean login() throws IOException {
         conn = new Connection(ip);
         conn.connect();
         return conn.authenticateWithPassword(osUsername, password);
     }

     /**
     * 执行脚本
     * 
     * @param cmds
     * @return
     * @throws Exception
     */
     public int exec(String cmds) throws Exception {
         InputStream stdOut = null;
         InputStream stdErr = null;
         String outStr = "";
         String outErr = "";
         int ret = -1;
         try {
         if (login()) {
             // Open a new {@link Session} on this connection
        	 //在这个连接上打开一个新的连接会话
             Session session = conn.openSession();
             // Execute a command on the remote machine.在远程计算机上执行命令
             session.execCommand(cmds);
             stdOut = new StreamGobbler(session.getStdout());
             outStr = processStream(stdOut, charset);
             
             stdErr = new StreamGobbler(session.getStderr());
             outErr = processStream(stdErr, charset);
             
             session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
             
             System.out.println("outStr=" + outStr);
             System.out.println("outErr=" + outErr);
             
             ret = session.getExitStatus();
         } else {
             throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
         }
         } finally {
             if (conn != null) {
                 conn.close();
             }
             IOUtils.closeQuietly(stdOut);
             IOUtils.closeQuietly(stdErr);
         }
         return ret;
     }

     /**
      * 执行脚本
      * 
      * @param cmds
      * @return
      * @throws Exception
      */
      public String exec1(String cmds) throws Exception {
          InputStream stdOut = null;
          InputStream stdErr = null;
          String outStr = "";
          String outErr = "";
          int ret = -1;
          try {
          if (login()) {
              // Open a new {@link Session} on this connection
         	 //在这个连接上打开一个新的连接会话
              Session session = conn.openSession();
              // Execute a command on the remote machine.在远程计算机上执行命令
              session.execCommand(cmds);
              stdOut = new StreamGobbler(session.getStdout());
              outStr = processStream(stdOut, charset);
              
              stdErr = new StreamGobbler(session.getStderr());
              outErr = processStream(stdErr, charset);
              
              session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
              
              System.out.println("outStr=" + outStr);
              System.out.println("outErr=" + outErr);
              
              ret = session.getExitStatus();
          } else {
              throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
          }
          } finally {
              if (conn != null) {
                  conn.close();
              }
              IOUtils.closeQuietly(stdOut);
              IOUtils.closeQuietly(stdErr);
          }
          return outStr;
      }
      
     /**
     * @param in
     * @param charset
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
     private String processStream(InputStream in, String charset) throws Exception {
         byte[] buf = new byte[1024];
         StringBuilder sb = new StringBuilder();
         while (in.read(buf) != -1) {
             sb.append(new String(buf, charset));
         }
         return sb.toString();
     }

     
    public static void main(String args[]) throws Exception {
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
    	System.out.println(isLastDayOfMonth(fmt1.parse("2018-03-31")));
        
    }
    
    public static boolean isLastDayOfMonth(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        return calendar.get(Calendar.DAY_OF_MONTH) == calendar  
                .getActualMaximum(Calendar.DAY_OF_MONTH);  
    } 
}
