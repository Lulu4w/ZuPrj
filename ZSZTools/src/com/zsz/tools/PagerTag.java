package com.zsz.tools;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PagerTag extends SimpleTagSupport {
	private long totalCount;// 总数据条数
	private int pageSize;// 一页的数据条数
	private long currentPageNum;// 当前的页面
	private String urlFormat;//url的格式，页码用{pageNum}带代替

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(long currentPageNum) {
		this.currentPageNum = currentPageNum;
	}

	public String getUrlFormat() {
		return urlFormat;
	}

	public void setUrlFormat(String urlFormat) {
		this.urlFormat = urlFormat;
	}
	
	private String getUrl(long pageNum)
	{
		return urlFormat.replace("{pageNum}", String.valueOf(pageNum));
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = this.getJspContext().getOut();
		out.println("<a href='");
		out.print(getUrl(1));
		out.print("'>首页</a>");
		// 当前显示范围内的第一个页码数
		long firstPageNum = Math.max(currentPageNum - 5, 1);// max(3,5)

		// 总页数
		long totalPageCount = (long) Math.ceil(totalCount * 1.0f / pageSize);// 4*1.0f/3
		// 当前显示范围内最后一页的页码数
		long lastPageNum = Math.min(totalPageCount, currentPageNum + 4);

		// 输出当前页面前面的页码
		for (long i = firstPageNum; i < currentPageNum; i++) {
			out.print("<a href='");
			out.print(getUrl(i));
			out.print("'>");
			//out.print("<a href='student?pagenum=1'>");
			out.print(i);
			out.println("</a>");
		}
		out.print("<span>");
		out.print(currentPageNum);
		out.println("</span>");

		// 输出当前页面后面的页码
		for (long i = currentPageNum + 1; i <= lastPageNum; i++) {
			out.print("<a href='");
			out.print(getUrl(i));
			out.print("'>");
			//out.print("<a href='student?pagenum=1'>");
			out.print(i);
			out.println("</a>");
		}
		out.println("<a href='");
		out.print(getUrl(totalPageCount));
		out.print("'>末页</a>");
	}
}
