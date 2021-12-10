package com.cepfinder.resource

import com.cepfinder.model.Address
import com.cepfinder.service.CepfinderService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Objects.isNull
import javax.validation.Valid

@RestController
@RequestMapping("/find")
class CepfinderResource {

    private val LOGGER = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var service: CepfinderService

    @GetMapping("/{cep}")
    private fun findCepDetails(@PathVariable cep: String): ResponseEntity<Address> {
        val address = service.find(cep)
        if (isNull(address)) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok().body(address)
    }

    @PostMapping
    private fun findCepList(@Valid @RequestBody ceps: List<String>): ResponseEntity<List<Address>> {
        if (ceps.isEmpty()) {
            return ResponseEntity.notFound().build()
        }

        val addressList = arrayListOf<Address>()
        ceps.forEach { cep ->
            addressList.add(service.find(cep))
        }

        return ResponseEntity.ok().body(addressList)
    }

    private fun fallback(@PathVariable cep: String): ResponseEntity<String> {
        LOGGER.info("Service https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/ is taking a long time to respond.")
        return ResponseEntity.badRequest().body("Request failed. It's taking a long time to respond.")
    }

}
