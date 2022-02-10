package fisei.vasconez.kotlin_carritocompras.routes

import fisei.vasconez.kotlin_carritocompras.models.ResponseHttpMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UsersRoutesMCLB {
    @POST("clientes/create")
    fun registerMCLB(@Body userMCLB : UserMCLB): Call<ResponseHttpMCLB>


    @FormUrlEncoded
    @POST("clientes/loginMCLB")
    fun loginMCLB (@Field("email") email : String, @Field("password") password : String)  : Call<ResponseHttpMCLB>


    @Multipart //Para Actualizar data y subir Imagenes
    @PUT("clientes/updateMCLB")
    fun updateMCLB (
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token : String
    ): Call<ResponseHttpMCLB>

    @PUT("clientes/updateWithoutImageMCLB")  //Para actualizar los datos sin la imagen
    fun updateWithoutImageMCLB (
        @Body userMCLB : UserMCLB,
        @Header("Authorization") token : String
    ): Call<ResponseHttpMCLB>
}