package fisei.vasconez.kotlin_carritocompras.api

import fisei.vasconez.kotlin_carritocompras.routes.CategoriesRoutesMCLB
import fisei.vasconez.kotlin_carritocompras.routes.ProductsRoutesMCLB
import fisei.vasconez.kotlin_carritocompras.routes.UsersRoutesMCLB

class ApiRoutesMCLB {
    val API_URL = "https://pruebabackendap.herokuapp.com/api/"
    //Inicializar rutas
    val retrofit = RetrofitClientMCLB()

    fun getUsersRoutes(): UsersRoutesMCLB {
        return retrofit.getClient(API_URL).create(UsersRoutesMCLB::class.java)
    }

    fun getUsersRoutesWithToken(token: String): UsersRoutesMCLB {
        return retrofit.getClientWithToken(API_URL, token).create(UsersRoutesMCLB::class.java)
    }


    fun getCategoriesRoutes(token : String) : CategoriesRoutesMCLB{
        return  retrofit.getClientWithToken(API_URL, token).create(CategoriesRoutesMCLB::class.java)
    }
    fun getProductsRoutes(token : String) : ProductsRoutesMCLB{
        return  retrofit.getClientWithToken(API_URL, token).create(ProductsRoutesMCLB::class.java)
    }
}