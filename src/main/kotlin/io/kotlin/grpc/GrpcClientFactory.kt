package io.kotlin.grpc

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory //informa ao kotlin que essa classe é uma factory
class GrpcClientFactory {

    @Singleton
    fun fretesClientStrub(@GrpcChannel("fretes") channel: ManagedChannel): FretesServiceGrpc.FretesServiceBlockingStub? {

        return FretesServiceGrpc.newBlockingStub(channel)
    }
}