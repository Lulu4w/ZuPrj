import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.zsz.dao.HouseDAO;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HouseSearchOptions;
import com.zsz.dto.HouseSearchOptions.OrderByType;

public class HouseDAOTest {

	@Test
	public void testGet() {
		HouseDAO dao = new HouseDAO();
		HouseDTO house =  dao.getById(1);
		assertEquals(house.getAddress(), "1-305");
		//assertArrayEquals(house.getAttachmentIds(), new long[]{1,2,3,4,20});
	}
	
	
	@Test
	public void testAddNew()
	{
		HouseDAO dao = new HouseDAO();
		HouseDTO house = new HouseDTO();
		house.setAddress("深南大道16号3-308");
		house.setArea(110);
		house.setAttachmentIds( new long[]{1,2,3,4,20});
		house.setCheckInDateTime(new Date());
		house.setCityId(3);
		house.setCommunityId(28);
		house.setDecorateStatusId(16);
		house.setDescription("房东是个大学教授");
		house.setDirection("南");
		house.setFloorIndex(6);
		house.setLookableDateTime(new Date());
		house.setMonthRent(3500);
		house.setOwnerName("杨教授");
		house.setOwnerPhoneNum("18918918189");
		house.setRegionId(24);
		house.setRoomTypeId(4);
		house.setStatusId(8);
		house.setTotalFloorCount(18);
		house.setTypeId(14);

		long houseId = dao.addnew(house);
		HouseDTO house2 = dao.getById(houseId);
		assertNotNull(house2);
		assertEquals(house2.getAddress(), "深南大道16号3-308");
		assertArrayEquals(house2.getAttachmentIds(), house.getAttachmentIds());
		
	}
	
	@Test
	public void testUpdate()
	{
		HouseDAO dao = new HouseDAO();
		HouseDTO house = dao.getById(1);
		house.setAttachmentIds(new long[]{18,19,20});
		dao.update(house);
		
		HouseDTO house2 = dao.getById(1);
		assertArrayEquals(house2.getAttachmentIds(), new long[]{18,19,20});
	}
	
	@Test
	public void testSearch()
	{
		HouseDAO dao = new HouseDAO();
		HouseSearchOptions opt = new HouseSearchOptions();
		opt.setCityId(3);
		opt.setCurrentIndex(1);
		//opt.setEndMonthRent(8000);
		opt.setKeywords("村");
		opt.setOrderByType(OrderByType.MonthRent);
		opt.setPageSize(10);
		opt.setRegionId(24L);
		opt.setStartMonthRent(500);
		opt.setTypeId(14L);
		
		HouseDTO[] result1 = dao.search(opt);
		for(HouseDTO dto : result1)
		{
			String s = dto.getAddress();
			System.out.println(s);
		}
	}

}
