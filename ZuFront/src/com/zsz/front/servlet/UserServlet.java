package com.zsz.front.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.dto.UserDTO;
import com.zsz.front.Utils.FrontUtils;
import com.zsz.front.Utils.RupengSMSAPI;
import com.zsz.service.SettingService;
import com.zsz.service.UserService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.VerifyCodeUtils;

@WebServlet("/User")
public class UserServlet extends BaseServlet {

	public void reg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/Register.jsp").forward(req, resp);
	}

	// 注册时候发送短信验证码
	public void regSendSmsCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//检查图形验证码的正确性
		//发短信
		String userCode = req.getParameter("userCode");//用户输入的图形验证码
		String serverCode = (String)req.getSession().getAttribute("VerifyCode");//服务器端记载的图形验证码
		if(!userCode.equalsIgnoreCase(serverCode))
		{
			writeJson(resp, new AjaxResult("error", "验证码输入错误！"));
			return;
		}
		String phoneNum = req.getParameter("phoneNum");
		
		UserService userService = new UserService();
		if(userService.getByPhoneNum(phoneNum)!=null)
		{
			writeJson(resp, new AjaxResult("error","手机号已经被注册！"));
			return;
		}
		
		String smsCode = VerifyCodeUtils.generateVerifyCode(4);//生成短信验证码
		req.getSession().setAttribute("smsCode", smsCode);//把短信验证码存入session
		req.getSession().setAttribute("regPhoneNum", phoneNum);//获取验证码时候的手机号
		
		RupengSMSAPI.send(smsCode, phoneNum);
		writeJson(resp, new AjaxResult("ok"));		
	}

	public void regSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String phoneNum= req.getParameter("phoneNum");
		String smsCode = req.getParameter("smsCode");
		String password = req.getParameter("password");
		
		
		
		//获取短信验证码时候的手机号。防范漏洞
		String serverRegPhoneNum =  (String)req.getSession().getAttribute("regPhoneNum");
		if(!serverRegPhoneNum.equals(phoneNum))
		{
			writeJson(resp, new AjaxResult("error","手机号不一致！"));
			return;
		}
		
		UserService userService = new UserService();
		if(userService.getByPhoneNum(phoneNum)!=null)
		{
			writeJson(resp, new AjaxResult("error","手机号已经被注册！"));
			return;
		}
		
		String serverSmsCode = (String)req.getSession().getAttribute("smsCode");
		if(!serverSmsCode.equalsIgnoreCase(smsCode))
		{
			writeJson(resp, new AjaxResult("error","短信验证码输入错误"));
			return;
		}
		
		
		userService.addnew(phoneNum, password);
		writeJson(resp, new AjaxResult("ok"));		
	}

	public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/Login.jsp").forward(req, resp);
	}

	public void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String phoneNum = req.getParameter("phoneNum");
		String password = req.getParameter("password");
		UserService service = new UserService();
		if(service.checkLogin(phoneNum, password))
		{
			Long userId = service.getByPhoneNum(phoneNum).getId();
			FrontUtils.setCurrentUserId(req, userId);			
			writeJson(resp, new AjaxResult("ok"));
		}
		else
		{
			writeJson(resp, new AjaxResult("error","用户名或者密码错误"));
		}
	}
	
	public void center(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Long userId = FrontUtils.getCurrentUserId(req);
		if(userId==null)//如果没登录，则重定向到登录页面
		{
			resp.sendRedirect(req.getContextPath()+"/User?action=login");
			return;
		}
		req.getRequestDispatcher("/WEB-INF/user/Center.jsp").forward(req, resp);
	}

	public void findPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/FindPassword.jsp").forward(req, resp);
	}

	public void findPasswordSubmit1(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String phoneNum = req.getParameter("phoneNum");
		String verifyCode = req.getParameter("verifyCode");
		String serverVerifyCode = (String)req.getSession().getAttribute("VerifyCode");
		if(!verifyCode.equalsIgnoreCase(serverVerifyCode))
		{
			writeJson(resp, new AjaxResult("error","验证码错误"));
			return;
		}
		String smsCode = VerifyCodeUtils.generateVerifyCode(4);//生成短信验证码
		SettingService settingsService = new SettingService();
		String templateId = settingsService.getValue("RuPengSMS.FindPwdTemplateId");//找回密码的短信模板id
		RupengSMSAPI.send(smsCode, phoneNum,templateId);
		
		req.getSession().setAttribute("SmsCode", smsCode);
		req.getSession().setAttribute("FindPwdPhoneNum", phoneNum);//要找回的手机号放到Session中
		
		req.getSession().removeAttribute("findPassword2OK");//清除之前的中间验证标记：避免先用自己的手机号进行密码重置，造成Sesson中保存了findPassword2OK
		//然后再通过漏洞重置别人的密码
		
		writeJson(resp, new AjaxResult("ok"));
	}
	
	public void findPassword2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/FindPassword2.jsp").forward(req, resp);
	}

	public void findPasswordSubmit2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String smsCode = req.getParameter("smsCode");
		String serverSmsCode = (String)req.getSession().getAttribute("SmsCode");
		if(!smsCode.equalsIgnoreCase(serverSmsCode))
		{
			writeJson(resp, new AjaxResult("error","短信验证码错误"));
			return;
		}
		req.getSession().setAttribute("findPassword2OK", "ok");//在Session中标记“第二步检查短信验证码成功”，便于第三步findPasswordSubmit3检查
		writeJson(resp, new AjaxResult("ok"));
	}
	
	public void findPassword3(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/FindPassword3.jsp").forward(req, resp);
	}

	public void findPasswordSubmit3(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String password = req.getParameter("password");
		String phoneNum = (String)req.getSession().getAttribute("FindPwdPhoneNum");//读取在第一步放到Session中的要找回 密码的手机号
		
		String findPassword2OK = (String)req.getSession().getAttribute("findPassword2OK");
		if(!"ok".equalsIgnoreCase(findPassword2OK))//检查用户是否有通过“短信验证码”验证，避免中间跳过步骤，避免漏洞
		{
			writeJson(resp, new AjaxResult("error","不要跳过中间验证！"));
			return;
		}
		
		UserDTO user = new UserService().getByPhoneNum(phoneNum);
		long userId = user.getId();
		UserService userService= new UserService();
		userService.updatePwd(userId, password);//重置密码
		writeJson(resp, new AjaxResult("ok"));
	}
	
	public void findPasswordComplete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/user/FindPasswordComplete.jsp").forward(req, resp);
	}


	//图片验证码
	public void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/jpeg");
		String code = VerifyCodeUtils.generateVerifyCode(4);
		req.getSession().setAttribute("VerifyCode", code);
		VerifyCodeUtils.outputImage(100, 50, resp.getOutputStream(), code);
	}

}
