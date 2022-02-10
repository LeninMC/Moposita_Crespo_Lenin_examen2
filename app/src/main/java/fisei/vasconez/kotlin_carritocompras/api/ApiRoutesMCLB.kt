package fisei.vasconez.kotlin_carritocompras.api

import fisei.vasconez.kotlin_carritocompras.routes.CategoriesRoutesMCLB
import fisei.vasconez.kotlin_carritocompras.routes.ProductsRoutesMCLB
import fisei.vasconez.kotlin_carritocompras.routes.UsersRoutesMCLB

class ApiRoutesMCLB {
    val API_URL = "https://pruebabackendap.herokuapp.com/api/"
    //Inicializar rutas
    val retrofit = RetrofitClientMCLB()

    fun getUsersRoutesMCLB(): UsersRoutesMCLB {
        return retrofit.getClientMCLB(API_URL).create(UsersRoutesMCLB::class.java)
    }

    fun getUsersRoutesWithTokenMCLB(token: String): UsersRoutesMCLB {
        return retrofit.getClientWithTokenMCLB(API_URL, token).create(UsersRoutesMCLB::class.java)
    }


    fun getCategoriesRoutesMCLB(token : String) : CategoriesRoutesMCLB{
        return  retrofit.getClientWithTokenMCLB(API_URL, token).create(CategoriesRoutesMCLB::class.java)
    }
    fun getProductsRoutesMCLB(token : String) : ProductsRoutesMCLB{
        return  retrofit.getClientWithTokenMCLB(API_URL, token).create(ProductsRoutesMCLB::class.java)
    }
}