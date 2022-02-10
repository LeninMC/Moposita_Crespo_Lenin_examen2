package fisei.vasconez.kotlin_carritocompras.activities


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.activities.Client.home.ClientHomeActivityMCLB
import fisei.vasconez.kotlin_carritocompras.models.ResponseHttpMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import fisei.vasconez.kotlin_carritocompras.providers.UsersProviderMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivityMCLB : AppCompatActivity() {

    val TAG = "SaveImageActivity"
    var circleImageUser: CircleImageView? = null
    var buttonNext: Button? = null
    var buttonConfirm: Button? = null
    private var imageFile: File? = null

    var usersProviderMCLB : UsersProviderMCLB? = null
    var userMCLB: UserMCLB? = null
    var sharedPrefMCLB: SharedPrefMCLB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image_mclb)

        sharedPrefMCLB = SharedPrefMCLB(this)
        getUserFromSessionMCLB()
        usersProviderMCLB = UsersProviderMCLB(userMCLB?.session_token)
        circleImageUser = findViewById(R.id.circleimage_user)
        buttonConfirm = findViewById(R.id.btn_confirm)
        buttonNext = findViewById(R.id.btn_next)

        circleImageUser?.setOnClickListener { selectImageMCLB() }
        buttonNext?.setOnClickListener { goToClientHomeMCLB() }
        buttonConfirm?.setOnClickListener {saveImageMCLB()}
    }

    /*
    *   Para confirmar el cambio de imagen y proceder a subir a firebase
     */
    private fun saveImageMCLB() {
        if (imageFile != null && userMCLB != null) {
            usersProviderMCLB?.updateMCLB(imageFile!!, userMCLB!!)?.enqueue(object : Callback<ResponseHttpMCLB> {
                override fun onResponse(
                    call: Call<ResponseHttpMCLB>,
                    responseMCLB: Response<ResponseHttpMCLB>
                ) {
                    Log.d(TAG, "Response $responseMCLB")
                    Log.d(TAG, "BODY ${responseMCLB.body()}")
                    saveUserInSessionMCLB(responseMCLB.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttpMCLB>, t: Throwable) {
                    Log.d(TAG, "Error no se pudo enviar la Imagen ${t.message}")
                    Toast.makeText(this@SaveImageActivityMCLB, "Error ${t.message}", Toast.LENGTH_LONG)
                        .show()

                }
            })
        }
        else {
            Toast.makeText(this, "Imagen no puede se Nula ni tampoco el Usuario", Toast.LENGTH_LONG).show()
        }

    }

    /*
    * Saltar el paso de tomar foto
     */
    /*
  *   Navegar a la pantalla de Home si es la Autenticacion es Correcta
   */
    private fun goToClientHomeMCLB() {
        val i = Intent(this, ClientHomeActivityMCLB::class.java)
        i.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Elimina el historial de Pantallas
        startActivity(i)
    }

    /*
   *   Obtener la data almacena de Session de SharedPref
    */
    private fun getUserFromSessionMCLB() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getData("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            userMCLB = gson.fromJson(sharedPrefMCLB?.getData("user"), UserMCLB::class.java)
        }
    }

    /*
    *   Para llamar a la accion de tomar de galeria o tomar foto
    *   Variable para captutar la imagen
     */
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) // El archivo que vamos a guardar en servidor
                circleImageUser?.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Tarea cancela", Toast.LENGTH_LONG).show()
            }
        }

    private fun selectImageMCLB() {
        //ImagePicker Permite seleccionar de galeria o tomar foto
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

    /*
   *   Almacenar el Session
    */
    private fun saveUserInSessionMCLB(data: String) {
        Log.d("SharedPred", "Save : $data")

        val gson = Gson()
        val user = gson.fromJson(data, UserMCLB::class.java)
        Log.d("SharedPred", "Trasformacion : $user")
        sharedPrefMCLB?.saveMCLB("user", user)
        goToClientHomeMCLB()

    }
}