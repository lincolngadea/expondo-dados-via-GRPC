package io.kotlin.grpc

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject

@Controller
class GRpcServerController(@Inject val grpcServer: GrpcEmbeddedServer) {

    //SIMULA UM PROBLEMA NO SERVIÇO DE GRPC... APENAS PARA TESTES
    @Get("/grpc-server/stop")
    fun stop(): HttpResponse<String> {
        grpcServer.stop();
        return HttpResponse.ok("is running? ${grpcServer.isRunning}")
    }
}