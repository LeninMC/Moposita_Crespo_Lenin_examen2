package fisei.vasconez.kotlin_carritocompras.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.activities.Client.home.ClientHomeActivityMCLB
import fisei.vasconez.kotlin_carritocompras.models.ResponseHttpMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import fisei.vasconez.kotlin_carritocompras.providers.UsersProviderMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityMCLB : AppCompatActivity() {

    var imageViewGToRegister: ImageView? =
        null  //laterinit tambien se usa para inicializar una variable
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null
    var usersProvider = UsersProviderMCLB()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_mclb)

        /*
        *   iNSTANCIAR LAS VARIABLES DECLARADAS ANTERIORMENTE
         */
        imageViewGToRegister = findViewById(R.id.imageview_go_to_register)
        editTextEmail = findViewById(R.id.edittext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        buttonLogin = findViewById(R.id.btn_login)


        /*
        *   iNVOCAR EL METODO PARA PASAR  A LA ACTIVITY REGISTRO
         */
        imageViewGToRegister?.setOnClickListener { goToRegisterMCLB() }
        /*
        *   BUTTON LOGIN LLAMAR AL METODO LOGIN
         */
        buttonLogin?.setOnClickListener { loginMCLB() }

        getUserFromSessionMCLB()

    }

    /*
    *   FUNCION PARA REALIZAR EL LOGIN
     */
    private fun loginMCLB() {
        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()

      // if(validarPassword(password)){
           if (isValidarFormMCLB(email, password)) {
               usersProvider.loginMCLB(email, password)?.enqueue(object : Callback<ResponseHttpMCLB> {
                   override fun onResponse(
                       call: Call<ResponseHttpMCLB>,
                       responseMCLB: Response<ResponseHttpMCLB>
                   ) {
                       Log.d("MainActivity", "Response : ${responseMCLB.body()}")
                       if (responseMCLB.body()?.isSuccess == true) {
                           Toast.makeText(
                               this@MainActivityMCLB,
                               "El Formulario es Valido",
                               Toast.LENGTH_SHORT
                           ).show()
                           //Amacena el Usuario
                           saveUserInSessionMCLB(responseMCLB.body()?.data.toString())
                           Log.d(
                               "MainActivity",
                               "Response SessionSave : ${responseMCLB.body()?.data.toString()}"
                           )
                           //Navega a la Activity de Home
                           goToClientHomeMCLB()
                       } else {
                           Toast.makeText(
                               this@MainActivityMCLB,
                               "Los Datos no son Correctos ",
                               Toast.LENGTH_SHORT
                           ).show()
                       }
                   }

                   override fun onFailure(call: Call<ResponseHttpMCLB>, t: Throwable) {
                       Toast.makeText(
                           this@MainActivityMCLB,
                           "Ocurrio un Error ${t.message}",
                           Toast.LENGTH_SHORT
                       ).show()
                       Log.d("MainActivity", "Ocurrio un Error ${t.message}")
                   }

               })


           } else {
               Toast.makeText(this, "El formulario no es Valido", Toast.LENGTH_SHORT).show()
           }
     //  }else{
       //    Toast.makeText(this,
       //        "Ingrese una contraseña entre 6 - 10 caracteres " +
        //           "Debe contener minimo una letra mayuscula, una letra minuscula" +
       //            "un caracter especial y un numero" , Toast.LENGTH_LONG).show()
      // }
    }


    //funcion para la validacion de la contraseña
    fun validarPassword(password: String): Boolean {
        var validar = true
        var seguidos = 0
        var ultimo = 0xFF.toChar()
        var minuscula = 0
        var mayuscula = 0
        var numero = 0
        var especial = 0
        if (password.length < 6 || password.length > 10) return false // tamaño
        for (i in 0 until password.length) {
            val c = password[i]
            if (c <= ' ' || c > '~') {
                validar = false //Espacio o fuera de rango
                break
            }
            if (c > ' ' && c < '0' || c >= ':' && c < 'A' || c >= '[' && c < 'a' || c >= '{' && c.toInt() < 127) {
                especial++
            }
            if (c >= '0' && c < ':') numero++
            if (c >= 'A' && c < '[') mayuscula++
            if (c >= 'a' && c < '{') minuscula++
            seguidos = if (c == ultimo) seguidos + 1 else 0
            if (seguidos >= 7) {
                validar = false // 3 seguidos
                break
            }
            ultimo = c
        }
        validar = validar && especial > 0 && numero > 0 && minuscula > 0 && mayuscula > 0
        return validar
    }
    /*
    *   FUNCION PARA VALIDAR SI ES CORREO
    *   String.isEmailValidMCLB hace que aplique para todos campos de tipo String
     */
    fun String.isEmailValidMCLB(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    /*
    *   Almacenar el Session
     */
    private fun saveUserInSessionMCLB(data: String) {
        Log.d("SharedPred", "Save : $data")
        val sharedPref = SharedPrefMCLB(this)
        val gson = Gson()
        val user = gson.fromJson(data, UserMCLB::class.java)
        Log.d("SharedPred", "Trasformacion : $user")
        sharedPref.saveMCLB("user", user)

    }

    /*
    *   Navegar a la pantalla de Home si es la Autenticacion es Correcta
     */
    private fun goToClientHomeMCLB() {
        val i = Intent(this, ClientHomeActivityMCLB::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK // Elimina el historial de Pantallas
        startActivity(i)
    }

    /*
    *   VALIDACION DEL FORMULARIO
     */
    private fun isValidarFormMCLB(email: String, password: String): Boolean {
        if (email.isBlank()) return false
        if (password.isBlank()) return false
        if (!email.isEmailValidMCLB()) return false

        return true
    }

    /*
   *   Obtener la data almacena de Session de SharedPref
    */
    private fun getUserFromSessionMCLB() {
        val sharedPref = SharedPrefMCLB(this)
        val gson = Gson()

        if (!sharedPref.getDataMCLB("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            val user = gson.fromJson(sharedPref.getDataMCLB("user"), UserMCLB::class.java)
            goToClientHomeMCLB()
        }
    }

    /*
     *  FUNCION NAVEGAR A LA ACTIVITY DE REGISTRO
     */
    private fun goToRegisterMCLB() {
        val i = Intent(this, RegisterActivityMCLB::class.java)
        startActivity(i)
    }
}