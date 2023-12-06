package com.gaurav.newsapp.webservices

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

sealed class NetworkResult<T>{

    class Success<T>(val data:T):NetworkResult<T>()
    class Error<T>(val code:Int, val message: String?): NetworkResult<T>()
    class Exception<T>(val e:Throwable):NetworkResult<T>()
    class Loading<T>():NetworkResult<T>()
}

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> Response<T>):NetworkResult<T> {

    return withContext(dispatcher){
        try {
            val response = apiCall.invoke()
            val body = response.body()

            if(response.isSuccessful && body != null){
                NetworkResult.Success(body)
            }else{
                NetworkResult.Error(code = response.code(), message = response.errorBody()?.string())
            }

        }catch (e:HttpException){
            NetworkResult.Error(code = e.code(), message = e.message())

        }catch (e: Throwable){
            NetworkResult.Exception(e)
        }
    }
}
