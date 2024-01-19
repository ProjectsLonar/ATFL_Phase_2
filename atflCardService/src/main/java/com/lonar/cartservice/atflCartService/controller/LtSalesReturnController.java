package com.lonar.cartservice.atflCartService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lonar.cartservice.atflCartService.model.CodeMaster;
import com.lonar.cartservice.atflCartService.service.LtSalesReturnService;

@RestController
@RequestMapping(value = "/ltsalesreturn")
public class LtSalesReturnController implements  CodeMaster {

	@Autowired
	LtSalesReturnService ltSalesReturnService;
}
