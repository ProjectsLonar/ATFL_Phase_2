//package com.lonar.cartservice.atflCartService.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.lonar.cartservice.atflCartService.model.EmailRequest;
//import com.lonar.cartservice.atflCartService.service.EmailService;
//
//@RestController
//@RequestMapping(value = "/email")
//public class EmailController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @PostMapping(value="/send-email", produces= MediaType.APPLICATION_JSON_VALUE, headers = "X-API-Version=v1.0")
//    public void sendEmail(@RequestBody EmailRequest emailRequest) {
//        emailService.sendSimpleMessage(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
//    }
//   
////    @PostMapping("/sendEmail")   //this api is for testing purpose
////    public void sendEmail1(@RequestBody EmailRequest emailRequest) {
////        emailService.sendSimpleMessage1(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
////    }
// 
//}
