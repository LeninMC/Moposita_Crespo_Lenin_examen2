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
    var editTextCedula          : EditText? = null
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
        editTextCedula          = findViewById(R.id.edittext_cedula)
        editTextDireccion       = findViewById(R.id.edittext_direccion)
        editTextPhone           = findViewById(R.id.edittext_phone)
        editTextPassword        = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister          = findViewById(R.id.btn_register)

        /*
        *   FUNCION QUE LLAMA AL ACTIVITY DE REGRESO AL LOGIN
         */
        imageViewGoToLogin?.setOnClickListener {
            goToLoginMCLB()
        }
        /*
        *   REALIZAR EL REGISTRO DEL USUARIO
         */
        buttonRegister?.setOnClickListener{ registerMCLB() }
    }

    /*
    *   REGISTRO DE USUARIO
     */
    private fun registerMCLB(){
        val name            = editTextName?.text.toString()
        val dni             = editTextCedula?.text.toString()
        val adress          = editTextDireccion?.text.toString()
        val lastname        = editTextLastName?.text.toString()
        val email           = editTextEmail?.text.toString()
        val phone           = editTextPhone?.text.toString()
        val password        = editTextPassword?.text.toString()
        val confirmPassword = editTextConfirmPassword?.text.toString()

        //Para lanzar la peticion
        var userProvider = UsersProviderMCLB()

        if (isValidarFormMCLB(
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
            userProvider.registerMCLB(user)?.enqueue(object : Callback<ResponseHttpMCLB>{
                override fun onResponse( //Responde Bien
                    call: Call<ResponseHttpMCLB>,
                    responseMCLB: Response<ResponseHttpMCLB>
                ) {
                    if(responseMCLB.body()?.isSuccess == true){
                        //Si el Registro es Exitoso guardamos las datos del Usuario y lo redirigimos a HomeClient
                        saveUserInSessionMCLB(responseMCLB.body()?.data.toString())

                        //Navega a la Activity de Home
                        goToClientHomeMCLB()
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

    private fun validarCedula(cedula: String): Boolean {
        if (cedula.length == 10) {
            var digitoRegion = cedula.substring(0, 2);
            if (digitoRegion.toInt() >= 1 && digitoRegion.toInt() <= 24) {
                var ultimoDigito = cedula.substring(9, 10);
                var digitosPares = cedula.substring(1, 2).toInt() + cedula.substring(3, 4)
                    .toInt() + cedula.substring(5, 6).toInt() + cedula.substring(7, 8).toInt();
                var num1 = 0
                var num7 = 0
                var num3 = 0
                var num9 = 0
                var num5 = 0
                var numero1 = cedula.substring(0, 1)
                numero1 = (numero1.toInt() * 2).toString()
                if (numero1.toInt() > 9) {
                    num1= (numero1.toInt() - 9) }

                var numero3 = cedula.substring(2, 3)
                numero3 = (numero3.toInt() * 2).toString()
                if (numero3.toInt() > 9) {
                    num3 = (numero3.toInt() - 9) }

                var numero5 = cedula.substring(4, 5)
                numero5 = (numero5.toInt() * 2).toString()
                if (numero5.toInt() > 9) {
                    num5 = (numero5.toInt() - 9); }

                var numero7 = cedula.substring(6, 7)
                numero7 = (numero7.toInt() * 2).toString()
                if (numero7.toInt() > 9) {
                    num7 = (numero7.toInt() - 9) }

                var numero9 = cedula.substring(8, 9)
                numero9 = (numero9.toInt() * 2).toString()
                if (numero9.toInt() > 9) {
                    num9 = (numero9.toInt() - 9) }

                var digitosImpares = num1 + num3 + num5 + num7 + num9
                var sumTot = (digitosPares + digitosImpares)
                var sumaPrimerDigito = (sumTot).toString().substring(0, 1)
                var decena = (sumaPrimerDigito.toInt() + 1) * 10

                var validarDigito = decena - sumTot

                if (validarDigito == 10) {
                    validarDigito = 0
                }

                if (validarDigito == ultimoDigito.toInt()) {

                    Toast.makeText(this, "Cedula Correcta ", Toast.LENGTH_SHORT)
                        .show()
                    return true
                } else {

                    Toast.makeText(this, "Cedula InCorrecta ", Toast.LENGTH_SHORT).show()
                    return false
                }
            } else {
                Toast.makeText(this, "Cedula no pertenerce a ninguna region", Toast.LENGTH_SHORT)
                    .show()
                return false
            }


            //TODO

        } else {
            Toast.makeText(this, "La cedula debe tener 10 digitos", Toast.LENGTH_SHORT)
                .show()
            return false
        }
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
        //val i = Intent(this, ClientHomeActivity::class.java)
        val i = Intent(this, SaveImageActivityMCLB::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Elimina el historial de Pantallas
        startActivity(i)
    }

    /*
    *   FUNCION PARA VALIDAR SI ES CORREO
    *   String.isEmailValidMCLB hace que aplique para todos campos de tipo String
     */
    fun String.isEmailValidMCLB() : Boolean {
        return !TextUtils.isEmpty(this ) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    /*
    *   VALIDACION DEL FORMULARIO
     */
    private fun isValidarFormMCLB(
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
        if(!email.isEmailValidMCLB()) {
            Toast.makeText(this, "El Email no es Valido", Toast.LENGTH_SHORT).show()
            return  false
        }


        return  true
    }

    /*
    * VOLVER A LA ACTIVITY DE LOGIN
     */
    private fun goToLoginMCLB() {
        val i = Intent(this, MainActivityMCLB::class.java)
        startActivity(i)

    }
}