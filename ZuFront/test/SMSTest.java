import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.front.Utils.RupengSMSAPI;
import com.zsz.front.Utils.RupengSMSResult;

public class SMSTest {

	@Test
	public void test() {
		RupengSMSResult r1 = RupengSMSAPI.send("1122", "18918918189");
		assertNotNull(r1);
		assertEquals(r1.getCode(), 0);
		
		RupengSMSResult r2 = RupengSMSAPI.send("1122", "188918189");
		assertEquals(r2.getCode(), 405);
	}

}
