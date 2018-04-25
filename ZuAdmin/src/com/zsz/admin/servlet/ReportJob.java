package com.zsz.admin.servlet;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zsz.service.ReportService;
import com.zsz.service.SettingService;

public class ReportJob implements Job {
	private static final Logger log = LogManager.getLogger(ReportJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.debug("开始执行定期任务报表");
		try
		{
			ReportService service = new ReportService();
			Map<String, Long> map = service.queryYesterdayCityInfo();
			StringBuilder sb = new StringBuilder();
			for(Entry<String, Long> entry:map.entrySet())
			{
				sb.append(entry.getKey()+":"+entry.getValue()+"<br/>");
			}
			
			SettingService settingService = new SettingService();
			
			
			HtmlEmail email = new HtmlEmail();//发送html格式邮件
			email.setHostName(settingService.getValue("Mail.Smtp"));
			email.setCharset("UTF-8");
			// 登陆邮件服务器的用户名和密码
			email.setAuthentication(settingService.getValue("Mail.UserName"), settingService.getValue("Mail.Password"));
			email.addTo(settingService.getValue("Boss.Email"));
			email.setFrom(settingService.getValue("Mail.From"));
			email.setSubject("近24小时的新增房源报表");
			email.setMsg(sb.toString());
			email.send();
			log.debug("完成执行定期任务报表");
		}
		catch(Throwable ex)
		{
			log.error("执行定期任务报表失败",ex);
		}
	}

}
