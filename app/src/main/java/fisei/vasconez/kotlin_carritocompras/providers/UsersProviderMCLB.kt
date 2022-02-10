package fisei.vasconez.kotlin_carritocompras.providers

import fisei.vasconez.kotlin_carritocompras.api.ApiRoutesMCLB
import fisei.vasconez.kotlin_carritocompras.models.ResponseHttpMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import fisei.vasconez.kotlin_carritocompras.routes.UsersRoutesMCLB
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProviderMCLB(val token : String? = null) {

    private  var usersRoutesMCLB : UsersRoutesMCLB? = null
    private  var usersRoutesMCLBToken : UsersRoutesMCLB? = null

    init {
        val api = ApiRoutesMCLB()
        usersRoutesMCLB = api.getUsersRoutes()
        if(token != null ){
            usersRoutesMCLBToken = api.getUsersRoutesWithToken(token!!)
        }

    }

    fun register (userMCLB : UserMCLB): Call<ResponseHttpMCLB>?{
        return usersRoutesMCLB?.register(userMCLB)
    }

    fun login (email : String , password : String ): Call<ResponseHttpMCLB>?{
        return usersRoutesMCLB?.login(email, password)
    }

    fun update (file : File, userMCLB : UserMCLB): Call<ResponseHttpMCLB>?{
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file )
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), userMCLB.toJson())

        return  usersRoutesMCLBToken?.update(image, requestBody, token!!)
    }

    fun updateWithoutImage (userMCLB : UserMCLB ): Call<ResponseHttpMCLB>?{
        return usersRoutesMCLBToken?.updateWithoutImage(userMCLB, token!!)
    }
}