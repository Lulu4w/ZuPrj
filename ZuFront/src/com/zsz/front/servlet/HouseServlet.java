package com.zsz.front.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.startup.HomesUserDatabase;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.zsz.dto.AttachmentDTO;
import com.zsz.dto.CityDTO;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HousePicDTO;
import com.zsz.dto.HouseSearchOptions;
import com.zsz.dto.HouseSearchOptions.OrderByType;
import com.zsz.dto.HouseSearchResult;
import com.zsz.dto.RegionDTO;
import com.zsz.front.Utils.CacheManager;
import com.zsz.front.Utils.FrontUtils;
import com.zsz.service.AttachmentService;
import com.zsz.service.CityService;
import com.zsz.service.HouseAppointmentService;
import com.zsz.service.HouseService;
import com.zsz.service.IdNameService;
import com.zsz.service.RegionService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;
import com.zsz.tools.Functions;

import redis.clients.jedis.Jedis;

@WebServlet("/House")
public class HouseServlet extends BaseServlet {

	public void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		
		HouseService service = new HouseService();
		
		//先尝试从缓存中获取数据，如果没有取到，则取数据库中取，并且把取到的结果放到缓存中；如果从缓存中取到了，则直接用缓存中数据
		//因此缓存可以提高系统的运行效率(数据库的访问速度比redis访问速度慢)，降低数据库的压力
		//缓存就是用访问速度快的设备来缓存访问速度慢的设备
		
		
		// Jedis jedis = FrontUtils.createJedis();//创建Redis连接
		// String houseJson = jedis.get("House"+id);//House5
		// 从redis中获取指定key（House+id）的值，看看有没有缓存
		// //key一定要注意不能和别人重复
		// if(StringUtils.isEmpty(houseJson))//如果没有取到数据，就说明没有缓存，则只能去数据库中取
		// {
		// service = new HouseService();
		// HouseDTO house = service.getById(id);
		// req.setAttribute("house", house);
		// houseJson = CommonUtils.createGson().toJson(house);
		// jedis.setex("House"+id,30,houseJson);//把对象json序列化成字符串（因为redis只支持字符串）保存，缓存时间30秒
		// }
		// else//如果从redis中取到了，则直接反序列化为HouseDTO，不用再去查询数据库了！
		// {
		// HouseDTO house = CommonUtils.createGson().fromJson(houseJson,
		// HouseDTO.class);
		// req.setAttribute("house", house);
		// }
		
		String cacheKey = "House"+id;
		CacheManager cacheMgr = CacheManager.getManager();
		HouseDTO house = (HouseDTO)cacheMgr.getValue(cacheKey, HouseDTO.class);//首先到缓存中找
		if(house==null)//如果没找到，则取数据库查询
		{
			house = service.getById(id);
			cacheMgr.setValue(cacheKey, house, 30);//把查询结果放入缓存
		}
		req.setAttribute("house", house);
				

		HousePicDTO[] pics = service.getPics(id);
		req.setAttribute("pics", pics);

		AttachmentService attService = new AttachmentService();
		AttachmentDTO[] attachs = attService.getAttachments(id);
		req.setAttribute("attachs", attachs);

