//package com.lonar.cartservice.atflCartService.service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//
//import com.lonar.cartservice.atflCartService.common.SendEmail;
//import com.lonar.cartservice.atflCartService.common.ServiceException;
//import com.lonar.cartservice.atflCartService.dao.LtMastEmailDao;
//import com.lonar.cartservice.atflCartService.dao.LtSoHeadersDao;
//import com.lonar.cartservice.atflCartService.dto.OrderDetailsDto;
//import com.lonar.cartservice.atflCartService.dto.RequestDto;
//import com.lonar.cartservice.atflCartService.model.CodeMaster;
//import com.lonar.cartservice.atflCartService.model.LtMastEmail;
//import com.lonar.cartservice.atflCartService.model.LtMastUsers;
//import com.lonar.cartservice.atflCartService.model.Mail;
//import com.lonar.cartservice.atflCartService.model.Status;
//
//@Service
//@PropertySource(value = "classpath:queries/DefaultId.properties", ignoreResourceNotFound = true)
//public class LtMastEmailServiceImpl implements LtMastEmailService, CodeMaster {
//
////	@Autowired
////    SendEmail sendEmail;
//	
//	@Autowired
//	private LtSoHeadersService ltSoHeadersService;
//
//	@Autowired
//	private LtSoHeadersDao ltSoHeadersDao;
//
////	@Autowired
////	private LtMastEmailDao ltMastEmailtokenDao;
//
//	@Autowired
//	private Environment env;
//
////	@Override
////	public Status saveEmailToken(OrderDetailsDto orderDetailsDto) throws InterruptedException, ServiceException {
////		Status status = new Status();
////		try {
////		RequestDto requestDto = new RequestDto();
////			requestDto.setHeaderId(orderDetailsDto.getSoHeaderDto().get(0).getHeaderId());
////			Status st = null;
////
////			if (orderDetailsDto != null) {
////				//OrderDetailsDto orderDetailsDto = (OrderDetailsDto) st.getData();
////
////				//Status statusInvoice = ltMastInvoicesService.createPdfVersion2(requestDto.getHeaderId());
////				
////				//LtMastUsers userDetails = ltSoHeadersDao
////				//		.getUserDetailsByCustomerId(orderDetailsDto.getSoHeaderDto().get(0).getCustomerId());
////				String filePath = "";
////				/*
////				 * if (statusInvoice.getData() != null && userDetails != null) { filePath =
////				 * (String) statusInvoice.getData(); String fileUploadPath =
////				 * env.getProperty("pdfUploadPath");
////				 * 
////				 * if(filePath.contains("/images/invoices/")) { System.out.println("contains");
////				 * filePath=filePath.replace("/images/invoices/",fileUploadPath);
////				 * 
////				 * } System.out.println(filePath); }
////				 */
////				Mail mail = new Mail();
////				Mail mail1 = new Mail();
////				/*
////				 * if (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
////				 * CANCELLED) ||
////				 * orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
////				 * DELIVERED) ||
////				 * orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(ORDERED)
////				 * ) { mail = sendEmail.getMailDetailsforCustomer(orderDetailsDto, filePath,
////				 * userDetails);
////				 * 
////				 * }
////				 */
////				/*
////				 * if (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
////				 * CANCELLED)
////				 * 
////				 * ||
////				 * orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(ORDERED)
////				 * ) { mail1 = sendEmail.getMailDetailsforOutlet(orderDetailsDto, filePath,
////				 * userDetails);
////				 * 
////				 * 
////				 * }
////				 */
////				//mail1 = sendEmail.getMailDetailsforOutlet(orderDetailsDto, filePath);
////				List<LtMastEmail> ltMastEmailTokenList = new ArrayList<LtMastEmail>();
////				LtMastEmail ltMastEmailTokenForCustomer =null;
////				LtMastEmail ltMastEmailTokenForOutlet=null;
////				if (mail.getMailContent() != null)
////				{
////				 ltMastEmailTokenForCustomer = getLtMastEmailToken(mail,
////						orderDetailsDto.getSoHeaderDto().get(0).getOrderNumber());}
////								if (mail1.getMailContent() != null) {
////					 ltMastEmailTokenForOutlet = getLtMastEmailToken(mail1,
////							orderDetailsDto.getSoHeaderDto().get(0).getOrderNumber());
////					
////				}
////				if (ltMastEmailTokenForCustomer != null) {
////					ltMastEmailTokenForCustomer.setEmailTemplate("ORDER");
////					ltMastEmailTokenList.add(ltMastEmailTokenForCustomer);
////
////				}
////				if (ltMastEmailTokenForOutlet != null) {
////					ltMastEmailTokenForOutlet.setEmailTemplate("ORDER");
////					ltMastEmailTokenList.add(ltMastEmailTokenForOutlet);
////
////				}
////
////				List<LtMastEmail> ltMastEmailTokenListOp = ltMastEmailtokenDao.saveall(ltMastEmailTokenList);
////				if (!ltMastEmailTokenListOp.isEmpty()) {
////					status.setCode(SUCCESS);
////					status.setMessage("Record Inserted Successfully.");
////				} else {
////
////					status.setMessage("Fail To Save.");
////					// status = ltMastCommonMessageService.getCodeAndMessage(INSERT_FAIL);
////					status.setCode(INSERT_FAIL);
////
////				}
////			}
////		} catch (ServiceException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			return null;
////		}
////		System.out.println(status.toString());
////		return status;
////	}
////
////	public LtMastEmail getLtMastEmailToken(Mail mail, String orderNumber) {
////		LtMastEmail ltMastEmailToken = new LtMastEmail();
////
////		Long trasid = System.currentTimeMillis();
////
////		ltMastEmailToken.setTransactionId(trasid);
////		if (mail.getAttachment() != null || mail.getAttachment() != "") {
////			ltMastEmailToken.setAttachmentPath(mail.getAttachment());
////
////		}
////
////		ltMastEmailToken.setEmailSubject(mail.getMailSubject());
////		ltMastEmailToken.setSendTo(mail.getMailTo());
////		ltMastEmailToken.setSendCc(mail.getMailCc());
////		ltMastEmailToken.setEmailBody(mail.getMailContent());
////		// ltMastLogins.setMobile(mobileNumber);
////		ltMastEmailToken.setSendDate(new Date());
////		ltMastEmailToken.setEmailStatus("PENDING");
////		ltMastEmailToken.setOrgId(env.getProperty("OrgId"));
////		ltMastEmailToken.setOrderNumber(orderNumber);
////		ltMastEmailToken.setCreationDate(new Date());
////
////		return ltMastEmailToken;
////	}
//}
