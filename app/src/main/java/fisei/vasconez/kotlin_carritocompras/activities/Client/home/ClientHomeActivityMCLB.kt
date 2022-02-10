package fisei.vasconez.kotlin_carritocompras.activities.Client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.activities.MainActivityMCLB
import fisei.vasconez.kotlin_carritocompras.fragments.client.ClientCategoriesFragmentMCLB
import fisei.vasconez.kotlin_carritocompras.fragments.client.ClientOrdersFragment
import fisei.vasconez.kotlin_carritocompras.fragments.client.ClientProfileFragmentMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB

class ClientHomeActivityMCLB : AppCompatActivity() {

    private val TAG = "ClientHomeActivity"
    var buttonLogout: Button? = null
    var sharedPrefMCLB: SharedPrefMCLB? = null
    var bottomNavigation: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_client_home_mclb)
        //buttonLogout = findViewById(R.id.btn_logout)
        bottomNavigation = findViewById(R.id.bottom_navegation)
        sharedPrefMCLB = SharedPrefMCLB(this)
        openFragmentMCLB(ClientCategoriesFragmentMCLB()) //Abre un fragmento por defecto
        //cerrar Session
        //buttonLogout?.setOnClickListener { logout() }
        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) { //Equivalente al Switch
                R.id.item_home -> {
                    openFragmentMCLB(ClientCategoriesFragmentMCLB())
                    true
                }
                R.id.item_orders -> {
                    openFragmentMCLB(ClientOrdersFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragmentMCLB(ClientProfileFragmentMCLB())
                    true
                }
                else -> false

            }
        }

        getUserFromSessionMCLB()
    }

    /*
    *   Funcion para mostrar el fragmento segun lo seleccionado
     */
    private fun openFragmentMCLB(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /*
    *   Para cerrar la session de usuario almacenada en ShardPref
     */
    private fun logoutMCLB() {
        sharedPrefMCLB?.remove("user")
        val i = Intent(this, MainActivityMCLB::class.java)
        startActivity(i)
    }

    /*
    *   Obtener la data almacena de Session de SharedPref
     */
    private fun getUserFromSessionMCLB() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getData("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            val user = gson.fromJson(sharedPrefMCLB?.getData("user"), UserMCLB::class.java)
            Log.d(TAG, "Usuario : $user")
        }
    }
}