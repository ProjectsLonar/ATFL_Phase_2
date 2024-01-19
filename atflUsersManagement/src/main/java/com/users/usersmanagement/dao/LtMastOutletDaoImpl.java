package com.users.usersmanagement.dao;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.BeatDetailsDto;
import com.users.usersmanagement.model.CodeMaster;
import com.users.usersmanagement.model.LtMastOrganisations;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutletsChannel;
import com.users.usersmanagement.model.LtMastOutletsDump;
import com.users.usersmanagement.model.LtMastOutletsType;
import com.users.usersmanagement.model.LtMastPricelist;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.OutletSequenceData;
import com.users.usersmanagement.model.RequestDto;

@Repository
@PropertySource(value = "classpath:queries/userMasterQueries.properties", ignoreResourceNotFound = true)
public class LtMastOutletDaoImpl implements LtMastOutletDao, CodeMaster {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public LtMastOutlets verifyOutlet(String outletCode, String distributorCrmCode) throws ServiceException {
		String query = env.getProperty("verifyOutletUser");
		List<LtMastOutlets> ltMastOutletslist = jdbcTemplate.query(query, new Object[] { outletCode, distributorCrmCode },
				new BeanPropertyRowMapper<LtMastOutlets>(LtMastOutlets.class));
		if (!ltMastOutletslist.isEmpty()) {
			return ltMastOutletslist.get(0);
		}
		return null;
	}

	@Override
	public List<LtMastOutlets> getOutlet(RequestDto requestDto) throws ServiceException, IOException {
		String query = env.getProperty("getOutlet");
		try {
		if (requestDto.getLimit() == 0) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}

		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}

