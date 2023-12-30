package com.lonar.atflMobileInterfaceService.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lonar.atflMobileInterfaceService.common.ServiceException;
import com.lonar.atflMobileInterfaceService.model.CodeMaster;
import com.lonar.atflMobileInterfaceService.model.LtMastDistributors;
import com.lonar.atflMobileInterfaceService.model.LtMastDistributorsCsvBk;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployees;
import com.lonar.atflMobileInterfaceService.model.LtMastEmployeesCsvBk;
import com.lonar.atflMobileInterfaceService.model.LtMastInventory;
import com.lonar.atflMobileInterfaceService.model.LtMastInventoryCsvBk;
import com.lonar.atflMobileInterfaceService.model.LtMastOutlets;
import com.lonar.atflMobileInterfaceService.model.LtMastOutletsCsvBk;
import com.lonar.atflMobileInterfaceService.model.LtMastPositions;
import com.lonar.atflMobileInterfaceService.model.LtMastPositionsCsvBk;
import com.lonar.atflMobileInterfaceService.model.LtMastPriceLists;
import com.lonar.atflMobileInterfaceService.model.LtMastPriceListsCsvBk;
import com.lonar.atflMobileInterfaceService.model.LtMastProducts;
import com.lonar.atflMobileInterfaceService.model.LtMastProductsCsvBk;
import com.lonar.atflMobileInterfaceService.repository.LtMastDistributorsCsvBkRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastEmployeeCsvBkRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastInventoryCsvBkRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastOutletCsvBkRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastPositionsCsvBkRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastPriceListCsvBkRepository;
import com.lonar.atflMobileInterfaceService.repository.LtMastProductCsvBkRepository;

@Service
public class LtJobeServiceCsvBkImpl implements LtJobeServiceCsvBk, CodeMaster {

	@Autowired
	LtMastEmployeeCsvBkRepository ltMastEmployeeCsvBkRepository;

	@Autowired
	LtMastDistributorsCsvBkRepository ltMastDistributorsCsvBkRepository;

	@Autowired
	LtMastOutletCsvBkRepository ltMastOutletCsvBkRepository;

	@Autowired
	LtMastPositionsCsvBkRepository ltMastPositionsCsvBkRepository;

	@Autowired
	LtMastProductCsvBkRepository ltMastProductCsvBkRepository;

	@Autowired
	LtMastPriceListCsvBkRepository ltMastPriceListCsvBkRepository;

	@Autowired
	LtMastInventoryCsvBkRepository ltMastInventoryCsvBkRepository;

