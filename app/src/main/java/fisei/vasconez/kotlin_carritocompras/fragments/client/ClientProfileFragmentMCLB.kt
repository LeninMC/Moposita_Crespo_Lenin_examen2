package fisei.vasconez.kotlin_carritocompras.fragments.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import fisei.vasconez.kotlin_carritocompras.R
import fisei.vasconez.kotlin_carritocompras.activities.Client.update.ClientUpdateActivityMCLB
import fisei.vasconez.kotlin_carritocompras.activities.MainActivityMCLB
import fisei.vasconez.kotlin_carritocompras.models.UserMCLB
import fisei.vasconez.kotlin_carritocompras.utils.SharedPrefMCLB

class ClientProfileFragmentMCLB : Fragment() {

    /*
    * TODO Variables y Constantes
     */
    var myView : View? = null
    var circleImageUser : CircleImageView? = null
    var buttonUpdateProfile : Button ? = null
    var textViewNombre : TextView? = null
    var textViewEmail : TextView? = null
    var textViewTelefono : TextView? = null
    var imageviewLogout : ImageView ? = null

    var sharedPrefMCLB : SharedPrefMCLB? = null
    var userMCLB : UserMCLB ? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       myView =  inflater.inflate(R.layout.fragment_client_profile_mclb, container, false)

        sharedPrefMCLB = SharedPrefMCLB(requireActivity())
        //TODO Instanciar
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        circleImageUser = myView?.findViewById(R.id.circleimage_user)
        textViewNombre = myView?.findViewById(R.id.textview_nombre)
        textViewEmail = myView?.findViewById(R.id.textview_email)
        textViewTelefono = myView?.findViewById(R.id.textview_telefono)
        imageviewLogout = myView?.findViewById(R.id.imageview_logout)


        imageviewLogout?.setOnClickListener{logoutMCLB()}
        buttonUpdateProfile?.setOnClickListener{goToUpdateMCLB()}

        getUserFromSessionMCLB()

        //TODO: Hacemos referencia a la Data que se guarda en la SharedPref
        textViewNombre?.text = "${userMCLB?.nombre} ${userMCLB?.apellido}"
        textViewEmail?.text = userMCLB?.email
        textViewTelefono?.text = userMCLB?.telefono
        if(!userMCLB?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(userMCLB?.image).into(circleImageUser!!)
        }




        return  myView
    }

    /*
   *  TODO Obtener la data almacena de Session de SharedPref
    */
    private fun getUserFromSessionMCLB() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getData("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            userMCLB = gson.fromJson(sharedPrefMCLB?.getData("user"), UserMCLB::class.java)

        }
    }

    /*
    *   TODO Redirigir a la activity de Update
     */
    private fun goToUpdateMCLB(){
        val i = Intent(requireContext(), ClientUpdateActivityMCLB ::class.java)
        startActivity(i)
    }


    /*
   *   Para cerrar la session de usuario almacenada en ShardPref
    */
    private fun logoutMCLB() {
        sharedPrefMCLB?.remove("user")
        val i = Intent(requireContext(), MainActivityMCLB::class.java)
        startActivity(i)
    }


}