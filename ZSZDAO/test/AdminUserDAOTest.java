import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import com.zsz.dao.AdminUserDAO;
import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.AdminUserDTO;
import com.zsz.tools.CommonUtils;

public class AdminUserDAOTest {

	@Test
	public void testAddAdminUser() {
		AdminUserDAO dao = new AdminUserDAO();
		long u1Id = dao.addAdminUser("abc", "18918918189", "123", "yzk@rupeng.com", 1L);//int Integer/Long
		long u2Id = dao.addAdminUser("bcd","18918918111", "123", "yzk@rupeng.com", null);
		AdminUserDTO u1 = dao.getByPhoneNum("18918918189");
		assertNotNull(u1);		
		
		AdminUserDTO u2 = dao.getByPhoneNum("18918918111");
		assertNotNull(u2);
		
		AdminUserDTO u3 = dao.getById(u1Id);
		AdminUserDTO u4 = dao.getById(u2Id);
		assertNotNull(u3);
		assertNotNull(u4);
	}

	@Test
	public void testUpdateAdminUser() {
		AdminUserDAO dao = new AdminUserDAO();
		
		dao.updateAdminUser(1, "haha", "123", "123@123.com", 3L);
		AdminUserDTO u1 = dao.getById(1);
		assertEquals((long)u1.getCityId(), 3);
		assertEquals(u1.getCityName(), "深圳");
		assertEquals(u1.getEmail(), "123@123.com");
		assertEquals(u1.getName(), "haha");
		assertEquals(u1.getPasswordHash(), CommonUtils.calcMD5("123"));
	}
	
	@Test
	public void testCheckLogin()
	{
		AdminUserDAO dao = new AdminUserDAO();
		assertTrue(dao.checkLogin("18911111111", "123456"));
		assertFalse(dao.checkLogin("18911111111", "aaaaaa"));
		assertFalse(dao.checkLogin("19911111111", "123"));
	}
	
	@Test
	public void test1()
	{
		AdminUserDAO dao = new AdminUserDAO();
		assertTrue(dao.hasPermission(1, "AdminUser.Query"));
		assertFalse(dao.hasPermission(1, "House.Hahah"));
	}
	
	@AfterClass
	public static void tearClassAfter()
	{
		//JDBCUtils.exe
	}
	
	
}
