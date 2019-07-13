package com.srmasset.thcepdetails.endpoint;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.srmasset.thcepdetails.model.CepDetails;
import com.srmasset.thcepdetails.service.CepDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RestController
@RequestMapping("/cepdetail")
public class CepDetailsEndpoint {

	private static Logger LOGGER = LoggerFactory.getLogger(CepDetailsEndpoint.class);
	
	@Autowired
	private CepDetailsService service;

	@GetMapping("/{cep}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findOne(@PathVariable Long cep) {
		LOGGER.debug("calling findOne method in CepDetailsEndpoint:");
		try {
			CepDetails cepDetails = service.findOne(cep);
			
			return ResponseEntity.ok(cepDetails);
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
