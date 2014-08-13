package com.baidu.cloudservice.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.baidu.cloudservice.conf.Config;

/**
 * 查询-德佑移动手机-应用(Android)-远程版本信息
 * @author PMTOAM
 *
 */
@SuppressWarnings("serial")
public class MySQLQueryRemoteVersionInfo extends HttpServlet
{ 
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		String appName = req.getParameter("appName");
		if (StringUtils.isEmpty(appName))
		{
			resp.getWriter().print("appName is null");
			return;
		}
		
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        Statement stmt = null;
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
          	/*至此连接已完全建立，就可对当前数据库进行相应的操作了*/
          
			stmt = connection.createStatement();
          	/*至此连接已完全建立，就可对当前数据库进行相应的操作了*/
          
			String tbName = "tb_version_ctrl";
			//创建一个数据库表
			sql = "create table if not exists " + tbName + "(" + 
						"id int primary key auto_increment," + 
						"appName varchar(25) NOT NULL," + 
						"appNameCn varchar(25) NOT NULL," + 
						"versionCode varchar(10) NOT NULL," + 
						"versionName varchar(20) NOT NULL," + 
						"apkUrl varchar(200) NOT NULL," + 
						"versionInfo varchar(70) NOT NULL)";
			stmt.execute(sql);
			
			// 检索数据
          	String sqls = "SELECT * FROM " + tbName + " WHERE appName=?";
          	ps = connection.prepareStatement(sqls);
          	ps.setString(1,appName);
          	rs = ps.executeQuery();
          	
          	while(rs.next())
          	{         
          		resp.getWriter().print(rs.getString("appName").trim().replaceAll(" ", "") 
          				+ " " + rs.getString("appNameCn").trim().replaceAll(" ", "")
          				+ " " + rs.getString("versionCode").trim().replaceAll(" ", "")
          				+ " " + rs.getString("versionName").trim().replaceAll(" ", "")
          				+ " " + rs.getString("apkUrl").trim().replaceAll(" ", "")
          				+ " " + rs.getString("versionInfo").trim().replaceAll(" ", ""));
          		break;
          	}
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
