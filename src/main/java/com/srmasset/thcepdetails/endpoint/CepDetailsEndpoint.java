package com.srmasset.thcepdetails.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.srmasset.thcepdetails.model.Address;
import com.srmasset.thcepdetails.service.CepDetailsService;

@RestController
@RequestMapping("/cepdetail")
public class CepDetailsEndpoint {

	private static Logger LOGGER = LoggerFactory.getLogger(CepDetailsEndpoint.class);

	@Autowired
	private CepDetailsService service;
	
	@HystrixCommand(fallbackMethod = "fallbackFindCepDetails", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
	@GetMapping("/{cep}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> findCepDetails(@PathVariable String cep) {
		LOGGER.info("calling findCepDetails method in CepDetailsEndpoint:");
		try {
			Address address = service.findCepDetails(cep);

			if (address == null) {
				return ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok(address);
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

	@HystrixCommand(fallbackMethod = "fallbackFindCepList", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> findCepList(@Valid @RequestBody List<String> ceps) {
		LOGGER.info("calling findCepList method in CepDetailsEndpoint:");
		try {
			List<Address> addressList = new ArrayList<>();

			if ((ceps.isEmpty()) || (ceps == null)) {
				return ResponseEntity.notFound().build();
			}

			ceps.forEach(cep -> {
				addressList.add(service.findCepDetails(cep));
			});

			return ResponseEntity.status(HttpStatus.CREATED).body(addressList);
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

	private ResponseEntity<?> fallbackFindCepDetails(@PathVariable String cep) {
		LOGGER.info("Service https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/ is taking a long time to respond.");
		String response = "Request failed. It's taking a long time to respond.";
		return ResponseEntity.badRequest().body(response);
	}

	private ResponseEntity<?> fallbackFindCepList(@Valid @RequestBody List<String> ceps) {
		LOGGER.info("Service https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/ is taking a long time to respond.");
		String response = "Request failed. It's taking a long time to respond.";
		return ResponseEntity.badRequest().body(response);
	}
}
