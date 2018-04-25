package com.zsz.front.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.dto.CityDTO;
import com.zsz.dto.UserDTO;
import com.zsz.front.Utils.CacheManager;
import com.zsz.front.Utils.FrontUtils;
import com.zsz.service.CityService;
import com.zsz.service.HouseAppointmentService;
import com.zsz.service.UserService;
import com.zsz.tools.AjaxResult;

import redis.clients.jedis.Jedis;

@WebServlet("/Index")
public class IndexServlet extends BaseServlet {
	public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//String path = req.getServletContext().getRealPath("/");
		CacheManager cacheMgr = CacheManager.getManager();
		CityDTO[] cities = null;
		cities = (CityDTO[])cacheMgr.getValue("Cities", CityDTO[].class);
		if(cities==null)
		{
			CityService cityService = new CityService();
			cities = cityService.getAll();
			cacheMgr.setValue("Cities", cities, 60*60);
		}
				
		req.setAttribute("cities", cities);
		
		Long houseAppTotalCount=null;
		houseAppTotalCount = (Long)cacheMgr.getValue("houseAppTotalCount", Long.class);
		if(houseAppTotalCount==null)
		{
			HouseAppointmentService houseAppService = new HouseAppointmentService();
			houseAppTotalCount = houseAppService.getTotalCount();//下的订单数量，作为“有**人成功租房”的数据
			cacheMgr.setValue("houseAppTotalCount", houseAppTotalCount, 60);
		}	
		
		/*
		Jedis jedis = FrontUtils.createJedis();
		houseAppTotalCount = Long.parseLong(jedis.get("zufangshulaing"));
		jedis.close();*/
		
		
		req.setAttribute("houseAppTotalCount", houseAppTotalCount+8848);
		
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
	}
	
	//获取当前用户的城市信息
	public void queryCurrentCity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long cityId= FrontUtils.getCurrentCityId(req);
		String cityName = new CityService().getById(cityId).getName();
		writeJson(resp, new AjaxResult("ok","",cityName));
	}
	
	//切换当前城市
	public void changeCity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long cityId = Long.parseLong(req.getParameter("cityId"));
		FrontUtils.setCurrentCityId(req, cityId);
		writeJson(resp, new AjaxResult("ok"));
	}
}
