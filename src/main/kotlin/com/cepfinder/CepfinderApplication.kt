package com.cepfinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CepfinderApplication

fun main(args: Array<String>) {
	runApplication<CepfinderApplication>(*args)
}
