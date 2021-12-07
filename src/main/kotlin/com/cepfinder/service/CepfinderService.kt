package com.cepfinder.service

import com.cepfinder.model.Address
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.client.RestTemplate
import java.util.Objects.isNull

@Service
class CepfinderService {

    @Value("https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/")
    private lateinit var url: String

    fun find(@PathVariable("cep") cep: String): Address {
        var fullPath = url + cep
        val address = RestTemplate().getForObject(fullPath, Address::class.java)!!

        if (isNull(address.estado)) {
            throw RuntimeException("sem estado cadastrado")
        }

        return address
    }
}
