package fisei.vasconez.kotlin_carritocompras.routes

import fisei.vasconez.kotlin_carritocompras.models.CategoryMCLB
import retrofit2.Call
import retrofit2.http.*

interface CategoriesRoutesMCLB {

    @GET("categorias/getAllMCLB")
    fun getAllMCLB (
        @Header("Authorization") token : String
    ): Call<ArrayList<CategoryMCLB>>
}