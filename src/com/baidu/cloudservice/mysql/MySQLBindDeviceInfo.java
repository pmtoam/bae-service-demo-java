package com.baidu.cloudservice.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.baidu.cloudservice.conf.Config;

@SuppressWarnings("serial")
public class MySQLBindDeviceInfo extends HttpServlet
{ 
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		String employeeCode = req.getParameter("employeeCode");
		String userId = req.getParameter("userId");
		String channelId = req.getParameter("channelId");
		
		if (StringUtils.isEmpty(employeeCode))
		{
			resp.getWriter().print("employeeCode null");
			return;
		}
		
		if (StringUtils.isEmpty(userId))
		{
			resp.getWriter().print("userId null");
			return;
		}
		
		if (StringUtils.isEmpty(channelId))
		{
			resp.getWriter().print("channelId null");
			return;
		}

        Connection connection = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        String sql = null;
        try 
        {
          	/**连接数据库所需要的五要素（可从数据库详情页中查到相应信息）*****/
			String databaseName = Config.MYSQLNAME;
			String host = Config.MYSQLHOST;
			String port = Config.MYSQLPORT;
			String username = Config.USER;
			String password = Config.PWD;
			String driverName = "com.mysql.jdbc.Driver";
			String dbUrl = "jdbc:mysql://";
			String serverName = host + ":" + port + "/";
          	String connName = dbUrl + serverName + databaseName;
          
			/******接着连接并选择数据库名为databaseName的服务器******/
          	Class.forName(driverName);
			connection = DriverManager.getConnection(connName, username, password);
			stmt = connection.createStatement();
          	/*至此连接已完全建立，就可对当前数据库进行相应的操作了*/
          
			//创建一个数据库表
			sql = "create table if not exists tb_employees(" + 
						"id int primary key auto_increment," + 
						"employeeCode varchar(10) NOT NULL, "+
						"userId varchar(50) NOT NULL," + 
						"channelId varchar(50) NOT NULL, UNIQUE (employeeCode))";
			stmt.execute(sql);
			
			//删除数据
			sql = "delete from tb_employees where employeeCode = " + employeeCode;
			stmt.execute(sql);
			
			// 插入数据
          	String sqls = "insert into tb_employees(employeeCode, userId, channelId) values (?, ?, ?)";
          	ps = connection.prepareStatement(sqls);
          	ps.setString(1,employeeCode);
          	ps.setString(2,userId);
          	ps.setString(3,channelId);
          	ps.execute();
          	
          	resp.getWriter().print("ok");
		}
        catch (Exception e) 
		{
			e.printStackTrace(resp.getWriter());
			resp.getWriter().print("error");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
