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
import fisei.vasconez.kotlin_carritocompras.activities.Client.products.list.ClientProductsListActivity
import fisei.vasconez.kotlin_carritocompras.models.CategoryMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB

class CategoriesAdapterMCLB(val context: Activity, val categoryMCLBS: ArrayList<CategoryMCLB>): RecyclerView.Adapter<CategoriesAdapterMCLB.CategoriesViewHolder>() {

    val sharedPref = SharedPrefMCLB(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryMCLBS.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val category = categoryMCLBS[position] // CADA UNA DE LAS CATEGORIAS

        holder.textViewCategory.text = category.nombre
        Glide.with(context).load(category.image).into(holder.imageViewCategory) //Establece la imagen

        holder.itemView.setOnClickListener{goToProducts(category)}
    }

    /*
    *   Una vez escogida la categoria le enviamos el id de la categoria para poder listar los productos segun la categoria
     */
    private  fun  goToProducts (categoryMCLB : CategoryMCLB){
        val i = Intent(context, ClientProductsListActivity::class.java)
        Log.d("AdapterCategoria", "$categoryMCLB")
        i.putExtra("idcategoria", categoryMCLB.idcategoria)
        context.startActivity(i)

    }


    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }

    }

}