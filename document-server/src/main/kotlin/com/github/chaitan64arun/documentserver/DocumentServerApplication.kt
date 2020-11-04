package com.github.chaitan64arun.documentserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DocumentServerApplication

fun main(args: Array<String>) {
  runApplication<DocumentServerApplication>(*args)
}
