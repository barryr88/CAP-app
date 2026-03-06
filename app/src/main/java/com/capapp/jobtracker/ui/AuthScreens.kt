package com.capapp.jobtracker.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    auth: FirebaseAuth
) {
    Text(modifier = modifier, text = "Sign In Screen")
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegistrationSuccess: () -> Unit,
    onBackToSignIn: () -> Unit,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore
) {
    // TODO: Implement Register Screen
}