	@Override
	public boolean saveDistributorCSVBk(List<LtMastDistributors> distributorsList)
			throws IOException, ServiceException {

		if (distributorsList != null) {

			for (LtMastDistributors ltMastDistributors : distributorsList) {

				LtMastDistributorsCsvBk ltMastDistributorsCsvBk = new LtMastDistributorsCsvBk();
				ltMastDistributorsCsvBk.setCreation_date(new Date());
				ltMastDistributorsCsvBk.setMobile_status(ltMastDistributors.getMobileStatus());
				ltMastDistributorsCsvBk.setMobile_remarks(ltMastDistributors.getMobileRemarks());
				ltMastDistributorsCsvBk.setMobile_insert_update(ltMastDistributors.getMobileInsertUpdate());
				ltMastDistributorsCsvBk.setDistributor_code(ltMastDistributors.getDistributorCode());
				ltMastDistributorsCsvBk.setDistributor_type(ltMastDistributors.getDistributorType());
				ltMastDistributorsCsvBk.setDis_code(ltMastDistributors.getDistributorCrmCode());
				ltMastDistributorsCsvBk.setDistributor_name(ltMastDistributors.getDistributorName());
				ltMastDistributorsCsvBk.setProprietor_name(ltMastDistributors.getProprietorName());
				ltMastDistributorsCsvBk.setAddress_1(ltMastDistributors.getAddress_1());
				ltMastDistributorsCsvBk.setAddress_2(ltMastDistributors.getAddress_2());
				ltMastDistributorsCsvBk.setAddress_3(ltMastDistributors.getAddress_3());
				ltMastDistributorsCsvBk.setAddress_4(ltMastDistributors.getAddress_4());
			//	ltMastDistributorsCsvBk.setLandmark(ltMastDistributors.getLandMark());
				ltMastDistributorsCsvBk.setCountry(ltMastDistributors.getCountry());
				ltMastDistributorsCsvBk.setState(ltMastDistributors.getState());
				ltMastDistributorsCsvBk.setCity(ltMastDistributors.getCity());
				ltMastDistributorsCsvBk.setPin_code(ltMastDistributors.getPinCode());
				ltMastDistributorsCsvBk.setRegion(ltMastDistributors.getRegion());
				ltMastDistributorsCsvBk.setArea(ltMastDistributors.getArea());
				ltMastDistributorsCsvBk.setTerritory(ltMastDistributors.getTerritory());
				ltMastDistributorsCsvBk.setDistributor_gstn(ltMastDistributors.getDistributorGstn());
				ltMastDistributorsCsvBk.setDistributor_pan(ltMastDistributors.getDistributorPan());
				ltMastDistributorsCsvBk.setLicence_no(ltMastDistributors.getLicenceNo());
				ltMastDistributorsCsvBk.setPosition_code(ltMastDistributors.getPositions());
				ltMastDistributorsCsvBk.setPhone(ltMastDistributors.getPrimaryMobile());
				ltMastDistributorsCsvBk.setEmail(ltMastDistributors.getEmail());
				ltMastDistributorsCsvBk.setStatus(ltMastDistributors.getStatus());

				LtMastDistributorsCsvBk ltMastDistributorsCsvBkSave = ltMastDistributorsCsvBkRepository
						.save(ltMastDistributorsCsvBk);
				if (ltMastDistributorsCsvBkSave != null) {

				}

			}

		}

		return false;
	}

	@Override
	public boolean savePositionCSVBk(List<LtMastPositions> positionsList) throws IOException, ServiceException {

		if (positionsList != null) {

			for (LtMastPositions ltMastPositions : positionsList) {

				LtMastPositionsCsvBk ltMastPositionsCsvBk = new LtMastPositionsCsvBk();

				ltMastPositionsCsvBk.setCreation_date(new Date());
				ltMastPositionsCsvBk.setMobile_status(ltMastPositions.getMobileStatus());
				ltMastPositionsCsvBk.setMobile_remarks(ltMastPositions.getMobileRemarks());
				ltMastPositionsCsvBk.setMobile_insert_update(ltMastPositions.getMobileInsertUpdate());
				ltMastPositionsCsvBk.setDistributor_code(ltMastPositions.getDistributorCode());
				ltMastPositionsCsvBk.setPosition_code(ltMastPositions.getPositionCode());
				ltMastPositionsCsvBk.setPosition(ltMastPositions.getPosition());
				ltMastPositionsCsvBk.setParent_position(ltMastPositions.getParentPosition());
				ltMastPositionsCsvBk.setPosition_type(ltMastPositions.getPositionType());
				ltMastPositionsCsvBk.setFirst_name(ltMastPositions.getFirstName());
				ltMastPositionsCsvBk.setLast_name(ltMastPositions.getLastName());
				ltMastPositionsCsvBk.setJob_title(ltMastPositions.getJobTitle());
				ltMastPositionsCsvBk.setStart_date(ltMastPositions.getStartDate());
				ltMastPositionsCsvBk.setEnd_date(ltMastPositions.getEndDate());

				LtMastPositionsCsvBk ltMastPositionsCsvBksave = ltMastPositionsCsvBkRepository
						.save(ltMastPositionsCsvBk);
				if (ltMastPositionsCsvBksave != null) {

				}
			}
		}

		return false;
	}

