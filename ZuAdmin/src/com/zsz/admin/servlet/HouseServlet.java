package com.zsz.admin.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.transform.SourceLocator;

import org.apache.catalina.startup.HomesUserDatabase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zsz.admin.utils.AdminUtils;
import com.zsz.dto.AttachmentDTO;
import com.zsz.dto.CommunityDTO;
import com.zsz.dto.HouseAppointmentDTO;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HousePicDTO;
import com.zsz.dto.IdNameDTO;
import com.zsz.dto.RegionDTO;
import com.zsz.service.AttachmentService;
import com.zsz.service.CommunityService;
import com.zsz.service.HouseAppointmentService;
import com.zsz.service.HouseService;
import com.zsz.service.IdNameService;
import com.zsz.service.RegionService;
import com.zsz.service.SettingService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@WebServlet("/House")
@MultipartConfig
public class HouseServlet extends BaseServlet {

	@HasPermission("House.Query")
	public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long adminUserCityId = AdminUtils.getAdminUserCityId(req);// 获取用户所在的城市
		if (adminUserCityId == null) {
			AdminUtils.showError(req, resp, "总部的人不能管理房源");
			return;
		}
		long typeId = Long.parseLong(req.getParameter("typeId"));
		long pageIndex = Long.parseLong(req.getParameter("pageIndex"));
		req.setAttribute("typeId", typeId);// 给forward中的jsp页面中的pager等地方用的
		req.setAttribute("pageIndex", pageIndex);

		HouseService houseService = new HouseService();
		long totalCount = houseService.getTotalCount(adminUserCityId, typeId);// 获取指定城市、指定类别下的数据总条数
		req.setAttribute("totalCount", totalCount);// 供<z:pager使用

		HouseDTO[] houses = houseService.getPagedData(adminUserCityId, typeId, 10, pageIndex);// 查询页数据
		req.setAttribute("houses", houses);

