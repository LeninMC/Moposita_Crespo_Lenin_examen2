package fisei.vasconez.kotlin_carritocompras.activities.Client.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.models.ProductMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB

class ClientProductsDetailActivityMCLB : AppCompatActivity() {

    val  TAG = "ClientProductsDetail"
    var productMCLB : ProductMCLB? = null
    val gson = Gson()

    var imageSlider : ImageSlider? = null
    var textViewName: TextView? = null
    var textViewDescripcion: TextView? = null
    var textViewPrecio: TextView? = null
    var textViewCounter: TextView? = null
    var imageViewAdd : ImageView? = null
    var imageViewRemove: ImageView? = null
    var buttonAdd : Button ? = null
    var counter = 1
    var productPrice = 0.0

    var sharedPrefMCLB : SharedPrefMCLB? = null
    var selectedProducts  = ArrayList<ProductMCLB>()






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail_mclb)



        //TODO Transformo el producto enviado de el Products Adapter en objeto manejable
        productMCLB = gson.fromJson(intent.getStringExtra("product"), ProductMCLB::class.java)
        sharedPrefMCLB = SharedPrefMCLB(this )


        imageSlider = findViewById(R.id.imageslider)
        textViewName = findViewById(R.id.textview_nombredetalle)
        textViewDescripcion = findViewById(R.id.textview_descripciondetalle)
        textViewPrecio = findViewById(R.id.textview_precio)
        textViewCounter = findViewById(R.id.textview_counter)
        imageViewAdd = findViewById(R.id.imageview_add)
        imageViewRemove = findViewById(R.id.imageview_remove)
        buttonAdd = findViewById(R.id.btn_add_product)



        val imagesList = ArrayList<SlideModel> ()
        imagesList.add(SlideModel(productMCLB?.image1, ScaleTypes.CENTER_CROP))
        imagesList.add(SlideModel(productMCLB?.image2, ScaleTypes.CENTER_CROP))
        imagesList.add(SlideModel(productMCLB?.image3, ScaleTypes.CENTER_CROP))

        imageSlider?.setImageList(imagesList)

        //TODO Asignacion de datos a los TextView de la Activity
        textViewName?.text = productMCLB?.nombre
        textViewDescripcion?.text = productMCLB?.descripcion
        textViewPrecio?.text = "${productMCLB?.precioUnitario}"


        imageViewAdd?.setOnClickListener{addItemMCLB()}
        imageViewRemove?.setOnClickListener{removeItemMCLB()}
        buttonAdd?.setOnClickListener{addToBagMCLB()}

        getProductsFromSharedPrefMCLB()


    }

    /*
    *    Funcion para agregar al carrito de compras
     */
    private  fun addToBagMCLB(){
        val index = getIndexOfMCLB(productMCLB?.id!!) //indice del pregunto si existe en el sharedPref
        if(index == -1 ){
            if(productMCLB?.quantity == 0 ){
                productMCLB?.quantity = 1
            }
            selectedProducts.add(productMCLB!!)
        }
        else { // si exite el producto debemos editar la cantidad
            selectedProducts[index].quantity = counter
        }

        sharedPrefMCLB?.save("order", selectedProducts)
        Toast.makeText(this, "Producto Agregado 🛒", Toast.LENGTH_SHORT).show()
    }

    /*
    *   TODO Obtener el sharedPref  los datos
     */
    private  fun getProductsFromSharedPrefMCLB(){
        Log.d("VerDATA", "${sharedPrefMCLB?.getData("order")}")
        if(!sharedPrefMCLB?.getData("order").isNullOrBlank()){ //Si existe
            val type  = object: TypeToken<ArrayList<ProductMCLB>>() {}.type //transfoma la lista JSON en una Array de Products
            selectedProducts  = gson.fromJson(sharedPrefMCLB?.getData("order"), type)
            val index = getIndexOfMCLB(productMCLB?.id!!)
            if(index != -1 ){
                //Establecemos valores en TextView
                productMCLB?.quantity = selectedProducts[index].quantity
                textViewCounter?.text = "${productMCLB?.quantity}"
                counter = selectedProducts[index].quantity!! //
                //Establecemos valores en TextView
                productPrice = productMCLB?.precioUnitario!! * productMCLB?.quantity!!
                textViewPrecio?.text = "${productPrice}"

                buttonAdd?.setText("Editar Producto")
                buttonAdd?.backgroundTintList = (ColorStateList.valueOf(Color.RED))
            }

            for (p in selectedProducts){
                Log.d(TAG, " La data de Shared Pref ${p}")
            }
        }
    }
    /*
    *   Obtener el indice del producto seleccionado en la bolsa
     */
    private  fun getIndexOfMCLB(idProduct : String): Int{
        var pos = 0

        for ( p in selectedProducts){
            if(p.id == idProduct){
                return pos
            }

            pos++
        }
        return -1
    }

    /*
    *   Aumentar cantidad al producto
     */

    private fun addItemMCLB (){
        Log.d(TAG, " La Cantidad  ${counter}")
         counter ++
        productPrice = productMCLB?.precioUnitario!! * counter
        productMCLB?.quantity  = counter
        textViewCounter?.text = "${productMCLB?.quantity}"
        textViewPrecio?.text = " $ ${productPrice}"
        Log.d(TAG, " La Cantidad Aumentada ${counter}")
    }

    /*
    *   Disminuir cantidad al producto
     */

    private fun removeItemMCLB (){
        if(counter > 1){
            counter --
            productPrice = productMCLB?.precioUnitario!! * counter
            productMCLB?.quantity  = counter
            textViewCounter?.text = "${productMCLB?.quantity}"
            textViewPrecio?.text = " $ ${productPrice}"
        }

    }




}