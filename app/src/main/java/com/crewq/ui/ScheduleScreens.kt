package com.crewq.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    onBackToMenu: () -> Unit,
    onNewTaskClick: () -> Unit
) {
    // TODO: Implement Schedule Screen
}

@Composable
fun NewTaskScreen(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    firebaseAnalytics: FirebaseAnalytics?,
    activityScope: CoroutineScope,
    onBackToSchedule: () -> Unit,
    onTaskCreated: () -> Unit
) {
    // TODO: Implement New Task Screen
}
