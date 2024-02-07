package com.users.usersmanagement.dao;

import java.io.IOException;
import java.util.List;

import com.users.usersmanagement.common.ServiceException;
import com.users.usersmanagement.model.BeatDetailsDto;
import com.users.usersmanagement.model.LtMastOrganisations;
import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutletsChannel;
import com.users.usersmanagement.model.LtMastOutletsDump;
import com.users.usersmanagement.model.LtMastOutletsType;
import com.users.usersmanagement.model.LtMastPricelist;
import com.users.usersmanagement.model.LtMastUsers;
import com.users.usersmanagement.model.OutletSequenceData;
import com.users.usersmanagement.model.RequestDto;

public interface LtMastOutletDao {

	public LtMastUsers getMastDataByOutletId(String getMastDataByOutletId) throws ServiceException;

	public List<LtMastOutlets> getOutlet(RequestDto requestDto) throws ServiceException, IOException;

	public LtMastOutlets verifyOutlet(String outletCode, String distributorCrmCode) throws ServiceException;
	
	public List<LtMastOutletsType> getAllOutletType() throws ServiceException, IOException;
	
	public List<LtMastOutletsChannel> getAllOutletChannel() throws ServiceException, IOException;
	
	public LtMastOrganisations getOrganisationDetailsById(String orgId)throws ServiceException, IOException;
	
	public LtMastOutlets getOutletByOutletCode(String outletCode)throws ServiceException, IOException;
	
	public List<LtMastPricelist> getPriceListAgainstDistributor(String outletId)throws ServiceException, IOException;
	
	public List<LtMastOutletsDump> getPendingAprrovalOutlet(RequestDto requestDto)throws ServiceException, IOException;
	
	public LtMastOutletsDump getOutletToChangeStatus(String distributorId,String orgId,String primaryMobile)throws ServiceException, IOException;
	
	public LtMastUsers getSystemAdministartorDetails(String orgId) throws ServiceException, IOException;
	
	public List<LtMastUsers> getAllSalesOfficerAgainstDist(String distributorId,String orgId)throws ServiceException, IOException;

	//public BeatDetailsDto getBeatDetailsAgainsDistirbutorCodeAndBeatName(String distributorCode, String beatName)throws ServiceException, IOException;
	public BeatDetailsDto getBeatDetailsAgainsDistirbutorCodeAndBeatName(BeatDetailsDto beatDetailsDto)throws ServiceException, IOException;

	//public List<OutletSequenceData> getBeatDetailsAgainsDistirbutorCode(String distributorCode, String beatName)throws ServiceException, IOException;
	public List<OutletSequenceData> getBeatDetailsAgainsDistirbutorCode(BeatDetailsDto beatDetailsDto)throws ServiceException, IOException;

	void updateBeatSequence(int outletSeq, String distCode, String beatName, String outletCode)throws ServiceException, IOException ;

	public BeatDetailsDto getUpdatedBeatSequence(String distCode, String beatName, String outletCode)throws ServiceException, IOException;

	public List<BeatDetailsDto> getOutletagainstBeat(BeatDetailsDto beatDetailsDto)throws ServiceException, IOException;


}
