package io.kotlin.grpc

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("io.kotlin.grpc")
		.start()
}

