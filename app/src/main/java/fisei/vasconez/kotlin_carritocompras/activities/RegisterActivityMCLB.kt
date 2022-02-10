package fisei.vasconez.kotlin_carritocompras.activities

import android.content.Intent
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
import fisei.vasconez.kotlin_carritocompras.models.ResponseHttpMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import fisei.vasconez.kotlin_carritocompras.providers.UsersProviderMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivityMCLB : AppCompatActivity() {

    val TAG = "RegisterActivity"
    var imageViewGoToLogin        : ImageView? = null
    var editTextEmail             : EditText? = null
    var editTextName              : EditText? = null
    var editTextLastName          : EditText? = null
   // var editTextCedula          : EditText? = null
    var editTextDireccion          : EditText? = null
    var editTextPhone             : EditText? = null
    var editTextPassword          : EditText? = null
    var editTextConfirmPassword   : EditText? = null
    var buttonRegister            : Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_mclb)

        /*
        *   INSTANCIAR LAS VARIABLES DECLARADAS ANTERIORMENTE
         */
        imageViewGoToLogin      = findViewById(R.id.imageview_go_to_login)
        editTextEmail           = findViewById(R.id.edittext_email)
        editTextName            = findViewById(R.id.edittext_name)
        editTextLastName        = findViewById(R.id.edittext_lastname)
        //editTextCedula          = findViewById(R.id.edittext_cedula)
        editTextDireccion       = findViewById(R.id.edittext_direccion)
        editTextPhone           = findViewById(R.id.edittext_phone)
        editTextPassword        = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister          = findViewById(R.id.btn_register)

        /*
        *   FUNCION QUE LLAMA AL ACTIVITY DE REGRESO AL LOGIN
         */
        imageViewGoToLogin?.setOnClickListener {
            goToLogin()
        }
        /*
        *   REALIZAR EL REGISTRO DEL USUARIO
         */
        buttonRegister?.setOnClickListener{ register() }
    }

    /*
    *   REGISTRO DE USUARIO
     */
    private fun register(){
        val name            = editTextName?.text.toString()
        //val dni             = editTextCedula?.text.toString()
        val adress          = editTextDireccion?.text.toString()
        val lastname        = editTextLastName?.text.toString()
        val email           = editTextEmail?.text.toString()
        val phone           = editTextPhone?.text.toString()
        val password        = editTextPassword?.text.toString()
        val confirmPassword = editTextConfirmPassword?.text.toString()

        //Para lanzar la peticion
        var userProvider = UsersProviderMCLB()

        if (isValidarForm(
                phone           = phone,
                lastName        = lastname,
                email           = email,
                password        = password,
                confirmPassword = confirmPassword,
                name = name)
        ){
           // Toast.makeText(this, "El formulario  es valido", Toast.LENGTH_SHORT).show()
               //Para crear el usuario
               val user = UserMCLB(
                   nombre       = name,
                   apellido     =  lastname,
                   direccion    = adress,
                   email        = email,
                   telefono     = phone,
                   password     = password
               )
            userProvider.register(user)?.enqueue(object : Callback<ResponseHttpMCLB>{
                override fun onResponse( //Responde Bien
                    call: Call<ResponseHttpMCLB>,
                    responseMCLB: Response<ResponseHttpMCLB>
                ) {
                    if(responseMCLB.body()?.isSuccess == true){
                        //Si el Registro es Exitoso guardamos las datos del Usuario y lo redirigimos a HomeClient
                        saveUserInSession(responseMCLB.body()?.data.toString())

                        //Navega a la Activity de Home
                        goToClientHome()
                    }
                    Toast.makeText(this@RegisterActivityMCLB, responseMCLB.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response Correcto : ${responseMCLB}")
                    Log.d(TAG, "Response Cuerpo : ${responseMCLB.body()}")


                }


                override fun onFailure(call: Call<ResponseHttpMCLB>, t: Throwable) { //Cuando falla
                   Log.d(TAG, "Se Produjo un error en la peticion HTTP ${t.message}")
                    Toast.makeText(this@RegisterActivityMCLB, "Se Produjo un error en la peticion HTTP 🙁 ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }



    /*
   *   Almacenar el Session
    */
    private fun saveUserInSession(data: String) {
        Log.d("SharedPred", "Save : $data")
        val sharedPref = SharedPrefMCLB(this)
        val gson = Gson()
        val user = gson.fromJson(data, UserMCLB::class.java)
        Log.d("SharedPred", "Trasformacion : $user")
        sharedPref.save("user", user)

    }

    /*
   *   Navegar a la pantalla de Home si es la Autenticacion es Correcta
    */
    private fun goToClientHome() {
        //val i = Intent(this, ClientHomeActivity::class.java)
        val i = Intent(this, SaveImageActivityMCLB::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Elimina el historial de Pantallas
        startActivity(i)
    }

    /*
    *   FUNCION PARA VALIDAR SI ES CORREO
    *   String.isEmailValid hace que aplique para todos campos de tipo String
     */
    fun String.isEmailValid() : Boolean {
        return !TextUtils.isEmpty(this ) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    /*
    *   VALIDACION DEL FORMULARIO
     */
    private fun isValidarForm(
        name            : String ,
        lastName        : String ,
        email           : String,
        phone           :  String ,
        password        : String,
        confirmPassword : String
    ): Boolean{
        if(name.isBlank()){
            Toast.makeText(this, "El Nombre esta En blanco", Toast.LENGTH_SHORT).show()
            return  false
        }
        if(lastName.isBlank()){
            Toast.makeText(this, "El Apellido esta En blanco", Toast.LENGTH_SHORT).show()
            return  false
        }
        if(email.isBlank()) {
            Toast.makeText(this, "El Email vacio ", Toast.LENGTH_SHORT).show()
            return false
        }
        if(phone.isBlank()) {
            Toast.makeText(this, "El Telefono esta En blanco", Toast.LENGTH_SHORT).show()
            return  false
        }
        if(password.isBlank()) {
            Toast.makeText(this, "El Password Vacio", Toast.LENGTH_SHORT).show()
            return false
        }
        if(confirmPassword.isBlank()) {
            Toast.makeText(this, "El Verificacion Password esta En blanco", Toast.LENGTH_SHORT).show()
            return  false
        }
        if(password != confirmPassword) {
            Toast.makeText(this, "El Password no coinscide verificalo", Toast.LENGTH_SHORT).show()
            return  false
        }
        if(!email.isEmailValid()) {
            Toast.makeText(this, "El Email no es Valido", Toast.LENGTH_SHORT).show()
            return  false
        }


        return  true
    }

    /*
    * VOLVER A LA ACTIVITY DE LOGIN
     */
    private fun goToLogin() {
        val i = Intent(this, MainActivityMCLB::class.java)
        startActivity(i)

    }
}