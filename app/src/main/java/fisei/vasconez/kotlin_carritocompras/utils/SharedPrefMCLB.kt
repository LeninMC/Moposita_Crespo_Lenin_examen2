package fisei.vasconez.kotlin_carritocompras.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import java.lang.Exception

// La clase se le pasa como parametro el activity donde se ejecutara
class SharedPrefMCLB(activity: Activity) {

    private var prefs: SharedPreferences? = null

    init {
        prefs = activity.getSharedPreferences(
            "fisei.vasconez.kotlin_carritocompras",
            Context.MODE_PRIVATE
        )
    }

    /*
    *   Funcion para guardar el token
     */
    fun saveMCLB(key: String, objeto: Any) {
        try {

            val gson = Gson()
            val json = gson.toJson(objeto)
            with(prefs?.edit()) {
                this?.putString(key, json)
                this?.commit()
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Error ${e.message}")
        }

    }

    /*
    *   Obtener la data almacenada
     */

    fun getDataMCLB(key: String): String? {
        val data = prefs?.getString(key, "")
        return data
    }
    /*
    *   Para remover la session de Usuario del Telefono
     */

    fun removeMCLB(key : String ){
        prefs?.edit()?.remove(key)?.apply()
    }
}