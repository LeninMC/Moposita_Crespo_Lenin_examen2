package fisei.vasconez.kotlin_carritocompras.activities.Client.shopping_bag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.adapters.ShoppingBagAdapterMCLB
import fisei.vasconez.kotlin_carritocompras.models.ProductMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB

class ClientShoppingBagActivityMCLB : AppCompatActivity() {

    var recyclerViewShoppingBag : RecyclerView? = null
    var textViewTotal : TextView? = null
    var buttonNext : Button? = null
    var toolbar : Toolbar? = null

    var adapterMCLB : ShoppingBagAdapterMCLB? = null
    var sharedPrefMCLB : SharedPrefMCLB? = null
    var gson = Gson()
    var selectedProducts = ArrayList<ProductMCLB>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shooping_bag_mclb)

        sharedPrefMCLB = SharedPrefMCLB(this)
        recyclerViewShoppingBag = findViewById(R.id.recyclerview_shopping_bag)
        textViewTotal = findViewById(R.id.textview_total)
        buttonNext = findViewById(R.id.btn_aceptar)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title = "Tu Order"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Habilitar la Fecha de Regreso a atras



        recyclerViewShoppingBag?.layoutManager = LinearLayoutManager(this)


        getProductsFromSharedPref()
    }

    /*
    *
     */
    fun setTotal (total : Double){
        textViewTotal?.text = "$ ${total}"
    }



    /*
    *   TODO Obtener el sharedPref  los datos
     */
    private  fun getProductsFromSharedPref(){
        if(!sharedPrefMCLB?.getData("order").isNullOrBlank()){ //Si existe
            Log.d("Comprobacion", "${sharedPrefMCLB?.getData("order")}")
            val type  = object: TypeToken<ArrayList<ProductMCLB>>() {}.type //transfoma la lista JSON en una Array de Products
            selectedProducts  = gson.fromJson(sharedPrefMCLB?.getData("order"), type)

            adapterMCLB = ShoppingBagAdapterMCLB(this, selectedProducts)
            recyclerViewShoppingBag?.adapter = adapterMCLB

        }
    }
}