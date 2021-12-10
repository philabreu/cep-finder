package com.cepfinder.service

import com.cepfinder.model.Address
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.client.RestTemplate

@Service
class CepfinderService {

    @Value("https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/")
    private lateinit var url: String

    fun find(@PathVariable("cep") cep: String): Address {
        return RestTemplate().getForObject(url + cep, Address::class.java)!!
    }
}
