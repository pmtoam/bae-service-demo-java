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

@SuppressWarnings("serial")
public class MySQLQueryEmployeeInfo extends HttpServlet
{ 
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		String employeeCode = req.getParameter("employeeCode");
		
		if (StringUtils.isEmpty(employeeCode))
		{
			resp.getWriter().print("employeeCode null");
			return;
		}

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
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
          
			
			// 检索数据
          	String sqls = "select userId,channelId from tb_employees where employeeCode=?";
          	ps = connection.prepareStatement(sqls);
          	ps.setString(1,employeeCode);
          	rs = ps.executeQuery();
          	
          	while(rs.next())
          	{         
          		resp.getWriter().print(rs.getString("userId") + " " + rs.getString("channelId"));
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
