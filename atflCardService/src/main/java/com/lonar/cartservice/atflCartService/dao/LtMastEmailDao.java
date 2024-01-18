package com.lonar.cartservice.atflCartService.dao;

import java.util.List;


import com.lonar.cartservice.atflCartService.common.ServiceException;
import com.lonar.cartservice.atflCartService.model.LtMastEmail;

public interface LtMastEmailDao {

	List<LtMastEmail> saveall(List<LtMastEmail> ltMastEmailToken) throws ServiceException;
}
