package com.srmasset.thcepdetails.ws;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.srmasset.thcepdetails.model.Address;

@Service
public class CepDetailsWsOutbound {

	private static Logger LOGGER = LoggerFactory.getLogger(CepDetailsWsOutbound.class);

	@Value("${api.find.cepdetails}")
	private String trustHubUrl;

	/**
	 * JUSTIFICATIVA: como o spring já possui uma anotação propria e trata-se de um
	 * objeto simples de retorno optei por usa-la.
	 **/
	@Cacheable(value = "address")
	public Address findCepDetails(@PathVariable("cep") String cep) {
		LOGGER.info("calling findCepDetails method in CepDetailsWsOutbound:");
		try {
			LOGGER.info("finding cep {} in external api {}", cep, trustHubUrl);

			RestTemplate restTemplate = new RestTemplate();
			Address address = restTemplate.getForObject(trustHubUrl + cep, Address.class);

			if (address.getEstado() == null) {
				return null;
			}

			return address;
		} catch (HttpServerErrorException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getCause().getMessage());
		}
	}
}