		List<LtMastOutlets> ltMastOutletslist = jdbcTemplate.query(query,
				new Object[] { requestDto.getDistributorId(), requestDto.getSalesPersonId(), requestDto.getOrgId(),
						searchField, requestDto.getLimit(), requestDto.getOffset() },
				new BeanPropertyRowMapper<LtMastOutlets>(LtMastOutlets.class));
		System.out.println("ltMastOutletslist"+ltMastOutletslist);
		if (!ltMastOutletslist.isEmpty()) {
			return ltMastOutletslist;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public LtMastUsers getMastDataByOutletId(String outletId) throws ServiceException {

		String query = env.getProperty("getMastDataByOutletId");

		List<LtMastUsers> ltMastOutletslist = jdbcTemplate.query(query, new Object[] { outletId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

		if (!ltMastOutletslist.isEmpty()) {
			return ltMastOutletslist.get(0);
		}
		return null;
	}
	
	@Override
	public List<LtMastOutletsType> getAllOutletType() throws ServiceException, IOException{
		String query = env.getProperty("getAllOutletType");
		
		List<LtMastOutletsType> list = jdbcTemplate.query(query,
				new Object[] { },
				new BeanPropertyRowMapper<LtMastOutletsType>(LtMastOutletsType.class));

		if (!list.isEmpty()) {
			return list;
		}

		return null;
	}


	
	@Override
	public List<LtMastOutletsChannel> getAllOutletChannel() throws ServiceException, IOException{
		String query = env.getProperty("getAllOutletChannel");
		

		List<LtMastOutletsChannel> list = jdbcTemplate.query(query,
				new Object[] { },
				new BeanPropertyRowMapper<LtMastOutletsChannel>(LtMastOutletsChannel.class));

		if (!list.isEmpty()) {
			return list;
		}

		return null;
	}
	
	@Override
	public LtMastOrganisations getOrganisationDetailsById(String orgId)throws ServiceException, IOException{
String query = env.getProperty("getOrganisationDetailsById");
		

		List<LtMastOrganisations> list = jdbcTemplate.query(query,
				new Object[] {orgId },
				new BeanPropertyRowMapper<LtMastOrganisations>(LtMastOrganisations.class));
System.out.println("list"+list);
		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}
	
	@Override
	public LtMastOutlets getOutletByOutletCode(String outletCode)throws ServiceException, IOException{
String query = env.getProperty("getOutletByOutletCode");
		

		List<LtMastOutlets> list = jdbcTemplate.query(query,
				new Object[] {outletCode },
				new BeanPropertyRowMapper<LtMastOutlets>(LtMastOutlets.class));
System.out.println("list"+list);
		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}
	
	@Override
	public List<LtMastPricelist> getPriceListAgainstDistributor(String outletId)throws ServiceException, IOException{

		String query = env.getProperty("getPriceListAgainstDistributor");
		System.out.println("outletId"+outletId);

		List<LtMastPricelist> list = jdbcTemplate.query(query,
				new Object[] {outletId},
				new BeanPropertyRowMapper<LtMastPricelist>(LtMastPricelist.class));
		System.out.println("query"+query);
System.out.println("list"+list);
		if (!list.isEmpty()) {
			return list;
		}

		return null;
	}
	
	@Override
	public List<LtMastOutletsDump> getPendingAprrovalOutlet(RequestDto requestDto)throws ServiceException, IOException{
		String query = env.getProperty("getPendingAprrovalOutlet");
		try {
		if (requestDto.getLimit() == 0) {
			requestDto.setLimit(Integer.parseInt(env.getProperty("limit_value")));
		}

		String searchField = null;
		if (requestDto.getSearchField() != null) {
			searchField = "%" + requestDto.getSearchField().toUpperCase() + "%";
		}

		List<LtMastOutletsDump> ltMastOutletslist = jdbcTemplate.query(query,
				new Object[] { requestDto.getDistributorId(), requestDto.getOrgId(),requestDto.getPrimaryMobile(),requestDto.getOutletName(),
						searchField, requestDto.getLimit(), requestDto.getOffset() },
				new BeanPropertyRowMapper<LtMastOutletsDump>(LtMastOutletsDump.class));

		System.out.println("list"+ltMastOutletslist);
		if (!ltMastOutletslist.isEmpty()) {
			return ltMastOutletslist;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}
	
	
	@Override
	public LtMastOutletsDump getOutletToChangeStatus(String distributorId,String orgId,String primaryMobile,String outletName)throws ServiceException, IOException{
		String query = env.getProperty("getOutletToChangeStatus");
		List<LtMastOutletsDump> ltMastOutletslist = jdbcTemplate.query(query,
				new Object[] { distributorId,orgId,primaryMobile,outletName},
				new BeanPropertyRowMapper<LtMastOutletsDump>(LtMastOutletsDump.class));

		if (!ltMastOutletslist.isEmpty()) {
			return ltMastOutletslist.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public LtMastUsers getSystemAdministartorDetails(String orgId) throws ServiceException, IOException{
		String query = env.getProperty("getSystemAdministartorDetails");
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] { orgId},
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist.get(0);
		}else {
			return null;
		}
	}
	
	
	@Override
	public List<LtMastUsers> getAllSalesOfficerAgainstDist(String distributorId,String orgId)throws ServiceException, IOException{
		String query = env.getProperty("getAllSalesOfficerAgainstDist");
		List<LtMastUsers> ltMastUserslist = jdbcTemplate.query(query,
				new Object[] {distributorId, orgId },
				new BeanPropertyRowMapper<LtMastUsers>(LtMastUsers.class));

		if (!ltMastUserslist.isEmpty()) {
			return ltMastUserslist;
		}else {
			return null;
		}
	}

	@Override
	public BeatDetailsDto getBeatDetailsAgainsDistirbutorCodeAndBeatName(String distributorCode, String beatName)throws ServiceException, IOException {
		String query = env.getProperty("getBeatDetailsAgainsDistirbutorCodeAndBeatName");
	       List<BeatDetailsDto> list = jdbcTemplate.query(query, new Object[] {distributorCode, beatName}, 
				new BeanPropertyRowMapper<BeatDetailsDto>(BeatDetailsDto.class));
	               if(!list.isEmpty()) {
				                         return list.get(0);
			                           }
				return null;
			}

	@Override
	public List<OutletSequenceData> getBeatDetailsAgainsDistirbutorCode(String distributorCode, String beatName)throws ServiceException, IOException {
		String query = env.getProperty("getBeatDetailsAgainsDistirbutorCodeAndBeat");
	       List<OutletSequenceData> list = jdbcTemplate.query(query, new Object[] {distributorCode, beatName}, 
				new BeanPropertyRowMapper<OutletSequenceData>(OutletSequenceData.class));
	               if(!list.isEmpty()) {
				                         return list;
			                           }
				return null;
			}

	@Override
	public void updateBeatSequence(int outletSeq, String distCode, String beatName, String outletCode)throws ServiceException, IOException {
		String query =  env.getProperty("updateBeatSequence");
		//Object value = new Object[] {outletSeq, distCode, beatName, outletCode};
		this.jdbcTemplate.update(query, outletSeq, distCode, beatName, outletCode);
		//return status;
	}
	
}
