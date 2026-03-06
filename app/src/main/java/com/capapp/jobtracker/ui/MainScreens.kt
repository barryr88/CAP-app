package com.capapp.jobtracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    username: String,
    userRole: String,
    onSignOut: () -> Unit,
    onScheduleClick: () -> Unit,
    onCustomersClick: () -> Unit,
    onTimeClockClick: () -> Unit,
    onTeamTimeClockClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // TODO: Implement Main Menu Screen
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit,
    firestore: FirebaseFirestore,
    auth: FirebaseAuth,
    companyName: String
) {
    // TODO: Implement Settings Screen
}
