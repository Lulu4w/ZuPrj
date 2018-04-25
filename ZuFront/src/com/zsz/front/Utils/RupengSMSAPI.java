package com.zsz.front.Utils;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.zsz.service.SettingService;
import com.zsz.tools.CommonUtils;

public class RupengSMSAPI {
	public static RupengSMSResult send(String code, String phoneNum) {
		SettingService settingService = new SettingService();
		String templateId = settingService.getValue("RuPengSMS.TemplateId");
		return send(code,phoneNum,templateId);

	}
	
	public static RupengSMSResult send(String code, String phoneNum,String templateId) {
		// 从系统配置表中读取出用户名、appkey等
		SettingService settingService = new SettingService();
		String username = settingService.getValue("RuPengSMS.UserName");
		String appkey = settingService.getValue("RuPengSMS.AppKey");

		// 构造请求的get字符串
		String sendUrl = "http://sms.rupeng.cn/SendSms.ashx?userName=" + CommonUtils.urlEncodeUTF8(username)
				+ "&appKey=" + CommonUtils.urlEncodeUTF8(appkey) + "&templateId=" + templateId + "&code="
				+ CommonUtils.urlEncodeUTF8(code) + "&phoneNum=" + phoneNum;
		try {
			String resp = IOUtils.toString(new URL(sendUrl), "UTF-8");// 发出http请求，获得响应报文
			Gson gson = CommonUtils.createGson();
			return gson.fromJson(resp, RupengSMSResult.class);// 解析成java对象
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