	@Override
	public boolean saveOutletCSVBk(List<LtMastOutlets> outletList) throws IOException, ServiceException {
		if (outletList != null) {

			for (LtMastOutlets ltMastOutlets : outletList) {
				LtMastOutletsCsvBk ltMastOutletsCsvBk = new LtMastOutletsCsvBk();
				ltMastOutletsCsvBk.setCreation_date(new Date());
				ltMastOutletsCsvBk.setMobile_status(ltMastOutlets.getMobileStatus());
				ltMastOutletsCsvBk.setMobile_remarks(ltMastOutlets.getMobileRemarks());
				ltMastOutletsCsvBk.setMobile_insert_update(ltMastOutlets.getMobileInsertUpdate());
				ltMastOutletsCsvBk.setOutlet_code(ltMastOutlets.getOutletCode());
				ltMastOutletsCsvBk.setDistributor_code(ltMastOutlets.getDistributorCode());
				ltMastOutletsCsvBk.setOutlet_type(ltMastOutlets.getOutletType());
				ltMastOutletsCsvBk.setOutlet_name(ltMastOutlets.getOutletName());
				ltMastOutletsCsvBk.setProprietor_name(ltMastOutlets.getProprietorName());
				ltMastOutletsCsvBk.setAddress_1(ltMastOutlets.getAddress1());
				ltMastOutletsCsvBk.setAddress_2(ltMastOutlets.getAddress2());
				ltMastOutletsCsvBk.setAddress_3(ltMastOutlets.getAddress3());
				ltMastOutletsCsvBk.setAddress_4(ltMastOutlets.getAddress4());
				//ltMastOutletsCsvBk.setLandmark(ltMastOutlets.getLandmark());
				ltMastOutletsCsvBk.setCountry(ltMastOutlets.getCountry());
				ltMastOutletsCsvBk.setState(ltMastOutlets.getState());
				ltMastOutletsCsvBk.setCity(ltMastOutlets.getCity());
				ltMastOutletsCsvBk.setPin_code(ltMastOutlets.getPin_code());
				ltMastOutletsCsvBk.setRegion(ltMastOutlets.getRegion());
				ltMastOutletsCsvBk.setArea(ltMastOutlets.getArea());
				ltMastOutletsCsvBk.setTerritory(ltMastOutlets.getTerritory());
				ltMastOutletsCsvBk.setOutlet_gstn(ltMastOutlets.getOutletGstn());
				ltMastOutletsCsvBk.setOutlet_pan(ltMastOutlets.getOutletPan());
				ltMastOutletsCsvBk.setLicence_no(ltMastOutlets.getLicenceNo());
				ltMastOutletsCsvBk.setPositions_code(ltMastOutlets.getPositionCode());
				ltMastOutletsCsvBk.setPhone(ltMastOutlets.getPhone());
				ltMastOutletsCsvBk.setEmail(ltMastOutlets.getEmail());
				//ltMastOutletsCsvBk.setPrimary_mobile(ltMastOutlets.getPrimaryMobile());
				ltMastOutletsCsvBk.setStatus(ltMastOutlets.getStatus());
				ltMastOutletsCsvBk.setPrice_list(ltMastOutlets.getPriceList());

				LtMastOutletsCsvBk ltMastOutletsCsvBksave = ltMastOutletCsvBkRepository.save(ltMastOutletsCsvBk);
				if (ltMastOutletsCsvBksave != null) {

				}
			}

		}
		return false;
	}

