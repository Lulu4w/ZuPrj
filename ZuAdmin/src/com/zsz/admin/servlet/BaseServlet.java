package com.zsz.admin.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsz.admin.utils.AdminUtils;
import com.zsz.service.AdminUserService;
import com.zsz.tools.AjaxResult;

public class BaseServlet extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(BaseServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		
		String action = req.getParameter("action");
		if (StringUtils.isEmpty(action)) {
			AdminUtils.showError(req, resp, "action is empty!");
			//resp.getWriter().print("action is empty!");
			logger.warn("action为空");
			return;
		}
		Class clz = this.getClass();// Class是子类的Class，不是BaseServlet
		// 约定方法的名字就是 action的名字(req,resp)
		try {
			Method methodAction = clz.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
			
			//AOP思想：
			
			//获得方法上标注的“是否允许匿名访问”AllowAnonymous		
			AllowAnonymous allowAnonymous = methodAction.getAnnotation(AllowAnonymous.class);
			if(allowAnonymous==null)//如果为null，就说明方法没有标注AllowAnonymous，则需要检查登录状态
			{
				//统一检查用户是否有登录，如果没有登录，则直接不执行methodAction.invoke
				Long userId = AdminUtils.getAdminUserId(req);
				if(userId==null)
				{
					String ctxPath = req.getContextPath();
					// target='_top'避免链接在iframe中打开，而是再顶层浏览器窗口中打开
					AdminUtils.showError(req, resp, "未登录<a target='_top' href='"+ctxPath+"/Index?action=login'>点此登录</a>");
					return;//!!!!!
				}
				//已经登录
				
				//统一的权限控制，需要进行权限控制的方法标注HasPermission即可
				//获得方法上标注的HasPermission
				HasPermission hasPermission = methodAction.getAnnotation(HasPermission.class);
				if(hasPermission!=null)//如果方法上标注了HasPermission，则要检查当前用户是否有相应权限
				{
					AdminUserService adminUserService = new AdminUserService();
					boolean isOK = adminUserService.hasPermission(userId, hasPermission.value());
					if(!isOK)
					{
						AdminUtils.showError(req, resp, "无权访问！");
						return;//!!!!!
					}
				}
			}	
			
			
			// 拿到了 public void index(HttpServletRequest req, HttpServletResponse
			// resp) 方法
			methodAction.invoke(this, req, resp); // 调用方法

		} catch (NoSuchMethodException | SecurityException e) {
			//resp.getWriter().print("cannot invoke action method:" + action);
			AdminUtils.showError(req, resp, "cannot invoke action method:" + action);
			logger.warn("找不到名字为"+action+"的方法",e);
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			//resp.getWriter().print("invoke method " + action + " error");
			AdminUtils.showError(req, resp, "invoke method " + action + " error");
			logger.warn("调用名字为"+action+"的方法失败",e);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	protected void writeJson(HttpServletResponse resp,AjaxResult ajaxResult) throws IOException
	{
		resp.setContentType("application/json");
		resp.getWriter().print(ajaxResult.toJson());
	}

}
