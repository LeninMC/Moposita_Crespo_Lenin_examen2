package fisei.vasconez.kotlin_carritocompras.providers

import fisei.vasconez.kotlin_carritocompras.api.ApiRoutesMCLB
import fisei.vasconez.kotlin_carritocompras.models.CategoryMCLB
import fisei.vasconez.kotlin_carritocompras.routes.CategoriesRoutesMCLB
import retrofit2.Call

class CategoriesProviderMCLB (val token : String) {
    private  var categoriesRoutesMCLB : CategoriesRoutesMCLB? = null

    init {
        val api  =ApiRoutesMCLB()
        categoriesRoutesMCLB = api.getCategoriesRoutesMCLB(token)
    }
    fun getAllMCLB () : Call<ArrayList<CategoryMCLB>>?{
        return categoriesRoutesMCLB?.getAll(token)
    }
}