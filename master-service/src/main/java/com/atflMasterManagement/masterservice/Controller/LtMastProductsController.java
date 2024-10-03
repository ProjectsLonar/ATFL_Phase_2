package com.atflMasterManagement.masterservice.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atflMasterManagement.masterservice.common.BusinessException;
import com.atflMasterManagement.masterservice.common.ServiceException;
import com.atflMasterManagement.masterservice.model.CodeMaster;
import com.atflMasterManagement.masterservice.model.RequestDto;
import com.atflMasterManagement.masterservice.model.Status;
import com.atflMasterManagement.masterservice.servcies.LtMastProductService;

@RestController
@RequestMapping(value = "/products")
public class LtMastProductsController implements CodeMaster {

	@Autowired
	private LtMastProductService ltMastProductService;

	@RequestMapping(value = "/getProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getProduct(@RequestBody RequestDto requestDto) {
		try {
			return new ResponseEntity<Status>(ltMastProductService.getProduct(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v2.0")
	public ResponseEntity<Status> getProductV2(@RequestBody RequestDto requestDto) {
		try {
			return new ResponseEntity<Status>(ltMastProductService.getProductV2(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/readImageProduct", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> readImageProduct() throws ServiceException, IOException {
		try {
			return new ResponseEntity<Status>(ltMastProductService.readImageProduct(), HttpStatus.OK);
		} catch (Exception e) {

			throw new BusinessException(INTERNAL_SERVER_ERROR, "FAQ not found", e);
		}
	}
	
	@RequestMapping(value = "/getInStockProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getInStockProduct(@RequestBody RequestDto requestDto) {
		try {
			
			System.out.print("In Controller getInStockProduct");
			return new ResponseEntity<Status>(ltMastProductService.getInStockProduct(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getInStockProductV1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getInStockProductV1(@RequestBody RequestDto requestDto) {
		try {
			
			System.out.print("In Controller getInStockProduct");
			return new ResponseEntity<Status>(ltMastProductService.getInStockProductV1(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getInStockProductV2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getInStockProductV2(@RequestBody RequestDto requestDto) {
		try {
			
			System.out.print("In Controller getInStockProduct");
			return new ResponseEntity<Status>(ltMastProductService.getInStockProductV2(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getOutOfStockProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOutOfStockProduct(@RequestBody RequestDto requestDto) {
		try {
			return new ResponseEntity<Status>(ltMastProductService.getOutOfStockProduct(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getOutOfStockProductV1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOutOfStockProductV1(@RequestBody RequestDto requestDto) {
		try {
			return new ResponseEntity<Status>(ltMastProductService.getOutOfStockProductV1(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getOutOfStockProductV2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getOutOfStockProductV2(@RequestBody RequestDto requestDto) {
		try {
			return new ResponseEntity<Status>(ltMastProductService.getOutOfStockProductV2(requestDto), HttpStatus.OK);
		} catch (Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@RequestMapping(value = "/getMultipleMrpForProduct/{distId}/{outId}/{prodId}/{priceList}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getMultipleMrpForProduct(@PathVariable("distId") String distId, 
			@PathVariable("outId") String outId, @PathVariable("prodId") String prodId, @PathVariable("priceList") String priceList) throws ServiceException, FileNotFoundException, IOException 
	{
		return new ResponseEntity<Status>(ltMastProductService.getMultipleMrpForProduct(distId, outId, prodId, priceList), HttpStatus.OK);	
	}
	
	
	@RequestMapping(value = "/getMultipleMrpForProductV1/{prodId}/{distId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,headers = "X-API-Version=v1.0")
	public ResponseEntity<Status> getMultipleMrpForProductV1(@PathVariable("prodId") String prodId, 
			@PathVariable("distId") String distId) throws ServiceException, FileNotFoundException, IOException 
	{
		return new ResponseEntity<Status>(ltMastProductService.getMultipleMrpForProductV1(prodId,distId), HttpStatus.OK);	
	}
	
	@RequestMapping(value= "/getTlForProductDescription/{priceList}/{productId}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers= "X-API-Version=v1.0")
      public ResponseEntity<Status>  getTlForProductDescription(@PathVariable("priceList") String priceList, 
    		                                                    @PathVariable("productId") String productId) throws ServiceException, IOException{	
	    try {
		       return new ResponseEntity<Status>(ltMastProductService.getTlForProductDescription(priceList,productId), HttpStatus.OK);
	        }
	    catch(Exception e) {
	    	throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
	        }
	    }
	
	@RequestMapping(value= "/getEtlForProductDescription/{priceList}/{productId}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, headers= "X-API-Version=v1.0")
    public ResponseEntity<Status>  getEtlForProductDescription(@PathVariable("priceList") String priceList, 
  		                                                    @PathVariable("productId") String productId) throws ServiceException, IOException{	
	    try {
		       return new ResponseEntity<Status>(ltMastProductService.getEtlForProductDescription(priceList,productId), HttpStatus.OK);
	        }
	    catch(Exception e) {
	    	throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
	        }
	    }
	
	@PostMapping(value = "/getInstockProductsForSysAdminTemplate", consumes = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0" )
	public ResponseEntity<Status> getInstockProductsForSysAdminTemplate(@RequestBody RequestDto requestDto){
		try {
			return new ResponseEntity<Status>(ltMastProductService.getInstockProductsForSysAdminTemplate(requestDto), HttpStatus.OK);
		}
		catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
	@PostMapping(value = "/getOutOfStockProductsForSysAdminTemplate", consumes = MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0" )
	public ResponseEntity<Status> getOutOfStockProductsForSysAdminTemplate(@RequestBody RequestDto requestDto){
		try {
			return new ResponseEntity<Status>(ltMastProductService.getOutOfStockProductsForSysAdminTemplate(requestDto), HttpStatus.OK);
		}
		catch(Exception e) {
			throw new BusinessException(INTERNAL_SERVER_ERROR, null, e);
		}
	}
	
}
 