		req.setAttribute("ctxPath", req.getContextPath());// 给<z:pager的urlFormat用
		req.getRequestDispatcher("/WEB-INF/house/houseList.jsp").forward(req, resp);
	}

	@HasPermission("House.Delete")
	public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		HouseService service = new HouseService();
		service.markDeleted(id);

		try {
			deleteFromSolr(id);// 从solr中删除
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		}
		writeJson(resp, new AjaxResult("ok"));
	}

	// 加载区域下的所有小区
	public void loadCommunities(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long regionId = Long.parseLong(req.getParameter("regionId"));
		CommunityDTO[] communities = new CommunityService().getByRegionId(regionId);
		writeJson(resp, new AjaxResult("ok", "", communities));
	}

	@HasPermission("House.AddNew")
	public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long typeId = Long.parseLong(req.getParameter("typeId"));// 一个typeId的“旅行”
		req.setAttribute("typeId", typeId);
		Long cityId = AdminUtils.getAdminUserCityId(req);
		if (cityId == null) {
			AdminUtils.showError(req, resp, "总部的人不能管理房源");
			return;
		}
		fillEditAddRequest(req, cityId);

		req.getRequestDispatcher("/WEB-INF/house/houseAdd.jsp").forward(req, resp);
	}

	@HasPermission("House.AddNew")
	public void addSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId = AdminUtils.getAdminUserCityId(req);

		long typeId = Long.parseLong(req.getParameter("typeId"));
		long regionId = Long.parseLong(req.getParameter("regionId"));
		long communityId = Long.parseLong(req.getParameter("communityId"));
		long roomTypeId = Long.parseLong(req.getParameter("roomTypeId"));
		String address = req.getParameter("address");
		int monthRent = Integer.parseInt(req.getParameter("monthRent"));
		long statusId = Long.parseLong(req.getParameter("statusId"));
		double area = Double.parseDouble(req.getParameter("area"));
		long decorateStatusId = Long.parseLong(req.getParameter("decorateStatusId"));
		int floorIndex = Integer.parseInt(req.getParameter("floorIndex"));
		int totalFloorCount = Integer.parseInt(req.getParameter("totalFloorCount"));
		String direction = req.getParameter("direction");
		Date lookableDateTime = CommonUtils.parseDate(req.getParameter("lookableDateTime"));
		Date checkInDateTime = CommonUtils.parseDate(req.getParameter("checkInDateTime"));
		String ownerName = req.getParameter("ownerName");
		String ownerPhoneNum = req.getParameter("ownerPhoneNum");
		String description = req.getParameter("description");
		String[] attachmentIds = req.getParameterValues("attachmentId");

		HouseDTO house = new HouseDTO();
		house.setAddress(address);
		house.setArea(area);
		house.setAttachmentIds(CommonUtils.toLongArray(attachmentIds));
		house.setCheckInDateTime(checkInDateTime);
		house.setCommunityId(communityId);
		house.setCityId(cityId);
		house.setDecorateStatusId(decorateStatusId);
		house.setDescription(description);
		house.setDirection(direction);
		house.setFloorIndex(floorIndex);
		house.setLookableDateTime(lookableDateTime);
		house.setMonthRent(monthRent);
		house.setOwnerName(ownerName);
		house.setOwnerPhoneNum(ownerPhoneNum);
		house.setRegionId(regionId);
		house.setRoomTypeId(roomTypeId);
		house.setStatusId(statusId);
		house.setTotalFloorCount(totalFloorCount);
		house.setTypeId(typeId);

		long id = new HouseService().addnew(house);

		// 把房源信息插入solr服务器
		house = new HouseService().getById(id);
		try {
			insertToSolr(house);
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		}

		createStaticPage(id);

		writeJson(resp, new AjaxResult("ok"));
	}

	// 生成房源的静态页面
	private void createStaticPage(long houseId) {
		URL url;
		try {
			url = new URL("http://localhost:8080/ZuFront/House?action=view&id=" + houseId);
			String html = IOUtils.toString(url, "UTF-8");// 向web服务器发送get请求获得id为houseId的房源的查看页面的html
			FileUtils.write(new File(
					"E:/快盘/NextBig/java课程/JavaWeb项目（掌上租）/上课代码/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/ZuFront/houses/"
							+ houseId + ".html"),
					html, "UTF-8");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	// 重建所有房源的静态页
	public void reBuildAllStaticPages(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HouseService houseService = new HouseService();
		HouseDTO[] houses = houseService.getAll();
		for (HouseDTO house : houses) {
			createStaticPage(house.getId());
		}
		writeJson(resp, new AjaxResult("ok"));
	}

	private void insertToSolr(HouseDTO house) throws SolrServerException, IOException {
		HttpSolrClient.Builder builder = new HttpSolrClient.Builder("http://127.0.0.1:8983/solr/houses");
		HttpSolrClient solrClient = builder.build();
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.setField("id", house.getId());
			doc.setField("cityId", house.getCityId());
			doc.setField("address", house.getAddress());
			doc.setField("area", house.getArea());
			doc.setField("checkInDateTime", house.getCheckInDateTime());
			doc.setField("communityBuiltYear", house.getCommunityBuiltYear());
			doc.setField("communityId", house.getCommunityId());
			doc.setField("communityLocation", house.getCommunityLocation());
			doc.setField("communityName", house.getCommunityName());
			doc.setField("communityTraffic", house.getCommunityTraffic());
			doc.setField("decorateStatusId", house.getDecorateStatusId());
			doc.setField("decorateStatusName", house.getDecorateStatusName());
			doc.setField("description", house.getDescription());
			doc.setField("direction", house.getDirection());
			doc.setField("floorIndex", house.getFloorIndex());
			doc.setField("lookableDateTime", house.getLookableDateTime());
			doc.setField("monthRent", house.getMonthRent());
			doc.setField("regionId", house.getRegionId());
			doc.setField("regionName", house.getRegionName());
			doc.setField("roomTypeId", house.getRoomTypeId());
			doc.setField("statusId", house.getStatusId());
			doc.setField("statusName", house.getStatusName());
			doc.setField("totalFloorCount", house.getTotalFloorCount());
			doc.setField("typeId", house.getTypeId());
			doc.setField("typeName", house.getTypeName());
			solrClient.add(doc);// insert
			solrClient.commit();// !!!!!!!!
		} finally {
			solrClient.close();
		}

	}

	@HasPermission("House.Edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId = AdminUtils.getAdminUserCityId(req);
		if (cityId == null) {
			AdminUtils.showError(req, resp, "总部的人不能管理房源");
			return;
		}

		long id = Long.parseLong(req.getParameter("id"));
		HouseDTO dto = new HouseService().getById(id);
		if (dto == null) {
			AdminUtils.showError(req, resp, "找不到这个房源");
			return;
		}
		req.setAttribute("house", dto);

		AttachmentDTO[] houseAttachments = new AttachmentService().getAttachments(id);// 获得房子的配套设施
		long[] houseAttachmentIds = new long[houseAttachments.length];
		for (int i = 0; i < houseAttachments.length; i++) {
			houseAttachmentIds[i] = houseAttachments[i].getId();
		}
		req.setAttribute("houseAttachmentIds", houseAttachmentIds);

		fillEditAddRequest(req, cityId);

		req.getRequestDispatcher("/WEB-INF/house/houseEdit.jsp").forward(req, resp);
	}

	private void fillEditAddRequest(HttpServletRequest req, Long cityId) {
		RegionService regionService = new RegionService();
		RegionDTO[] regions = regionService.getAll(cityId);// 取得城市中的区域

		IdNameService idNameService = new IdNameService();
		IdNameDTO[] roomTypes = idNameService.getAll("户型");
		IdNameDTO[] statuses = idNameService.getAll("房屋状态");
		IdNameDTO[] decorateStatus = idNameService.getAll("装修状态");

		AttachmentDTO[] attachments = new AttachmentService().getAll();

		req.setAttribute("regions", regions);
		req.setAttribute("roomTypes", roomTypes);
		req.setAttribute("statuses", statuses);
		req.setAttribute("decorateStatus", decorateStatus);
		req.setAttribute("attachments", attachments);

		Date now = new Date();
		req.setAttribute("now", DateFormatUtils.format(now, "yyyy-MM-dd"));
	}

	@HasPermission("House.Edit")
	public void editSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));

		long typeId = Long.parseLong(req.getParameter("typeId"));
		long regionId = Long.parseLong(req.getParameter("regionId"));
		long communityId = Long.parseLong(req.getParameter("communityId"));
		long roomTypeId = Long.parseLong(req.getParameter("roomTypeId"));
		String address = req.getParameter("address");
		int monthRent = Integer.parseInt(req.getParameter("monthRent"));
		long statusId = Long.parseLong(req.getParameter("statusId"));
		double area = Double.parseDouble(req.getParameter("area"));
		long decorateStatusId = Long.parseLong(req.getParameter("decorateStatusId"));
		int floorIndex = Integer.parseInt(req.getParameter("floorIndex"));
		int totalFloorCount = Integer.parseInt(req.getParameter("totalFloorCount"));
		String direction = req.getParameter("direction");
		Date lookableDateTime = CommonUtils.parseDate(req.getParameter("lookableDateTime"));
		Date checkInDateTime = CommonUtils.parseDate(req.getParameter("checkInDateTime"));
		String ownerName = req.getParameter("ownerName");
		String ownerPhoneNum = req.getParameter("ownerPhoneNum");
		String description = req.getParameter("description");
		String[] attachmentIds = req.getParameterValues("attachmentId");

		HouseDTO house = new HouseDTO();
		house.setId(id);
		house.setAddress(address);
		house.setArea(area);
		house.setAttachmentIds(CommonUtils.toLongArray(attachmentIds));
		house.setCheckInDateTime(checkInDateTime);
		house.setCommunityId(communityId);
		house.setDecorateStatusId(decorateStatusId);
		house.setDescription(description);
		house.setDirection(direction);
		house.setFloorIndex(floorIndex);
		house.setLookableDateTime(lookableDateTime);
		house.setMonthRent(monthRent);
		house.setOwnerName(ownerName);
		house.setOwnerPhoneNum(ownerPhoneNum);
		house.setRegionId(regionId);
		house.setRoomTypeId(roomTypeId);
		house.setStatusId(statusId);
		house.setTotalFloorCount(totalFloorCount);
		house.setTypeId(typeId);

		new HouseService().update(house);

		// 修改房源的时候先删除solr中的对应数据，再添加
		try {
			deleteFromSolr(house.getId());
			insertToSolr(new HouseService().getById(id));
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		}
		createStaticPage(id);
		writeJson(resp, new AjaxResult("ok"));
	}

	private void deleteFromSolr(long houseId) throws SolrServerException, IOException {
		HttpSolrClient.Builder builder = new HttpSolrClient.Builder("http://127.0.0.1:8983/solr/houses");
		HttpSolrClient solrClient = builder.build();
		try {
			solrClient.deleteById(String.valueOf(houseId));
			solrClient.commit();
		} finally {
			solrClient.close();
		}

	}

	@HasPermission("House.Pic")
	public void picsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		HousePicDTO[] pics = new HouseService().getPics(id);// 获取房子的配图
		req.setAttribute("pics", pics);
		req.setAttribute("id", id);
		req.getRequestDispatcher("/WEB-INF/house/picsList.jsp").forward(req, resp);
	}

	private void uploadToQiniu(byte[] bytes, String fileName) {
		SettingService settingsService = new SettingService();
		String ak = settingsService.getValue("QiNiu.AK");
		String sk = settingsService.getValue("QiNiu.SK");
		String bucketName = settingsService.getValue("QiNiu.BucketName");

		UploadManager uploadManager = new UploadManager();
		Auth auth = Auth.create(ak, sk);
		String token = auth.uploadToken(bucketName);
		try {
			Response r = uploadManager.put(bytes, fileName, token);
		} catch (QiniuException e) {
			throw new RuntimeException(e);
		}
	}

	@HasPermission("House.Pic")
	public void uploadImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long houseId = Long.parseLong(req.getParameter("houseId"));

		Part part = req.getPart("file");// 用户上传的文件。由于webupload是每个文件一次请求，而且每个上传文件的表单名字都是file
		String filename = part.getSubmittedFileName();// 用户提交的文件名。如果是在IE6、7、8上传文件名会带着路径
		String fileExt = FilenameUtils.getExtension(filename);// 不带.的后缀名
		if (!fileExt.equalsIgnoreCase("jpg") && !fileExt.equalsIgnoreCase("png") && !fileExt.equalsIgnoreCase("jpeg")) {
			return;
		}

		Calendar calendar = Calendar.getInstance();
		String fileRelativePath;// 大图相对路径
		String thumbFileRelativePath;// 缩略图相对路径

		InputStream inStream1 = null;
		InputStream inStream2 = null;

		try {
			inStream1 = part.getInputStream();

			String fileMd5 = CommonUtils.calcMD5(inStream1);// 为了避免第二次算md5值时候指针已经指向结尾，所以只算一次，重复使用
			
			//水印图路径 
			fileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + fileMd5 + "." + fileExt;

			// 缩略图的路径
			thumbFileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + fileMd5 + ".thumb." + fileExt;

			// String filePath = rootDir + fileRelativePath;// 文件的文件夹全路径

			inStream2 = new BufferedInputStream(part.getInputStream());
			inStream2.mark(Integer.MAX_VALUE);

			
			ByteArrayOutputStream thumbnailOS = new ByteArrayOutputStream();
			try {
				// 生成缩略图
				Thumbnails.of(inStream2).size(150, 150).toOutputStream(thumbnailOS);// 把缩略图写入内存输出流ByteArrayOutputStream
				uploadToQiniu(thumbnailOS.toByteArray(), thumbFileRelativePath);
			} finally {
				IOUtils.closeQuietly(thumbnailOS);
			}


			inStream2.reset();// 指针归位

			ByteArrayOutputStream waterMarkOS = new ByteArrayOutputStream();
			try
			{
				// 生成水印图片保存
				BufferedImage imgWaterMark = ImageIO
						.read(new File(req.getServletContext().getRealPath("/images/watermark.png")));
				Thumbnails.of(inStream2).size(500, 500).watermark(Positions.BOTTOM_RIGHT, imgWaterMark, 0.5f)
					.toOutputStream(waterMarkOS);
				uploadToQiniu(waterMarkOS.toByteArray(), fileRelativePath);
			}finally
			{
				IOUtils.closeQuietly(waterMarkOS);
			}

			SettingService settingService = new SettingService();
			String qiniuDomain = settingService.getValue("QiNiu.Domain");
			
			HousePicDTO housePic = new HousePicDTO();
			housePic.setHouseId(houseId);
			housePic.setThumbUrl(qiniuDomain+"/" + thumbFileRelativePath);
			housePic.setUrl(qiniuDomain+"/" + fileRelativePath);
			housePic.setHeight(500);
			housePic.setWidth(500);
			HouseService houseService = new HouseService();
			houseService.addnewHousePic(housePic);

			// FileUtils.copyInputStreamToFile(inStream2, new File(rootDir,
			// fileRelativePath));// 第一个：帮助我们创建不存在的文件夹、会自动把InputStream的流归位
		} finally {
			IOUtils.closeQuietly(inStream1);
			IOUtils.closeQuietly(inStream2);
		}
	}

	@HasPermission("House.Pic")
	public void uploadImage2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long houseId = Long.parseLong(req.getParameter("houseId"));

		Part part = req.getPart("file");// 用户上传的文件。由于webupload是每个文件一次请求，而且每个上传文件的表单名字都是file
		String filename = part.getSubmittedFileName();// 用户提交的文件名。如果是在IE6、7、8上传文件名会带着路径
		String fileExt = FilenameUtils.getExtension(filename);// 不带.的后缀名
		if (!fileExt.equalsIgnoreCase("jpg") && !fileExt.equalsIgnoreCase("png") && !fileExt.equalsIgnoreCase("jpeg")) {
			return;
		}

		String rootDir = req.getServletContext().getRealPath("/");// 网站根目录的物理路径
																	// c:/1/upload
		rootDir = FilenameUtils.separatorsToUnix(rootDir);// 把路径中的\改成/

		Calendar calendar = Calendar.getInstance();
		String fileRelativePath;// 大图相对路径
		String thumbFileRelativePath;// 缩略图相对路径

		InputStream inStream1 = null;
		InputStream inStream2 = null;

		try {
			inStream1 = part.getInputStream();

			String fileMd5 = CommonUtils.calcMD5(inStream1);// 为了避免第二次算md5值时候指针已经指向结尾，所以只算一次，重复使用
			fileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + fileMd5 + "." + fileExt;

			// 缩略图的路径
			thumbFileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + fileMd5 + ".thumb." + fileExt;

			// String filePath = rootDir + fileRelativePath;// 文件的文件夹全路径

			inStream2 = new BufferedInputStream(part.getInputStream());
			inStream2.mark(Integer.MAX_VALUE);

			File fileThumb = new File(rootDir, thumbFileRelativePath);
			fileThumb.getParentFile().mkdirs();// 创建父文件夹
			// 生成缩略图
			Thumbnails.of(inStream2).size(150, 150).toFile(fileThumb);
			inStream2.reset();// 指针归位

			File fileWaterMark = new File(rootDir, fileRelativePath);
			fileWaterMark.getParentFile().mkdirs();
			// 生成水印图片保存
			BufferedImage imgWaterMark = ImageIO
					.read(new File(req.getServletContext().getRealPath("/images/watermark.png")));
			Thumbnails.of(inStream2).size(500, 500).watermark(Positions.BOTTOM_RIGHT, imgWaterMark, 0.5f)
					.toFile(fileWaterMark);// new
											// File(rootDir,fileRelativePath)这样可以避免考虑路径拼接是否有分割符的问题

			HousePicDTO housePic = new HousePicDTO();
			housePic.setHouseId(houseId);
			housePic.setThumbUrl("http://localhost:8080/ZuAdmin/" + thumbFileRelativePath);
			housePic.setUrl("http://localhost:8080/ZuAdmin/" + fileRelativePath);
			housePic.setHeight(500);
			housePic.setWidth(500);
			HouseService houseService = new HouseService();
			houseService.addnewHousePic(housePic);

			// FileUtils.copyInputStreamToFile(inStream2, new File(rootDir,
			// fileRelativePath));// 第一个：帮助我们创建不存在的文件夹、会自动把InputStream的流归位
		} finally {
			IOUtils.closeQuietly(inStream1);
			IOUtils.closeQuietly(inStream2);
		}
	}

	@HasPermission("House.Pic")
	public void deletePics(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] picIds = req.getParameterValues("picIds");
		HouseService houseService = new HouseService();
		for (String picId : picIds) {
			houseService.deleteHousePic(Long.parseLong(picId));
		}
		writeJson(resp, new AjaxResult("ok"));
	}

	public void uploadImage1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long houseId = Long.parseLong(req.getParameter("houseId"));

		Part part = req.getPart("file");// 用户上传的文件。由于webupload是每个文件一次请求，而且每个上传文件的表单名字都是file
		String filename = part.getSubmittedFileName();// 用户提交的文件名。如果是在IE6、7、8上传文件名会带着路径
		// 1.jpg c:\aaa\1.jpg
		// FileUtils.copyInputStreamToFile(part.getInputStream(), new
		// File("d:/"+fn));
		String fileExt = FilenameUtils.getExtension(filename);// 不带.的后缀名
		if (!fileExt.equalsIgnoreCase("jpg") && !fileExt.equalsIgnoreCase("png") && !fileExt.equalsIgnoreCase("jpeg")) {
			return;
		}
		/*
		 * 
		 * //InputStream inStream =
		 * part.getInputStream();//每次调用.getInputStream()都会返回一个新的对象
		 * BufferedInputStream inStream = new
		 * BufferedInputStream(part.getInputStream());//用支持mark、
		 * reset的BufferedInputStream包装这个流
		 * //inStream.mark(Integer.MAX_VALUE);//把当前流的读取指针的位置做一个标记。
		 * 不是所有InputStream都支持mark、reset //inStream.reset();//把指针移动回上一次mark的位置
		 * inStream.mark(Integer.MAX_VALUE);
		 * 
		 * String uploadDir =
		 * req.getServletContext().getRealPath("/upload");//获得上传文件夹的物理路径
		 * c:/1/upload uploadDir =
		 * FilenameUtils.separatorsToUnix(uploadDir);//把路径中的\改成/ Calendar
		 * calendar = Calendar.getInstance(); String fileDir =
		 * uploadDir+"/"+calendar.get(Calendar.YEAR)+"/"
		 * +calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)
		 * ;//文件的文件夹全路径 String filePath =
		 * fileDir+"/"+CommonUtils.calcMD5(inStream)+"."+fileExt;
		 * inStream.reset();//流的读取指针重新指回原始位置
		 * 
		 * new File(fileDir).mkdirs();//如果fileDir这个文件夹或者父文件夹不存在，则一路创建出所有的文件夹
		 * FileOutputStream fos = new FileOutputStream(filePath);//如果文件夹不存在，则报错
		 * 
		 * byte[] bytes=new byte[1024*124]; int len;
		 * while((len=inStream.read(bytes))>0)//part.getInputStream().read() {
		 * fos.write(bytes,0,len); } fos.close();
		 */
		// FileUtils.copyInputStreamToFile(part.getInputStream(), new
		// File(filePath));//第一个：帮助我们创建不存在的文件夹、会自动把InputStream的流归位

		String rootDir = req.getServletContext().getRealPath("/");// 网站根目录的物理路径
																	// c:/1/upload
		rootDir = FilenameUtils.separatorsToUnix(rootDir);// 把路径中的\改成/

		Calendar calendar = Calendar.getInstance();
		String fileRelativePath;

		InputStream inStream1 = null;
		InputStream inStream2 = null;

		try {
			inStream1 = part.getInputStream();

			fileRelativePath = "upload/" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "/" + CommonUtils.calcMD5(inStream1) + "." + fileExt;
			String filePath = rootDir + fileRelativePath;// 文件的文件夹全路径

			inStream2 = part.getInputStream();
			FileUtils.copyInputStreamToFile(inStream2, new File(filePath));// 第一个：帮助我们创建不存在的文件夹、会自动把InputStream的流归位
		} finally {
			IOUtils.closeQuietly(inStream1);
			IOUtils.closeQuietly(inStream2);
		}

		HousePicDTO housePic = new HousePicDTO();
		housePic.setHouseId(houseId);
		housePic.setThumbUrl("http://localhost:8080/ZuAdmin/" + fileRelativePath);
		housePic.setUrl("http://localhost:8080/ZuAdmin/" + fileRelativePath);
		HouseService houseService = new HouseService();
		houseService.addnewHousePic(housePic);

		// 1、文件的路径用“年/月/日/”，避免一个文件夹下文件过多
		// 2、文件的文件名用文件内容的md5值，避免文件重名的问题
		// 3、上传的文件是在网站部署目录下，不是在源代码的目录下，通过getRealPath拿到物理路径
		// 4、part.getInputStream()每次调用都会返回一个新的InputStream对象
		// 5、如果计算md5值和保存文件用的是同一个InputStream，就会有计算md5值把文件流的指针指到最后，那么写入文件就写入0字节
		// 因此需要使用mark、reset来让读取指针返回原始位置。不是所有的流都支持reset，因此最好用BufferedInputStream包装一下
		// 因为part.getInputStream()每次调用都会返回一个新的InputStream对象，这样最简单的方法就是计算md5值和拷贝文件的时候用两个part.getInputStream()
		// 6、最简单的方法就是用FileUtils.copyInputStreamToFile，他会帮我们处理文件夹不存在的问题

	}

	// @HasPermission("Houes.AppMgr")
	public void appList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long cityId = AdminUtils.getAdminUserCityId(req);
		if (cityId == null) {
			AdminUtils.showError(req, resp, "总部的人不能进行预约管理！");
			return;
		}
		long pageNum = Long.parseLong(req.getParameter("pageNum"));
		String status = req.getParameter("status");

		HouseAppointmentService appService = new HouseAppointmentService();
		HouseAppointmentDTO[] apps = appService.getPagedData(cityId, status, 10, pageNum);
		long totalCount = appService.getTotalCount(cityId, status);
		req.setAttribute("apps", apps);
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("status", status);
		req.setAttribute("pageNum", pageNum);
		req.setAttribute("ctxPath", req.getContextPath());
		req.getRequestDispatcher("/WEB-INF/house/houseAppList.jsp").forward(req, resp);
	}

	// 抢单
	public void follow(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long adminUserId = AdminUtils.getAdminUserId(req);

		long appId = Long.parseLong(req.getParameter("id"));// 预约id
		HouseAppointmentService service = new HouseAppointmentService();
		boolean isOK = service.follow(adminUserId, appId);
		if (isOK) {
			writeJson(resp, new AjaxResult("ok"));
		} else {
			HouseAppointmentDTO appDto = service.getById(appId);
			if (appDto.getFollowAdminUserId() == adminUserId) {
				writeJson(resp, new AjaxResult("error", "此单你已经抢下来了，不用重复抢单！"));
			} else {
				writeJson(resp, new AjaxResult("error", "抢单失败，已经被别人抢走了！"));
			}
		}
	}
}
