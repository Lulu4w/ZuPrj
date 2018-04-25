import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.zsz.front.Utils.FrontUtils;

import redis.clients.jedis.Jedis;

public class RedisTest {

	@Test
	public void test() {
		Jedis jedis = FrontUtils.createJedis();
		/*
		jedis.incr("test1");//test1++
		jedis.incr("test1");
		jedis.incr("test1");*/
		
		//String v = jedis.get("test1");
		//System.out.println(v);
		jedis.zadd("HotNews", 1, "Art1");
		jedis.zadd("HotNews", 1, "Art1");
		jedis.zadd("HotNews", 1, "Art1");
		jedis.zadd("HotNews", 1, "Art1");
		jedis.zadd("HotNews", 1, "Art1");
		jedis.zadd("HotNews", 1, "Art1");
		
		jedis.zadd("HotNews", 1, "Art2");
		jedis.zadd("HotNews", 1, "Art2");
		jedis.zadd("HotNews", 1, "Art2");
		
		jedis.zadd("HotNews", 1, "Art3");
		jedis.zadd("HotNews", 1, "Art3");
		jedis.zadd("HotNews", 1, "Art3");
		
		jedis.zadd("HotNews", 1, "Art4");
		jedis.zadd("HotNews", 1, "Art4");
		jedis.zadd("HotNews", 1, "Art4");
		jedis.zadd("HotNews", 1, "Art4");
		jedis.zadd("HotNews", 1, "Art2");
		
		Set<String> set = jedis.zrevrangeByScore("HotNews", 0, 4);
		for(String s:set)
		{
			System.out.println(s);
		}
		
		jedis.close();
	}

}
