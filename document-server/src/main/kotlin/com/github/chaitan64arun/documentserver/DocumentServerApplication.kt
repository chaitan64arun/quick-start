package com.github.chaitan64arun.documentserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DocumentServerApplication

fun main(args: Array<String>) {
	runApplication<DocumentServerApplication>(*args)
}
