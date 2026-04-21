package com.crewq.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun TimeClockScreen(
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    companyName: String
) {
    // TODO: Implement Time Clock Screen
}

@Composable
fun TeamTimeClockScreen(
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    companyName: String
) {
    // TODO: Implement Team Time Clock Screen
}
