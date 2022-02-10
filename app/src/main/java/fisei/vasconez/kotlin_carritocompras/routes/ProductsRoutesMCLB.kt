package fisei.vasconez.kotlin_carritocompras.routes

import fisei.vasconez.kotlin_carritocompras.models.ProductMCLB
import retrofit2.Call
import retrofit2.http.*

interface ProductsRoutesMCLB {

    @GET("products/getAllMCLB")
    fun getAllMCLB (
        @Header("Authorization") token : String
    ): Call<ArrayList<ProductMCLB>>


    //TODO solo por categoria la peticion
    @GET("products/findByCategoryMCLB/{idcategoria}")
    fun findByCategoryMCLB (
        @Path("idcategoria") idcategoria : String,
        @Header("Authorization") token : String
    ): Call<ArrayList<ProductMCLB>>

}
