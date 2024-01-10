/*
 * package com.lonar.cartservice.atflCartService.common;
 * 
 * import java.text.SimpleDateFormat; import java.util.List; import
 * java.util.Map;
 * 
 * import javax.mail.internet.AddressException; import
 * javax.mail.internet.InternetAddress;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.core.env.Environment;
 * 
 * import com.lonar.cartservice.atflCartService.AtflCartServiceApplication;
 * import com.lonar.cartservice.atflCartService.dto.OrderDetailsDto; import
 * com.lonar.cartservice.atflCartService.dto.SoHeaderDto; import
 * com.lonar.cartservice.atflCartService.dto.SoLineDto; import
 * com.lonar.cartservice.atflCartService.model.CodeMaster; import
 * com.lonar.cartservice.atflCartService.model.LtMastUsers; import
 * com.lonar.cartservice.atflCartService.model.Mail;
 * 
 * public class SendEmail implements CodeMaster {
 * 
 * 
 * @Autowired Mailer mailer;
 * 
 * @Autowired private Environment env;
 * 
 * public Mail getMailDetailsforCustomer(OrderDetailsDto orderDetailsDto, String
 * filePath, LtMastUsers ltMastUser) { SimpleDateFormat dateFormat = new
 * SimpleDateFormat("dd MMM, yyyy");
 * 
 * String mailSubject = ""; String mailBody = ""; Map<String, String>
 * configPropertyMap = AtflCartServiceApplication.configMap
 * .get(Long.parseLong(env.getProperty("OrgId"))); String emailUserName = "";
 * 
 * if (configPropertyMap != null) { emailUserName =
 * configPropertyMap.get("EMAIL_USERNAME_SALE"); Mail mail = new Mail(); try {
 * mail.setMailFrom(new InternetAddress(emailUserName)); } catch
 * (AddressException e1) { // TODO Auto-generated catch block
 * e1.printStackTrace(); } if (ltMastUser.getEmail() == null ||
 * ltMastUser.getEmail().isEmpty()) { return null; } else {
 * mail.setMailTo(ltMastUser.getEmail());
 * 
 * String argMail = " "; try { List<SoHeaderDto> soHeaderDtolist =
 * orderDetailsDto.getSoHeaderDto();
 * 
 * if (argMail.equals(argMail)) { Double totalAmount =
 * soHeaderDtolist.get(0).getTotalAmount() == null ?
 * soHeaderDtolist.get(0).getToPayAmount() :
 * soHeaderDtolist.get(0).getTotalAmount(); Double discountAmount =
 * soHeaderDtolist.get(0).getDiscountAmount() == null ? 0.0 :
 * soHeaderDtolist.get(0).getDiscountAmount(); Double deliveryFeeCharges =
 * soHeaderDtolist.get(0).getDeliveryFeeCharges() == null ? 0.0 :
 * soHeaderDtolist.get(0).getDeliveryFeeCharges();
 * 
 * String imagePath = env.getProperty("basePath") +
 * env.getProperty("imageMailpath");
 * 
 * //System.out.println("imagePath"+imagePath); mailBody =
 * mailBody.replace("${totalAmount}", totalAmount.toString()); mailBody =
 * mailBody.replace("${discount}", discountAmount.toString()); mailBody =
 * mailBody.replace("${totalToPay}", totalAmount.toString()); mailBody =
 * mailBody.replace("${deliveryFeeCharges}", deliveryFeeCharges.toString());
 * 
 * mailBody = mailBody.replace("${orderNumber}",
 * soHeaderDtolist.get(0).getOrderNumber()); mailSubject =
 * mailSubject.replace("${orderNumber}",
 * soHeaderDtolist.get(0).getOrderNumber()); mailBody =
 * mailBody.replace("${orderDate}",
 * dateFormat.format(soHeaderDtolist.get(0).getOrderDate())); mailBody =
 * mailBody.replace("${userName}", soHeaderDtolist.get(0).getCustomerName());
 * mailBody = mailBody.replace("${deliveryAdress}",
 * soHeaderDtolist.get(0).getDeliveryAddress()); mailBody =
 * mailBody.replace("${imagePath}", imagePath); mailBody =
 * mailBody.replace("${paymentMethod}",
 * soHeaderDtolist.get(0).getPaymentMethod()); mailBody =
 * mailBody.replace("${cancelledBy}", soHeaderDtolist.get(0).getCustomerName());
 * mailBody = mailBody.replace("${outletName}",
 * soHeaderDtolist.get(0).getOutletName()); mailBody =
 * mailBody.replace("${mobileNumber}", ltMastUser.getMobileNumber()); mailBody =
 * mailBody.replace("${userMailId}", ltMastUser.getEmail());
 * 
 * String tablHtml = new String(); List<SoLineDto> soLineDtoList =
 * soHeaderDtolist.get(0).getSoLineDtoList(); for (SoLineDto soLineDto :
 * soLineDtoList) { // tablHtml = tablHtml + "<table border=\"1\">"; // tablHtml
 * = tablHtml + "<tr colspan = '5'>Date: " + workingDate + "</tr>"; tablHtml =
 * tablHtml+"  <tr>\r\n" +
 * "                                                        <td style=\"color:#636363;border:1px solid #e5e5e5;padding:12px;text-align:left;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;word-wrap:break-word\">\r\n"
 * + "                                                            " +
 * soLineDto.getProductName() +
 * "                                                        </td>\r\n" +
 * "                                                        <td style=\"color:#636363;border:1px solid #e5e5e5;padding:12px;text-align:left;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif\">\r\n"
 * + "                                                            " +
 * soLineDto.getQuantity() + "\r\n" +
 * "                                                        </td>\r\n" +
 * "                                                        <td style=\"color:#636363;border:1px solid #e5e5e5;padding:12px;text-align:left;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif\">\r\n"
 * +
 * "                                                            <span><span>₹</span>"
 * + soLineDto.getPtrPrice() + "</span>\r\n" +
 * "                                                        </td>\r\n" +
 * "                                                    </tr>";
 * 
 * }
 * 
 * mailBody = mailBody.replace("${tableHtml}", tablHtml);
 * mail.setMailSubject(mailSubject); mail.setMailContent(mailBody);
 * 
 * return mail; } else { return null; }
 * 
 * } catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
 * return null; } } return null;
 * 
 * }
 * 
 * public Mail getMailDetailsforOutlet(OrderDetailsDto orderDetailsDto, String
 * filePath, LtMastUsers ltMastUser) { SimpleDateFormat dateFormat = new
 * SimpleDateFormat("dd MMM, yyyy");
 * 
 * String mailSubject = ""; String mailBody = ""; Map<String, String>
 * configPropertyMap = AtflCartServiceApplication.configMap
 * .get(Long.parseLong(env.getProperty("OrgId"))); String emailUserName = "";
 * 
 * if (configPropertyMap != null) { emailUserName =
 * configPropertyMap.get("EMAIL_USERNAME_SALE"); Mail mail = new Mail(); try {
 * mail.setMailFrom(new InternetAddress(emailUserName)); } catch
 * (AddressException e1) { // TODO Auto-generated catch block
 * e1.printStackTrace(); } if (ltMastUser.getEmail() == null ||
 * ltMastUser.getEmail().isEmpty()) { return null; } else {
 * mail.setMailTo(emailUserName); mail.setMailCc(env.getProperty("saleCC"));
 * 
 * //mail.setMailCc(ltMastUser.getEmail()); if
 * (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
 * CANCELLED)) { mailSubject =
 * configPropertyMap.get("EMAIL_OUTLET_CANCELLED_MAILSUB"); mailBody =
 * configPropertyMap.get("EMAIL_OUTLET_CANCELLED_MAILBODY");
 * 
 * // mail.setTemplateName("emailCancelled.vm");
 * 
 * } else { if
 * (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(ORDERED
 * )) { mailSubject = configPropertyMap.get("EMAIL_OUTLET_ODERED_MAILSUB");
 * mailBody = configPropertyMap.get("EMAIL_OUTLET_ODERED_MAILBODY"); }
 * 
 * } String argMail = " "; try { List<SoHeaderDto> soHeaderDtolist =
 * orderDetailsDto.getSoHeaderDto();
 * //System.out.println(soHeaderDtolist.toString());
 * 
 * if (argMail.equals(argMail)) { Double totalAmount =
 * soHeaderDtolist.get(0).getTotalAmount() == null ?
 * soHeaderDtolist.get(0).getToPayAmount() :
 * soHeaderDtolist.get(0).getTotalAmount(); Double discountAmount =
 * soHeaderDtolist.get(0).getDiscountAmount() == null ? 0.0 :
 * soHeaderDtolist.get(0).getDiscountAmount(); Double deliveryFeeCharges =
 * soHeaderDtolist.get(0).getDeliveryFeeCharges() == null ? 0.0 :
 * soHeaderDtolist.get(0).getDeliveryFeeCharges();
 * 
 * 
 * String imagePath = env.getProperty("basePath") +
 * env.getProperty("imageMailpath"); mailBody =
 * mailBody.replace("${totalAmount}", totalAmount.toString()); mailBody =
 * mailBody.replace("${discount}", discountAmount.toString()); mailBody =
 * mailBody.replace("${totalToPay}", totalAmount.toString()); mailBody =
 * mailBody.replace("${deliveryFeeCharges}", deliveryFeeCharges.toString());
 * 
 * mailBody = mailBody.replace("${orderNumber}",
 * soHeaderDtolist.get(0).getOrderNumber()); mailSubject =
 * mailSubject.replace("${orderNumber}",
 * soHeaderDtolist.get(0).getOrderNumber()); mailBody =
 * mailBody.replace("${orderDate}",
 * dateFormat.format(soHeaderDtolist.get(0).getOrderDate())); mailBody =
 * mailBody.replace("${userName}", soHeaderDtolist.get(0).getCustomerName());
 * mailBody = mailBody.replace("${deliveryAdress}",
 * soHeaderDtolist.get(0).getDeliveryAddress()); mailBody =
 * mailBody.replace("${imagePath}", imagePath); mailBody =
 * mailBody.replace("${paymentMethod}",
 * soHeaderDtolist.get(0).getPaymentMethod());
 * if(soHeaderDtolist.get(0).getUserName()!=null &&
 * mailBody.contains("${cancelledBy}")) { mailBody =
 * mailBody.replace("${cancelledBy}", soHeaderDtolist.get(0).getUserName());
 * }mailBody = mailBody.replace("${outletName}",
 * soHeaderDtolist.get(0).getOutletName()); mailBody =
 * mailBody.replace("${mobileNumber}", ltMastUser.getMobileNumber()); mailBody =
 * mailBody.replace("${userMailId}", ltMastUser.getEmail());
 * 
 * String tablHtml = new String(); List<SoLineDto> soLineDtoList =
 * soHeaderDtolist.get(0).getSoLineDtoList(); for (SoLineDto soLineDto :
 * soLineDtoList) { // tablHtml = tablHtml + "<table border=\"1\">"; // tablHtml
 * = tablHtml + "<tr colspan = '5'>Date: " + workingDate + "</tr>"; tablHtml =
 * tablHtml+"  <tr>\r\n" +
 * "                                                        <td style=\"color:#636363;border:1px solid #e5e5e5;padding:12px;text-align:left;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;word-wrap:break-word\">\r\n"
 * + "                                                            " +
 * soLineDto.getProductName() +
 * "                                                        </td>\r\n" +
 * "                                                        <td style=\"color:#636363;border:1px solid #e5e5e5;padding:12px;text-align:left;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif\">\r\n"
 * + "                                                            " +
 * soLineDto.getQuantity() + "\r\n" +
 * "                                                        </td>\r\n" +
 * "                                                        <td style=\"color:#636363;border:1px solid #e5e5e5;padding:12px;text-align:left;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif\">\r\n"
 * +
 * "                                                            <span><span>₹</span>"
 * + soLineDto.getPtrPrice() + "</span>\r\n" +
 * "                                                        </td>\r\n" +
 * "                                                    </tr>";
 * 
 * }
 * 
 * mailBody = mailBody.replace("${tableHtml}", tablHtml);
 * mail.setMailSubject(mailSubject); mail.setMailContent(mailBody);
 * 
 * return mail; } else { return null; }
 * 
 * } catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
 * return null; } } return null;
 * 
 * }
 * 
 * 
 * public Mail getMailDetails(OrderDetailsDto orderDetailsDto, String filePath,
 * LtMastUsers ltMastUser) { SimpleDateFormat dateFormat = new
 * SimpleDateFormat("dd MMM, yyyy"); String emailUserName = ""; Map<String,
 * String> configPropertyMap = AtflCartServiceApplication.configMap
 * .get(Long.parseLong(env.getProperty("OrgId"))); if (configPropertyMap !=
 * null) { emailUserName = configPropertyMap.get("EMAIL_USERNAME_SALE"); }
 * Status status = new Status();
 * 
 * try { List<SoHeaderDto> soHeaderDtolist = orderDetailsDto.getSoHeaderDto();
 * System.out.println(soHeaderDtolist.toString());
 * 
 * Mail mail = new Mail(); mail.setMailFrom(new InternetAddress(emailUserName));
 * mail.setMailTo("priyanka.patil@lonartech.com"); if
 * (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
 * CANCELLED)) { mail.setMailSubject( "Your NutsnFlakes order #" +
 * soHeaderDtolist.get(0).getOrderNumber() + " has been cancelled!");
 * mail.setTemplateName("emailCancelled.vm");
 * 
 * } else if
 * (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
 * DELIVERED)) { mail.setMailSubject( "Your NutsnFlakes order #" +
 * soHeaderDtolist.get(0).getOrderNumber() + " has been delivered!");
 * mail.setTemplateName("emailtemplate.vm");
 * 
 * } else { mail.setMailSubject("Your NutsnFlakes order has been received!");
 * mail.setTemplateName("emailtemplate.vm");
 * 
 * } mail.setAttachment(filePath); VelocityEngine velocityEngine =
 * mailer.getVelocityEngine(); VelocityContext velocityContext = new
 * VelocityContext(); velocityContext.put("userName",
 * soHeaderDtolist.get(0).getCustomerName()); velocityContext.put("orderNumber",
 * soHeaderDtolist.get(0).getOrderNumber()); velocityContext.put("orderDate",
 * dateFormat.format(soHeaderDtolist.get(0).getOrderDate())); String fileName =
 * "Invoice"; Template template = velocityEngine.getTemplate("./templates/" +
 * mail.getTemplateName()); StringWriter stringWriter = new StringWriter();
 * 
 * template.merge(velocityContext, stringWriter);
 * mail.setMailContent(stringWriter.toString()); return mail; } catch (Exception
 * e) { // TODO: handle exception e.printStackTrace(); return null; } }
 * 
 * 
 * public Status sendMail(OrderDetailsDto orderDetailsDto, String filePath,
 * LtMastUsers ltMastUser) throws ServiceException { //
 * System.out.println("sendMail class--spplier Id"+orgId); SimpleDateFormat
 * dateFormat = new SimpleDateFormat("dd MMM, yyyy"); String emailUserName = "";
 * Map<String, String> configPropertyMap = AtflCartServiceApplication.configMap
 * .get(Long.parseLong(env.getProperty("OrgId"))); if (configPropertyMap !=
 * null) { emailUserName = configPropertyMap.get("EMAIL_USERNAME_SALE"); }
 * Status status = new Status();
 * 
 * try { List<SoHeaderDto> soHeaderDtolist = orderDetailsDto.getSoHeaderDto();
 * System.out.println(soHeaderDtolist.toString());
 * 
 * Mail mail = new Mail(); mail.setMailFrom(new InternetAddress(emailUserName));
 * mail.setMailTo("priyanka.patil@lonartech.com"); if
 * (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
 * CANCELLED)) { mail.setMailSubject( "Your NutsnFlakes order #" +
 * soHeaderDtolist.get(0).getOrderNumber() + " has been cancelled!");
 * mail.setTemplateName("emailCancelled.vm");
 * 
 * } else if
 * (orderDetailsDto.getSoHeaderDto().get(0).getStatus().equalsIgnoreCase(
 * DELIVERED)) { mail.setMailSubject( "Your NutsnFlakes order #" +
 * soHeaderDtolist.get(0).getOrderNumber() + " has been delivered!");
 * mail.setTemplateName("emailtemplate.vm");
 * 
 * } else { mail.setMailSubject("Your NutsnFlakes order has been received!");
 * mail.setTemplateName("emailtemplate.vm");
 * 
 * } mail.setAttachment(filePath); VelocityContext velocityContext = new
 * VelocityContext(); velocityContext.put("userName",
 * soHeaderDtolist.get(0).getCustomerName()); velocityContext.put("orderNumber",
 * soHeaderDtolist.get(0).getOrderNumber()); velocityContext.put("orderDate",
 * dateFormat.format(soHeaderDtolist.get(0).getOrderDate())); String fileName =
 * "Invoice"; int result = mailer.sendMail(mail, velocityContext,
 * Long.parseLong(env.getProperty("OrgId")), fileName); if (result == 1) {
 * status.setMessage("Email has been sent successfully");
 * status.setCode(SUCCESS); //
 * ltMastEmailtokenService.updateStatus(ltMastEmailtoken2.getEmailTokenId(), //
 * "SEND", 0L); } else { status.setCode(FAIL);
 * status.setMessage("Fail to send a email"); } } catch (Exception ex) {
 * ex.printStackTrace(); } // } return status; }
 * 
 * 
 * }
 */