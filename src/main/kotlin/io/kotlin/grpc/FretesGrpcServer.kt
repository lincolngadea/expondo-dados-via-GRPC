package io.kotlin.grpc

import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.StatusProto
import io.grpc.Status
import io.grpc.protobuf.StatusProto.toStatusRuntimeException
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FretesGrpcServer: FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FretesGrpcServer::class.java)

    override fun calculaFrete(
        request: CalculaFreteRequest?,
        responseObserver: StreamObserver<CalculaFreteResponse>?
    ) {

        logger.info("Calculando Frete para request: $request")

        val cep = request?.cep
        if(cep == null || cep.isBlank()){
            val e = Status.INVALID_ARGUMENT
                .withDescription("cep deve ser informado")
                .asRuntimeException()

            responseObserver?.onError(e)
        }

        if(!cep!!.matches("[0-9]{5}-[0-9]{3}".toRegex())){
            val e = Status.INVALID_ARGUMENT
                .withDescription("cep inválido")
                .augmentDescription("Formato esperado deve ser 99999-999")
                .asRuntimeException()

            responseObserver?.onError(e)
        }

        //SIMULA UMA FALHA DE SEGURANÇA
        if(cep.endsWith("333")){

            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.number)
                .setMessage("usuário não pode acessar esse recurso")
                .addDetails(Any.pack(ErrorDetails.newBuilder()
                    .setCode(401)
                    .setMessage("Token expirado")
                    .build()))
                .build()

            val e = toStatusRuntimeException(statusProto)
            responseObserver?.onError(e)
        }



        var valor = 0.0
        try{
            valor = Random.nextDouble(0.0, 140.0)
            if(valor > 100.0){
                throw IllegalArgumentException("Erro inesperado ao executar uma lógica de negócio!")
            }
        }catch (e: Exception){
            responseObserver?.onError(Status.INTERNAL
                .withDescription(e.message)
                .withCause(e)
                .asRuntimeException()
            )
        }


        val response = CalculaFreteResponse.newBuilder()
            .setCep(request!!.cep)
            .setValor(valor)
            .build()

        logger.info("Frete calculado: $response")


        responseObserver!!.onNext(response)
        responseObserver.onCompleted()

    }
}