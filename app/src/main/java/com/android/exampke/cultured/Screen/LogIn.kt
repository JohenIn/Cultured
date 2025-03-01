package com.android.exampke.cultured.Screen

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.android.exampke.cultured.BuildConfig
import com.android.exampke.cultured.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class Authlogin : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (FirebaseApp.getApps(this).isNotEmpty()) {
            Log.d("Authlogin", "Firebase initialized successfully")
        } else {
            Log.e("Authlogin", "Firebase initialization failed")
        }
    }
}

@Composable
fun LogInScreen(navController: NavController) {
    val context = LocalContext.current
    val token = BuildConfig.DEFAULT_WEB_CLIENT_ID
    val launcherNav = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        navController.navigate("today")
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        val task =
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    .getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("today")
                        }
                    }
            } catch (e: ApiException) {
                Log.w("TAG", "GoogleSign in Failed", e)
            }
    }


    Column() {
        Image(
            painter = painterResource(id = R.drawable.sign_in_google),
            contentDescription = "Google Sign In",
            modifier = Modifier.clickable {
                val gso = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
            })
        Button(
            onClick = {
                signOut(context, navController, token = token)
            }
        ) {
            Text(text = "Log Out")
        }
    }

}

fun signOut(context: Context, navController: NavController, token: String) {

    // Firebase 로그아웃
    FirebaseAuth.getInstance().signOut()

    // GoogleSignInClient 로그아웃
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    googleSignInClient.signOut().addOnCompleteListener {
        // 로그아웃 완료 후, 로그인 화면으로 이동하거나 원하는 처리를 할 수 있습니다.
        navController.navigate("today") {

        }
    }
}
