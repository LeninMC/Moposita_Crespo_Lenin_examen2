package fisei.vasconez.kotlin_carritocompras.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.activities.Client.products.detail.ClientProductsDetailActivityMCLB
import fisei.vasconez.kotlin_carritocompras.activities.Client.shopping_bag.ClientShoppingBagActivityMCLB
import fisei.vasconez.kotlin_carritocompras.models.ProductMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB

class ShoppingBagAdapterMCLB(val context: Activity, val productMCLBS: ArrayList<ProductMCLB>): RecyclerView.Adapter<ShoppingBagAdapterMCLB.ShoppingBagViewHolder>() {

    val sharedPref = SharedPrefMCLB(context)

    init {
        (context as ClientShoppingBagActivityMCLB).setTotal(getTotal())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_bag, parent, false)
        return ShoppingBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productMCLBS.size
    }

    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {

        val product = productMCLBS[position] // CADA UNA DE LAS CATEGORIAS

        Log.d("Verlo" , "${product}")
        holder.textViewName.text = product.nombre
        holder.textViewCounter.text = "${product.quantity}"
        holder.textViewPrice.text ="${product.precioUnitario!! * product.quantity!!}"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct) //Establece la imagen
        holder.imageViewAdd.setOnClickListener{addItem(product, holder)}
        holder.imageViewRemove.setOnClickListener{removeItem(product, holder)}
        holder.imageViewDelete.setOnClickListener{ deleteItem(position)}

      //  holder.itemView.setOnClickListener{goToDetail(product)}
    }


    /*
    * Calcular el total
     */
    private  fun getTotal ():Double{
        var total = 0.0
        for (p in productMCLBS){
            total = total + (p.quantity!! * p.precioUnitario!!)
        }
        return  total
    }
    /*
  *   Obtener el indice del producto seleccionado en la bolsa
   */
    private  fun getIndexOf(idProduct : String): Int{
        var pos = 0

        for ( p in productMCLBS){
            if(p.id == idProduct){
                return pos
            }

            pos++
        }
        return -1
    }

    private fun deleteItem(position : Int){
        productMCLBS.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, productMCLBS.size)
        sharedPref.save("order", productMCLBS)
        (context as ClientShoppingBagActivityMCLB).setTotal(getTotal())
    }

    /*
    *   Aumentar cantidad al producto
     */

    private fun addItem (productMCLB : ProductMCLB, holder : ShoppingBagViewHolder){

        val index = getIndexOf(productMCLB.id!!)
        productMCLB.quantity  = productMCLB.quantity!! + 1
        productMCLBS[index].quantity = productMCLB.quantity

        holder.textViewCounter.text = "${productMCLB.quantity}"
        holder.textViewPrice.text = " $ ${productMCLB.quantity!! * productMCLB.precioUnitario!!}"
        sharedPref.save("order", productMCLBS)
        (context as ClientShoppingBagActivityMCLB).setTotal(getTotal())
    }

    /*
    *   Disminuir cantidad al producto
     */

    private fun removeItem (productMCLB : ProductMCLB, holder : ShoppingBagViewHolder){

        if(productMCLB.quantity!! > 1){
            val index = getIndexOf(productMCLB.id!!)
            productMCLB.quantity  = productMCLB.quantity!! - 1
            productMCLBS[index].quantity = productMCLB.quantity

            holder.textViewCounter.text = "${productMCLB.quantity}"
            holder.textViewPrice.text = " $ ${productMCLB.quantity!! * productMCLB.precioUnitario!!}"
            sharedPref.save("order", productMCLBS)
            (context as ClientShoppingBagActivityMCLB).setTotal(getTotal())
        }



    }

    /*
    * Funcion para enviar al activity de detalle del producto
     */
    private fun goToDetail(productMCLB : ProductMCLB){

        val i = Intent(context, ClientProductsDetailActivityMCLB::class.java)
        i.putExtra("product", productMCLB.toJson())
        context.startActivity(i)

    }



    class ShoppingBagViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView
        val imageViewAdd: ImageView
        val imageViewRemove: ImageView
        val imageViewDelete: ImageView
        val textViewCounter: TextView

        init {
            textViewName  = view.findViewById(R.id.textview_nombrebag)
            textViewPrice  = view.findViewById(R.id.textview_pricebag)
            imageViewProduct = view.findViewById(R.id.imageview_productbag)
            imageViewAdd = view.findViewById(R.id.imageview_add)
            imageViewRemove = view.findViewById(R.id.imageview_remove)
            imageViewDelete = view.findViewById(R.id.imageview_delete)
            textViewCounter = view.findViewById(R.id.textview_counter)
        }

    }

}