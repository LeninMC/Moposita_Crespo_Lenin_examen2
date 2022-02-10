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

class ShoppingBagAdapterMCLB(val context: Activity, val productMCLBS: ArrayList<ProductMCLB>): RecyclerView.Adapter<ShoppingBagAdapterMCLB.ShoppingBagViewHolderMCLB>() {

    val sharedPref = SharedPrefMCLB(context)

    init {
        (context as ClientShoppingBagActivityMCLB).setTotal(getTotalMCLB())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolderMCLB {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_bag_mclb, parent, false)
        return ShoppingBagViewHolderMCLB(view)
    }

    override fun getItemCount(): Int {
        return productMCLBS.size
    }

    override fun onBindViewHolder(holder: ShoppingBagViewHolderMCLB, position: Int) {

        val product = productMCLBS[position] // CADA UNA DE LAS CATEGORIAS

        Log.d("Verlo" , "${product}")
        holder.textViewIdProduct.text = product.id
        holder.textViewName.text = product.nombre
        holder.textViewCounter.text = "${product.quantity}"
        holder.textViewPrice.text ="${product.precioUnitario!! * product.quantity!!}"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct) //Establece la imagen
        holder.imageViewAdd.setOnClickListener{addItemMCLB(product, holder)}
        holder.imageViewRemove.setOnClickListener{removeItemMCLB(product, holder)}
        holder.imageViewDelete.setOnClickListener{ deleteItemMCLB(position)}

      //  holder.itemView.setOnClickListener{goToDetail(product)}
    }


    /*
    * Calcular el total
     */
    private  fun getTotalMCLB ():Double{
        var total = 0.0
        for (p in productMCLBS){
            total = total + (p.quantity!! * p.precioUnitario!!)
        }
        return  total
    }
    /*
  *   Obtener el indice del producto seleccionado en la bolsa
   */
    private  fun getIndexOfMCLB(idProduct : String): Int{
        var pos = 0

        for ( p in productMCLBS){
            if(p.id == idProduct){
                return pos
            }

            pos++
        }
        return -1
    }

    private fun deleteItemMCLB(position : Int){
        productMCLBS.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, productMCLBS.size)
        sharedPref.saveMCLB("order", productMCLBS)
        (context as ClientShoppingBagActivityMCLB).setTotal(getTotalMCLB())
    }

    /*
    *   Aumentar cantidad al producto
     */

    private fun addItemMCLB (productMCLB : ProductMCLB, holder : ShoppingBagViewHolderMCLB){

        val index = getIndexOfMCLB(productMCLB.id!!)
        productMCLB.quantity  = productMCLB.quantity!! + 1
        productMCLBS[index].quantity = productMCLB.quantity

        holder.textViewCounter.text = "${productMCLB.quantity}"
        holder.textViewPrice.text = " $ ${productMCLB.quantity!! * productMCLB.precioUnitario!!}"
        sharedPref.saveMCLB("order", productMCLBS)
        (context as ClientShoppingBagActivityMCLB).setTotal(getTotalMCLB())
    }

    /*
    *   Disminuir cantidad al producto
     */

    private fun removeItemMCLB (productMCLB : ProductMCLB, holder : ShoppingBagViewHolderMCLB){

        if(productMCLB.quantity!! > 1){
            val index = getIndexOfMCLB(productMCLB.id!!)
            productMCLB.quantity  = productMCLB.quantity!! - 1
            productMCLBS[index].quantity = productMCLB.quantity

            holder.textViewCounter.text = "${productMCLB.quantity}"
            holder.textViewPrice.text = " $ ${productMCLB.quantity!! * productMCLB.precioUnitario!!}"
            sharedPref.saveMCLB("order", productMCLBS)
            (context as ClientShoppingBagActivityMCLB).setTotal(getTotalMCLB())
        }



    }

    /*
    * Funcion para enviar al activity de detalle del producto
     */
    private fun goToDetailMCLB(productMCLB : ProductMCLB){

        val i = Intent(context, ClientProductsDetailActivityMCLB::class.java)
        i.putExtra("product", productMCLB.toJsonMCLB())
        context.startActivity(i)

    }



    class ShoppingBagViewHolderMCLB(view: View): RecyclerView.ViewHolder(view) {

        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView
        val imageViewAdd: ImageView
        val imageViewRemove: ImageView
        val imageViewDelete: ImageView
        val textViewCounter: TextView
        val textViewIdProduct: TextView

        init {
            textViewIdProduct = view.findViewById(R.id.textview_idProduct)
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