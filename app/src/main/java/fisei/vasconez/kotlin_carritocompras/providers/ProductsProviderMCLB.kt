package fisei.vasconez.kotlin_carritocompras.providers

import fisei.vasconez.kotlin_carritocompras.api.ApiRoutesMCLB
import fisei.vasconez.kotlin_carritocompras.models.ProductMCLB
import fisei.vasconez.kotlin_carritocompras.routes.ProductsRoutesMCLB
import retrofit2.Call

class ProductsProviderMCLB (val token : String) {
    private  var productsRoutesMCLB : ProductsRoutesMCLB? = null

    init {
        val api  =ApiRoutesMCLB()
        productsRoutesMCLB = api.getProductsRoutesMCLB(token)
    }
    fun findByCategoryMCLB (idcategoria : String ) : Call<ArrayList<ProductMCLB>>?{
        return productsRoutesMCLB?.findByCategoryMCLB(idcategoria, token)
    }
}