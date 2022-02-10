package fisei.vasconez.kotlin_carritocompras.activities.Client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.adapters.ProductsAdapterMCLB
import fisei.vasconez.kotlin_carritocompras.models.ProductMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import fisei.vasconez.kotlin_carritocompras.providers.ProductsProviderMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivityMCLB : AppCompatActivity() {

    val TAG = "ProductsListActivity"
    var recyclerViewProducts : RecyclerView? = null
    var adapterMCLB : ProductsAdapterMCLB? = null
    var userMCLB : UserMCLB? =null
    var sharedPrefMCLB : SharedPrefMCLB ? = null
    var productsProviderMCLB : ProductsProviderMCLB? =null

    //TODO Recibir los datos de la activity padre
    var idCategory : String  ? = null


    var productMCLBS : ArrayList<ProductMCLB> = ArrayList()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list_mclb)

        sharedPrefMCLB = SharedPrefMCLB(this)
        //TODO Recibir los datos de la activity padre
        idCategory = intent.getStringExtra("idcategoria")
        getUserFromSessionMCLB()
        productsProviderMCLB = ProductsProviderMCLB(userMCLB?.session_token!!)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = GridLayoutManager(this, 2)//este forma la pantalla en grid de columnas de 2
        getProductsMCLB()

    }

    private fun getProductsMCLB (){
        Log.d(TAG, "error IDcATEGORIA : $idCategory")
        productsProviderMCLB?.findByCategoryMCLB(idCategory!!)?.enqueue(object : Callback<ArrayList<ProductMCLB>>{
            override fun onResponse(
                call: Call<ArrayList<ProductMCLB>>,
                response: Response<ArrayList<ProductMCLB>>
            ) {
               if(response.body() != null){
                   productMCLBS = response.body()!!
                   adapterMCLB = ProductsAdapterMCLB(this@ClientProductsListActivityMCLB, productMCLBS)
                   recyclerViewProducts?.adapter = adapterMCLB
                   Log.d(TAG, "Peticion realizada con exito : ${response.body()}")
               }
            }

            override fun onFailure(call: Call<ArrayList<ProductMCLB>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivityMCLB, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "error : ${t.message}")
            }

        })
    }

    /*
*  TODO Obtener la data almacena de Session de SharedPref
*/
    private fun getUserFromSessionMCLB() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getDataMCLB("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            userMCLB = gson.fromJson(sharedPrefMCLB?.getDataMCLB("user"), UserMCLB::class.java)

        }
    }
}