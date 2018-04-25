import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.dao.UserDAO;
import com.zsz.dto.UserDTO;

public class UserDAOTest {

	@Test
	public void test() {
		UserDAO dao = new UserDAO();
		long id =  dao.addnew("110", "123");
		UserDTO user = dao.getById(id);
		assertNotNull(user);
		dao.updatePwd(id, "654321");
		assertTrue(dao.checkLogin("110", "654321"));
		assertFalse(dao.checkLogin("110", "1111"));
	}

}
