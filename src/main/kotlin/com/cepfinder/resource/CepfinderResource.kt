package com.cepfinder.resource

import com.cepfinder.model.Address
import com.cepfinder.service.CepfinderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Objects.isNull

@RestController
@RequestMapping("/find")
class CepfinderResource {

    @Autowired
    private lateinit var service: CepfinderService

    @GetMapping("/{cep}")
    fun findCepDetails(@PathVariable cep: String): ResponseEntity<Address> {
        var address: Address = service.find(cep)

        if(isNull(address)){
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok().body(address)
    }
}