package fisei.vasconez.kotlin_carritocompras.routes

import fisei.vasconez.kotlin_carritocompras.models.ResponseHttpMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UsersRoutesMCLB {
    @POST("clientes/create")
    fun register(@Body userMCLB : UserMCLB): Call<ResponseHttpMCLB>


    @FormUrlEncoded
    @POST("clientes/login")
    fun login (@Field("email") email : String , @Field("password") password : String)  : Call<ResponseHttpMCLB>


    @Multipart //Para Actualizar data y subir Imagenes
    @PUT("clientes/update")
    fun update (
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token : String
    ): Call<ResponseHttpMCLB>

    @PUT("clientes/updateWithoutImage")  //Para actualizar los datos sin la imagen
    fun updateWithoutImage (
        @Body userMCLB : UserMCLB,
        @Header("Authorization") token : String
    ): Call<ResponseHttpMCLB>
}