	@Override
	public boolean saveProductCSVBk(List<LtMastProducts> productList) throws IOException, ServiceException {
		if (productList != null) {

			for (LtMastProducts ltMastProducts : productList) {

				LtMastProductsCsvBk ltMastProductsCsvBk = new LtMastProductsCsvBk();
				ltMastProductsCsvBk.setCreation_date(new Date());
				ltMastProductsCsvBk.setMobile_status(ltMastProducts.getMobileStatus());
				ltMastProductsCsvBk.setMobile_remarks(ltMastProducts.getMobileRemarks());
				ltMastProductsCsvBk.setMobile_insert_update(ltMastProducts.getMobileInsertUpdate());
				ltMastProductsCsvBk.setProduct_type(ltMastProducts.getProductType());
				ltMastProductsCsvBk.setCategory(ltMastProducts.getCategory());
				ltMastProductsCsvBk.setSubcategory(ltMastProducts.getSubCategory());
				ltMastProductsCsvBk.setProduct_code(ltMastProducts.getProductCode());
				ltMastProductsCsvBk.setProduct_name(ltMastProducts.getProductDesc());
				ltMastProductsCsvBk.setProduct_desc(ltMastProducts.getProductName());
				ltMastProductsCsvBk.setPrimary_uom(ltMastProducts.getPrimaryUom());
				ltMastProductsCsvBk.setSecondary_uom(ltMastProducts.getSecondaryUom());
				ltMastProductsCsvBk.setSecondary_uom_value(ltMastProducts.getSecondaryUomValue());
				ltMastProductsCsvBk.setUnits_per_case(ltMastProducts.getUnitsPerCase());
				ltMastProductsCsvBk.setSegment(ltMastProducts.getSegment());
				ltMastProductsCsvBk.setBrand(ltMastProducts.getBrand());
				ltMastProductsCsvBk.setSubbrand(ltMastProducts.getSubBrand());
				ltMastProductsCsvBk.setStyle(ltMastProducts.getStyle());
				ltMastProductsCsvBk.setFlavor(ltMastProducts.getFlavor());
				ltMastProductsCsvBk.setCase_pack(ltMastProducts.getCasePack());
				ltMastProductsCsvBk.setHsn_code(ltMastProducts.getHsnCode());
				ltMastProductsCsvBk.setOrderable(ltMastProducts.getOrderable());
				ltMastProductsCsvBk.setProduct_image(ltMastProducts.getProductImage());
				ltMastProductsCsvBk.setThumbnail_image(ltMastProducts.getThumbnailImage());
				ltMastProductsCsvBk.setStatus(ltMastProducts.getStatus());

				LtMastProductsCsvBk ltMastProductsCsvBksave = ltMastProductCsvBkRepository.save(ltMastProductsCsvBk);
				if (ltMastProductsCsvBksave != null) {

				}
			}
		}
		return false;
	}

	@Override
	public boolean savePriceListCSVBk(List<LtMastPriceLists> priceList) throws IOException, ServiceException {
		if (priceList != null) {

			for (LtMastPriceLists LtMastPriceLists : priceList) {

				LtMastPriceListsCsvBk ltMastPriceListsCsvBk = new LtMastPriceListsCsvBk();
				ltMastPriceListsCsvBk.setCreation_date(new Date());
				ltMastPriceListsCsvBk.setMobile_status(LtMastPriceLists.getMobileStatus());
				ltMastPriceListsCsvBk.setMobile_remarks(LtMastPriceLists.getMobileRemarks());
				ltMastPriceListsCsvBk.setMobile_insert_update(LtMastPriceLists.getMobileInsertUpdate());
				ltMastPriceListsCsvBk.setPrice_list(LtMastPriceLists.getPriceList());
				ltMastPriceListsCsvBk.setPrice_list_desc(LtMastPriceLists.getPriceListDesc());
				ltMastPriceListsCsvBk.setCurrency(LtMastPriceLists.getCurrency());
				ltMastPriceListsCsvBk.setProduct_code(LtMastPriceLists.getProductCode());
				ltMastPriceListsCsvBk.setList_price(LtMastPriceLists.getListPrice());
				ltMastPriceListsCsvBk.setStart_date(LtMastPriceLists.getStartDate());
				ltMastPriceListsCsvBk.setEnd_date(LtMastPriceLists.getEndDate());
				ltMastPriceListsCsvBk.setPtr_price(LtMastPriceLists.getPtrPrice());

				LtMastPriceListsCsvBk ltMastPriceListsCsvBksave = ltMastPriceListCsvBkRepository
						.save(ltMastPriceListsCsvBk);

				if (ltMastPriceListsCsvBksave != null) {

				}
			}
		}
		return false;
	}

