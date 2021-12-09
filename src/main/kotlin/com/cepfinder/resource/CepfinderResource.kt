package com.cepfinder.resource

import com.cepfinder.model.Address
import com.cepfinder.service.CepfinderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Objects.isNull
import javax.validation.Valid

@RestController
@RequestMapping("/find")
class CepfinderResource {

    @Autowired
    private lateinit var service: CepfinderService

    @GetMapping("/{cep}")
    fun findCepDetails(@PathVariable cep: String): ResponseEntity<Address> {
        val address = service.find(cep)
        if (isNull(address)) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok().body(address)
    }

    @PostMapping
    fun findCepList(@Valid @RequestBody ceps: List<String>): ResponseEntity<List<Address>> {
        val addressList = arrayListOf<Address>()

        if(ceps.isEmpty()){
            return ResponseEntity.notFound().build()
        }

        ceps.forEach { cep ->
            addressList.add(service.find(cep))
        }

        return ResponseEntity.ok().body(addressList)
    }
}
