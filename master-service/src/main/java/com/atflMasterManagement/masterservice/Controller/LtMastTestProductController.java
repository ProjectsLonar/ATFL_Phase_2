package com.atflMasterManagement.masterservice.Controller;

import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atflMasterManagement.masterservice.common.BusinessException;
import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.LtMastTestProduct;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.servcies.LtMastTestProductService;

@RestController
@RequestMapping(value = "/testProducts")
public class LtMastTestProductController implements CodeMaster {

	@Autowired
	LtMastTestProductService ltMastTestProductService;

	@PostMapping(value = "/saveProduct", consumes = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> saveProduct(@RequestBody LtMastTestProduct ltMastTestProduct) throws ServerException {
		try {
			return new ResponseEntity<Status>(ltMastTestProductService.saveProduct(ltMastTestProduct), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@DeleteMapping(value = "/deleteByProductId/{productId}", produces = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> deleteByProductId(@PathVariable("productId") Long productId) throws ServiceException {
		try {
			return new ResponseEntity<Status>(ltMastTestProductService.deleteByProductId(productId), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@GetMapping(value = "/getAllProducts/{limit}/{offset}", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllProducts(@PathVariable("limit") Long limit,
			@PathVariable("offset") Long offset) throws ServiceException {
		try {
			return new ResponseEntity<Status>(ltMastTestProductService.getAllProducts(limit,offset), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@PostMapping(value = "/updateProductDetails", consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> updateProductDetails(@RequestBody LtMastTestProduct ltMastTestProduct)
			throws ServiceException {
		try {
			return new ResponseEntity<Status>(ltMastTestProductService.updateProductDetails(ltMastTestProduct),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}

	@GetMapping(value = "/getAllState", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getStatesMap() throws ServiceException {
		return new ResponseEntity<Status>(ltMastTestProductService.getStatesMap(), HttpStatus.OK);
	}

	@GetMapping(value = "/getAllCategory", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllCategory() throws ServiceException {
		return new ResponseEntity<Status>(ltMastTestProductService.getCategoryMap(), HttpStatus.OK);
	}

	@GetMapping(value = "/getAllSubcategoryAgainstCatId/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getAllSubcategoryAgainstCatId(@PathVariable("categoryId") Integer categoryId)
			throws ServiceException {
		return new ResponseEntity<Status>(ltMastTestProductService.getAllSubcategoryAgainstCatId(categoryId),
				HttpStatus.OK);
	}
}
