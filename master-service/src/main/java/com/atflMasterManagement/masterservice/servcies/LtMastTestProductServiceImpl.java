package com.atflMasterManagement.masterservice.servcies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atflMasterManagement.masterservice.MasterServiceApplication;
import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.dao.LtMastTestProductDao;
import com.atflMasterManagement.masterservice.dto.Category;
import com.atflMasterManagement.masterservice.dto.State;
import com.atflMasterManagement.masterservice.dto.SubCategory;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastTestProduct;
import com.atflMasterManagement.masterservice.model.Status;

@Service
public class LtMastTestProductServiceImpl implements LtMastTestProductService, CodeMaster {

	@Autowired
	private LtMastCommonMessageService ltMastCommonMessageService;

	@Autowired
	LtMastTestProductDao ltMastTestProductDao;

	Map<Integer, String> map = MasterServiceApplication.stateMap;

	public static String getValue(Map<Integer, String> map, String value) {
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			int val = Integer.parseInt(value);
			if (val == entry.getKey()) {
				return entry.getValue();
			}
		}
		return null;
	}

	public static <K, V> K getKey(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public Status saveProduct(LtMastTestProduct ltMastTestProduct) throws ServiceException {

		Status status = new Status();
		if (ltMastTestProduct == null) {
			return status;
		}

		ArrayList<String> stateList = new ArrayList<>();

		stateList = ltMastTestProduct.getStatesList();

		if (stateList != null) {

			// String[] stateArray = state.split(",");

			ArrayList<Integer> stateIdList = new ArrayList<>();

			for (String w : stateList) {
				if (map.containsValue(w)) {
					int id = getKey(map, w);
					stateIdList.add(id);
				}
			}

			int count = 1;

			String stateIds = "";

			for (int id : stateIdList) {

				if (count == stateIdList.size()) {
					stateIds = stateIds + Integer.toString(id);
					break;
				}
				stateIds = stateIds + Integer.toString(id) + ",";
				count++;
			}
			ltMastTestProduct.setStates(stateIds.toString());

		}
		LtMastTestProduct ltMastTestProductSave = ltMastTestProductDao.saveProduct(ltMastTestProduct);
		// String[] stateArray = ltMastTestProductSave.getStates().trim().split(",");
		ltMastTestProductSave.setStates(null);
		ltMastTestProductSave.setStatesList(stateList);

		if (ltMastTestProductSave.getProductId() != null) {
			// status = ltMastCommonMessageService.getCodeAndMessage(INSERT_SUCCESSFULLY);
			status.setCode(INSERT_SUCCESSFULLY);
			status.setMessage("Record Inserted Successfully");
			status.setData(ltMastTestProductSave);
			return status;
		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
			status.setData(null);
		}
		return status;
	}

	@Override
	public Status deleteByProductId(Long productId) throws ServiceException {

		Status status = new Status();
		LtMastTestProduct ltMastTestProduct = ltMastTestProductDao.getById(productId);
		if (ltMastTestProduct != null) {

			LtMastTestProduct ltMastTestProductDelete = ltMastTestProductDao.deleteByProductId(productId);
			if (ltMastTestProductDelete == null) {
				status = ltMastCommonMessageService.getCodeAndMessage(DELETE_SUCCESSFULLY);
				status.setData(null);
			} else {
				status = ltMastCommonMessageService.getCodeAndMessage(DELETE_FAIL);
				status.setData(null);
			}

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(DELETE_FAIL);
			status.setData(productId);
		}
		return status;
	}

	@Override
	public Status getAllProducts(Long limit, Long offset) throws ServiceException {
		Status status = new Status();
		try {
			List<LtMastTestProduct> ltMastTestProduct = ltMastTestProductDao.getAllProducts(limit, offset);

			List<LtMastTestProduct> testList = new ArrayList<LtMastTestProduct>();

			for (LtMastTestProduct testProduct : ltMastTestProduct) {
				ArrayList<String> statesList = new ArrayList<String>();
				if (testProduct.getStates() != null) {
					String[] stateArray = testProduct.getStates().trim().split(",");
					int count = 1;
					for (String w : stateArray) {
						if (!w.isEmpty()) {
							if (map.containsKey(Integer.parseInt(w))) {

								if (count == stateArray.length) {
									statesList.add(getValue(map, w));
									break;
								}
								statesList.add(getValue(map, w));
								count++;
							}
						}
					}
					testProduct.setStates(null);
					testProduct.setStatesList(statesList);
					testList.add(testProduct);
				} else {
					testProduct.setStatesList(null);
					testList.add(testProduct);
				}

			}
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(testList);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
		status.setData(null);
		return status;
	}

	@Override
	public Status updateProductDetails(LtMastTestProduct ltMastTestProduct) throws ServiceException {
		Status status = new Status();
		LtMastTestProduct ltProduct = ltMastTestProductDao.getById(ltMastTestProduct.getProductId());
		if (ltMastTestProduct.getProductId() != null) {

			ArrayList<String> stateList = new ArrayList<>();

			stateList = ltMastTestProduct.getStatesList();

			if (stateList != null) {
				ArrayList<Integer> stateIdList = new ArrayList<>();
				for (String w : stateList) {
					if (map.containsValue(w)) {
						int id = getKey(map, w);
						System.out.println("State id======>" + id);
						stateIdList.add(id);
					}
				}
				int count = 1;
				String stateIds = "";
				for (int id : stateIdList) {
					if (count == stateIdList.size()) {
						stateIds = stateIds + Integer.toString(id);
						break;
					}
					stateIds = stateIds + Integer.toString(id) + ",";
					count++;
				}
				ltMastTestProduct.setStates(stateIds.toString());
			} else {
				ltMastTestProduct.setStates(ltProduct.getStates());
			}

			if (ltProduct != null) {

				LtMastTestProduct ltMastTestProducts = ltMastTestProductDao.saveProduct(ltMastTestProduct);
				ltMastTestProducts.setStates(null);
				ltMastTestProducts.setStatesList(stateList);

				if (ltMastTestProducts.getProductId() != null) {
					status.setCode(UPDATE_SUCCESSFULLY);
					status.setMessage("Record Updated Successfully");
					status.setData(ltMastTestProducts);
					return status;
				} else {
					status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
					status.setData(null);
				}
			} else {
				status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_FAIL);
				status.setData(null);
			}
		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(UPDATE_FAIL);
			status.setData(null);
		}

		return status;
	}

	@Override
	public Status getStatesMap() throws ServiceException {
		Status status = new Status();
		Map<Integer, String> stateMap = MasterServiceApplication.stateMap;
		List<State> stateList = new ArrayList<State>();
		for (Map.Entry<Integer, String> entry : stateMap.entrySet()) {
			State productObj = new State();
			productObj.setStateId(entry.getKey());
			productObj.setState(entry.getValue());
			stateList.add(productObj);
		}

		if (!stateList.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(stateList);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(null);
		}
		return status;
	}

	@Override
	public Status getCategoryMap() throws ServiceException {
		Status status = new Status();
		//Map<String, String> categoryMap = MasterServiceApplication.categoryMap;

		List<Category> categoryList = new ArrayList<Category>();
	//	for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
		/*
		 * for (Map.Entry<String, String> entry : categoryMap.entrySet()) { Category
		 * productObj = new Category(); productObj.setCategoryId(entry.getKey());
		 * productObj.setCategory(entry.getValue()); categoryList.add(productObj); }
		 */
		if (!categoryList.isEmpty()) {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(categoryList);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(null);
		}
		return status;
	}

	@Override
	public Status getAllSubcategoryAgainstCatId(Integer categoryId) throws ServiceException {
		Status status = new Status();

	//	Map<Integer, String> subCategoryMap = MasterServiceApplication.subCategoryMap.get(categoryId);
		//Map<String, String> subCategoryMap = MasterServiceApplication.subCategoryMap.get(categoryId);
		
		List<SubCategory> categoryList = new ArrayList<SubCategory>();
		/*
		 * if (subCategoryMap != null) {
		 * 
		 * for (Map.Entry<String, String> entry : subCategoryMap.entrySet()) {
		 * SubCategory productObj = new SubCategory();
		 * productObj.setSubCategoryId(entry.getKey());
		 * productObj.setSubCategory(entry.getValue()); categoryList.add(productObj); }
		 * }
		 */

		if (!categoryList.isEmpty()) {

			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_FOUND);
			status.setData(categoryList);

		} else {
			status = ltMastCommonMessageService.getCodeAndMessage(RECORD_NOT_FOUND);
			status.setData(null);
		}
		return status;
	}
}
