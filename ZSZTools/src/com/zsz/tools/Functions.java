package com.zsz.tools;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Functions {
	
	/**
	 * 判断value是否存在于values这个数组或者集合中
	 * @param values
	 * @param value
	 * @return
	 */
	public static  boolean contains(Object values,Object value)
	{
		if(values==null||value==null)
		{
			return false;
		}
		
		Iterable iterable;
		if(values.getClass().isArray())
		{
			iterable = CommonUtils.toList(values);//把数组转换为一个List对象		
		}
		else if(values instanceof Iterable)//实现了Iterable都可以用增强for循环来遍历
			//List、Queue、Set等都实现了Iterable
			//但是数组没有实现这个接口
		{
			iterable = (Iterable)values;		
		}
		else
		{
			throw new IllegalArgumentException("第一个参数要传递数组或者实现了Iterable的对象");
		}
		
		for(Object item : iterable)
		{
			if(value.equals(item))//不要用==
			{
				return true;//找到了，存在!
			}
		}
		
		return false;//没有
	}
	
	/**
	 * 在queryString中增加或者修改name=value这样一个键值对
	 * 
	 * @param queryString
	 * @param name
	 * @param value
	 * @return
	 */
	public static String addQueryStringPart(String queryString,String name,String value)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<>();//
		//HashMap<String, String> map = new HashMap<>();
		
		
		//HashMap遍历的顺序for(Entry<String,String> entry :map.entrySet())
		//和put的顺序不一致
		//LinkedHashMap遍历的顺序和put的顺序一致
		
		String[] segments = queryString.split("&");//首先按照&分割
		for(String segment : segments)
		{
			String[] strs=segment.split("=");
			String segName = strs[0];
			String segValue=strs[1];
			map.put(segName, segValue);
		}
		/*
		for(Entry<String, String> entry:  map.entrySet())//entrySet()就是每一项键值对的集合，每一项Entry就是一个键值对象
		{
			System.out.println(entry.getKey()+"="+entry.getValue());
		}*/
		map.put(name,value);//不存在则添加，存在则更新
		StringBuilder sb = new StringBuilder();
		for(Entry<String,String> entry :map.entrySet())
		{
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		if(sb.charAt(sb.length()-1)=='&')//如果最后一个字符是&，则删除他
		{
			sb.delete(sb.length()-1, sb.length());
		}
		return sb.toString();
	}
}
