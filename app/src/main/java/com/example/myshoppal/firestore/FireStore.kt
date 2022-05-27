package com.example.myshoppal.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myshoppal.activities.LoginActivity
import com.example.myshoppal.activities.RegisterActivity
import com.example.myshoppal.activities.UserProfileActivity
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStore {
     private val mFireStore=FirebaseFirestore.getInstance()

     fun registerUser(activity: RegisterActivity,userInfo: User) {
          mFireStore.collection(Constants.USERS)
               .document(userInfo.id)
               .set(userInfo, SetOptions.merge())
               .addOnSuccessListener {
                    activity.userregistrationSucess()
               }
               .addOnFailureListener{
                    e -> activity.hideprogressDialog()
                    Log.e(
                         activity.javaClass.simpleName,
                         "Error while registering the user",e
                    )
               }
     }
     fun getCurrentUserId():String {

          val currentUser=FirebaseAuth.getInstance().currentUser
          var currentUserId=""
          if(currentUser != null){
               currentUserId=currentUser.uid

          }
          return currentUserId
     }
     fun getUserDetails(activity: Activity){
          //heree we pass the collection name from which we wants the data

          mFireStore.collection(Constants.USERS)
          //the documnet id to get the Field of user

               .document(getCurrentUserId())
               .get()
               .addOnSuccessListener { document ->
                    Log.i(activity.javaClass.simpleName, document.toString())

                    //here we have recive the document snapshot which is converted into the user data model objects

                    val user=document.toObject(User::class.java)!!

                    val sharedPreferences=
                         activity.getSharedPreferences(
                              Constants.MYSHOPPAL_PREFERENCES,
                              Context.MODE_PRIVATE
                         )
                    val editor:SharedPreferences.Editor=sharedPreferences.edit()

                    editor.putString(
                         Constants.LOGGED_IN_USERNAME,
                         "${user.firstname} ${user.lastname}"
                    )
                    editor.apply()


                    when(activity){
                         is LoginActivity ->{
                                   //call a function of base activity for trasferring the result to it

                              activity.userLoggedInSuccess(user!!)
                         }
                    }
               }.addOnFailureListener{

               }
     }
     fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String, Any>){
          mFireStore.collection(Constants.USERS).document(getCurrentUserId())
               .update(userHashMap)
               .addOnSuccessListener {
                    when(activity)
                    {
                         is UserProfileActivity ->{
                              activity.hideprogressDialog()
                         }
                    }

               }
               .addOnFailureListener{e->
                    when(activity)
                    {
                         is UserProfileActivity ->{
                              activity.hideprogressDialog()
                         }
                    }
                    Log.e(
                         activity.javaClass.simpleName,"error message details",e
                    )
               }
     }
}