		req.getRequestDispatcher("/WEB-INF/house/View.jsp").forward(req, resp);
	}
	
	public void view1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		
		
		HouseService service = new HouseService();
		HouseDTO house = service.getById(id);
		req.setAttribute("house", house);

		HousePicDTO[] pics = service.getPics(id);
		req.setAttribute("pics", pics);

		AttachmentService attService = new AttachmentService();
		AttachmentDTO[] attachs = attService.getAttachments(id);
		req.setAttribute("attachs", attachs);

		req.getRequestDispatcher("/WEB-INF/house/View.jsp").forward(req, resp);
	}

	// solr实现搜索
	public void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long cityId = FrontUtils.getCurrentCityId(req);

		String strRegionId = req.getParameter("regionId");
		String strMonthRent = req.getParameter("monthRent");
		String strOrderBy = req.getParameter("orderBy");
		String strTypeId = req.getParameter("typeId");
		String keywords = req.getParameter("keywords");

		StringBuilder query = new StringBuilder();
		query.append("cityId:").append(cityId).append(" ");

		Integer startMonthRent = null;// 起始租金
		Integer endMonthRent = null;// 结束租金

		if (!StringUtils.isEmpty(strMonthRent)) {
			// 500-600
			// monthRent:[500 TO 600]
			query.append(" AND monthRent:[").append(strMonthRent.replace("-", " TO ")).append("]");
			String[] monthRents = strMonthRent.split("-");// 100-200,200-*,*-200
			if (!monthRents[0].equals("*"))// 如果为*就是不 设限
			{
				startMonthRent = Integer.parseInt(monthRents[0]);
			}

			if (!monthRents[1].equals("*"))// 如果为*就是不 设限
			{
				endMonthRent = Integer.parseInt(monthRents[1]);
			}
		}

		Long regionId = null;
		if (!StringUtils.isEmpty(strRegionId)) {
			regionId = Long.parseLong(strRegionId);// 防止注入漏洞
			query.append(" AND regionId:").append(regionId);
		}

		Long typeId = null;
		if (!StringUtils.isEmpty(strTypeId)) {
			typeId = Long.parseLong(strTypeId);
			query.append(" AND typeId:").append(typeId);
		}

		StringBuilder sbSearchDisplay = new StringBuilder();// 显示到界面上的搜索条件
		sbSearchDisplay.append(new CityService().getById(cityId).getName()).append(",");
		if (regionId != null) {
			sbSearchDisplay.append(new RegionService().getById(regionId).getName()).append(",");
		}
		if (startMonthRent != null) {
			sbSearchDisplay.append("房租高于").append(startMonthRent).append(",");
		}
		if (endMonthRent != null) {
			sbSearchDisplay.append("房租低于").append(endMonthRent).append(",");
		}
		if (typeId != null) {
			sbSearchDisplay.append(new IdNameService().getById(typeId).getName()).append(",");
		}
		if (!StringUtils.isEmpty(keywords)) {
			sbSearchDisplay.append(keywords).append(",");

			query.append(" AND (communityLocation:").append(keywords).append(" OR communityName:").append(keywords)
					.append(" OR communityTraffic:").append(keywords).append(" OR ").append("decorateStatusName:")
					.append(keywords).append(" OR description:").append(keywords).append(" OR regionName:")
					.append(keywords).append(")");
		}
		req.setAttribute("searchDisplay", sbSearchDisplay.toString());
		
		Long pageIndex = 1L;//页码
		String strPageIndex = req.getParameter("pageIndex");
		if (!StringUtils.isEmpty(strPageIndex)) {
			pageIndex = Long.parseLong(strPageIndex);
		}

		HttpSolrClient.Builder builder = new HttpSolrClient.Builder("http://127.0.0.1:8983/solr/houses");
		HttpSolrClient solrClient = builder.build();

		SolrQuery solrQuery = new SolrQuery(query.toString());
		// query.setSort("age", ORDER.desc);
		if(!StringUtils.isEmpty(strOrderBy))
		{
			solrQuery.setSort(strOrderBy,ORDER.asc);//排序
		}
		
		//分页处理
		int pageSize=3;
		solrQuery.setStart((int)(pageIndex-1)*pageSize);//limit start,rows
		solrQuery.setRows(pageSize);
		QueryResponse queryResp;
		try {
			queryResp = solrClient.query(solrQuery);
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		}
		SolrDocumentList docList = queryResp.getResults();
		List<HouseDTO> houses = new ArrayList<>();

		HouseService houseService = new HouseService();
		for (SolrDocument doc : docList) {
			/*
			 * HouseDTO house = new HouseDTO();
			 * house.setId((Long)doc.get("id"));
			 * house.setAddress((String)doc.get("address")); house.set
			 */
			long houseId = Long.parseLong((String)doc.get("id"));
			HouseDTO house = houseService.getById(houseId);
			houses.add(house);
		}
		solrClient.close();

		req.setAttribute("houses", houses.toArray());

		RegionService regionService = new RegionService();
		RegionDTO[] regions = regionService.getAll(cityId);
		req.setAttribute("regions", regions);

		req.setAttribute("queryString", req.getQueryString());
		req.setAttribute("totalCount", docList.getNumFound());// docList.getNumFound() 匹配的总条数
		req.setAttribute("pageSize", pageSize);

		
		req.setAttribute("pageIndex", pageIndex);

		// 为z:pager准备urlFormat
		String pagerUrlFormat = Functions.addQueryStringPart(req.getQueryString(), "pageIndex", "{pageNum}");
		req.setAttribute("pagerUrlFormat", req.getContextPath() + "/House?" + pagerUrlFormat);

		req.getRequestDispatcher("/WEB-INF/house/Search.jsp").forward(req, resp);
	}

	// 数据库实现的搜索
	public void search1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long cityId = FrontUtils.getCurrentCityId(req);

		String strRegionId = req.getParameter("regionId");
		String strMonthRent = req.getParameter("monthRent");
		String strOrderBy = req.getParameter("orderBy");
		String strTypeId = req.getParameter("typeId");
		String keywords = req.getParameter("keywords");

		Integer startMonthRent = null;// 起始租金
		Integer endMonthRent = null;// 结束租金

		if (!StringUtils.isEmpty(strMonthRent)) {
			String[] monthRents = strMonthRent.split("-");// 100-200,200-*,*-200
			if (!monthRents[0].equals("*"))// 如果为*就是不 设限
			{
				startMonthRent = Integer.parseInt(monthRents[0]);
			}

			if (!monthRents[1].equals("*"))// 如果为*就是不 设限
			{
				endMonthRent = Integer.parseInt(monthRents[1]);
			}
		}
		Long regionId = null;
		if (!StringUtils.isEmpty(strRegionId)) {
			regionId = Long.parseLong(strRegionId);
		}

		Long typeId = null;
		if (!StringUtils.isEmpty(strTypeId)) {
			typeId = Long.parseLong(strTypeId);
		}

		StringBuilder sbSearchDisplay = new StringBuilder();// 显示到界面上的搜索条件
		sbSearchDisplay.append(new CityService().getById(cityId).getName()).append(",");
		if (regionId != null) {
			sbSearchDisplay.append(new RegionService().getById(regionId).getName()).append(",");
		}
		if (startMonthRent != null) {
			sbSearchDisplay.append("房租高于").append(startMonthRent).append(",");
		}
		if (endMonthRent != null) {
			sbSearchDisplay.append("房租低于").append(endMonthRent).append(",");
		}
		if (typeId != null) {
			sbSearchDisplay.append(new IdNameService().getById(typeId).getName()).append(",");
		}
		if (!StringUtils.isEmpty(keywords)) {
			sbSearchDisplay.append(keywords).append(",");
		}
		req.setAttribute("searchDisplay", sbSearchDisplay.toString());

		Long pageIndex = 1L;
		String strPageIndex = req.getParameter("pageIndex");
		if (!StringUtils.isEmpty(strPageIndex)) {
			pageIndex = Long.parseLong(strPageIndex);
		}
		
		HouseSearchOptions searchOpts = new HouseSearchOptions();
		searchOpts.setCityId(cityId);
		searchOpts.setCurrentIndex(pageIndex);
		searchOpts.setEndMonthRent(endMonthRent);
		searchOpts.setKeywords(keywords);
		searchOpts.setOrderByType("monthRent".equals(strOrderBy) ? OrderByType.MonthRent : OrderByType.Area);
		searchOpts.setPageSize(10);
		searchOpts.setRegionId(regionId);
		searchOpts.setStartMonthRent(startMonthRent);
		searchOpts.setTypeId(typeId);

		HouseService houseService = new HouseService();
		HouseSearchResult searchResult = houseService.search2(searchOpts);
		req.setAttribute("houses", searchResult.getResult());

		RegionService regionService = new RegionService();
		RegionDTO[] regions = regionService.getAll(cityId);
		req.setAttribute("regions", regions);

		req.setAttribute("queryString", req.getQueryString());
		req.setAttribute("totalCount", searchResult.getTotalCount());

		
		req.setAttribute("pageIndex", pageIndex);

		// 为z:pager准备urlFormat
		String pagerUrlFormat = Functions.addQueryStringPart(req.getQueryString(), "pageIndex", "{pageNum}");
		req.setAttribute("pagerUrlFormat", req.getContextPath() + "/House?" + pagerUrlFormat);

		req.getRequestDispatcher("/WEB-INF/house/Search.jsp").forward(req, resp);
	}

	// 预约
	public void makeAppointment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		/*
		Jedis jedis = FrontUtils.createJedis();
		jedis.incr("zufangshulaing");//用计数器实现有多少人租房
		jedis.close();*/
		
		String name = req.getParameter("name");
		String phoneNum = req.getParameter("phoneNum");
		String visitDate = req.getParameter("visitDate");
		long houseId = Long.parseLong(req.getParameter("houseId"));
		Long userId = FrontUtils.getCurrentUserId(req);

		HouseAppointmentService service = new HouseAppointmentService();
		service.addnew(userId, name, phoneNum, houseId, CommonUtils.parseDate(visitDate));
		writeJson(resp, new AjaxResult("ok"));
	}
}