	@Override
	public boolean saveEmployeeCSVBk(List<LtMastEmployees> employeeList) throws IOException, ServiceException {
		if (employeeList != null) {

			for (LtMastEmployees ltMastEmployees : employeeList) {
				LtMastEmployeesCsvBk ltMastEmployeesCsvBk = new LtMastEmployeesCsvBk();
				ltMastEmployeesCsvBk.setCreation_date(new Date());
				ltMastEmployeesCsvBk.setMobile_status(ltMastEmployees.getMobileStatus());
				ltMastEmployeesCsvBk.setMobile_remarks(ltMastEmployees.getMobileRemarks());
				ltMastEmployeesCsvBk.setMobile_insert_update(ltMastEmployees.getMobileInsertUpdate());
				ltMastEmployeesCsvBk.setDistributor_code(ltMastEmployees.getDistributorCode());
				ltMastEmployeesCsvBk.setPosition_code(ltMastEmployees.getPositionCode());
				ltMastEmployeesCsvBk.setEmployee_code(ltMastEmployees.getRowNumber());
				ltMastEmployeesCsvBk.setLogin(ltMastEmployees.getEmployeeCode());
				ltMastEmployeesCsvBk.setFirst_name(ltMastEmployees.getFirstName());
				ltMastEmployeesCsvBk.setLast_name(ltMastEmployees.getLastName());
				ltMastEmployeesCsvBk.setJob_title(ltMastEmployees.getJobTitle());
				ltMastEmployeesCsvBk.setEmployee_type(ltMastEmployees.getEmployeeType());
				ltMastEmployeesCsvBk.setPrimary_mobile(ltMastEmployees.getPrimaryMobile());
				ltMastEmployeesCsvBk.setEmail(ltMastEmployees.getEmail());

				LtMastEmployeesCsvBk ltMastEmployeesCsvBksave = ltMastEmployeeCsvBkRepository
						.save(ltMastEmployeesCsvBk);

				if (ltMastEmployeesCsvBksave != null) {

				}
			}
		}
		return false;
	}

	@Override
	public boolean saveInventoryCSVBk(List<LtMastInventory> inventoryList) throws IOException, ServiceException {
		if (inventoryList != null) {

			for (LtMastInventory ltMastInventory : inventoryList) {

				LtMastInventoryCsvBk ltMastInventoryCsvBk = new LtMastInventoryCsvBk();
				ltMastInventoryCsvBk.setCreation_date(new Date());
				ltMastInventoryCsvBk.setMobile_status(ltMastInventory.getMobileStatus());
				ltMastInventoryCsvBk.setMobile_remarks(ltMastInventory.getMobileRemarks());
				ltMastInventoryCsvBk.setMobile_insert_update(ltMastInventory.getMobileInsertUpdate());
				ltMastInventoryCsvBk.setInventory_code(ltMastInventory.getInventoryCode());
				ltMastInventoryCsvBk.setInventory_name(ltMastInventory.getInventoryName());
				ltMastInventoryCsvBk.setDist_code(ltMastInventory.getDistCode());
				ltMastInventoryCsvBk.setInventory_status(ltMastInventory.getInventoryStatus());
				ltMastInventoryCsvBk.setProduct_rid(ltMastInventory.getProductRid());
				ltMastInventoryCsvBk.setProd_code(ltMastInventory.getProductCode());
				ltMastInventoryCsvBk.setQuantity(ltMastInventory.getQuantity());

				LtMastInventoryCsvBk ltMastInventoryCsvBksave = ltMastInventoryCsvBkRepository
						.save(ltMastInventoryCsvBk);
				
				if (ltMastInventoryCsvBksave != null) {

				}

			}
		}
		return false;
	}
}
