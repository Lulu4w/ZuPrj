import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.zsz.dao.HouseAppointmentDAO;
import com.zsz.dto.HouseAppointmentDTO;

public class HouseAppTest {

	@Test
	public void testGetByid() {
		HouseAppointmentDAO dao = new HouseAppointmentDAO();
		HouseAppointmentDTO app =  dao.getById(1);
		assertNotNull(app);
		assertNull(dao.getById(99999));
		
		/*
		assertEquals(app.getName(), "杨");
		assertEquals(app.getFollowAdminUserName(), "老深圳");
		assertEquals(app.getRegionName(), "南山区");
		assertEquals(app.getCommunityName(), "南国丽城");*/
	}

	@Test
	public void testPagedData()
	{
		HouseAppointmentDAO dao = new HouseAppointmentDAO();
		dao.getTotalCount(3, "跟进中");
		//assertEquals(dao.getTotalCount(3, "跟进中"), 2);
		
		HouseAppointmentDTO[] apps =   dao.getPagedData(3, "跟进中", 10, 1);
		//assertEquals(apps.length, 2);
	}
	
	@Test
	public void testFollow()
	{
		HouseAppointmentDAO dao = new HouseAppointmentDAO();
		long appId = dao.addnew(null, "张三", "138888888", 1, new Date());
		assertTrue( dao.follow(1, appId));
		assertFalse( dao.follow(12, appId));
	}
}
