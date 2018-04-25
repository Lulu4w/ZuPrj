import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.tools.Functions;

public class FuntionTest {

	@Test
	public void test() {
		
		String s1 = Functions.addQueryStringPart("action=search&regionId=5", "monthRent", "100-200");
		System.out.println(s1);
		
		assertEquals(s1,
				"action=search&regionId=5&monthRent=100-200");
		
		String s2 = Functions.addQueryStringPart("action=search", "regionId", "5");
		System.out.println(s2);
		assertEquals(s2,
				"action=search&regionId=5");
		String s3 = Functions.addQueryStringPart("action=search&regionId=5", "regionId", "6");
		System.out.println(s3);
		assertEquals(s3,
				"action=search&regionId=6");
	}

}
