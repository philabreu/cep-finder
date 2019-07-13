package com.srmasset.thcepdetails.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.srmasset.thcepdetails.model.Address;
import com.srmasset.thcepdetails.ws.CepDetailsWsOutbound;

@Service
public class CepDetailsService {

	private static Logger LOGGER = LoggerFactory.getLogger(CepDetailsService.class);

	@Autowired
	private CepDetailsWsOutbound ws;

	public Address findCepDetails(String cep) {
		LOGGER.info("calling findCepDetails method in CepDetailsService:");
		try {
			Address address = ws.findCepDetails(cep);

			return address;
		} catch (HttpClientErrorException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getCause().getMessage());
		} catch (HttpServerErrorException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getCause().getMessage());
		}
	}
}
