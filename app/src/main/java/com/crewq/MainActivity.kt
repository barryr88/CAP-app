package com.crewq


import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.Instant
import kotlin.text.format
import android.app.NotificationChannel
import com.kizitonwose.calendar.compose.CalendarState
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.OutlinedButton
import coil.compose.AsyncImage
import com.google.firebase.storage.storage
import java.util.UUID
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.layout.PaddingValues
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.DayPosition
import android.app.DownloadManager
import android.app.Service
import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import kotlinx.coroutines.async
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.CollectionReference
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withTranslation
import androidx.core.net.toUri
import com.google.firebase.firestore.SetOptions
import android.provider.OpenableColumns
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.AttachFile
import com.google.maps.android.compose.Polyline
import coil.imageLoader
import coil.request.ImageRequest
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.text.format.DateUtils
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TabRow
import androidx.compose.runtime.mutableDoubleStateOf
import coil.request.CachePolicy
import com.crewq.ui.theme.JobTrackerTheme
import com.google.firebase.storage.FirebaseStorage
import androidx.core.graphics.scale
import androidx.compose.ui.text.style.TextOverflow
import com.google.firebase.functions.functions
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.firestore.PropertyName
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.mutableStateMapOf
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.foundation.gestures.detectTransformGestures
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import qrcode.QRCode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntSize
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.awaitCancellation
import java.io.ByteArrayOutputStream
import java.util.TimeZone
import kotlin.math.sqrt


fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "draft" -> Color.Gray
        "sent" -> Color(0xFF2196F3) // Blue
        "approved", "paid" -> Color(0xFF4CAF50) // Green
        "declined" -> Color(0xFFF44336) // Red
        "converted" -> Color(0xFF9C27B0) // Purple
        "due" -> Color(0xFFFF9800) // Orange
        else -> Color.Black
    }
}

fun scheduleShiftReminder(context: Context, scheduleMap: Map<String, String>) {
    val calendar = Calendar.getInstance()
    val daysOfWeek = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    // Figure out what day of the week it is today
    val currentDayName = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    val todaysShift = scheduleMap[currentDayName] ?: return

    // Clean up the text so we can read it easily
    val cleanStr = todaysShift.lowercase().replace(" ", "")
    if (cleanStr == "off" || cleanStr.isEmpty()) return

    // Grab just the start time (everything before the "-" or "to")
    val startPart = cleanStr.split("-", "to").firstOrNull() ?: return

    // Regex magic to find hours, minutes, and am/pm
    val regex = Regex("""^(\d{1,2})(?::(\d{2}))?(am|pm)?""")
    val match = regex.find(startPart) ?: return

    var hour = match.groupValues[1].toInt()
    val minute = match.groupValues[2].takeIf { it.isNotEmpty() }?.toInt() ?: 0
    val ampm = match.groupValues[3]

    // Convert to 24-hour military time for the alarm clock
    if (ampm == "pm" && hour < 12) hour += 12
    if (ampm == "am" && hour == 12) hour = 0

    // Build the exact time of the shift today
    val shiftTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }

    // Subtract 15 minutes for the reminder!
    shiftTime.add(Calendar.MINUTE, -15)

    // If the reminder time has already passed today, abort!
    if (shiftTime.timeInMillis <= System.currentTimeMillis()) return

    // Set the Android System Alarm
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ShiftReminderReceiver::class.java)

    // Use FLAG_IMMUTABLE to comply with modern Android security rules
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        currentDayName.hashCode(), // Unique ID for today
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    try {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            shiftTime.timeInMillis,
            pendingIntent
        )
        Log.d("CrewQ_Alarms", "Shift reminder successfully set for: ${shiftTime.time}")
    } catch (e: SecurityException) {
        Log.e("CrewQ_Alarms", "User denied exact alarm permission", e)
    }
}
data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

data class MapUser(
        val id: String,
        val name: String,
        val position: LatLng
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressAutocompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        if (value.isNotBlank()) {
            delay(300) // Debounce API calls
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(value)
                .setTypesFilter(listOf("address"))
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    // 👇 FIX 1: Safely convert Java List to Kotlin List 👇
                    predictions = response.autocompletePredictions?.toList() ?: emptyList()
                    isDropdownExpanded = predictions.isNotEmpty()
                }
                .addOnFailureListener { exception ->
                    Log.e("AddressAutocomplete", "Prediction failed", exception)
                }
        } else {
            predictions = emptyList()
            isDropdownExpanded = false
        }
    }

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = {
                // If they are manually typing, update the text instantly
                onValueChange(it)
            },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable)
        )

        if (predictions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                predictions.forEach { prediction ->
                    DropdownMenuItem(
                        text = { Text(prediction.getFullText(null).toString(), color = Color.Black) },
                        onClick = {
                            // 👇 FIX 2: Use Place.Field.ADDRESS instead of FORMATTED_ADDRESS 👇
                            val placeFields = listOf(Place.Field.ADDRESS)
                            val fetchRequest = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)

                            placesClient.fetchPlace(fetchRequest)
                                .addOnSuccessListener { response ->
                                    // 👇 FIX 3: Use response.place.address 👇
                                    val fullAddress = response.place.address ?: prediction.getFullText(null).toString()

                                    // Remove the ", USA" at the end if you want it cleaner for QBO
                                    val cleanAddress = fullAddress.removeSuffix(", USA")

                                    onValueChange(cleanAddress)
                                }
                                .addOnFailureListener {
                                    // Fallback just in case the fetch fails
                                    onValueChange(prediction.getFullText(null).toString().removeSuffix(", USA"))
                                }

                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}
data class UserPath(
    val userId: String,
    val color: Color,
    val points: List<LatLng>
)
// Helper to drop the .0 from whole numbers
val Double.formattedQuantity: String
    get() = if (this % 1.0 == 0.0) this.toInt().toString() else this.toString()


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamMapScreen(
    modifier: Modifier = Modifier,
    firestore: FirebaseFirestore,
    companyName: String
) {
    var usersOnMap by remember { mutableStateOf<List<MapUser>>(emptyList()) }
    var userPaths by remember { mutableStateOf<List<UserPath>>(emptyList()) }

    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<MapUser?>(null) }

    // --- NEW: Date Selection State ---
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val selectedDateMillis = remember(datePickerState.selectedDateMillis) {
        val millis = datePickerState.selectedDateMillis
        if (millis != null) {
            // 1. Read the date as UTC
            val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utcCalendar.timeInMillis = millis

            val year = utcCalendar.get(Calendar.YEAR)
            val month = utcCalendar.get(Calendar.MONTH)
            val day = utcCalendar.get(Calendar.DAY_OF_MONTH)

            // 2. Rebuild the date in the user's Local Timezone
            val localCalendar = Calendar.getInstance()
            localCalendar.set(year, month, day, 0, 0, 0)
            localCalendar.set(Calendar.MILLISECOND, 0)

            localCalendar.timeInMillis
        } else {
            System.currentTimeMillis()
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(39.8283, -98.5795), 4f)
    }

    val pathColors = listOf(Color.Blue, Color.Red, Color.Green, Color.Magenta, Color.Cyan, Color(0xFFFF5722))

    // Re-run this effect whenever the date or company name changes
    LaunchedEffect(companyName, selectedDateMillis) {
        if (companyName.isNotBlank()) {
            // Calculate Start and End of the selected day
            val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = Timestamp(calendar.time)

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDay = Timestamp(calendar.time)

            val isToday = DateUtils.isToday(selectedDateMillis)

            firestore.collection("companies").document(companyName).collection("users")
                .get()
                .addOnSuccessListener { result ->
                    val userList = mutableListOf<MapUser>()
                    val pathsList = mutableListOf<UserPath>()

                    result.documents.forEach { doc ->
                        val firstName = doc.getString("firstName") ?: "Unknown"
                        val lastName = doc.getString("lastName") ?: ""
                        val userId = doc.id
                        val isClockedIn = doc.getBoolean("isClockedIn") ?: false

                        // Consistent color assignment
                        val colorIndex = Math.abs(userId.hashCode()) % pathColors.size
                        val userColor = pathColors[colorIndex]

                        // Query the location_history for this specific user on the selected date
                        doc.reference.collection("location_history")
                            .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                            .whereLessThanOrEqualTo("timestamp", endOfDay)
                            .orderBy("timestamp", Query.Direction.ASCENDING)
                            .get()
                            .addOnSuccessListener { historyResult ->
                                val points = historyResult.documents.mapNotNull { historyDoc ->
                                    val histGeo = historyDoc.getGeoPoint("location")
                                    if (histGeo != null) LatLng(histGeo.latitude, histGeo.longitude) else null
                                }

                                if (points.isNotEmpty()) {
                                    pathsList.add(UserPath(userId, userColor, points))

                                    // Use their last known position for the day as their marker
                                    val lastPosition = points.last()

                                    // If we are looking at a past date, show everyone who had a path.
                                    // If we are looking at today, only show them on the map if they are currently clocked in.
                                    if (!isToday || isClockedIn) {
                                        userList.add(
                                            MapUser(
                                                id = userId,
                                                name = "$firstName $lastName",
                                                position = lastPosition
                                            )
                                        )
                                    }

                                    // Trigger recomposition
                                    userPaths = pathsList.toList()
                                    usersOnMap = userList.toList()

                                    // Center camera on the first loaded user
                                    if (userList.size == 1 && cameraPositionState.position.zoom == 4f) {
                                        cameraPositionState.position = CameraPosition.fromLatLngZoom(lastPosition, 12f)
                                    }
                                }
                            }
                    }
                }
        }
    }

    // --- NEW: Date Picker Dialog ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(modifier = modifier.fillMaxSize()) { // <-- Uses the lowercase 'modifier' passed into the function

        // --- Top Bar for Date Selection ---
        val displayDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(selectedDateMillis))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .clickable { showDatePicker = true }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Viewing Map For: $displayDate",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                Icons.Default.Search,
                contentDescription = "Change Date",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {
            userPaths.forEach { path ->
                Polyline(
                    points = path.points,
                    color = path.color,
                    width = 10f
                )
            }

            usersOnMap.forEach { user ->
                Marker(
                    state = MarkerState(position = user.position),
                    title = user.name,
                    snippet = if (DateUtils.isToday(selectedDateMillis)) "Current Location" else "Last Known Location"
                )
            }
        }

        // Bottom Dropdown Menu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                TextField(
                    value = selectedUser?.name ?: "Find Employee Location...",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Employee") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    if (usersOnMap.isEmpty()) {
                        DropdownMenuItem(text = { Text("No active paths for this date") }, onClick = { })
                    } else {
                        usersOnMap.forEach { user ->
                            DropdownMenuItem(
                                text = { Text(user.name, color = Color.Black) },
                                onClick = {
                                    selectedUser = user
                                    isDropdownExpanded = false
                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(user.position, 15f)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val TAG = "LocationServiceDebug"

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.d(TAG, "onCreate: Service has been created.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: Service is starting.")

        // This must be called before startForeground()
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, "location_channel")
            .setContentTitle("Location Tracking Active")
            .setContentText("Your location is being shared with your team.")
            // Use your newly renamed package R class directly here
            .setSmallIcon(R.drawable.crewq_logo)
            .build()

        startForeground(1, notification)
        startLocationUpdates()

        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(30))
            .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(15))
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d(TAG, "onLocationResult: Location received - Lat: ${location.latitude}, Lon: ${location.longitude}")
                    updateLocationInFirestore(location.latitude, location.longitude)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
            Log.d(TAG, "startLocationUpdates: Location updates requested successfully.")
        } catch (e: SecurityException) {
            Log.e(TAG, "startLocationUpdates: Location permission not granted.", e)
            stopSelf() // Stop service if permission is missing
        }
    }

    private fun updateLocationInFirestore(latitude: Double, longitude: Double) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Log.e(TAG, "updateLocationInFirestore: User is null, cannot save location")
            return
        }

        Log.d(TAG, "updateLocationInFirestore: Attempting to save location for UID: ${user.uid}")

        Firebase.firestore.collectionGroup("users").whereEqualTo("uid", user.uid).limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDoc = querySnapshot.documents[0]
                    val companyName = userDoc.getString("companyName")

                    if (!companyName.isNullOrBlank()) {
                        val userRef = Firebase.firestore.collection("companies").document(companyName)
                            .collection("users").document(user.uid)

                        val now = Timestamp.now()
                        val geoPoint = GeoPoint(latitude, longitude)

                        // 1. Update Current Location
                        val locationData = mapOf(
                            "location" to geoPoint,
                            "lastLocationUpdate" to now
                        )
                        userRef.update(locationData)

                        // 2. ADD HISTORY ENTRY
                        val historyData = mapOf(
                            "location" to geoPoint,
                            "timestamp" to now
                        )
                        userRef.collection("location_history").add(historyData)
                            .addOnSuccessListener {
                                Log.d(TAG, "updateLocationInFirestore: SUCCESS! Location saved to history.")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "updateLocationInFirestore: Failed to save history", e)
                            }
                    } else {
                        Log.e(TAG, "updateLocationInFirestore: companyName is null for this user.")
                    }
                } else {
                    Log.e(TAG, "updateLocationInFirestore: No user document found matching UID.")
                }
            }
            .addOnFailureListener { e ->
                // 👇 THIS IS LIKELY WHERE IT WAS FAILING SILENTLY 👇
                Log.e(TAG, "updateLocationInFirestore: Query failed! You likely need a Firebase Index.", e)
            }
    }



    // --- THIS IS THE CORRECT IMPLEMENTATION ---
    private fun createNotificationChannel() {
        // The if-check is removed as minSdkVersion is 26 or higher
        val channel = NotificationChannel(
            "location_channel",
            "Location Tracking",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d(TAG, "onDestroy: Service has been destroyed and location updates removed.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
/**
 * Parses a CSV file and imports the customer data into Firestore using a batched write.
 *
 * @return A Pair containing the number of customers imported and an optional error message.
 */
suspend fun parseCsvAndImportCustomers(
    context: Context,
    fileUri: Uri,
    firestore: FirebaseFirestore,
    companyName: String
): Pair<Int, String?> {
    // Use the IO dispatcher for file reading and network operations
    return withContext(Dispatchers.IO) {
        var customersImported = 0
        try {
            // Use contentResolver to open an InputStream for the given URI
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                // Use Kotlin's 'use' to automatically close the reader
                InputStreamReader(inputStream).use { reader ->
                    // Build the CSVReader, skipping the header row (skip(1))
                    val csvReader = CSVReaderBuilder(reader).withSkipLines(1).build()
                    val allRows = csvReader.readAll()
                    val batch = firestore.batch()

                    for (row in allRows) {
                        // IMPORTANT: Adjust these indices to match your CSV file's column order.
                        // This example assumes:
                        // row[0] = Customer Name
                        // row[1] = Phone
                        // row[2] = Email
                        // row[3] = Billing Address
                        val customerName = row.getOrNull(0)?.trim()

                        if (!customerName.isNullOrBlank()) {
                            val customerData = hashMapOf(
                                "name" to customerName,
                                "phone" to row.getOrNull(1)?.trim().orEmpty(),
                                "email" to row.getOrNull(2)?.trim().orEmpty(),
                                "address" to row.getOrNull(3)?.trim().orEmpty()
                            )

                            // Get a new document reference for each customer in the subcollection
                            val customerRef = firestore.collection("companies").document(companyName)
                                .collection("customers").document()
                            batch.set(customerRef, customerData)
                            customersImported++
                        }
                    }

                    // Commit the batched write to Firestore
                    batch.commit().await()
                }
            }
            Pair(customersImported, null) // Success
        } catch (e: Exception) {
            Log.e("CSV_IMPORT", "Error importing CSV", e)
            Pair(0, e.message ?: "An unknown error occurred.") // Failure
        }
    }
}
data class Tool(
    val id: String = "",
    val name: String = "",
    val brandModel: String = "",
    val serialNumber: String = "",
    val status: String = "Available", // "Available", "Checked Out", "Maintenance", "Lost"
    val assignedUserId: String? = null,
    val assignedUserName: String? = null,
    val checkoutDate: Timestamp? = null,
    val notes: String = ""
)
data class MileageEntry(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val companyName: String = "",
    val date: Timestamp = Timestamp.now(),
    val vehicle: String = "",
    val startOdometer: Double? = null,
    val endOdometer: Double? = null,
    val totalMiles: Double = 0.0,
    val notes: String = ""
)
data class PdfEstimateData(
    val estimate: Estimate,
    val lineItems: List<EstimateLineItem>,
    val companyName: String,
    val companyLogoUrl: String? = null,
    val companyProfile: CompanyProfile = CompanyProfile(),
    val hideItemization: Boolean = false
)

data class PdfInvoiceData(
    val invoice: Invoice,
    val lineItems: List<EstimateLineItem>,
    val companyName: String,
    val companyLogoUrl: String? = null,
    val companyProfile: CompanyProfile = CompanyProfile(),
    val hideItemization: Boolean = false
)
suspend fun generateInvoicePdf(
    context: Context,
    data: PdfInvoiceData
): File? = withContext(Dispatchers.IO) {
    val pageHeight = 1120
    val pageWidth = 792
    val pdfDocument = PdfDocument()

    // --- PAINTS ---
    val titlePaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 28f; isFakeBoldText = true }
    val headerPaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 14f; isFakeBoldText = true }
    val normalPaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 14f }
    val smallPaint = TextPaint().apply { color = android.graphics.Color.DKGRAY; textSize = 12f }
    val whiteTextPaint = TextPaint().apply { color = android.graphics.Color.WHITE; textSize = 14f; isFakeBoldText = true }
    val tableHeaderPaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 12f; isFakeBoldText = true }
    val grayFillPaint = Paint().apply { color = "#E0E0E0".toColorInt(); style = Paint.Style.FILL }
    val blackFillPaint = Paint().apply { color = android.graphics.Color.BLACK; style = Paint.Style.FILL }
    val linePaint = Paint().apply { color = android.graphics.Color.GRAY; strokeWidth = 1f }

    // --- LAYOUT CONSTANTS ---
    val margin = 50f
    val topMargin = 60f
    val bottomMargin = 80f
    val rowPadding = 30f

    // --- PAGE SETUP ---
    var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas: Canvas = page.canvas
    var yPos: Float = 0f

    // --- NEW: FETCH DYNAMIC LOGO ---
    var logoBitmap: Bitmap? = null
    try {
        if (data.companyLogoUrl != null) {
            val request = ImageRequest.Builder(context)
                .data(data.companyLogoUrl)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.DISABLED) // <-- FIX 1: Bypass the hardware-cached version
                .build()
            val result = context.imageLoader.execute(request)
            logoBitmap = (result.drawable as? BitmapDrawable)?.bitmap
        }
        // Fallback to default crewq_logo if URL is null or fetching failed
        if (logoBitmap == null) {
            logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.crewq_logo)
        }
    } catch (_: Exception) {
        logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.crewq_logo)
    }

    // --- HELPER TO DRAW THE REPEATING HEADER ---
    fun drawPageHeader(): Float {
        var logoX = pageWidth - margin

        // Draw the fetched logo proportionally
        logoBitmap?.let { bmp ->
            // <-- FIX 2: Absolute safety fallback. If it is STILL a hardware bitmap, force a software copy.
            val safeBmp = if (bmp.config == Bitmap.Config.HARDWARE) {
                bmp.copy(Bitmap.Config.ARGB_8888, false)
            } else {
                bmp
            }

            val aspectRatio = safeBmp.width.toFloat() / safeBmp.height.toFloat()
            val targetHeight = 90
            val targetWidth = (targetHeight * aspectRatio).toInt().coerceIn(1, 250)

            // Create the scaled bitmap using our guaranteed-software bitmap
            val scaledBitmap = safeBmp.scale(targetWidth, targetHeight, true)
            logoX = pageWidth - targetWidth - margin
            canvas.drawBitmap(scaledBitmap, logoX, topMargin - 40, null)
        }

        // Draw Company Info
        val profile = data.companyProfile
        val companyTitle = profile.displayName.ifBlank { data.companyName.uppercase() }

        canvas.drawText(companyTitle, margin, topMargin, headerPaint)
        canvas.drawText(profile.address, margin, topMargin + 20, normalPaint)
        canvas.drawText(profile.cityStateZip, margin, topMargin + 40, normalPaint)
        canvas.drawText(profile.phone, margin, topMargin + 60, normalPaint)
        canvas.drawText(profile.email, margin, topMargin + 80, normalPaint)

        // Draw "Estimate" Title & Number
        canvas.drawText("INVOICE", 300f, 80f, titlePaint)
        val invoiceNumber = data.invoice.invoiceNumber
        val numberWidth = titlePaint.measureText(invoiceNumber)
        canvas.drawText(invoiceNumber, logoX - numberWidth - 20, 80f, titlePaint)

        // Draw Customer Info and Totals Block
        var currentY = topMargin + 140f
        canvas.drawText("ADDRESS", margin, currentY, smallPaint); currentY += 25
        canvas.drawText(data.invoice.customerName, margin, currentY, headerPaint); currentY += 20
        data.invoice.address.split(",").forEach {
            canvas.drawText(it.trim(), margin, currentY, normalPaint); currentY += 20
        }
        val addressBottomY = currentY

        val rightBoxX = pageWidth - margin - 250f
        val dateBoxRect = RectF(rightBoxX, addressBottomY - 80, rightBoxX + 125, addressBottomY - 20)
        val totalBoxRect = RectF(rightBoxX + 125, addressBottomY - 80, rightBoxX + 250, addressBottomY - 20)
        val dateStr = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(data.invoice.creationDate.toDate())

        canvas.drawRect(dateBoxRect, grayFillPaint)
        canvas.drawText("DATE", dateBoxRect.centerX() - 20, dateBoxRect.top + 20, smallPaint)
        canvas.drawText(dateStr, dateBoxRect.centerX() - 40, dateBoxRect.top + 45, normalPaint)

        canvas.drawRect(totalBoxRect, blackFillPaint)
        canvas.drawText("TOTAL", totalBoxRect.centerX() - 25, totalBoxRect.top + 20, whiteTextPaint)
        canvas.drawText("$%.2f".format(data.invoice.totalAmount), totalBoxRect.centerX() - 40, totalBoxRect.top + 45, whiteTextPaint)

        return addressBottomY + 40
    }

    // --- TABLE DRAWING HELPERS ---
    val amountColX = pageWidth - margin - 100f
    val qtyColX = amountColX - 80f
    val rateColX = qtyColX - 100f

    fun drawLineItemHeader(startY: Float): Float {
        var currentY = startY + 30f
        canvas.drawText("DESCRIPTION", margin, currentY, tableHeaderPaint)
        canvas.drawText("QTY", qtyColX, currentY, tableHeaderPaint)
        canvas.drawText("RATE", rateColX, currentY, tableHeaderPaint)
        canvas.drawText("AMOUNT", amountColX, currentY, tableHeaderPaint)
        currentY += 15f
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
        return currentY + 25f
    }

    fun drawItemSection(title: String, items: List<EstimateLineItem>) {
        if (items.isEmpty()) return
        canvas.drawText(title, margin, yPos, tableHeaderPaint)
        yPos += 20f
        items.forEach { item ->
            val textLayout = StaticLayout.Builder.obtain(item.name, 0, item.name.length, normalPaint, (rateColX - margin - 20).toInt()).build()
            canvas.withTranslation(x = margin, y = yPos) { textLayout.draw(this) }

            canvas.drawText(item.quantity.formattedQuantity, qtyColX, yPos + 12f, normalPaint)
            canvas.drawText("%.2f".format(item.unitPrice), rateColX, yPos + 12f, normalPaint)
            canvas.drawText("%.2f".format(item.quantity * item.unitPrice), amountColX, yPos + 12f, normalPaint)

            yPos += textLayout.height + 20f
        }
        yPos += 10f
    }
    // --- DRAW INITIAL HEADER ---
    yPos = drawPageHeader()

// --- FILTER LINE ITEMS ---
    val services = data.lineItems.filter { it.type == "Service" }
    val nonInventory = data.lineItems.filter { it.type != "Service" }

// --- NEW: PRINT DETAILED SCOPE OF WORK AT THE TOP ---
    val scopeOfWork = data.invoice.detailedDescription.ifBlank { data.invoice.description }
    if (scopeOfWork.isNotBlank() && scopeOfWork != "Thank you for your business!") {
        yPos += 30f
        canvas.drawText("SCOPE OF WORK", margin, yPos, tableHeaderPaint)
        yPos += 15f
        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, linePaint)
        yPos += 20f

        // Wrap the text so it doesn't run off the edge of the page
        val textLayout = StaticLayout.Builder.obtain(
            scopeOfWork, 0, scopeOfWork.length, normalPaint, (pageWidth - margin * 2).toInt()
        ).build()

        canvas.withTranslation(x = margin, y = yPos) { textLayout.draw(this) }
        yPos += textLayout.height + 30f // Add space below the paragraph
    }

// --- DRAW THE TABLE OR THE SINGLE SUMMARY LINE ---
    if (data.hideItemization) {
        // --- DRAW GENERIC SUMMARY INSTEAD OF TABLE ---
        canvas.drawText("DESCRIPTION", margin, yPos, tableHeaderPaint)
        canvas.drawText("AMOUNT", amountColX, yPos, tableHeaderPaint)
        yPos += 15f
        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, linePaint)
        yPos += 25f

        // If they hid the table, we just print "Services rendered" because the
        // detailed scope of work was already printed above!
        val descText = "Services rendered."
        val descLayout = StaticLayout.Builder.obtain(descText, 0, descText.length, normalPaint, (amountColX - margin - 40).toInt()).build()

        canvas.withTranslation(x = margin, y = yPos) { descLayout.draw(this) }
        canvas.drawText("%.2f".format(data.invoice.subtotal), amountColX, yPos + (descLayout.height / 2f), normalPaint)

        yPos += descLayout.height + 40f
        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)
        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)

    } else {
        // --- DRAW FULL LINE ITEMS TABLE ---
        yPos = drawLineItemHeader(yPos)

        // (Assuming you have a helper function called drawItemSection defined somewhere above this!)
        drawItemSection("SERVICES", services)
        drawItemSection("NON-INVENTORY / MATERIALS", nonInventory)

        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)
        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)
    }
    canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)

    // --- DRAW FOOTER (check for page break first) ---
    val footerHeight = 150f
    if (yPos + footerHeight > pageHeight - bottomMargin) {
        pdfDocument.finishPage(page)
        pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
        page = pdfDocument.startPage(pageInfo)
        canvas = page.canvas
        yPos = drawPageHeader()
    }

    val totalsValueX = amountColX
    val totalsLabelX = totalsValueX - 120f
    yPos += 20
    canvas.drawText("SUBTOTAL", totalsLabelX, yPos, normalPaint)
    canvas.drawText("%.2f".format(data.invoice.subtotal), totalsValueX, yPos, normalPaint)
    yPos += 25
    canvas.drawText("TAX (${data.invoice.taxRate}%)", totalsLabelX, yPos, normalPaint)
    canvas.drawText("%.2f".format(data.invoice.taxAmount), totalsValueX, yPos, normalPaint)
    if (data.invoice.applyCreditCardFee && data.invoice.creditCardFeeAmount > 0) {
        yPos += 25
        canvas.drawText("CC FEE", totalsLabelX, yPos, normalPaint)
        canvas.drawText("%.2f".format(data.invoice.creditCardFeeAmount), totalsValueX, yPos, normalPaint)
    }
    yPos += 15
    canvas.drawLine(totalsLabelX - 20, yPos, pageWidth - margin, yPos, linePaint)
    yPos += 25

    canvas.drawText("TOTAL", totalsLabelX, yPos, headerPaint)
    canvas.drawText("$%.2f".format(data.invoice.totalAmount), totalsValueX, yPos, headerPaint)
    if (data.invoice.amountPaid > 0) {
        yPos += 25
        canvas.drawText("LESS AMOUNT PAID", totalsLabelX, yPos, normalPaint)
        canvas.drawText("-$%.2f".format(data.invoice.amountPaid), totalsValueX, yPos, normalPaint)

        yPos += 15
        canvas.drawLine(totalsLabelX - 20, yPos, pageWidth - margin, yPos, linePaint)

        yPos += 25
        val balanceDue = data.invoice.totalAmount - data.invoice.amountPaid
        canvas.drawText("BALANCE DUE", totalsLabelX, yPos, headerPaint)
        canvas.drawText("$%.2f".format(balanceDue), totalsValueX, yPos, headerPaint)
    }

    yPos += 25
    canvas.drawText("THANK YOU.", totalsLabelX + 50, yPos, smallPaint)

    if (!data.invoice.creatorName.isNullOrBlank()) {
        yPos += 20
        canvas.drawText("Prepared By: ${data.invoice.creatorName}", margin, yPos, smallPaint)
    }

    yPos = pageHeight - 100f
    if (data.invoice.signatureUrl != null) {
        try {
            val req = ImageRequest.Builder(context)
                .data(data.invoice.signatureUrl)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()
            val res = context.imageLoader.execute(req)
            val sigBmp = (res.drawable as? BitmapDrawable)?.bitmap

            if (sigBmp != null) {
                // Ensure it's software-backed
                val safeSig = if (sigBmp.config == Bitmap.Config.HARDWARE) sigBmp.copy(Bitmap.Config.ARGB_8888, false) else sigBmp

                // Scale it so it fits nicely over the signature line
                val aspectRatio = safeSig.width.toFloat() / safeSig.height.toFloat()
                val targetHeight = 60
                val targetWidth = (targetHeight * aspectRatio).toInt()
                val scaledSig = safeSig.scale(targetWidth, targetHeight, true)

                // Draw the signature directly onto the line!
                canvas.drawBitmap(scaledSig, margin + 100, yPos - targetHeight, null)
            }
        } catch (e: Exception) {
            Log.e("PDF_GEN", "Failed to load signature into PDF", e)
        }
    }

// Draw the signature lines and dates
    canvas.drawText("Accepted By", margin, yPos, normalPaint)
    canvas.drawLine(margin + 100, yPos, margin + 300, yPos, linePaint)

    canvas.drawText("Accepted Date", margin + 350, yPos, normalPaint)

    val acceptedDateStr = data.invoice.acceptedDate?.toDate()?.let {
        SimpleDateFormat("MM/dd/yyyy", Locale.US).format(it)
    } ?: ""
    canvas.drawText(acceptedDateStr, margin + 480, yPos - 5, normalPaint)
    canvas.drawLine(margin + 480, yPos, margin + 680, yPos, linePaint)

    // --- FINISH AND SAVE ---
    pdfDocument.finishPage(page)
    val file = File(context.cacheDir, "Invoice_${data.invoice.invoiceNumber}.pdf")
    try {
        FileOutputStream(file).use { pdfDocument.writeTo(it) }
        pdfDocument.close()
        return@withContext file
    } catch (e: Exception) {
        Log.e("PDF_GEN", "Failed to generate Invoice PDF", e)
        return@withContext null
    }
}
suspend fun generateEstimatePdf(
    context: Context,
    data: PdfEstimateData
): File? = withContext(Dispatchers.IO) {
    val pageHeight = 1120
    val pageWidth = 792
    val pdfDocument = PdfDocument()

    // --- PAINTS ---
    val titlePaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 28f; isFakeBoldText = true }
    val headerPaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 14f; isFakeBoldText = true }
    val normalPaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 14f }
    val smallPaint = TextPaint().apply { color = android.graphics.Color.DKGRAY; textSize = 12f }
    val whiteTextPaint = TextPaint().apply { color = android.graphics.Color.WHITE; textSize = 14f; isFakeBoldText = true }
    val tableHeaderPaint = TextPaint().apply { color = android.graphics.Color.BLACK; textSize = 12f; isFakeBoldText = true }
    val grayFillPaint = Paint().apply { color = "#E0E0E0".toColorInt(); style = Paint.Style.FILL }
    val blackFillPaint = Paint().apply { color = android.graphics.Color.BLACK; style = Paint.Style.FILL }
    val linePaint = Paint().apply{ color = android.graphics.Color.GRAY; strokeWidth = 1f }

    // --- LAYOUT CONSTANTS ---
    val margin = 50f
    val topMargin = 60f
    val bottomMargin = 120f // Extra space for signature
    val rowPadding = 30f

    // --- PAGE SETUP ---
    var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas: Canvas = page.canvas
    var yPos: Float = 0f

    // --- NEW: FETCH DYNAMIC LOGO ---
    var logoBitmap: Bitmap? = null
    try {
        if (data.companyLogoUrl != null) {
            val request = ImageRequest.Builder(context)
                .data(data.companyLogoUrl)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.DISABLED) // <-- FIX 1: Bypass the hardware-cached version
                .build()
            val result = context.imageLoader.execute(request)
            logoBitmap = (result.drawable as? BitmapDrawable)?.bitmap
        }
        // Fallback to default crewq_logo if URL is null or fetching failed
        if (logoBitmap == null) {
            logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.crewq_logo)
        }
    } catch (_: Exception) {
        logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.crewq_logo)
    }

    // --- HELPER TO DRAW THE REPEATING HEADER ---
    fun drawPageHeader(): Float {
        var logoX = pageWidth - margin

        // Draw the fetched logo proportionally
        logoBitmap?.let { bmp ->
            // <-- FIX 2: Absolute safety fallback. If it is STILL a hardware bitmap, force a software copy.
            val safeBmp = if (bmp.config == Bitmap.Config.HARDWARE) {
                bmp.copy(Bitmap.Config.ARGB_8888, false)
            } else {
                bmp
            }

            val aspectRatio = safeBmp.width.toFloat() / safeBmp.height.toFloat()
            val targetHeight = 90
            val targetWidth = (targetHeight * aspectRatio).toInt().coerceIn(1, 250)

            // Create the scaled bitmap using our guaranteed-software bitmap
            val scaledBitmap = safeBmp.scale(targetWidth, targetHeight, true)
            logoX = pageWidth - targetWidth - margin
            canvas.drawBitmap(scaledBitmap, logoX, topMargin - 40, null)
        }

        // Draw Company Info
        val profile = data.companyProfile
        val companyTitle = profile.displayName.ifBlank { data.companyName.uppercase() }

        canvas.drawText(companyTitle, margin, topMargin, headerPaint)
        canvas.drawText(profile.address, margin, topMargin + 20, normalPaint)
        canvas.drawText(profile.cityStateZip, margin, topMargin + 40, normalPaint)
        canvas.drawText(profile.phone, margin, topMargin + 60, normalPaint)
        canvas.drawText(profile.email, margin, topMargin + 80, normalPaint)

        // Draw "Estimate" Title & Number
        canvas.drawText("Estimate", 300f, 80f, titlePaint)
        val estimateNumber = data.estimate.estimateNumber
        val numberWidth = titlePaint.measureText(estimateNumber)
        canvas.drawText(estimateNumber, logoX - numberWidth - 20, 80f, titlePaint)

        // Draw Customer Info and Totals Block
        var currentY = topMargin + 140f
        canvas.drawText("ADDRESS", margin, currentY, smallPaint); currentY += 25
        canvas.drawText(data.estimate.customerName, margin, currentY, headerPaint); currentY += 20
        data.estimate.address.split(",").forEach {
            canvas.drawText(it.trim(), margin, currentY, normalPaint); currentY += 20
        }
        val addressBottomY = currentY

        val rightBoxX = pageWidth - margin - 250f
        val dateBoxRect = RectF(rightBoxX, addressBottomY - 80, rightBoxX + 125, addressBottomY - 20)
        val totalBoxRect = RectF(rightBoxX + 125, addressBottomY - 80, rightBoxX + 250, addressBottomY - 20)
        val dateStr = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(data.estimate.creationDate.toDate())

        canvas.drawRect(dateBoxRect, grayFillPaint)
        canvas.drawText("DATE", dateBoxRect.centerX() - 20, dateBoxRect.top + 20, smallPaint)
        canvas.drawText(dateStr, dateBoxRect.centerX() - 40, dateBoxRect.top + 45, normalPaint)

        canvas.drawRect(totalBoxRect, blackFillPaint)
        canvas.drawText("TOTAL", totalBoxRect.centerX() - 25, totalBoxRect.top + 20, whiteTextPaint)
        canvas.drawText("$%.2f".format(data.estimate.totalAmount), totalBoxRect.centerX() - 40, totalBoxRect.top + 45, whiteTextPaint)

        return addressBottomY + 40
    }

    // --- TABLE DRAWING HELPERS ---
    val amountColX = pageWidth - margin - 100f
    val qtyColX = amountColX - 80f
    val rateColX = qtyColX - 100f

    fun drawLineItemHeader(startY: Float): Float {
        var currentY = startY + 30f
        canvas.drawText("DESCRIPTION", margin, currentY, tableHeaderPaint)
        canvas.drawText("QTY", qtyColX, currentY, tableHeaderPaint)
        canvas.drawText("RATE", rateColX, currentY, tableHeaderPaint)
        canvas.drawText("AMOUNT", amountColX, currentY, tableHeaderPaint)
        currentY += 15f
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
        return currentY + 25f
    }

    fun drawItemSection(title: String, items: List<EstimateLineItem>) {
        if (items.isEmpty()) return
        canvas.drawText(title, margin, yPos, tableHeaderPaint)
        yPos += 20f
        items.forEach { item ->
            val textLayout = StaticLayout.Builder.obtain(item.name, 0, item.name.length, normalPaint, (rateColX - margin - 20).toInt()).build()
            canvas.withTranslation(x = margin, y = yPos) { textLayout.draw(this) }

            canvas.drawText(item.quantity.formattedQuantity, qtyColX, yPos + 12f, normalPaint)
            canvas.drawText("%.2f".format(item.unitPrice), rateColX, yPos + 12f, normalPaint)
            canvas.drawText("%.2f".format(item.quantity * item.unitPrice), amountColX, yPos + 12f, normalPaint)

            yPos += textLayout.height + 20f
        }
        yPos += 10f
    }
    // --- DRAW INITIAL HEADER ---
    yPos = drawPageHeader()

// --- FILTER LINE ITEMS ---
    val services = data.lineItems.filter { it.type == "Service" }
    val nonInventory = data.lineItems.filter { it.type != "Service" }

// --- NEW: PRINT DETAILED SCOPE OF WORK AT THE TOP ---
    val scopeOfWork = data.estimate.detailedDescription.ifBlank { data.estimate.description }
    if (scopeOfWork.isNotBlank() && scopeOfWork != "Estimate generated from CrewQ") {
        yPos += 30f
        canvas.drawText("SCOPE OF WORK", margin, yPos, tableHeaderPaint)
        yPos += 15f
        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, linePaint)
        yPos += 20f

        // Wrap the text so it doesn't run off the edge of the page
        val textLayout = StaticLayout.Builder.obtain(
            scopeOfWork, 0, scopeOfWork.length, normalPaint, (pageWidth - margin * 2).toInt()
        ).build()

        canvas.withTranslation(x = margin, y = yPos) { textLayout.draw(this) }
        yPos += textLayout.height + 30f // Add space below the paragraph
    }

// --- DRAW THE TABLE OR THE SINGLE SUMMARY LINE ---
    if (data.hideItemization) {
        // --- DRAW GENERIC SUMMARY INSTEAD OF TABLE ---
        canvas.drawText("DESCRIPTION", margin, yPos, tableHeaderPaint)
        canvas.drawText("AMOUNT", amountColX, yPos, tableHeaderPaint)
        yPos += 15f
        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, linePaint)
        yPos += 25f

        // If they hid the table, we just print "Services rendered" because the
        // detailed scope of work was already printed above!
        val descText = "Services rendered."
        val descLayout = StaticLayout.Builder.obtain(descText, 0, descText.length, normalPaint, (amountColX - margin - 40).toInt()).build()

        canvas.withTranslation(x = margin, y = yPos) { descLayout.draw(this) }
        canvas.drawText("%.2f".format(data.estimate.subtotal), amountColX, yPos + (descLayout.height / 2f), normalPaint)

        yPos += descLayout.height + 40f
        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)
        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)

    } else {
        // --- DRAW FULL LINE ITEMS TABLE ---
        yPos = drawLineItemHeader(yPos)

        drawItemSection("SERVICES", services)
        drawItemSection("NON-INVENTORY / MATERIALS", nonInventory)

        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)
        canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)
    }
    canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)

    // --- DRAW FOOTER AND SIGNATURE ---
    val footerHeight = 220f
    if (yPos + footerHeight > pageHeight - bottomMargin) {
        pdfDocument.finishPage(page)
        pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
        page = pdfDocument.startPage(pageInfo)
        canvas = page.canvas
        yPos = drawPageHeader()
    }

    val totalsValueX = amountColX
    val totalsLabelX = totalsValueX - 120f
    yPos += 20
    canvas.drawText("SUBTOTAL", totalsLabelX, yPos, normalPaint)
    canvas.drawText("%.2f".format(data.estimate.subtotal), totalsValueX, yPos, normalPaint)
    yPos += 25
    canvas.drawText("TAX (${data.estimate.taxRate}%)", totalsLabelX, yPos, normalPaint)
    canvas.drawText("%.2f".format(data.estimate.taxAmount), totalsValueX, yPos, normalPaint)
    yPos += 15
    canvas.drawLine(totalsLabelX - 20, yPos, pageWidth - margin, yPos, linePaint)
    yPos += 25
    canvas.drawText("TOTAL", totalsLabelX, yPos, headerPaint)
    canvas.drawText("$%.2f".format(data.estimate.totalAmount), totalsValueX, yPos, headerPaint)
    yPos += 25
    canvas.drawText("THANK YOU.", totalsLabelX + 50, yPos, smallPaint)

    if (!data.estimate.creatorName.isNullOrBlank()) {
        yPos += 20
        canvas.drawText("Prepared By: ${data.estimate.creatorName}", margin, yPos, smallPaint)
    }

    yPos = pageHeight - 100f
    if (data.estimate.signatureUrl != null) {
        try {
            val req = ImageRequest.Builder(context)
                .data(data.estimate.signatureUrl)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()
            val res = context.imageLoader.execute(req)
            val sigBmp = (res.drawable as? BitmapDrawable)?.bitmap

            if (sigBmp != null) {
                // Ensure it's software-backed
                val safeSig = if (sigBmp.config == Bitmap.Config.HARDWARE) sigBmp.copy(Bitmap.Config.ARGB_8888, false) else sigBmp

                // Scale it so it fits nicely over the signature line
                val aspectRatio = safeSig.width.toFloat() / safeSig.height.toFloat()
                val targetHeight = 60
                val targetWidth = (targetHeight * aspectRatio).toInt()
                val scaledSig = safeSig.scale(targetWidth, targetHeight, true)

                // Draw the signature directly onto the line!
                canvas.drawBitmap(scaledSig, margin + 100, yPos - targetHeight, null)
            }
        } catch (e: Exception) {
            Log.e("PDF_GEN", "Failed to load signature into PDF", e)
        }
    }
    canvas.drawText("Accepted By", margin, yPos, normalPaint)
    canvas.drawLine(margin + 100, yPos, margin + 300, yPos, linePaint)

    canvas.drawText("Accepted Date", margin + 350, yPos, normalPaint)

// Draw the actual date they signed it, or leave it blank
    val acceptedDateStr = data.estimate.acceptedDate?.toDate()?.let {
        SimpleDateFormat("MM/dd/yyyy", Locale.US).format(it)
    } ?: ""
    canvas.drawText(acceptedDateStr, margin + 480, yPos - 5, normalPaint)
    canvas.drawLine(margin + 480, yPos, margin + 680, yPos, linePaint)


    // --- FINISH AND SAVE ---
    pdfDocument.finishPage(page)
    val file = File(context.cacheDir, "Estimate_${data.estimate.estimateNumber}.pdf")
    try {
        FileOutputStream(file).use { outputStream -> pdfDocument.writeTo(outputStream) }
        pdfDocument.close()
        return@withContext file
    } catch (e: Exception) {
        Log.e("PDF_GEN", "Failed to generate Estimate PDF", e)
        return@withContext null
    }
}
/**
 * Parses a CSV file and imports Products and Services into Firestore.
 */
suspend fun parseCsvAndImportProducts(
    context: Context,
    fileUri: Uri,
    firestore: FirebaseFirestore,
    companyName: String
): Pair<Int, String?> {
    return withContext(Dispatchers.IO) {
        var itemsImported = 0
        try {
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val csvReader = CSVReaderBuilder(reader).withSkipLines(1).build()
                    val allRows = csvReader.readAll()
                    val batch = firestore.batch()

                    for (row in allRows) {
                        // --- FIX: Updated comment to reflect new expected types ---
                        // IMPORTANT: Adjust these indices to match your CSV file's column order.
                        // Assumes: row[0]=Name, row[1]=Description, row[2]=Price
                        // Assumes: row[3]=Type (should contain "Service" or "Non-inventory")
                        val itemName = row.getOrNull(0)?.trim()

                        if (!itemName.isNullOrBlank()) {
                            val itemData = hashMapOf(
                                "name" to itemName,
                                "description" to row.getOrNull(1)?.trim().orEmpty(),
                                "price" to (row.getOrNull(2)?.trim()?.toDoubleOrNull() ?: 0.0),
                                "type" to (row.getOrNull(3)?.trim()?.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                                    else it.toString() } ?: "Service")
                            )

                            // Target the 'productsAndServices' subcollection
                            val itemRef = firestore.collection("companies").document(companyName)
                                .collection("productsAndServices").document()
                            batch.set(itemRef, itemData)
                            itemsImported++
                        }
                    }
                    batch.commit().await()
                }
            }
            Pair(itemsImported, null) // Success
        } catch (e: Exception) {
            Log.e("PRODUCTS_CSV_IMPORT", "Error importing products CSV", e)
            Pair(0, e.message ?: "An unknown error occurred.") // Failure
        }
    }
}

private const val TAG = "JobTrackerDB"
private const val AUTH_TAG = "Auth-Firestore"


class MainActivity :  FragmentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseFunctions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        auth = Firebase.auth
        firestore = Firebase.firestore
        firebaseFunctions = Firebase.functions
        val channelId = "crewq_notifications"
        val channelName = "Job Updates"
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH // HIGH makes it pop up on screen and make a sound!
        )
        notificationManager?.createNotificationChannel(channel)
        val targetTaskId = intent.getStringExtra("taskId")
        // 👇 PASTE YOUR PUBLISHABLE KEY (pk_test_...) HERE 👇
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51THviUHefrvyxRLzwP1zpKKOwQYfQgZQipz6yC9BqdwDwcgC3RP3fO990QFGiAfBBOBASn8yPYHlccdbQkF8UHZj00p0L4EFkM"
        )

        // setContent { ... }
        setContent {
            JobTrackerTheme {
                MainApp(firebaseAnalytics, auth, firestore, firebaseFunctions, targetTaskId)
            }
        }
    }
}

@Parcelize
data class JobDocument(
    val name: String = "",
    val url: String = "",
    val type: String = "" // Optional: to show different icons
) : Parcelable

data class CompanyProfile(
    val displayName: String = "",
    val address: String = "",
    val cityStateZip: String = "",
    val phone: String = "",
    val email: String = ""
)
data class TimeEntry(
    val id: String = "",
    val clockIn: Timestamp? = null,
    val clockOut: Timestamp? = null,
    val companyName: String = "",
    val userId: String = ""
)
data class CustomerInfo(
    val id: String = "",
    val name: String = "",
    val companyName: String = "", // Add this field
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val additionalPhone: String? = null,
    val jobsiteAddresses: List<String> = emptyList(),
    val searchableName: String = "",
    val qboId: String? = null
)
data class ProductOrService(
    val id: String = "",
    val name: String = "",
    val sku: String = "",
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val incomeAccount: String = "Sales",

    // FORCING FIREBASE TO KEEP THE "is" PREFIX
    @get:PropertyName("isTaxable")
    @set:PropertyName("isTaxable")
    var isTaxable: Boolean = true,

    val type: String = "Non-inventory",

    // FORCING FIREBASE TO KEEP THE "is" PREFIX
    @get:PropertyName("isPurchased")
    @set:PropertyName("isPurchased")
    var isPurchased: Boolean = false,

    val purchasingDescription: String = "",
    val cost: Double = 0.0,
    val expenseAccount: String = "Cost of Goods Sold",
    val preferredVendor: String = "",

    val qboId: String? = null,
    val needsSync: Boolean = false
)

data class TeamTimeEntry(
    val id: String,
    val userId: String,
    val userName: String,
    val clockIn: Timestamp?,
    val clockOut: Timestamp?
)
data class ActivityLogEntry(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val action: String = "",
    val details: String = "",
    val timestamp: Timestamp? = null
)

data class UserDetails(
    val userName: String,
    val totalHours: String,
    val entries: List<TeamTimeEntry>
)

data class Estimate(
    val id: String = "",
    val estimateNumber: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerEmail: String = "",
    val address: String = "",
    val description: String = "",
    val detailedDescription: String = "",
    // Denormalized for easy list display
    val creationDate: Timestamp = Timestamp.now(),
    val status: String = "Draft",
    // e.g., "Draft", "Sent", "Approved", "Declined"
    val totalAmount: Double = 0.0, // Note: Line items will be stored in a subcollection for scalability
    val invoiceId: String? = null,
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val taxRate: Double = 0.0,
    val creatorId: String? = null,
    val creatorName: String? = null,
    val qboId: String? = null,
    val needsSync: Boolean = false,
    val signatureUrl: String? = null,
    val acceptedDate: Timestamp? = null
)
data class Invoice(
    val id: String = "",
    val invoiceNumber: String = "",
    val customerId: String = "",
    val customerName: String = "", // Denormalized for easy list display
    val customerEmail: String = "", // <-- THE FIX IS HERE
    val address: String = "",
    val description: String = "",
    val detailedDescription: String = "",
    val creationDate: Timestamp = Timestamp.now(),
    val dueDate: Timestamp = Timestamp(Calendar.getInstance().apply { add(Calendar.DATE, 30) }.time),
    val status: String = "Due",
    val totalAmount: Double = 0.0,
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val taxRate: Double = 0.0,
    val estimateId: String? = null,
    val creatorId: String? = null,
    val creatorName: String? = null,
    val qboId: String? = null,
    val needsSync: Boolean = false,
    val signatureUrl: String? = null,
    val acceptedDate: Timestamp? = null,
    val applyCreditCardFee: Boolean = false,
    val creditCardFeeAmount: Double = 0.0,
    val amountPaid: Double = 0.0,
    val paymentHistory: List<PaymentRecord> = emptyList()
)
data class PaymentRecord(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double = 0.0,
    val method: String = "", // e.g., "Check/Cash" or "Credit Card"
    val date: Timestamp = Timestamp.now(),
    val qboId: String? = null // Remembers if THIS specific payment was synced
)
data class CompanyUser(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: String = "",
    val weeklySchedule: Map<String, String> = emptyMap(),
    val canCreateDeleteEstimates: Boolean = true,
    val canCreateDeleteInvoices: Boolean = true,
    val canAddDeleteJobs: Boolean = false,
    val canAddDeleteItems: Boolean = false,
    val canCollectDispatchFee: Boolean = true,
    val canEditTimeClock: Boolean = false
)
@Parcelize
data class EstimateLineItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val quantity: Double = 1.0,
    val unitPrice: Double = 0.0,
    val type: String = "Service",
    val cost: Double = 0.0,
    @get:PropertyName("isTaxable")
    @set:PropertyName("isTaxable")
    var isTaxable: Boolean = true

): Parcelable
@Parcelize
@IgnoreExtraProperties
data class FirestoreTask(
    val id: String = "",
    val jobNumber: String = "",
    val customerId: String = "",
    val address: String = "",
    val status: String = "Pending",
    val customerName: String = "",
    val customerCompanyName: String = "",
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val date: Long = 0L,
    val timeWindow: String = "",
    val jobDescription: String = "",
    val detailedDescription: String = "",
    val notes: String = "",
    val assignedUserIds: List<String> = emptyList(),
    val assignedUserNames: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val documents: List<JobDocument> = emptyList(),
    val dispatchFeeSignatureUrl: String? = null,
    val dispatchFeeAcceptedDate: Timestamp? = null
): Parcelable
fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) {
                    result = cursor.getString(index)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result ?: "unknown_file"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    firebaseAnalytics: FirebaseAnalytics?,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    targetTaskId: String? = null
) {

    var viewingEstimateId by rememberSaveable { mutableStateOf<String?>(null) }
    var viewingInvoiceId by rememberSaveable { mutableStateOf<String?>(null) }
    var invoiceReturnScreen by rememberSaveable { mutableStateOf("viewCustomer") }
    var editingTask by remember { mutableStateOf<FirestoreTask?>(null) }
    var selectedTaskForDetails by rememberSaveable { mutableStateOf<FirestoreTask?>(null) }
    var selectedCustomerId by rememberSaveable { mutableStateOf<String?>(null) } // Add this state
    var currentScreen by rememberSaveable { mutableStateOf("signIn") }
    var refreshTrigger by remember { mutableStateOf(0) }
    var loggedInUser by rememberSaveable { mutableStateOf<String?>(null) }
    var editingInvoiceId by rememberSaveable { mutableStateOf<String?>(null) }
    var editingEstimateId by rememberSaveable { mutableStateOf<String?>(null) }
    var isAddingUser by remember { mutableStateOf(false) }
    var estimateReturnScreen by rememberSaveable { mutableStateOf("viewCustomer") }
    val selectedDate = rememberSaveable {
        mutableLongStateOf(
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        )
    }
    var userRole by rememberSaveable { mutableStateOf("") }
    var companyName by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                Log.d("Notifications", "User denied notification permission")
            }
        }
    )

    var taskForConversion by rememberSaveable { mutableStateOf<FirestoreTask?>(null) }
    var isConverting by remember { mutableStateOf(false) }

    var showDeleteEstimateDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteInvoiceDialog by rememberSaveable { mutableStateOf(false) }
    var isDuplicating by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf(auth.currentUser) }

    var subStatus by rememberSaveable { mutableStateOf("Loading...") }
    var trialDaysLeft by rememberSaveable { mutableStateOf<Int?>(null) }
    var isTrial by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            // We set up a "Snapshot Listener" which stays open in the background
            val listener = firestore.collection("user_profiles").document(currentUser!!.uid)
                .collection("subscriptions")
                .whereIn("status", listOf("trialing", "active"))
                .addSnapshotListener { snapshot, error ->
                    if (snapshot != null && !snapshot.isEmpty) {
                        // Stripe just updated the database!
                        // Incrementing this trigger forces the main Gatekeeper to re-run.
                        refreshTrigger++
                    }
                }
        }
    }

    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
        }
        auth.addAuthStateListener(authStateListener)

        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
    // --- MASTER AUTH & DATA GATEKEEPER ---
    LaunchedEffect(currentUser, refreshTrigger) {
        if (currentUser != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (isAddingUser) return@LaunchedEffect

        // 👇 FIX: Capture the state in a local, unchanging variable for Kotlin's smart caster 👇
        val user = currentUser

        if (user != null) {
            try {
                // 1. Load Profile & Company
                val profileDoc = firestore.collection("user_profiles").document(user.uid).get().await()
                val fetchedCompanyName = profileDoc.getString("companyName")

                if (!fetchedCompanyName.isNullOrBlank()) {
                    val companyDoc = firestore.collection("companies").document(fetchedCompanyName).get().await()
                    val manualStatus = companyDoc.getString("subscriptionStatus") ?: "expired"
                    val trialEndDate = companyDoc.getTimestamp("trialEndDate")?.toDate()

                    var ownerUid = companyDoc.getString("ownerUid")

                    // Safety Net 1: If ownerUid is missing, try to find the manager
                    if (ownerUid == null) {
                        try {
                            val managerSnap = firestore.collection("companies").document(fetchedCompanyName)
                                .collection("users").whereEqualTo("role", "Manager").limit(1).get().await()
                            ownerUid = if (!managerSnap.isEmpty) managerSnap.documents[0].id else user.uid
                        } catch (e: Exception) {
                            ownerUid = user.uid // Fallback if security rules block the query
                        }
                    }

                    // Safety Net 2: Stripe Subscription Check
                    var hasActiveStripeSub = false
                    var stripePeriodEnd: Date? = null

                    try {
                        val subsSnap = firestore.collection("user_profiles").document(ownerUid!!)
                            .collection("subscriptions")
                            .whereIn("status", listOf("trialing", "active"))
                            .limit(1).get().await()

                        if (!subsSnap.isEmpty) {
                            hasActiveStripeSub = true
                            stripePeriodEnd = subsSnap.documents[0].getTimestamp("current_period_end")?.toDate()
                        }
                    } catch (e: Exception) {
                        Log.w("Gatekeeper", "Could not fetch Stripe sub (likely a permission rule). Relying on company doc.", e)
                    }

                    // Calculate Days Left based on whichever query succeeded
                    if (hasActiveStripeSub) {
                        subStatus = "Active Pro Plan"
                        isTrial = false
                        if (stripePeriodEnd != null) {
                            val diff = stripePeriodEnd.time - System.currentTimeMillis()
                            trialDaysLeft = TimeUnit.MILLISECONDS.toDays(diff).toInt().coerceAtLeast(0)
                        } else {
                            trialDaysLeft = 30
                        }
                    } else if (manualStatus == "trial" || manualStatus == "active") {
                        subStatus = if (manualStatus == "trial") "Free Trial" else "Active Pro Plan"
                        isTrial = manualStatus == "trial"
                        if (trialEndDate != null) {
                            val diff = trialEndDate.time - System.currentTimeMillis()
                            trialDaysLeft = TimeUnit.MILLISECONDS.toDays(diff).toInt().coerceAtLeast(0)
                        } else {
                            trialDaysLeft = 14
                        }
                    } else {
                        subStatus = "Expired"
                        trialDaysLeft = 0
                        isTrial = false
                    }

                    // 3. User Data & Routing Logic
                    val userRef = firestore.collection("companies").document(fetchedCompanyName)
                        .collection("users").document(user.uid)
                    val userDoc = userRef.get().await()

                    if (userDoc.exists()) {
                        loggedInUser = "${userDoc.getString("firstName") ?: ""} ${userDoc.getString("lastName") ?: ""}".trim()
                        userRole = userDoc.getString("role") ?: "Technician"
                        companyName = fetchedCompanyName

                        val isPaid = hasActiveStripeSub || manualStatus == "trial" || manualStatus == "active"
                        val sharedPref = context.getSharedPreferences("CrewQPrefs", Context.MODE_PRIVATE)
                        val requireBiometrics = sharedPref.getBoolean("use_biometrics", false)

                        if (!isPaid && currentScreen != "register" && currentScreen != "signIn") {
                            currentScreen = "paywall"
                        } else if (isPaid && currentScreen == "signIn") {
                            // Safety Net 3: Routing to a specific job
                            targetTaskId?.let { nonNullTaskId ->
                                try {
                                    val taskDoc = firestore.collection("companies").document(fetchedCompanyName)
                                        .collection("tasks").document(nonNullTaskId).get().await()
                                    val task = taskDoc.toObject(FirestoreTask::class.java)?.copy(id = taskDoc.id)
                                    if (task != null) {
                                        selectedTaskForDetails = task
                                        currentScreen = "jobDetails"
                                    } else {
                                        currentScreen = if (requireBiometrics) "biometricLock" else "mainMenu"
                                    }
                                } catch (e: Exception) {
                                    currentScreen = if (requireBiometrics) "biometricLock" else "mainMenu"
                                }
                            } ?: run {
                                currentScreen = if (requireBiometrics) "biometricLock" else "mainMenu"
                            }
                        }
                    } else {
                        Toast.makeText(context, "User profile not found in company.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Company link missing.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e("AuthGatekeeper", "Data Sync Error", e)
                Toast.makeText(context, "Data Sync Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        } else {
            if (currentScreen != "register") currentScreen = "signIn"
        }
    }

    val showNavigationDrawer = currentScreen !in listOf("signIn", "register", "paywall", "biometricLock")
    val enableDrawerGestures = showNavigationDrawer && currentScreen != "teamMap"

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = enableDrawerGestures,
        drawerContent = {
            if (showNavigationDrawer) {
                AppDrawer(
                    currentScreen = currentScreen,
                    userRole = userRole,
                    onScreenSelected = { screen ->
                        currentScreen = screen
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onSignOut = {
                        scope.launch {
                            drawerState.close()
                        }
                        auth.signOut()
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                // --- DYNAMIC TOP APP BAR LOGIC ---
                val screensWithOwnAppBar = listOf(
                    "jobDetails",
                    "viewInvoice",
                    "editInvoice",
                    "newInvoice",
                    "viewEstimate",
                    "editEstimate",
                    "newEstimate"
                )
                val canGoBack = currentScreen in listOf("newTask", "upcomingJobs", "inProgressJobs")
                val showTopBar = currentScreen !in listOf("signIn", "register", "paywall", "biometricLock") + screensWithOwnAppBar
                if (showTopBar) {
                    TopAppBar(
                        title = {
                            val titleText = when (currentScreen) {
                                "jobDetails" -> "Job Details"
                                "newTask" -> if (editingTask == null) "New Task" else "Edit Task"
                                else -> currentScreen.replaceFirstChar { it.uppercase() }
                            }
                            Text(text = titleText)
                        },
                        navigationIcon = {
                            if (canGoBack) {
                                // Show back button for specific screens
                                IconButton(onClick = { currentScreen = "schedule" }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            } else {
                                // Show menu button for main screens
                                IconButton(onClick = {
                                    scope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            if (isDuplicating) {
                Dialog(onDismissRequest = { /* Prevent dismissal */ }) {
                    Card {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(16.dp))
                            Text("Duplicating Estimate...")
                        }
                    }
                }
            }
            if (isConverting) {
                Dialog(onDismissRequest = { /* Prevent dismissal while converting */ }) {
                    Card {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(16.dp))
                            Text("Converting to Invoice...")
                        }
                    }
                }
            }
            if (showDeleteEstimateDialog && viewingEstimateId != null) {
                ConfirmationDialog(
                    title = "Delete Estimate",
                    message = "Are you sure you want to permanently delete this estimate and all of its line items?",
                    onConfirm = {
                        scope.launch {
                            try {
                                val estimateRef = firestore.collection("companies").document(companyName)
                                    .collection("estimates").document(viewingEstimateId!!)

                                // Best practice: Delete subcollections in a batch
                                val lineItems = estimateRef.collection("lineItems").get().await()
                                val batch = firestore.batch()
                                lineItems.documents.forEach { doc -> batch.delete(doc.reference) }
                                batch.delete(estimateRef) // Then delete the main document
                                batch.commit().await()

                                Toast.makeText(context, "Estimate deleted", Toast.LENGTH_SHORT).show()
                                currentScreen = "viewCustomer" // Navigate back
                                viewingEstimateId = null
                            } catch (e: Exception) {
                                Log.e("DELETE_ESTIMATE", "Error deleting estimate", e)
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                showDeleteEstimateDialog = false
                            }
                        }
                    },
                    onDismiss = { showDeleteEstimateDialog = false }
                )
            }

            // --- NEW: Confirmation Dialog for Deleting Invoices ---
            if (showDeleteInvoiceDialog && viewingInvoiceId != null) {
                ConfirmationDialog(
                    title = "Delete Invoice",
                    message = "Are you sure you want to permanently delete this invoice and all of its line items?",
                    onConfirm = {
                        scope.launch {
                            try {
                                val invoiceRef = firestore.collection("companies").document(companyName)
                                    .collection("invoices").document(viewingInvoiceId!!)

                                val lineItems = invoiceRef.collection("lineItems").get().await()
                                val batch = firestore.batch()
                                lineItems.documents.forEach { doc -> batch.delete(doc.reference) }
                                batch.delete(invoiceRef)
                                batch.commit().await()

                                Toast.makeText(context, "Invoice deleted", Toast.LENGTH_SHORT).show()
                                currentScreen = "viewCustomer"
                                viewingInvoiceId = null
                            } catch (e: Exception) {
                                Log.e("DELETE_INVOICE", "Error deleting invoice", e)
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                showDeleteInvoiceDialog = false
                            }
                        }
                    },
                    onDismiss = { showDeleteInvoiceDialog = false }
                )
            }
            val screenModifier = Modifier.padding(innerPadding)
            when (currentScreen) {
                "signIn" -> SignInScreen(
                    modifier = screenModifier,
                    onSignInSuccess = {
                        firebaseAnalytics?.logEvent("sign_in", null)

                        // 👇 1. INSTANT NAVIGATION 👇
                        // Instantly move the user forward, don't wait for background checks!
                        val sharedPref = context.getSharedPreferences("CrewQPrefs", Context.MODE_PRIVATE)
                        val requireBiometrics = sharedPref.getBoolean("use_biometrics", false)
                        currentScreen = if (requireBiometrics) "biometricLock" else "mainMenu"

                        // 👇 2. FORCE DATA SYNC 👇
                        // Tell the Gatekeeper to start downloading their company data
                        refreshTrigger++
                    },
                    onRegisterClick = { currentScreen = "register" },
                    auth = auth
                )
                "tools" -> {
                    if (companyName.isNotBlank()) {
                        ToolsScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            firestore = firestore,
                            companyName = companyName,
                            userRole = userRole,
                            currentUserId = auth.currentUser?.uid ?: "",
                            currentUserName = loggedInUser ?: "Unknown"
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                    }
                }
                "mileage" -> {
                    if (companyName.isNotBlank() && auth.currentUser != null) {
                        MileageScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            firestore = firestore,
                            companyName = companyName,
                            userRole = userRole, // 👇 ADD THIS LINE 👇
                            userId = auth.currentUser!!.uid,
                            userName = loggedInUser ?: "Unknown"
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                "register" -> RegisterScreen(
                    modifier = screenModifier,
                    onRegistrationSuccess = {
                        currentScreen = "signIn"
                        firebaseAnalytics?.logEvent("sign_up", null)
                    },
                    onBackToSignIn = { currentScreen = "signIn" },
                    auth = auth,
                    firestore = firestore
                )
                "paywall" -> {
                    PaywallScreen(
                        userRole = userRole,
                        companyName = companyName, // <-- ADDED
                        firestore = firestore,     // <-- ADDED
                        onSignOut = {
                            auth.signOut()
                            currentScreen = "signIn"
                        }
                    )
                }
                "biometricLock" -> {
                    // 👇 This is the magic modern fix 👇
                    val fragmentActivity = LocalActivity.current as? FragmentActivity
                    var authFailed by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        if (fragmentActivity != null) {
                            showBiometricPrompt(
                                activity = fragmentActivity,
                                onSuccess = { currentScreen = "mainMenu" }, // Unlock successful!
                                onError = { authFailed = true }
                            )
                        } else {
                            // Fallback just in case something went wrong
                            currentScreen = "mainMenu"
                        }
                    }

                    Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Lock, contentDescription = "Locked", modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(16.dp))

                            if (authFailed) {
                                Text("App Locked", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(16.dp))
                                Button(onClick = {
                                    authFailed = false
                                    fragmentActivity?.let {
                                        showBiometricPrompt(it, { currentScreen = "mainMenu" }, { authFailed = true })
                                    }
                                }) {
                                    Text("Unlock with Biometrics")
                                }
                                Spacer(Modifier.height(8.dp))
                                TextButton(onClick = {
                                    auth.signOut()
                                    currentScreen = "signIn"
                                }) {
                                    Text("Sign Out", color = Color.Red)
                                }
                            } else {
                                Text("Unlocking...", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                            }
                        }
                    }
                }
                "mainMenu" -> MainMenu(
                    modifier = screenModifier,
                    username = loggedInUser ?: "User",
                    firestore = firestore,
                    companyName = companyName,
                    currentUserId = auth.currentUser?.uid ?: "",
                    onUpcomingJobsClick = { currentScreen = "upcomingJobs" },
                    onInProgressJobsClick = { currentScreen = "inProgressJobs" }
                )

                "schedule" -> ScheduleScreen(
                    modifier = screenModifier,
                    selectedDate = selectedDate.longValue,
                    onDateSelected = { selectedDate.longValue = it },
                    onNewTaskClick = {
                        editingTask = null
                        currentScreen = "newTask"
                    },
                    onEditTaskClick = { task ->
                        editingTask = task
                        currentScreen = "newTask"
                    },
                    onTaskClick = { task ->
                        selectedTaskForDetails = task
                        currentScreen = "jobDetails"
                    },
                    onRefresh = { refreshTrigger++ },
                    firestore = firestore,
                    companyName = companyName,
                    userRole = userRole,
                    currentUserId = auth.currentUser?.uid ?: "",
                    // --- THIS IS THE FIX ---
                    // Add the missing parameter here.
                    // This lambda increments the refreshTrigger, forcing the
                    // LaunchedEffect inside ScheduleScreen to re-run and fetch fresh data.
                    refreshTrigger = refreshTrigger
                )
                "chat" -> {
                    if (companyName.isNotBlank()) {
                        CompanyChatScreen(
                            onBack = { currentScreen = "mainMenu" },
                            firestore = firestore,
                            companyName = companyName,
                            currentUserId = auth.currentUser?.uid ?: "",
                            currentUserName = loggedInUser ?: "Unknown"
                        )
                    }
                }
                "upcomingJobs" -> {
                    UpcomingJobsScreen(
                        modifier = screenModifier,
                        onJobClick = { task ->
                            selectedTaskForDetails = task
                            currentScreen = "jobDetails"
                        },
                        firestore = firestore,
                        companyName = companyName,
                        currentUserId = auth.currentUser?.uid ?: ""
                    )
                }

                "inProgressJobs" -> {
                    InProgressJobsScreen(
                        modifier = screenModifier,
                        onJobClick = { task ->
                            selectedTaskForDetails = task
                            currentScreen = "jobDetails"
                        },
                        firestore = firestore,
                        companyName = companyName,
                        currentUserId = auth.currentUser?.uid ?: ""
                    )
                }
                "jobDetails" -> {
                    selectedTaskForDetails?.let { task ->
                        JobDetailsScreen(
                            task = task,
                            modifier = screenModifier,
                            firestore = firestore,
                            functions = functions,
                            companyName = companyName,
                            onBack = { currentScreen = "schedule" },
                            onNotesUpdated = { updatedNotes ->
                                selectedTaskForDetails = selectedTaskForDetails?.copy(notes = updatedNotes)
                                refreshTrigger++
                            },
                            onStatusUpdated = { updatedStatus ->
                                selectedTaskForDetails = selectedTaskForDetails?.copy(status = updatedStatus)
                                refreshTrigger++
                            },
                            onCreateEstimate = { taskToConvert ->
                                taskForConversion = taskToConvert
                                currentScreen = "newEstimate"
                            },
                            onCreateInvoice = { taskToConvert ->
                                taskForConversion = taskToConvert
                                currentScreen = "newInvoice"
                            },
                            // --- NEW: Add the navigation logic here ---
                            onEstimateClick = { estimateId ->
                                // Set the customer context so the back button from ViewEstimateScreen works correctly
                                selectedCustomerId = task.customerId
                                viewingEstimateId = estimateId
                                estimateReturnScreen = "jobDetails"
                                currentScreen = "viewEstimate"
                            },
                            onInvoiceClick = { invoiceId ->
                                // Set the customer context so the back button from ViewInvoiceScreen works correctly
                                selectedCustomerId = task.customerId
                                viewingInvoiceId = invoiceId
                                invoiceReturnScreen = "jobDetails"
                                currentScreen = "viewInvoice"
                            }
                        )
                    }
                }

                "newTask" -> NewTaskScreen(
                    modifier = screenModifier,
                    selectedDate = selectedDate.longValue,
                    editingTask = editingTask,
                    firebaseAnalytics = firebaseAnalytics,
                    onTaskCreated = {
                        editingTask = null
                        currentScreen = "schedule"
                    },
                    firestore = firestore,
                    companyName = companyName,
                    currentUserId = auth.currentUser?.uid ?: "",
                    currentUserName = loggedInUser ?: "Unknown"
                )

                "timeClock" -> {
                    val currentUserId = auth.currentUser?.uid
                    val currentUserName = loggedInUser
                    if (companyName.isNotBlank() && !currentUserId.isNullOrBlank() && !currentUserName.isNullOrBlank()) {
                        TimeClockScreen(
                            modifier = screenModifier,
                            onBackToMenu = { currentScreen = "mainMenu" },
                            firestore = firestore,
                            companyName = companyName,
                            userRole = userRole,
                            userId = currentUserId,
                            userName = currentUserName
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

                // ... Other screen cases from your project ...

                "teamMap" -> TeamMapScreen(
                    modifier = screenModifier,
                    firestore = firestore,
                    companyName = companyName
                )

                "estimates" -> EstimatesScreen(
                    onBackToMenu = { currentScreen = "mainMenu" },
                    onNewEstimateClick = { currentScreen = "newEstimate" },
                    onEstimateClick = { estimateId ->
                        viewingEstimateId = estimateId
                        estimateReturnScreen = "estimates"
                        currentScreen = "viewEstimate"
                    },
                    onEditEstimateClick = { estimateId ->
                        editingEstimateId = estimateId
                        currentScreen = "editEstimate"
                    },
                    onEstimateConverted = { currentScreen = "invoices" },
                    firestore = firestore,
                    companyName = companyName,
                    currentUserId = auth.currentUser?.uid ?: "",
                    currentUserName = loggedInUser ?: "Unknown"
                )

                "newEstimate" -> NewEstimateScreen(
                    onBackToEstimates = {
                        taskForConversion = null // Clear the state when navigating away
                        currentScreen = "estimates"
                    },
                    onEstimateCreated = {
                        taskForConversion = null // Clear the state on success
                        currentScreen = "estimates"
                    },
                    firestore = firestore,
                    companyName = companyName,
                    currentUserId = auth.currentUser?.uid ?: "",
                    currentUserName = loggedInUser ?: "Unknown",
                    // Pass the task to the screen
                    taskToPrePopulate = taskForConversion
                )

                "teamTimeClock" -> TeamTimeClockScreen(
                    modifier = screenModifier,
                    onBackToMenu = { currentScreen = "mainMenu" },
                    firestore = firestore,
                    companyName = companyName
                )

                "settings" -> SettingsScreen(
                    modifier = screenModifier,
                    onBackToMenu = { currentScreen = "mainMenu" },
                    subStatus = subStatus,            // Variable defined in MainApp
                    daysLeft = trialDaysLeft,         // Variable defined in MainApp
                    isTrial = isTrial,                // Variable defined in MainApp
                    auth = auth,                      // Pass the auth object
                    firestore = firestore,            // Pass the firestore object
                    functions = functions,            // Pass the functions object
                    companyName = companyName,        // Pass the company name string
                    userRole = userRole,              // Pass the user role string
                    onAddingUserChange = { isAddingUser = it },
                    onNavigateToAuditLog = { currentScreen = "activityLog" }
                )
                "activityLog" -> {
                    if (companyName.isNotBlank() && userRole == "Manager") {
                        ActivityLogScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            firestore = firestore,
                            companyName = companyName
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                    }
                }

                "customers" -> {
                    if (companyName.isNotBlank()) {
                        CustomersScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            onCustomerClick = { customerId ->
                                selectedCustomerId = customerId
                                currentScreen = "viewCustomer"
                            },
                            firestore = firestore,
                            companyName = companyName,
                            currentUserId = auth.currentUser?.uid ?: "",
                            currentUserName = loggedInUser ?: "Unknown"
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator() }
                    }
                }
                "viewCustomer" -> {
                    selectedCustomerId?.let { customerId ->
                        ViewCustomerScreen(
                            customerId = customerId,
                            firestore = firestore,
                            functions = functions,
                            companyName = companyName,
                            onBack = { currentScreen = "customers" },
                            onEstimateClick = { estimateId ->
                                viewingEstimateId = estimateId
                                estimateReturnScreen = "viewCustomer"
                                currentScreen = "viewEstimate"
                            },
                            onInvoiceClick = { invoiceId ->
                                viewingInvoiceId = invoiceId
                                invoiceReturnScreen = "viewCustomer"
                                currentScreen = "viewInvoice"
                            },
                            onJobClick = { task ->
                                selectedTaskForDetails = task
                                currentScreen = "jobDetails"
                            },
                            onDuplicateEstimateClick = { estimateIdToDuplicate, onDuplicationComplete ->
                                scope.launch {
                                    isDuplicating = true
                                    try {
                                        // 1. Get references to the original estimate
                                        val originalEstimateRef = firestore.collection("companies").document(companyName)
                                            .collection("estimates").document(estimateIdToDuplicate)

                                        val originalEstimateDoc = originalEstimateRef.get().await()
                                        val originalEstimate = originalEstimateDoc.toObject(Estimate::class.java)
                                            ?: throw IllegalStateException("Original estimate not found")

                                        val originalLineItems = originalEstimateRef.collection("lineItems").get().await()

                                        // 2. Get a new estimate number and create the new estimate document within a transaction
                                        val companyRef = firestore.collection("companies").document(companyName)
                                        val newEstimateRef = companyRef.collection("estimates").document()
                                        // This counter document lives in a 'metadata' subcollection for organization
                                        val counterRef = companyRef.collection("metadata").document("estimateCounter")

                                        val newEstimateNumber = firestore.runTransaction { transaction ->
                                            val counterDoc = transaction.get(counterRef)
                                            val lastNumber = counterDoc.getLong("lastEstimateNumber") ?: 9999L // Start at 10000
                                            val newNumber = lastNumber + 1

                                            // Create the new estimate object with updated fields
                                            val newEstimate = originalEstimate.copy(
                                                id = newEstimateRef.id,
                                                estimateNumber = newNumber.toString(),
                                                creationDate = Timestamp.now(),
                                                status = "Draft", // Always reset status to Draft
                                                invoiceId = null  // A duplicated estimate is not yet converted
                                            )
                                            // Set the new estimate document within the transaction
                                            transaction.set(newEstimateRef, newEstimate)

                                            // Update the counter document within the transaction
                                            if (counterDoc.exists()) {
                                                transaction.update(counterRef, "lastEstimateNumber", newNumber)
                                            } else {
                                                // Create the counter if it doesn't exist
                                                transaction.set(counterRef, mapOf("lastEstimateNumber" to newNumber))
                                            }

                                            // The transaction returns the new number for our toast message
                                            newNumber.toString()
                                        }.await()

                                        // 3. Use a batched write to copy all line items to the new estimate
                                        val batch = firestore.batch()
                                        originalLineItems.documents.forEach { doc ->
                                            val newLineItemRef = newEstimateRef.collection("lineItems").document()
                                            // Copy the data from the old line item to the new one
                                            batch.set(newLineItemRef, doc.data!!)
                                        }
                                        batch.commit().await()

                                        Toast.makeText(context, "Estimate #${originalEstimate.estimateNumber} duplicated to #${newEstimateNumber}", Toast.LENGTH_LONG).show()
                                        onDuplicationComplete() // Triggers a refresh on the ViewCustomerScreen

                                    } catch (e: Exception) {
                                        Log.e("DUPLICATE_ESTIMATE", "Error duplicating estimate", e)
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isDuplicating = false
                                    }
                                }
                            }
                        )
                    }
                }
                "viewEstimate" -> {
                    viewingEstimateId?.let { estimateId ->
                        ViewEstimateScreen(
                            estimateId = estimateId,
                            firestore = firestore,
                            functions = functions,
                            companyName = companyName,
                            onBack = { currentScreen = estimateReturnScreen },
                            onEditClick = {
                                editingEstimateId = estimateId
                                currentScreen = "editEstimate"
                            },
                            onConvertToInvoiceClick = {
                                scope.launch {
                                    isConverting = true
                                    try {
                                        val estimateDoc = firestore.collection("companies").document(companyName)
                                            .collection("estimates").document(estimateId).get().await()
                                        val estimate = estimateDoc.toObject(Estimate::class.java)?.copy(id = estimateDoc.id)

                                        if (estimate == null || estimate.invoiceId != null) {
                                            Toast.makeText(context, "Cannot convert this estimate.", Toast.LENGTH_SHORT).show()
                                            return@launch
                                        }

                                        val companyRef = firestore.collection("companies").document(companyName)
                                        val newInvoiceRef = companyRef.collection("invoices").document()
                                        val counterRef = companyRef.collection("metadata").document("invoiceCounter")

                                        // 👇 1. Capture the new invoice number from the transaction 👇
                                        val newInvoiceNumber = firestore.runTransaction { transaction ->
                                            val counterDoc = transaction.get(counterRef)
                                            val lastNumber = counterDoc.getLong("lastInvoiceNumber") ?: 9999L
                                            val newNumber = lastNumber + 1

                                            val newInvoice = Invoice(
                                                id = newInvoiceRef.id,
                                                invoiceNumber = newNumber.toString(),
                                                customerId = estimate.customerId,
                                                customerName = estimate.customerName,
                                                customerEmail = estimate.customerEmail,
                                                address = estimate.address,
                                                description = estimate.description,
                                                detailedDescription = estimate.detailedDescription,
                                                subtotal = estimate.subtotal,
                                                taxAmount = estimate.taxAmount,
                                                taxRate = estimate.taxRate,
                                                totalAmount = estimate.totalAmount,
                                                estimateId = estimate.id,
                                                creatorId = auth.currentUser?.uid ?: "",
                                                creatorName = loggedInUser ?: "Unknown"
                                            )
                                            transaction.set(newInvoiceRef, newInvoice)

                                            if (counterDoc.exists()) {
                                                transaction.update(counterRef, "lastInvoiceNumber", newNumber)
                                            } else {
                                                transaction.set(counterRef, mapOf("lastInvoiceNumber" to newNumber))
                                            }

                                            // 👇 2. Return the number here 👇
                                            newNumber
                                        }.await()

                                        val lineItemsSnapshot = firestore.collection("companies").document(companyName)
                                            .collection("estimates").document(estimate.id)
                                            .collection("lineItems").get().await()

                                        val batch = firestore.batch()
                                        lineItemsSnapshot.documents.forEach { doc ->
                                            val newLineItemRef = newInvoiceRef.collection("lineItems").document()
                                            batch.set(newLineItemRef, doc.data!!)
                                        }

                                        val estimateRef = firestore.collection("companies").document(companyName)
                                            .collection("estimates").document(estimate.id)
                                        batch.update(estimateRef, mapOf(
                                            "status" to "Converted",
                                            "invoiceId" to newInvoiceRef.id
                                        ))

                                        batch.commit().await()

                                        Toast.makeText(context, "Converted to Invoice successfully!", Toast.LENGTH_SHORT).show()

                                        // 👇 3. Log the conversion using the captured number 👇
                                        logCompanyActivity(
                                            firestore, companyName, auth.currentUser?.uid ?: "", loggedInUser ?: "Unknown",
                                            "CONVERT_ESTIMATE",
                                            "Converted Estimate #${estimate.estimateNumber} into Invoice #$newInvoiceNumber"
                                        )

                                        currentScreen = "invoices"
                                        viewingEstimateId = null

                                    } catch (e: Exception) {
                                        Log.e("CONVERSION", "Error converting from view screen: ${e.message}", e)
                                        Toast.makeText(context, "Conversion failed: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isConverting = false
                                    }
                                }
                            },
                            onDeleteClick = {
                                showDeleteEstimateDialog = true
                                            },
                            onEmailClick = { estimate, lineItems ->
                                scope.launch {
                                    try {
                                        val companyDoc = firestore.collection("companies").document(companyName).get().await()
                                        val logoUrl = companyDoc.getString("logoUrl")
                                        val hideItemization = companyDoc.getBoolean("hideItemization") ?: false
                                        val profile = CompanyProfile(
                                            displayName = companyDoc.getString("displayName") ?: companyName,
                                            address = companyDoc.getString("address") ?: "",
                                            cityStateZip = companyDoc.getString("cityStateZip") ?: "",
                                            phone = companyDoc.getString("phone") ?: "",
                                            email = companyDoc.getString("email") ?: ""
                                        )
                                        val pdfData = PdfEstimateData(estimate, lineItems, companyName, logoUrl, profile, hideItemization)

                                        // 1. Generate the file
                                        val file = generateEstimatePdf(context, pdfData)

                                        if (file != null) {
                                            firestore.collection("companies").document(companyName)
                                                .collection("estimates").document(estimate.id)
                                                .update("status", "Sent")
                                            // 2. Fire the Email Intent
                                            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                                            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "application/pdf"
                                                putExtra(Intent.EXTRA_EMAIL, arrayOf(estimate.customerEmail))
                                                putExtra(Intent.EXTRA_SUBJECT, "Estimate #${estimate.estimateNumber}")
                                                putExtra(Intent.EXTRA_TEXT, "Please find your estimate attached.")
                                                putExtra(Intent.EXTRA_STREAM, uri)
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(emailIntent, "Send Estimate via..."))
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },

                            onViewPdfClick = { estimate, lineItems ->
                                scope.launch {
                                    try {
                                        val companyDoc = firestore.collection("companies").document(companyName).get().await()
                                        val logoUrl = companyDoc.getString("logoUrl")
                                        val hideItemization = companyDoc.getBoolean("hideItemization") ?: false
                                        val profile = CompanyProfile(
                                            displayName = companyDoc.getString("displayName") ?: companyName,
                                            address = companyDoc.getString("address") ?: "",
                                            cityStateZip = companyDoc.getString("cityStateZip") ?: "",
                                            phone = companyDoc.getString("phone") ?: "",
                                            email = companyDoc.getString("email") ?: ""
                                        )
                                        val pdfData = PdfEstimateData(estimate, lineItems, companyName, logoUrl, profile, hideItemization)

                                        // 1. Generate the file
                                        val file = generateEstimatePdf(context, pdfData)

                                        if (file != null) {
                                            // 2. Fire the View Intent!
                                            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                                            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                                                setDataAndType(uri, "application/pdf")
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(viewIntent, "View PDF"))
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error viewing PDF: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        )
                    } ?: run {
                        Text("Error: No Estimate ID provided. Navigating back.")
                        LaunchedEffect(Unit) {
                            delay(2000)
                            currentScreen = "estimates"
                        }
                    }
                }

                "viewInvoice" -> {
                    viewingInvoiceId?.let { invoiceId ->
                        ViewInvoiceScreen(
                            invoiceId = invoiceId,
                            firestore = firestore,
                            functions = functions,
                            companyName = companyName,
                            onBack = { currentScreen = invoiceReturnScreen },
                            onEditClick = {
                                editingInvoiceId = invoiceId
                                currentScreen = "editInvoice"
                            },
                            onEmailClick = { invoice, lineItems ->
                                scope.launch {
                                    try {
                                        val companyDoc = firestore.collection("companies").document(companyName).get().await()
                                        val logoUrl = companyDoc.getString("logoUrl")
                                        val hideItemization = companyDoc.getBoolean("hideItemization") ?: false
                                        val profile = CompanyProfile(
                                            displayName = companyDoc.getString("displayName") ?: companyName,
                                            address = companyDoc.getString("address") ?: "",
                                            cityStateZip = companyDoc.getString("cityStateZip") ?: "",
                                            phone = companyDoc.getString("phone") ?: "",
                                            email = companyDoc.getString("email") ?: ""
                                        )

                                        // 👇 GUARANTEED INVOICE DATA 👇
                                        val pdfData = PdfInvoiceData(invoice, lineItems, companyName, logoUrl, profile, hideItemization)

                                        // 👇 GUARANTEED INVOICE GENERATOR 👇
                                        val file = generateInvoicePdf(context, pdfData)

                                        if (file != null) {
                                            firestore.collection("companies").document(companyName)
                                                .collection("invoices").document(invoice.id)
                                                .update("status", "Sent")

                                            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                                            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "application/pdf"
                                                putExtra(Intent.EXTRA_EMAIL, arrayOf(invoice.customerEmail))
                                                putExtra(Intent.EXTRA_SUBJECT, "Service Invoice #${invoice.invoiceNumber}")
                                                putExtra(Intent.EXTRA_TEXT, "Please find your invoice attached.")
                                                putExtra(Intent.EXTRA_STREAM, uri)
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(emailIntent, "Send Invoice via..."))
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onViewPdfClick = { invoice, lineItems ->
                                scope.launch {
                                    try {
                                        val companyDoc = firestore.collection("companies").document(companyName).get().await()
                                        val logoUrl = companyDoc.getString("logoUrl")
                                        val hideItemization = companyDoc.getBoolean("hideItemization") ?: false
                                        val profile = CompanyProfile(
                                            displayName = companyDoc.getString("displayName") ?: companyName,
                                            address = companyDoc.getString("address") ?: "",
                                            cityStateZip = companyDoc.getString("cityStateZip") ?: "",
                                            phone = companyDoc.getString("phone") ?: "",
                                            email = companyDoc.getString("email") ?: ""
                                        )

                                        // 👇 GUARANTEED INVOICE DATA 👇
                                        val pdfData = PdfInvoiceData(invoice, lineItems, companyName, logoUrl, profile, hideItemization)

                                        // 👇 GUARANTEED INVOICE GENERATOR 👇
                                        val file = generateInvoicePdf(context, pdfData)

                                        if (file != null) {
                                            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                                            val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                                                setDataAndType(uri, "application/pdf")
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(viewIntent, "View PDF"))
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onDeleteClick = { showDeleteInvoiceDialog = true }
                        )
                    } ?: run {
                        Text("Error: No Invoice ID provided. Navigating back.")
                        LaunchedEffect(Unit) {
                            delay(2000)
                            currentScreen = "customers"
                        }
                    }
                }
                "accounting" -> {
                    if (companyName.isNotBlank()) {
                        AccountingScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            onEstimateClick = { estimateId ->
                                viewingEstimateId = estimateId
                                estimateReturnScreen = "accounting" // Ensures back button works!
                                currentScreen = "viewEstimate"
                            },
                            onInvoiceClick = { invoiceId ->
                                viewingInvoiceId = invoiceId
                                invoiceReturnScreen = "accounting" // Ensures back button works!
                                currentScreen = "viewInvoice"
                            },
                            firestore = firestore,
                            functions = functions,
                            companyName = companyName,
                            userRole = userRole
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                "productsAndServices" -> {
                    if (companyName.isNotBlank()) {
                        ProductsAndServicesScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            firestore = firestore,
                            functions = functions,
                            companyName = companyName,
                            userRole = userRole
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

                "editEstimate" -> {
                    editingEstimateId?.let { id ->
                        EditEstimateScreen(
                            estimateId = id,
                            onBackToEstimates = { currentScreen = "viewEstimate" },
                            onEstimateSaved = { currentScreen = "viewEstimate" },
                            firestore = firestore,
                            companyName = companyName
                        )
                    } ?: run {
                        Text("Error: No Estimate ID provided. Navigating back.")
                        LaunchedEffect(Unit) {
                            delay(2000)
                            currentScreen = "estimates"
                        }
                    }
                }

                "invoices" -> {
                    if (companyName.isNotBlank()) {
                        InvoicesScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            onNewInvoiceClick = { currentScreen = "newInvoice" },
                            onInvoiceClick = { invoiceId ->            // <-- Handle click
                                viewingInvoiceId = invoiceId
                                invoiceReturnScreen = "invoices"       // <-- Set return path
                                currentScreen = "viewInvoice"
                            },
                            onEditInvoiceClick = { invoiceId ->
                                editingInvoiceId = invoiceId
                                currentScreen = "editInvoice"
                            },
                            firestore = firestore,
                            companyName = companyName
                        )
                    } else {
                        Box(modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

                "newInvoice" -> NewInvoiceScreen(
                    onBackToInvoices = {
                        taskForConversion = null // Clear the state when navigating away
                        currentScreen = "invoices"
                    },
                    onInvoiceCreated = {
                        taskForConversion = null // Clear the state on success
                        currentScreen = "invoices"
                    },
                    firestore = firestore,
                    companyName = companyName,
                    currentUserId = auth.currentUser?.uid ?: "",
                    currentUserName = loggedInUser ?: "Unknown",
                    // Pass the task to the screen
                    taskToPrePopulate = taskForConversion
                )

                "editInvoice" -> {
                    editingInvoiceId?.let { id ->
                        EditInvoiceScreen(
                            invoiceId = id,
                            onBackToInvoices = { currentScreen = "viewInvoice" },
                            onInvoiceSaved = { currentScreen = "viewInvoice" },
                            firestore = firestore,
                            companyName = companyName
                        )
                    } ?: run {
                        Text("Error: No Invoice ID provided. Navigating back.")
                        LaunchedEffect(Unit) {
                            delay(2000)
                            currentScreen = "invoices"
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppDrawer(
    currentScreen: String,
    userRole: String,
    onScreenSelected: (String) -> Unit,
    onSignOut: () -> Unit
) {
    ModalDrawerSheet {
        // 👇 Wrap everything in a Column with verticalScroll enabled
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp) // Standard Material 3 padding
        ) {
            Spacer(Modifier.height(12.dp))

            NavigationDrawerItem(
                label = { Text("Main Menu") },
                selected = currentScreen == "mainMenu",
                onClick = { onScreenSelected("mainMenu") }
            )
            NavigationDrawerItem(
                label = { Text("Schedule") },
                selected = currentScreen == "schedule",
                onClick = { onScreenSelected("schedule") }
            )
            NavigationDrawerItem(
                label = { Text("Company Chat") },
                selected = currentScreen == "chat",
                onClick = { onScreenSelected("chat") }
            )
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.estimates)) },
                selected = currentScreen == "estimates",
                onClick = { onScreenSelected("estimates") }
            )
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.invoices)) },
                selected = currentScreen == "invoices",
                onClick = { onScreenSelected("invoices") }
            )
            NavigationDrawerItem(
                label = { Text("Customers") },
                selected = currentScreen == "customers",
                onClick = { onScreenSelected("customers") }
            )
            NavigationDrawerItem(
                label = { Text("Products & Services") },
                selected = currentScreen == "productsAndServices",
                onClick = { onScreenSelected("productsAndServices") }
            )
            NavigationDrawerItem(
                label = { Text("Tool Catalog") },
                selected = currentScreen == "tools",
                onClick = { onScreenSelected("tools") }
            )
            NavigationDrawerItem(
                label = { Text("Time Clock") },
                selected = currentScreen == "timeClock",
                onClick = { onScreenSelected("timeClock") }
            )
            NavigationDrawerItem(
                label = { Text("Mileage Tracker") },
                selected = currentScreen == "mileage",
                onClick = { onScreenSelected("mileage") }
            )

            if (userRole == "Manager") {
                NavigationDrawerItem(
                    label = { Text("Team Time Clock") },
                    selected = currentScreen == "teamTimeClock",
                    onClick = { onScreenSelected("teamTimeClock") }
                )
                NavigationDrawerItem(
                    label = { Text("Team Map") },
                    selected = currentScreen == "teamMap",
                    onClick = { onScreenSelected("teamMap") }
                )
                NavigationDrawerItem(
                    label = { Text("Accounting (Sync)") },
                    selected = currentScreen == "accounting",
                    onClick = { onScreenSelected("accounting") }
                )
            }
            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = currentScreen == "settings",
                onClick = { onScreenSelected("settings") }
            )

            // 👇 Added extra space before Sign Out
            Spacer(modifier = Modifier.height(24.dp))

            NavigationDrawerItem(
                label = { Text("Sign Out", color = Color.Red) },
                selected = false,
                onClick = onSignOut
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInSuccess: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    auth: FirebaseAuth? = null
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.crewq_logo),
            contentDescription = "CrewQ Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Sign In", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { showForgotPasswordDialog = true }) {
            Text("Forgot Password?", color = MaterialTheme.colorScheme.primary)
        }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            // 2. Add ImeAction.Next so the keyboard has a "Next" button
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            // 3. Add ImeAction.Done for the password field
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    auth?.signInWithEmailAndPassword(email, password)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Sign In Successful!", Toast.LENGTH_SHORT).show()
                                onSignInSuccess(email)
                            } else {
                                Toast.makeText(context, "Sign In Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text("Don't have an account? Register")
        }
    }
    if (showForgotPasswordDialog) {
        var resetEmail by remember { mutableStateOf("") }
        var isSending by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("Reset Password", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter the email address associated with your account. We will send you a secure link to reset your password.", color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = textFieldColors
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (resetEmail.isNotBlank()) {
                            isSending = true
                            auth?.sendPasswordResetEmail(resetEmail.trim())
                                ?.addOnCompleteListener { task ->
                                    isSending = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_LONG).show()
                                        showForgotPasswordDialog = false
                                    } else {
                                        Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isSending
                ) {
                    if (isSending) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Send Link")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegistrationSuccess: () -> Unit = {},
    onBackToSignIn: () -> Unit = {},
    auth: FirebaseAuth,
    firestore: FirebaseFirestore
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var companyName by rememberSaveable { mutableStateOf("") }

    // State to track password visibility
    var passwordVisible by rememberSaveable() { mutableStateOf(false) }

    val isFormValid = remember(email, password, firstName, lastName, companyName) {
        email.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank() && companyName.isNotBlank()
    }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp)
            // Add imePadding and verticalScroll to handle the keyboard covering fields
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Account", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        // 1. Create the user in Firebase Authentication FIRST
                        val authResult = auth.createUserWithEmailAndPassword(email.trim(), password).await()
                        val user = authResult.user

                        if (user != null) {
                            // 2. Now write their profile to Firestore
                            val batch = firestore.batch()

                            // Format the company name to be a safe Document ID (lowercase, no spaces/special chars)
                            val safeCompanyId = companyName.trim().lowercase().replace(Regex("[^a-z0-9]"), "")

                            // The company document (NOW WITH SUBSCRIPTION STATUS)
                            val calendar = Calendar.getInstance()
                            calendar.add(Calendar.DAY_OF_YEAR, 14)

                            val companyRef = firestore.collection("companies").document(safeCompanyId)
                            val companyData = mapOf(
                                "name" to companyName.trim(), // Keep the original formatted name here
                                "subscriptionStatus" to "trial", // Explicitly start them on a trial!
                                "ownerUid" to user.uid,
                                "trialEndDate" to Timestamp(calendar.time)
                            )
                            batch.set(companyRef, companyData)

                            // The user document inside the company
                            val userRef = companyRef.collection("users").document(user.uid)
                            val userData = hashMapOf(
                                "uid" to user.uid,
                                "firstName" to firstName.trim(),
                                "lastName" to lastName.trim(),
                                "email" to email.trim(),
                                "role" to "Manager",
                                "companyName" to safeCompanyId // Link them to the safe ID
                            )
                            batch.set(userRef, userData)

                            // NEW: The top-level user_profile document required by Stripe
                            val profileRef = firestore.collection("user_profiles").document(user.uid)
                            val profileData = mapOf("companyName" to safeCompanyId)
                            batch.set(profileRef, profileData)

                            batch.commit().await()

                            // Success!
                            Toast.makeText(context, "Registration successful! Please sign in.", Toast.LENGTH_LONG).show()
                            onRegistrationSuccess()
                        }
                    } catch (authException: Exception) {
                        if (authException is CancellationException) throw authException
                        Toast.makeText(context, "Registration Failed: ${authException.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackToSignIn,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text("Already have an account? Sign In")
        }
    }
}

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    username: String,
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String,
    onUpcomingJobsClick: () -> Unit = {},
    onInProgressJobsClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // --- DASHBOARD STATE ---
    var inProgressCount by remember { mutableIntStateOf(0) }
    var futureCount by remember { mutableIntStateOf(0) }
    var monthlyRevenue by remember { mutableDoubleStateOf(0.0) }
    var laborRevenue by remember { mutableDoubleStateOf(0.0) }
    var productRevenue by remember { mutableDoubleStateOf(0.0) }
    var isDashboardLoading by remember { mutableStateOf(true) }

    // --- DEV NOTES STATE ---
    var developmentNotes by rememberSaveable { mutableStateOf("") }
    var isNotesLoading by remember { mutableStateOf(true) }
    var isInEditMode by rememberSaveable { mutableStateOf(false) }

    // --- TIME/DATE FORMATTING ---
    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = remember(currentHour) {
        when (currentHour) {
            in 0..11 -> "Good morning,"
            in 12..16 -> "Good afternoon,"
            else -> "Good evening,"
        }
    }
    val displayDate = remember { SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date()) }

    // --- FETCH DATA ---
    LaunchedEffect(companyName, currentUserId) {
        if (companyName.isNotBlank() && currentUserId.isNotBlank()) {
            isDashboardLoading = true
            isNotesLoading = true

            try {
                val doc = firestore.collection("companies").document(companyName).get().await()
                developmentNotes = doc.getString("developmentNotes") ?: ""
            } catch (e: Exception) {
                Log.e("MainMenu", "Error loading notes", e)
            } finally {
                isNotesLoading = false
            }

            try {
                val now = Calendar.getInstance()
                val todayCal = now.clone() as Calendar
                todayCal.set(Calendar.HOUR_OF_DAY, 0); todayCal.set(Calendar.MINUTE, 0); todayCal.set(Calendar.SECOND, 0)
                val startOfToday = Timestamp(todayCal.time)

                val monthCal = now.clone() as Calendar
                monthCal.set(Calendar.DAY_OF_MONTH, 1); monthCal.set(Calendar.HOUR_OF_DAY, 0); monthCal.set(Calendar.MINUTE, 0)
                val startOfMonth = Timestamp(monthCal.time)

                val inProgressSnap = firestore.collection("companies").document(companyName).collection("tasks")
                    .whereEqualTo("status", "In Progress")
                    .whereArrayContains("assignedUserIds", currentUserId)
                    .get().await()
                inProgressCount = inProgressSnap.size()

                val futureSnap = firestore.collection("companies").document(companyName).collection("tasks")
                    .whereArrayContains("assignedUserIds", currentUserId)
                    .whereGreaterThanOrEqualTo("startDate", startOfToday)
                    .get().await()

                val futureDocs = futureSnap.documents.mapNotNull { it.toObject(FirestoreTask::class.java) }
                futureCount = futureDocs.count { it.status != "Completed" && it.status != "Canceled" }

                val invoicesSnap = firestore.collection("companies").document(companyName).collection("invoices")
                    .whereEqualTo("creatorId", currentUserId)
                    .whereGreaterThanOrEqualTo("creationDate", startOfMonth)
                    .get().await()

                var totalRev = 0.0
                var laborRev = 0.0
                var prodRev = 0.0

                for (doc in invoicesSnap.documents) {
                    val inv = doc.toObject(Invoice::class.java)
                    if (inv != null) totalRev += inv.subtotal

                    val linesSnap = doc.reference.collection("lineItems").get().await()
                    for (lineDoc in linesSnap.documents) {
                        val line = lineDoc.toObject(EstimateLineItem::class.java)
                        if (line != null) {
                            val lineTotal = line.quantity * line.unitPrice
                            if (line.type == "Service") laborRev += lineTotal else prodRev += lineTotal
                        }
                    }
                }
                monthlyRevenue = totalRev
                laborRevenue = laborRev
                productRevenue = prodRev

            } catch (e: Exception) {
                Log.e("MainMenu", "Error loading dashboard", e)
            } finally {
                isDashboardLoading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(24.dp), // Increased padding for a more spacious, premium feel
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- PREMIUM HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.crewq_logo),
                contentDescription = "CrewQ Logo",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = greeting, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Text(text = username, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = displayDate, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isDashboardLoading) {
            CircularProgressIndicator()
        } else {
            // --- JOB METRICS WITH ICONS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // In Progress Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(16.dp), // Softer corners
                    onClick = onInProgressJobsClick
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start // Left-aligned looks more professional
                    ) {
                        Box(modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE3F2FD), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Build, contentDescription = "In Progress", tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(inProgressCount.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("Active Jobs", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Upcoming Jobs Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = onUpcomingJobsClick
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFFF3E0), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.DateRange, contentDescription = "Upcoming", tint = Color(0xFFF57C00), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(futureCount.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("Upcoming", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- MODERN DONUT CHART REVENUE CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sales This Month", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("$%.2f".format(monthlyRevenue), style = MaterialTheme.typography.headlineLarge, color = Color(0xFF2CA01C), fontWeight = FontWeight.ExtraBold)

                    Spacer(modifier = Modifier.height(24.dp))

                    val totalForChart = laborRevenue + productRevenue
                    val laborSweep = if (totalForChart > 0) (laborRevenue / totalForChart).toFloat() * 360f else 0f
                    val productSweep = if (totalForChart > 0) (productRevenue / totalForChart).toFloat() * 360f else 0f

                    val laborPercent = if (totalForChart > 0) Math.round((laborRevenue / totalForChart) * 100).toInt() else 0
                    val productPercent = if (totalForChart > 0) Math.round((productRevenue / totalForChart) * 100).toInt() else 0

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.size(160.dp)) {
                            if (totalForChart == 0.0) {
                                drawArc(color = Color(0xFFE0E0E0), startAngle = 0f, sweepAngle = 360f, useCenter = true)
                            } else {
                                // Draw the solid pie
                                drawArc(color = Color(0xFF2196F3), startAngle = -90f, sweepAngle = laborSweep, useCenter = true)
                                drawArc(color = Color(0xFF4CAF50), startAngle = -90f + laborSweep, sweepAngle = productSweep, useCenter = true)
                            }
                            // Hollow out the center to make it a beautiful Donut Chart!
                            drawCircle(color = Color.White, radius = size.minDimension / 3.5f)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // DETAILED LEGEND
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier
                                    .size(10.dp)
                                    .background(Color(0xFF2196F3), CircleShape))
                                Spacer(Modifier.width(6.dp))
                                Text("Labor", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                            Text("$laborPercent%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier
                                    .size(10.dp)
                                    .background(Color(0xFF4CAF50), CircleShape))
                                Spacer(Modifier.width(6.dp))
                                Text("Products", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                            Text("$productPercent%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- DEV NOTES ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "App Development Ideas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        if (isInEditMode) {
                            coroutineScope.launch {
                                try {
                                    firestore.collection("companies").document(companyName)
                                        .set(mapOf("developmentNotes" to developmentNotes), SetOptions.merge()).await()
                                    Toast.makeText(context, "Notes Saved!", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error saving notes.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        isInEditMode = !isInEditMode
                    }) {
                        Icon(imageVector = if (isInEditMode) Icons.Default.Check else Icons.Default.Edit, contentDescription = "Edit Notes")
                    }
                }

                OutlinedTextField(
                    value = developmentNotes,
                    onValueChange = { developmentNotes = it },
                    readOnly = !isInEditMode,
                    placeholder = { Text("Jot down your ideas here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
/**
 * Generates a list of 14-day, bi-weekly pay periods.
 * Each period starts on a Friday and ends on the second following Thursday.
 *
 * @return A list of start and end Timestamps for the current and three past pay periods.
 */
/**
 * Generates a list of pay periods dynamically based on a company's specific start date.
 */
fun generatePayPeriods(
    referenceDateString: String?,
    cycleDays: Int
): List<Pair<Timestamp, Timestamp>> {
    val periods = mutableListOf<Pair<Timestamp, Timestamp>>()

    // Default to a known past Monday if the company hasn't set one yet
    val refDate = if (!referenceDateString.isNullOrBlank()) {
        try {
            LocalDate.parse(referenceDateString, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            LocalDate.of(2024, 1, 1)
        }
    } else {
        LocalDate.of(2024, 1, 1)
    }

    val today = LocalDate.now()

    // Calculate how many full cycles have passed since the anchor date
    val daysBetween = ChronoUnit.DAYS.between(refDate, today)
    // Math.floorDiv handles negative numbers perfectly if they pick a future date!
    val cyclesPassed = Math.floorDiv(daysBetween, cycleDays.toLong())

    // The start date of the current cycle we are currently living in
    val currentPeriodStart = refDate.plusDays(cyclesPassed * cycleDays)
    val currentPeriodEnd = currentPeriodStart.plusDays(cycleDays.toLong() - 1)

    // Generate the current period and 3 past periods
    for (i in 0..3) {
        val start = currentPeriodStart.minusDays(i * cycleDays.toLong())
        val end = currentPeriodEnd.minusDays(i * cycleDays.toLong())

        val startTimestamp = Timestamp(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        val endTimestamp = Timestamp(Date.from(end.atTime(23, 59, 59, 999_000_000).atZone(ZoneId.systemDefault()).toInstant()))

        periods.add(Pair(startTimestamp, endTimestamp))
    }

    return periods
}

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    refreshTrigger: Int,
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    onNewTaskClick: () -> Unit,
    onEditTaskClick: (FirestoreTask) -> Unit,
    onTaskClick: (FirestoreTask) -> Unit,
    onRefresh: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    userRole: String,
    currentUserId: String
) {
    var tasks by remember { mutableStateOf<List<FirestoreTask>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var taskToDelete by remember { mutableStateOf<FirestoreTask?>(null) }
    val currentTaskToDelete = taskToDelete

// Fetches tasks from Firestore whenever the date, company, user, or refresh trigger changes.
    LaunchedEffect(selectedDate, companyName, refreshTrigger, userRole, currentUserId) {        if (companyName.isNotBlank()) {
            val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate }

            // Get the very beginning of the selected day
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDayTimestamp = Timestamp(calendar.time)

            // Get the very end of the selected day
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDayTimestamp = Timestamp(calendar.time)

            var query = firestore.collection("companies").document(companyName).collection("tasks")
                // 1. Get all tasks that STARTED on or before the end of the selected day
                .whereLessThanOrEqualTo("startDate", endOfDayTimestamp)

            // If the user is not a Manager, add a filter to show only their assigned tasks.
            if (userRole != "Manager" && currentUserId.isNotBlank()) {
                query = query.whereArrayContains("assignedUserIds", currentUserId)
            }

            query.orderBy("startDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    tasks = result.documents.mapNotNull { documentSnapshot ->
                        val parsedTask = documentSnapshot.toObject(FirestoreTask::class.java)

                        // Use copy() to safely replace any invisible nulls injected by Firebase
                        // with actual empty strings or default values so the UI doesn't crash.
                        parsedTask?.copy(
                            id = documentSnapshot.id
                        )
                    }
                        .filter { task ->
                            // 2. Safely filter the dates
                            val startMs = task.startDate?.toDate()?.time ?: 0L
                            val taskEndDate = task.endDate?.toDate()?.time ?: startMs
                            taskEndDate >= startOfDayTimestamp.toDate().time
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error fetching tasks: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

// Confirmation Dialog for deleting a task
    if (currentTaskToDelete != null) {
        ConfirmationDialog(
            title = "Confirm Deletion",
            message = "Are you sure you want to delete the task for ${currentTaskToDelete.customerName}?",
            onConfirm = {
                coroutineScope.launch {
                    try {
                        firestore.collection("companies").document(companyName)
                            .collection("tasks").document(currentTaskToDelete.id)
                            .delete().await()

                        Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                        onRefresh() // Trigger a data refresh
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        taskToDelete = null
                    }
                }
            },
            onDismiss = {
                taskToDelete = null
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Schedule", fontSize = 26.sp)
            if (userRole == "Manager") {
                Button(onClick = onNewTaskClick) {
                    Text("New Task")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- STABLE CALENDAR STATE INITIALIZATION ---
        val selectedLocalDate = remember(selectedDate) {
            Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
        }
        val startMonth = remember { YearMonth.now().minusMonths(100) }
        val endMonth = remember { YearMonth.now().plusMonths(100) }
        val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            // This is the key fix: The initial visible month is calculated ONCE
            // and does not change on recomposition, preventing the crash.
            firstVisibleMonth = remember { YearMonth.from(selectedLocalDate) },
            firstDayOfWeek = firstDayOfWeek
        )

        ComposeCalendarView(
            state = state, // Pass the hoisted state down
            selectedDate = selectedLocalDate,
            onDateSelected = { newLocalDate ->
                val newMillis = newLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                onDateSelected(newMillis)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                Card(
                    onClick = { onTaskClick(task) },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.customerName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            if (task.customerCompanyName.isNotBlank()) {
                                Text(
                                    text = task.customerCompanyName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                            }
                            if (task.jobNumber.isNotBlank()) {
                                Text(
                                    text = "Job #${task.jobNumber}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            if (task.address.isNotBlank()) {
                                Text(
                                    text = task.address,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            // Display Date Range
                            val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
                            val startDateStr = task.startDate?.let { dateFormat.format(it.toDate()) } ?: ""
                            val endDateStr = task.endDate?.let { dateFormat.format(it.toDate()) } ?: ""
                            val dateDisplay = if (startDateStr == endDateStr || endDateStr.isEmpty()) startDateStr else "$startDateStr - $endDateStr"

                            Text(
                                text = "Date(s): $dateDisplay",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Time: ${task.timeWindow}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (task.assignedUserNames.isNotEmpty()) {
                                Text(
                                    text = "Assigned to: ${task.assignedUserNames.joinToString()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (task.jobDescription.isNotBlank()) {
                                Text(
                                    text = "Job: ${task.jobDescription}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                        if (userRole == "Manager") {
                            IconButton(onClick = { onEditTaskClick(task) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                            }
                            IconButton(onClick = {
                                taskToDelete = task
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete Task",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DateRangeCalendarDialog(
    onDismiss: () -> Unit,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit,
    initialStartDate: LocalDate?,
    initialEndDate: LocalDate?
) {
    var selection by remember {
        mutableStateOf(Pair(initialStartDate, initialEndDate))
    }

    val firstDayOfWeek = remember { daysOfWeek().first() }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberWeekCalendarState(
        startDate = LocalDate.now().minusMonths(6), // Reduced range to save memory
        endDate = LocalDate.now().plusMonths(6),
        firstVisibleWeekDate = LocalDate.now(),
        firstDayOfWeek = firstDayOfWeek
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Select Date Range", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))

                // Days of week header
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayOfWeek in daysOfWeek) {
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = dayOfWeek.name.take(1),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // The Week Calendar is much shorter than the Month one
                WeekCalendar(
                    state = state,
                    dayContent = { day -> // 'day' here is a WeekDay
                        DateRangeDay(
                            day = day,
                            selection = selection,
                            onClick = { clickedDate ->
                                selection = if (selection.first == null || selection.second != null) {
                                    Pair(clickedDate, null)
                                } else {
                                    if (clickedDate.isBefore(selection.first)) {
                                        Pair(clickedDate, null)
                                    } else {
                                        Pair(selection.first, clickedDate)
                                    }
                                }
                            }
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = {
                            val start = selection.first
                            val end = selection.second ?: selection.first
                            if (start != null && end != null) {
                                onDateRangeSelected(start, end)
                            }
                        },
                        enabled = selection.first != null
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun DateRangeDay(    day: WeekDay, // This now works with the import above
                     selection: Pair<LocalDate?, LocalDate?>,
                     onClick: (LocalDate) -> Unit
) {
    val (start, end) = selection
    val isSelected = when {
        start == null -> false
        end == null -> day.date == start
        else -> day.date >= start && day.date <= end
    }

    Box(
        modifier = Modifier
            .height(40.dp) // Shrinks the row height in the dialog
            .fillMaxWidth()
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else Color.Transparent
            )
            .clickable { onClick(day.date) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface
        )
    }
}
@Composable
fun ComposeCalendarView(
    state: CalendarState,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
)
{
    val today = remember { LocalDate.now() }
// Wrap in a Column to stack the day titles and the calendar
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // This header is static and does not scroll with the calendar
        DaysOfWeekTitle()
        HorizontalDivider()

        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(
                    day = day,
                    isSelected = selectedDate == day.date,
                    isToday = day.date == today
                ) { clickedDay ->
                    onDateSelected(clickedDay.date)
                }
            },
            // This header scrolls with the calendar and updates for each month
            monthHeader = { calendarMonth ->
                MonthHeader(month = calendarMonth.yearMonth)
            }
        )
    }
}

@Composable
fun DaysOfWeekTitle() { // This function from the calendar library provides the days of the week
    // in the correct order for the user's locale.
    val daysOfWeek = remember { daysOfWeek() }
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) { Text( modifier = Modifier.weight(1f), textAlign = TextAlign.Center, text = dayOfWeek.name.take(1), // e.g., "M" for Monday
            style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold )
        }
    }
}
@Composable
fun MonthHeader(
    month: YearMonth
            ) {
                val formatter = remember {
                    DateTimeFormatter.ofPattern("MMMM yyyy")
                }
                Text(
                    text = month.format(formatter),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    task: FirestoreTask,
    modifier: Modifier = Modifier,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    companyName: String,
    onBack: () -> Unit,
    onNotesUpdated: (String) -> Unit,
    onStatusUpdated: (String) -> Unit,
    onCreateEstimate: (FirestoreTask) -> Unit,
    onCreateInvoice: (FirestoreTask) -> Unit,
    onEstimateClick: (String) -> Unit,
    onInvoiceClick: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // --- STATE: Related Data ---
    var estimates by remember { mutableStateOf<List<Estimate>>(emptyList()) }
    var invoices by remember { mutableStateOf<List<Invoice>>(emptyList()) }
    var isLoadingRelatedItems by remember { mutableStateOf(true) }

    // --- STATE: Editable Fields ---
    var isInEditMode by rememberSaveable { mutableStateOf(false) }
    var editedNotes by rememberSaveable { mutableStateOf(task.notes) }

    var isDescInEditMode by rememberSaveable { mutableStateOf(false) }
    var editedDetailedDesc by rememberSaveable { mutableStateOf(task.detailedDescription) }

    var isStatusInEditMode by rememberSaveable { mutableStateOf(false) }
    var editedStatus by rememberSaveable { mutableStateOf(task.status) }
    var isStatusDropdownExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Unassigned", "Assigned", "In Progress", "Completed", "Canceled")

    // --- STATE: Customer Info ---
    var customerPhoneNumber by remember { mutableStateOf<String?>(null) }

    // --- STATE: Files & Photos ---
    var isUploading by remember { mutableStateOf(false) }
    var imageUrls by remember { mutableStateOf(task.imageUrls) }
    var jobDocuments by remember { mutableStateOf(task.documents) }
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }
    var photoUrlToDelete by remember { mutableStateOf<String?>(null) }
    var documentToDelete by remember { mutableStateOf<JobDocument?>(null) }

    // --- STATE: Dispatch Fee Agreement ---
    var showDispatchFeeDialog by remember { mutableStateOf(false) }
    var isUploadingDispatchSignature by remember { mutableStateOf(false) }
    var dispatchFeeSignatureUrl by remember { mutableStateOf(task.dispatchFeeSignatureUrl) }
    var dispatchFeeAcceptedDate by remember { mutableStateOf(task.dispatchFeeAcceptedDate) }

    // --- STATE: Company Settings ---
    var enableDispatchFee by remember { mutableStateOf(false) }
    var dispatchFeeAmount by remember { mutableDoubleStateOf(99.0) }

    // --- EFFECT: Load Company Settings for Dispatch Fee ---
    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            try {
                val compDoc = firestore.collection("companies").document(companyName).get().await()
                enableDispatchFee = compDoc.getBoolean("enableDispatchFee") ?: false
                dispatchFeeAmount = compDoc.getDouble("dispatchFeeAmount") ?: 99.0
            } catch(e: Exception) {
                Log.e("JobDetails", "Error fetching company settings", e)
            }
        }
    }

    // --- EFFECT: Load Related Data ---
    LaunchedEffect(task.address, companyName) {
        if (companyName.isNotBlank() && task.address.isNotBlank()) {
            isLoadingRelatedItems = true
            try {
                val estimatesDeferred = coroutineScope.async {
                    firestore.collection("companies").document(companyName)
                        .collection("estimates").whereEqualTo("address", task.address)
                        .orderBy("creationDate", Query.Direction.DESCENDING).get().await()
                }
                val invoicesDeferred = coroutineScope.async {
                    firestore.collection("companies").document(companyName)
                        .collection("invoices").whereEqualTo("address", task.address)
                        .orderBy("creationDate", Query.Direction.DESCENDING).get().await()
                }
                estimates = estimatesDeferred.await().documents.mapNotNull {
                    it.toObject(Estimate::class.java)?.copy(id = it.id)
                }
                invoices = invoicesDeferred.await().documents.mapNotNull {
                    it.toObject(Invoice::class.java)?.copy(id = it.id)
                }
            } catch (e: Exception) {
                Log.e("JobDetails", "Error fetching related docs", e)
            } finally {
                isLoadingRelatedItems = false
            }
        } else {
            isLoadingRelatedItems = false
        }
    }

    // --- EFFECT: Load Customer Phone ---
    LaunchedEffect(task.customerId, companyName) {
        if (companyName.isNotBlank() && task.customerId.isNotBlank()) {
            firestore.collection("companies").document(companyName)
                .collection("customers").document(task.customerId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) customerPhoneNumber = doc.getString("phone")
                }
        }
    }

    // --- HELPER: Delete Files ---
    fun deleteFile(url: String, isDocument: Boolean, docObject: JobDocument?) {
        coroutineScope.launch {
            try {
                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                storageRef.delete().await()

                val taskRef = firestore.collection("companies").document(companyName)
                    .collection("tasks").document(task.id)

                if (isDocument && docObject != null) {
                    taskRef.update("documents", FieldValue.arrayRemove(docObject)).await()
                    jobDocuments = jobDocuments.filter { it != docObject }
                } else {
                    taskRef.update("imageUrls", FieldValue.arrayRemove(url)).await()
                    imageUrls = imageUrls.filter { it != url }
                }
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error deleting: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- LAUNCHERS: File Pickers ---
    val docPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            isUploading = true
            coroutineScope.launch {
                try {
                    val fileName = getFileName(context, it)
                    val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                    val safeTaskId = task.id.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                    val storageRef = Firebase.storage.reference.child("companies/$safeCompany/tasks/$safeTaskId/docs/$fileName")
                    storageRef.putFile(it).await()
                    val downloadUrl = storageRef.downloadUrl.await().toString()
                    val newDoc = JobDocument(name = fileName, url = downloadUrl)
                    firestore.collection("companies").document(companyName).collection("tasks").document(task.id).update("documents", FieldValue.arrayUnion(newDoc)).await()
                    jobDocuments = jobDocuments + newDoc
                    Toast.makeText(context, "Document uploaded!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Upload failed", Toast.LENGTH_LONG).show()
                } finally { isUploading = false }
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            isUploading = true
            coroutineScope.launch {
                try {
                    val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                    val safeTaskId = task.id.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                    val imageFileName = "${UUID.randomUUID()}.jpg"
                    val compressedBytes =
                        withContext(Dispatchers.IO) { compressImageFromUri(context, it) }

                    if (compressedBytes != null) {
                        val storageRef = Firebase.storage.reference.child("companies/$safeCompany/tasks/$safeTaskId/$imageFileName")
                        storageRef.putBytes(compressedBytes).await()
                        val downloadUrl = storageRef.downloadUrl.await().toString()
                        firestore.collection("companies").document(companyName).collection("tasks").document(task.id).update("imageUrls", FieldValue.arrayUnion(downloadUrl)).await()
                        imageUrls = imageUrls + downloadUrl
                        Toast.makeText(context, "Photo uploaded!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to process image", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                } finally { isUploading = false }
            }
        }
    }

    val displayDate = remember(task.startDate, task.endDate) {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val start = task.startDate?.toDate()
        val end = task.endDate?.toDate()
        when {
            start == null -> "No date set"
            end == null || start == end -> formatter.format(start)
            else -> "${formatter.format(start)} - ${formatter.format(end)}"
        }
    }

    if (selectedImageIndex != null) {
        FullScreenImageDialog(imageUrls = imageUrls, initialIndex = selectedImageIndex!!, onDismiss = { selectedImageIndex = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { onCreateEstimate(task) }) { Text("Create Estimate") }
                    Button(onClick = { onCreateInvoice(task) }) { Text("Create Invoice") }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. HEADER INFO
            DetailItem(label = "Customer Name", value = task.customerName)
            if (task.jobNumber.isNotBlank()) DetailItem(label = "Job Number", value = "#${task.jobNumber}")

            customerPhoneNumber?.takeIf { it.isNotBlank() }?.let { phone ->
                DetailItem(label = "Phone Number", value = phone, onClick = {
                    try { context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())) }
                    catch (_: Exception) { Toast.makeText(context, "Cannot open dialer", Toast.LENGTH_SHORT).show() }
                })
            }

            // 1.5 DISPATCH FEE AGREEMENT
            if (enableDispatchFee) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (dispatchFeeSignatureUrl != null) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ),
                    border = BorderStroke(1.dp, if (dispatchFeeSignatureUrl != null) Color(0xFF2E7D32) else Color(0xFFFF9800))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val formattedAmount = "%.2f".format(dispatchFeeAmount)
                        Text("Pre-Work Agreement: $$formattedAmount Dispatch Fee", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                        if (dispatchFeeSignatureUrl != null) {
                            Text("✓ Customer has agreed to the dispatch fee.", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
                            dispatchFeeAcceptedDate?.let {
                                Text("Signed: ${dateFormat.format(it.toDate())}", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                            }
                        } else {
                            Text("Customer must authorize the dispatch fee before work begins.", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                            Button(
                                onClick = { showDispatchFeeDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00))
                            ) {
                                Text("Collect Signature")
                            }
                        }
                    }
                }
            }

            // 2. STATUS EDIT
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Status", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    if (isStatusInEditMode) {
                        ExposedDropdownMenuBox(expanded = isStatusDropdownExpanded, onExpandedChange = { isStatusDropdownExpanded = !isStatusDropdownExpanded }) {
                            TextField(
                                value = editedStatus, onValueChange = {}, readOnly = true,
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStatusDropdownExpanded) }
                            )
                            ExposedDropdownMenu(expanded = isStatusDropdownExpanded, onDismissRequest = { isStatusDropdownExpanded = false }) {
                                statusOptions.forEach { status ->
                                    DropdownMenuItem(text = { Text(status) }, onClick = { editedStatus = status; isStatusDropdownExpanded = false })
                                }
                            }
                        }
                    } else {
                        Text(editedStatus, style = MaterialTheme.typography.titleMedium)
                    }
                }
                IconButton(onClick = {
                    if (isStatusInEditMode) {
                        if (editedStatus != task.status) {
                            coroutineScope.launch {
                                try {
                                    firestore.collection("companies").document(companyName).collection("tasks").document(task.id).update("status", editedStatus).await()
                                    onStatusUpdated(editedStatus)
                                    isStatusInEditMode = false
                                    Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) { Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
                            }
                        } else { isStatusInEditMode = false }
                    } else { isStatusInEditMode = true }
                }) { Icon(if (isStatusInEditMode) Icons.Default.Check else Icons.Default.Edit, "Edit Status") }
            }

            HorizontalDivider()
            DetailItem(label = "Scheduled Date", value = displayDate)
            DetailItem(label = "Address", value = task.address, onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "geo:0,0?q=${Uri.encode(task.address)}".toUri())
                intent.setPackage("com.google.android.apps.maps")
                try { context.startActivity(intent) } catch (_: Exception) { Toast.makeText(context, "Maps not available", Toast.LENGTH_SHORT).show() }
            })

            HorizontalDivider()

            // 3. DOCUMENTS SECTION
            Text("Documents", style = MaterialTheme.typography.titleLarge)
            if (jobDocuments.isEmpty()) { Text("No documents attached.", color = Color.Gray) }
            else {
                jobDocuments.forEach { doc ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            try { context.startActivity(Intent(Intent.ACTION_VIEW, doc.url.toUri())) }
                            catch (_: Exception) { Toast.makeText(context, "No app found to open file.", Toast.LENGTH_SHORT).show() }
                        },
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, "File", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(12.dp))
                            Text(doc.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            IconButton(onClick = { documentToDelete = doc }) { Icon(Icons.Default.Delete, "Delete", tint = Color.Red) }
                        }
                    }
                }
            }

            OutlinedButton(onClick = { docPickerLauncher.launch("*/*") }, enabled = !isUploading, modifier = Modifier.fillMaxWidth()) {
                if (isUploading) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else { Icon(Icons.Default.AttachFile, null); Spacer(Modifier.width(8.dp)); Text("Attach Document") }
            }

            // 4. PHOTOS SECTION
            Text("Photos", style = MaterialTheme.typography.titleLarge)
            if (imageUrls.isEmpty()) { Text("No photos added.", color = Color.Gray) }
            else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(imageUrls) { index, url ->
                        Box(modifier = Modifier.size(100.dp)) {
                            Card(onClick = { selectedImageIndex = index }, elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.matchParentSize()) {
                                AsyncImage(model = url, contentDescription = "Job Image", modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                            }
                            IconButton(onClick = { photoUrlToDelete = url }, modifier = Modifier.align(Alignment.TopEnd).size(24.dp).background(Color.White.copy(alpha = 0.7f), CircleShape)) {
                                Icon(Icons.Default.Close, "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            OutlinedButton(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, enabled = !isUploading, modifier = Modifier.fillMaxWidth()) {
                if (isUploading) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else { Icon(Icons.Default.AddAPhoto, null); Spacer(Modifier.width(8.dp)); Text("Add Job Photo") }
            }

            HorizontalDivider()

            // 5. RELATED ITEMS
            Text("Related Documents", style = MaterialTheme.typography.titleLarge)
            if (isLoadingRelatedItems) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else {
                if (estimates.isEmpty() && invoices.isEmpty()) {
                    Text("No related documents found.", color = Color.Gray)
                } else {
                    if (estimates.isNotEmpty()) {
                        Text("Estimates", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                        estimates.forEach { RelatedEstimateCard(it) { onEstimateClick(it.id) } }
                    }
                    if (invoices.isNotEmpty()) {
                        Text("Invoices", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                        invoices.forEach { RelatedInvoiceCard(it) { onInvoiceClick(it.id) } }
                    }
                }
            }

            HorizontalDivider()

            // 6. DETAILED DESCRIPTION
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Detailed Job Description", style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = {
                    if (isDescInEditMode) {
                        coroutineScope.launch {
                            try {
                                firestore.collection("companies").document(companyName).collection("tasks").document(task.id).update("detailedDescription", editedDetailedDesc).await()
                                isDescInEditMode = false
                                Toast.makeText(context, "Description saved", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) { Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
                        }
                    } else { isDescInEditMode = true }
                }) { Icon(if (isDescInEditMode) Icons.Default.Check else Icons.Default.Edit, "Edit") }
            }
            if (isDescInEditMode) {
                OutlinedTextField(value = editedDetailedDesc, onValueChange = { editedDetailedDesc = it }, modifier = Modifier.fillMaxWidth().height(150.dp), label = { Text("Edit Description") })
            } else {
                Text(if (task.detailedDescription.isBlank()) "No detailed description provided." else task.detailedDescription, style = MaterialTheme.typography.bodyLarge)
            }

            HorizontalDivider()

            // 7. INTERNAL NOTES
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Internal Notes", style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = {
                    if (isInEditMode) {
                        coroutineScope.launch {
                            try {
                                firestore.collection("companies").document(companyName).collection("tasks").document(task.id).update("notes", editedNotes).await()
                                onNotesUpdated(editedNotes)
                                isInEditMode = false
                                Toast.makeText(context, "Notes saved", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) { Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
                        }
                    } else { isInEditMode = true }
                }) { Icon(if (isInEditMode) Icons.Default.Check else Icons.Default.Edit, "Edit Notes") }
            }
            if (isInEditMode) {
                OutlinedTextField(value = editedNotes, onValueChange = { editedNotes = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Edit Notes") })
            } else {
                Text(if (task.notes.isBlank()) "No notes provided." else task.notes, style = MaterialTheme.typography.bodyLarge)
            }
        }

        // --- DIALOGS ---
        if (documentToDelete != null) {
            AlertDialog(
                onDismissRequest = { documentToDelete = null },
                title = { Text("Delete Document?") },
                text = { Text("Are you sure you want to delete '${documentToDelete?.name}'?") },
                confirmButton = { Button(onClick = { deleteFile(documentToDelete!!.url, true, documentToDelete); documentToDelete = null }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Delete") } },
                dismissButton = { TextButton(onClick = { documentToDelete = null }) { Text("Cancel") } }
            )
        }
        if (photoUrlToDelete != null) {
            AlertDialog(
                onDismissRequest = { photoUrlToDelete = null },
                title = { Text("Delete Photo?") },
                text = { Text("Are you sure you want to delete this photo?") },
                confirmButton = { Button(onClick = { deleteFile(photoUrlToDelete!!, false, null); photoUrlToDelete = null }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Delete") } },
                dismissButton = { TextButton(onClick = { photoUrlToDelete = null }) { Text("Cancel") } }
            )
        }

        // DISPATCH FEE SIGNATURE DIALOG
        if (showDispatchFeeDialog) {
            val formattedAmount = "%.2f".format(dispatchFeeAmount)
            SignatureDialog(
                title = "$$formattedAmount Dispatch Fee Agreement",
                subtitle = "I understand and agree to a $$formattedAmount dispatch fee. This fee covers travel, gas, and technician wages to arrive at the property. I agree that this fee is due regardless of the diagnosis or whether additional estimated work is approved.",
                onDismiss = { showDispatchFeeDialog = false },
                onSave = { bitmap ->
                    showDispatchFeeDialog = false
                    isUploadingDispatchSignature = true
                    coroutineScope.launch {
                        try {
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                            val data = baos.toByteArray()

                            val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                            val storageRef = Firebase.storage.reference.child("companies/$safeCompany/tasks/${task.id}/dispatch_signature.png")
                            storageRef.putBytes(data).await()
                            val downloadUrl = storageRef.downloadUrl.await().toString()

                            firestore.collection("companies").document(companyName).collection("tasks").document(task.id)
                                .update(
                                    mapOf(
                                        "dispatchFeeSignatureUrl" to downloadUrl,
                                        "dispatchFeeAcceptedDate" to FieldValue.serverTimestamp(),
                                        "status" to "In Progress"
                                    )
                                ).await()

                            Toast.makeText(context, "Agreement Signed & Job Started!", Toast.LENGTH_SHORT).show()

                            dispatchFeeSignatureUrl = downloadUrl
                            dispatchFeeAcceptedDate = Timestamp.now()
                            editedStatus = "In Progress"
                            onStatusUpdated("In Progress")

                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to save signature: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isUploadingDispatchSignature = false
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun RelatedEstimateCard(estimate: Estimate, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("#${estimate.estimateNumber}", fontWeight = FontWeight.Bold)
                Text(estimate.status, style = MaterialTheme.typography.bodySmall)
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    dateFormat.format(estimate.creationDate.toDate()),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("$%.2f".format(estimate.totalAmount), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RelatedInvoiceCard(invoice: Invoice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("#${invoice.invoiceNumber}", fontWeight = FontWeight.Bold)
                Text(invoice.status, style = MaterialTheme.typography.bodySmall)
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Due: ${dateFormat.format(invoice.dueDate.toDate())}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("$%.2f".format(invoice.totalAmount), fontWeight = FontWeight.Bold)
            }
        }
    }
}
fun downloadImage(
    context: Context,
    imageUrl: String,
    title: String,
    description: String
) {
    try {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val imageUri = imageUrl.toUri()

        val request = DownloadManager.Request(imageUri)
            .setTitle(title)
            .setDescription(description)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "CrewQ/${title}"
            )
            .setAllowedOverMetered(true)

        downloadManager.enqueue(request)
        Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to start download: ${e.message}", Toast.LENGTH_LONG).show()
        Log.e("ImageDownload", "Error starting download", e)
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable fun FullScreenImageDialog(
    imageUrls: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = initialIndex, pageCount = { imageUrls.size })

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                var scale by remember { mutableFloatStateOf(1f) }
                var offsetX by remember { mutableFloatStateOf(0f) }
                var offsetY by remember { mutableFloatStateOf(0f) }

                // Reset zoom instantly when sliding to a new photo
                LaunchedEffect(pagerState.currentPage) {
                    if (pagerState.currentPage != page) {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    }
                }

                // The base image modifier with Double-Tap to zoom
                var imageModifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (scale > 1f) {
                                    scale = 1f
                                    offsetX = 0f
                                    offsetY = 0f
                                } else {
                                    scale = 2.5f
                                }
                            },
                            onTap = { onDismiss() }
                        )
                    }

                // THE FIX: If normal size, ignore 1-finger swipes (let the pager swipe!)
                // but listen for 2-finger pinches to start zooming.
                if (scale <= 1f) {
                    imageModifier = imageModifier.pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                // Only consume the touch if there are 2 fingers!
                                if (event.changes.size >= 2) {
                                    val p1 = event.changes[0].position
                                    val p2 = event.changes[1].position
                                    val dx = p1.x - p2.x
                                    val dy = p1.y - p2.y
                                    val currentDist = sqrt((dx * dx + dy * dy).toDouble()).toFloat()

                                    val prevP1 = event.changes[0].previousPosition
                                    val prevP2 = event.changes[1].previousPosition
                                    val prevDx = prevP1.x - prevP2.x
                                    val prevDy = prevP1.y - prevP2.y
                                    val prevDist = sqrt((prevDx * prevDx + prevDy * prevDy).toDouble()).toFloat()

                                    if (prevDist > 0f) {
                                        scale = (scale * (currentDist / prevDist)).coerceIn(1f, 5f)
                                    }
                                    event.changes.forEach { it.consume() }
                                }
                            }
                        }
                    }
                } else {
                    // Once zoomed in, enable normal panning and zooming
                    imageModifier = imageModifier.pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 5f)
                            val maxX = (size.width * (scale - 1)) / 2
                            val maxY = (size.height * (scale - 1)) / 2
                            offsetX = (offsetX + pan.x * scale).coerceIn(-maxX, maxX)
                            offsetY = (offsetY + pan.y * scale).coerceIn(-maxY, maxY)

                            if (scale <= 1f) {
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }
                }

                AsyncImage(
                    model = imageUrls[page],
                    contentDescription = "Full screen job photo",
                    modifier = imageModifier,
                    contentScale = ContentScale.Fit
                )
            }

            // TOP BAR: Indicator & Close Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${imageUrls.size}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close Photo", tint = Color.White)
                }
            }

            // LEFT ARROW
            if (pagerState.currentPage > 0) {
                IconButton(
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", tint = Color.White)
                }
            }

            // RIGHT ARROW
            if (pagerState.currentPage < imageUrls.size - 1) {
                IconButton(
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", tint = Color.White)
                }
            }

            // DOWNLOAD BUTTON
            IconButton(
                onClick = {
                    val fileName = "JobImage_${System.currentTimeMillis()}.jpg"
                    downloadImage(
                        context = context,
                        imageUrl = imageUrls[pagerState.currentPage],
                        title = fileName,
                        description = "Downloading job photo..."
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(Icons.Default.Download, contentDescription = "Download Photo", tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
@Composable
fun DetailItem(label: String, value: String, onClick: (() -> Unit)? = null) {
    val isClickable = onClick != null

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = if (isClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (isClickable) TextDecoration.Underline else TextDecoration.None,
            modifier = if (isClickable) {
                Modifier.clickable { onClick() }
            } else Modifier
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: (CalendarDay) -> Unit // The caret was here
) {
    Box(
        modifier = Modifier
            .height(42.dp) // Fixed height shrinks the calendar vertically
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.primaryContainer
                    else -> Color.Transparent
                }
            )
            .clickable { onClick(day) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isToday -> MaterialTheme.colorScheme.onPrimaryContainer
                day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                else -> Color.LightGray // Dates from other months
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimatesScreen(
    onBackToMenu: () -> Unit,
    onNewEstimateClick: () -> Unit,
    onEstimateClick: (String) -> Unit,
    onEditEstimateClick: (String) -> Unit,
    onEstimateConverted: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String,
    currentUserName: String
) {
    var estimateList by remember { mutableStateOf<List<Estimate>>(emptyList()) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedEstimate by remember { mutableStateOf<Estimate?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var isConverting by remember { mutableStateOf(false) }

    // --- NEW: SEARCH AND TAB STATE ---
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("All", "Draft", "Sent", "Converted")

    LaunchedEffect(companyName, refreshTrigger) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).collection("estimates")
                .orderBy("creationDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    estimateList = result.documents.mapNotNull {
                        it.toObject(Estimate::class.java)?.copy(id = it.id)
                    }
                }
                .addOnFailureListener { e -> Log.w("EstimatesScreen", "Error getting estimates.", e) }
        }
    }

    // --- NEW: FILTER LOGIC (Applies Search AND Tab Selection) ---
    val filteredEstimates = remember(estimateList, searchQuery, selectedTabIndex) {
        estimateList.filter { estimate ->
            val matchesSearch = estimate.customerName.contains(searchQuery, ignoreCase = true) ||
                    estimate.estimateNumber.contains(searchQuery, ignoreCase = true)

            val matchesTab = when (selectedTabIndex) {
                0 -> true // All
                1 -> estimate.status.equals("Draft", ignoreCase = true)
                2 -> estimate.status.equals("Sent", ignoreCase = true)
                3 -> estimate.status.equals("Converted", ignoreCase = true)
                else -> true
            }

            matchesSearch && matchesTab
        }
    }

    if (showDeleteConfirmation && selectedEstimate != null) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_deletion),
            message = stringResource(R.string.confirm_delete_estimate, selectedEstimate!!.estimateNumber),
            onConfirm = {
                firestore.collection("companies").document(companyName)
                    .collection("estimates").document(selectedEstimate!!.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, R.string.toast_estimate_deleted, Toast.LENGTH_SHORT).show()
                        refreshTrigger++
                        showDeleteConfirmation = false
                    }
            },
            onDismiss = { showDeleteConfirmation = false }
        )
    }

    if (isConverting) {
        Dialog(onDismissRequest = { /* Prevent dismissal while converting */ }) {
            Card {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text(stringResource(R.string.converting_to_invoice))
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estimates Pipeline") }, // Upgraded title!
                navigationIcon = {
                    IconButton(onClick = onBackToMenu) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNewEstimateClick, containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_estimate))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Professional light gray background
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- NEW: SEARCH BAR ---
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by customer or #") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- NEW: STATUS TABS ---
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                divider = { HorizontalDivider(color = Color.LightGray) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (filteredEstimates.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (searchQuery.isBlank()) "No estimates found in this tab." else "No estimates match your search.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Slightly more breathing room
                ) {
                    items(filteredEstimates, key = { it.id }) { estimate ->
                        EstimateCard(
                            estimate = estimate,
                            onClick = { onEstimateClick(estimate.id) },
                            onEditClick = { onEditEstimateClick(estimate.id) },
                            onDeleteClick = {
                                selectedEstimate = estimate
                                showDeleteConfirmation = true
                            },
                            onConvertToInvoiceClick = {
                                coroutineScope.launch {
                                    isConverting = true
                                    val companyRef = firestore.collection("companies").document(companyName)
                                    val newInvoiceRef = companyRef.collection("invoices").document()
                                    val counterRef = companyRef.collection("metadata").document("invoiceCounter")
                                    try {
                                        // 👇 1. Capture the new invoice number from the transaction 👇
                                        val newInvoiceNumber = firestore.runTransaction { transaction ->
                                            val counterDoc = transaction.get(counterRef)
                                            val lastNumber = counterDoc.getLong("lastInvoiceNumber") ?: 9999L
                                            val newNumber = lastNumber + 1

                                            val newInvoice = Invoice(
                                                id = newInvoiceRef.id,
                                                invoiceNumber = newNumber.toString(),
                                                customerId = estimate.customerId,
                                                customerName = estimate.customerName,
                                                customerEmail = estimate.customerEmail,
                                                address = estimate.address,
                                                description = estimate.description,
                                                detailedDescription = estimate.detailedDescription,
                                                subtotal = estimate.subtotal,
                                                taxAmount = estimate.taxAmount,
                                                taxRate = estimate.taxRate,
                                                totalAmount = estimate.totalAmount,
                                                estimateId = estimate.id,
                                                creatorId = currentUserId,
                                                creatorName = currentUserName
                                            )
                                            transaction.set(newInvoiceRef, newInvoice)

                                            if (counterDoc.exists()) {
                                                transaction.update(counterRef, "lastInvoiceNumber", newNumber)
                                            } else {
                                                transaction.set(counterRef, mapOf("lastInvoiceNumber" to newNumber))
                                            }

                                            // 👇 2. Return the number here 👇
                                            newNumber
                                        }.await()

                                        val lineItemsSnapshot = firestore.collection("companies").document(companyName)
                                            .collection("estimates").document(estimate.id)
                                            .collection("lineItems").get().await()

                                        val batch = firestore.batch()
                                        lineItemsSnapshot.documents.forEach { doc ->
                                            val newLineItemRef = newInvoiceRef.collection("lineItems").document()
                                            batch.set(newLineItemRef, doc.data!!)
                                        }

                                        val estimateRef = firestore.collection("companies").document(companyName)
                                            .collection("estimates").document(estimate.id)
                                        batch.update(estimateRef, mapOf(
                                            "status" to "Converted",
                                            "invoiceId" to newInvoiceRef.id
                                        ))

                                        batch.commit().await()

                                        Toast.makeText(context, "Converted to Invoice successfully!", Toast.LENGTH_SHORT).show()

                                        // 👇 3. Log the conversion using the captured number 👇
                                        logCompanyActivity(
                                            firestore, companyName, currentUserId, currentUserName,
                                            "CONVERT_ESTIMATE",
                                            "Converted Estimate #${estimate.estimateNumber} into Invoice #$newInvoiceNumber"
                                        )

                                        onEstimateConverted()
                                        refreshTrigger++
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Conversion failed: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isConverting = false
                                    }
                                }
                            },
                            firestore = firestore,
                            companyName = companyName
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEstimateScreen(
    estimateId: String,
    onBackToEstimates: () -> Unit,
    onEstimateSaved: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    var customerName by rememberSaveable { mutableStateOf("") }
    var addressText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var detailedDescription by rememberSaveable { mutableStateOf("") }
    var estimateNumber by rememberSaveable { mutableStateOf("") }
    var taxRate by rememberSaveable { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }

    val lineItems = rememberSaveable(
        saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })
    ) { mutableStateListOf<EstimateLineItem>() }

    var productSearchQuery by rememberSaveable { mutableStateOf("") }
    var isProductDropdownExpanded by remember { mutableStateOf(false) }
    var allProductsAndServices by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
    val productFocusRequester = remember { FocusRequester() }

    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(estimateId) {
        if (companyName.isNotBlank()) {
            coroutineScope.launch {
                try {
                    val estimateRef = firestore.collection("companies").document(companyName)
                        .collection("estimates").document(estimateId)
                    val estSnap = estimateRef.get().await()

                    if (estSnap.exists()) {
                        val data = estSnap.toObject(Estimate::class.java)
                        if (data != null) {
                            customerName = data.customerName
                            addressText = data.address
                            description = data.description
                            detailedDescription = data.detailedDescription
                            estimateNumber = data.estimateNumber
                            taxRate = data.taxRate
                        }
                    } else {
                        isLoading = false
                        return@launch
                    }

                    val lineItemsSnapshot = estimateRef.collection("lineItems").get().await()
                    lineItems.clear()
                    lineItems.addAll(lineItemsSnapshot.documents.mapNotNull {
                        it.toObject(EstimateLineItem::class.java)?.copy(id = it.id)
                    })

                    val catalogSnap = firestore.collection("companies").document(companyName)
                        .collection("productsAndServices").orderBy("name").get().await()
                    allProductsAndServices = catalogSnap.documents.mapNotNull {
                        it.toObject(ProductOrService::class.java)?.copy(id = it.id)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    val subtotal = remember(lineItems.toList()) { lineItems.sumOf { it.quantity * it.unitPrice } }
    val taxAmount = remember(lineItems.toList(), taxRate) {
        val taxableSubtotal = lineItems.filter { it.isTaxable }.sumOf { it.quantity * it.unitPrice }
        ((taxableSubtotal * (taxRate / 100.0)) * 100).toLong() / 100.0
    }
    val totalAmount = remember(subtotal, taxAmount) { subtotal + taxAmount }

    val productSearchResults = remember(allProductsAndServices, productSearchQuery) {
        if (productSearchQuery.isBlank()) emptyList()
        else allProductsAndServices.filter { p ->
            p.name.contains(productSearchQuery, ignoreCase = true) ||
                    p.description.contains(productSearchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Estimate") },
                navigationIcon = { IconButton(onClick = onBackToEstimates) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        },
        // 👇 STICKY BOTTOM BAR FOR SAVE/CANCEL 👇
        bottomBar = {
            Surface(shadowElevation = 16.dp, color = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onBackToEstimates, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (!isSaving) {
                                isSaving = true
                                coroutineScope.launch {
                                    try {
                                        val estRef = firestore.collection("companies").document(companyName).collection("estimates").document(estimateId)
                                        val batch = firestore.batch()

                                        batch.update(estRef, mapOf(
                                            "address" to addressText,
                                            "description" to description,
                                            "detailedDescription" to detailedDescription,
                                            "subtotal" to subtotal,
                                            "taxRate" to taxRate,
                                            "taxAmount" to taxAmount,
                                            "totalAmount" to totalAmount,
                                            "needsSync" to true,
                                            "signatureUrl" to null,
                                            "acceptedDate" to null,
                                            "status" to "Draft"
                                        ))

                                        val oldLineItems = estRef.collection("lineItems").get().await()
                                        oldLineItems.documents.forEach { doc -> batch.delete(doc.reference) }

                                        lineItems.forEach { lineItem ->
                                            if (lineItem.name.isNotBlank()) batch.set(estRef.collection("lineItems").document(), lineItem)
                                        }

                                        batch.commit().await()
                                        Toast.makeText(context, "Estimate updated!", Toast.LENGTH_SHORT).show()
                                        onEstimateSaved()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isSaving = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                    ) {
                        if (isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White) else Text("Save")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = customerName, onValueChange = {}, readOnly = true, label = { Text("Customer") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = addressText, onValueChange = { addressText = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("General Description") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = detailedDescription, onValueChange = { detailedDescription = it }, label = { Text("Scope of Work (Visible on PDF)") }, modifier = Modifier.fillMaxWidth().height(100.dp))
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(value = estimateNumber, onValueChange = {}, readOnly = true, label = { Text("Estimate #") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                Spacer(Modifier.height(24.dp))

                Text("Line Items", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                // Compact Line Items
                lineItems.forEachIndexed { index, item ->
                    LineItemRow(item = item, onItemChange = { updatedItem -> lineItems[index] = updatedItem }, onRemoveClick = { lineItems.removeAt(index) })
                }

                Column(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExposedDropdownMenuBox(expanded = isProductDropdownExpanded, onExpandedChange = { isProductDropdownExpanded = !it }) {
                        OutlinedTextField(
                            value = productSearchQuery,
                            onValueChange = { productSearchQuery = it; isProductDropdownExpanded = it.isNotBlank() },
                            label = { Text("Search Products and Services") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable).focusRequester(productFocusRequester),
                            singleLine = true
                        )
                        if (productSearchResults.isNotEmpty()) {
                            ExposedDropdownMenu(expanded = isProductDropdownExpanded, onDismissRequest = { isProductDropdownExpanded = false }, modifier = Modifier.background(Color.White)) {
                                productSearchResults.forEach { product ->
                                    DropdownMenuItem(
                                        text = { Text(product.name, color = Color.Black) },
                                        onClick = {
                                            lineItems.add(EstimateLineItem(name = product.name, description = product.description, quantity = 1.0, unitPrice = product.price, cost = product.cost, type = product.type, isTaxable = product.isTaxable))
                                            productSearchQuery = ""
                                            isProductDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    TextButton(onClick = { lineItems.add(EstimateLineItem()) }) { Text("+ Add Custom Line Item") }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // Totals Area
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { Text("Subtotal:", textAlign = TextAlign.End); Text("$%.2f".format(subtotal)) }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) { Text("Tax (${taxRate}%):", textAlign = TextAlign.End); Text("$%.2f".format(taxAmount)) }
                    HorizontalDivider(modifier = Modifier.width(150.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.End)
                        Text("$%.2f".format(totalAmount), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }

                // Extra space to prevent the sticky bottom bar from covering content
                Spacer(Modifier.height(120.dp))
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEstimateScreen(
    onBackToEstimates: () -> Unit,
    onEstimateCreated: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String,
    currentUserName: String,
    taskToPrePopulate: FirestoreTask? = null
) {
    // STATE FOR THE ESTIMATE HEADER
    var customerSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCustomer by remember { mutableStateOf<CustomerInfo?>(null) }
    var customerEmail by rememberSaveable { mutableStateOf("") }
    var addressText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var isCustomerDropdownExpanded by remember { mutableStateOf(false) }
    var customerSearchResults by remember { mutableStateOf<List<CustomerInfo>>(emptyList()) }
    val customerFocusRequester = remember { FocusRequester() }
    var companyTaxRate by remember { mutableStateOf(0.0) }
    var detailedDescription by rememberSaveable { mutableStateOf("") }

    // STATE FOR ADDRESS DROPDOWN
    var addressOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isAddressDropdownExpanded by remember { mutableStateOf(false) }

    // STATE FOR LINE ITEMS
    val lineItems = rememberSaveable(saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })) {
        mutableStateListOf<EstimateLineItem>()
    }

    // STATE FOR PRODUCT SEARCH
    var productSearchQuery by rememberSaveable { mutableStateOf("") }
    var isProductDropdownExpanded by remember { mutableStateOf(false) }
    var allProductsAndServices by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
    val productFocusRequester = remember { FocusRequester() }

    // GENERAL STATE
    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    // DATA FETCHING (INCLUDING TAX RATE)
    LaunchedEffect(Unit) {
        customerFocusRequester.requestFocus()
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).get()
                .addOnSuccessListener { doc ->
                    companyTaxRate = doc.getDouble("taxRate") ?: 0.0
                }

            firestore.collection("companies").document(companyName)
                .collection("productsAndServices").orderBy("name").get()
                .addOnSuccessListener { result ->
                    allProductsAndServices = result.documents.mapNotNull {
                        it.toObject(ProductOrService::class.java)?.copy(id = it.id)
                    }
                }
        }
    }

    // Handles populating address options and email when a customer is selected
    LaunchedEffect(selectedCustomer) {
        val customer = selectedCustomer
        if (customer != null) {
            val combinedAddresses = mutableListOf<String>()
            if (customer.address.isNotBlank()) {
                combinedAddresses.add(customer.address)
            }
            combinedAddresses.addAll(customer.jobsiteAddresses)
            addressOptions = combinedAddresses.distinct()
            addressText = customer.address
            customerEmail = customer.email
        } else {
            addressOptions = emptyList()
        }
    }
    LaunchedEffect(taskToPrePopulate) {
        if (taskToPrePopulate != null) {
            // Pre-fill general description and address
            description = taskToPrePopulate.jobDescription
            detailedDescription = taskToPrePopulate.detailedDescription
            addressText = taskToPrePopulate.address

            // Fetch the full customer details using the ID from the task
            if (companyName.isNotBlank() && taskToPrePopulate.customerId.isNotBlank()) {
                firestore.collection("companies").document(companyName)
                    .collection("customers").document(taskToPrePopulate.customerId)
                    .get()
                    .addOnSuccessListener { doc ->
                        val customer = doc.toObject(CustomerInfo::class.java)?.copy(id = doc.id)
                        if (customer != null) {
                            selectedCustomer = customer
                            customerSearchQuery = customer.name
                        }
                    }
            }
        }
    }

    // Handles customer search
    LaunchedEffect(customerSearchQuery) {
        if (customerSearchQuery.isNotBlank() && selectedCustomer == null) {
            delay(300) // Debounce search
            firestore.collection("companies").document(companyName)
                .collection("customers")
                .orderBy("searchableName")
                .whereGreaterThanOrEqualTo("searchableName", customerSearchQuery.lowercase())
                .whereLessThanOrEqualTo("searchableName", customerSearchQuery.lowercase() + '\uf8ff')
                .limit(10)
                .get()
                .addOnSuccessListener { result ->
                    customerSearchResults = result.documents.mapNotNull {
                        it.toObject(CustomerInfo::class.java)?.copy(id = it.id)
                    }
                    isCustomerDropdownExpanded = customerSearchResults.isNotEmpty()
                }
        } else {
            customerSearchResults = emptyList()
            isCustomerDropdownExpanded = false
        }
    }

    val productSearchResults = remember(allProductsAndServices, productSearchQuery) {
        if (productSearchQuery.isBlank()) { emptyList() }
        else {
            allProductsAndServices.filter { product ->
                product.name.contains(productSearchQuery, ignoreCase = true) ||
                        product.description.contains(productSearchQuery, ignoreCase = true)
            }
        }
    }

    // --- MODIFIED: AUTOMATIC TAX CALCULATION ---
    val subtotal = remember(lineItems.toList()) { lineItems.sumOf { it.quantity * it.unitPrice } }

    val taxAmount = remember(lineItems.toList(), companyTaxRate) {
        val taxableSubtotal = lineItems.filter { it.isTaxable }.sumOf { it.quantity * it.unitPrice }
        ((taxableSubtotal * (companyTaxRate / 100.0)) * 100).toLong() / 100.0
    }
    val totalAmount = remember(subtotal, taxAmount) {
        subtotal + taxAmount
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_estimate)) },
                navigationIcon = {
                    IconButton(onClick = onBackToEstimates) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")                                }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Customer and address fields...
            ExposedDropdownMenuBox(
                expanded = isCustomerDropdownExpanded,
                onExpandedChange = { isCustomerDropdownExpanded = !it }
            ) {
                TextField(
                    value = customerSearchQuery,
                    onValueChange = {
                        customerSearchQuery = it
                        selectedCustomer = null
                        addressText = ""
                        customerEmail = ""
                    },
                    label = { Text(stringResource(R.string.customer)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .focusRequester(customerFocusRequester),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCustomerDropdownExpanded) },
                    colors = textFieldColors
                )
                if (customerSearchResults.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = isCustomerDropdownExpanded,
                        onDismissRequest = { isCustomerDropdownExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        customerSearchResults.forEach { customer ->
                            DropdownMenuItem(
                                text = { Text(customer.name, color = Color.Black) },
                                onClick = {
                                    selectedCustomer = customer
                                    customerSearchQuery = customer.name
                                    isCustomerDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            TextField(
                value = customerEmail,
                onValueChange = {},
                readOnly = true,
                label = { Text("Customer Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
            )
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = isAddressDropdownExpanded,
                onExpandedChange = {
                    if (addressOptions.size > 1) { isAddressDropdownExpanded = !isAddressDropdownExpanded }
                }
            ) {
                TextField(
                    value = addressText,
                    onValueChange = { addressText = it },
                    readOnly = addressOptions.isNotEmpty(),
                    label = { Text(stringResource(R.string.address)) },
                    trailingIcon = { if (addressOptions.size > 1) { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAddressDropdownExpanded) } },
                    colors = textFieldColors,
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isAddressDropdownExpanded,
                    onDismissRequest = { isAddressDropdownExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    addressOptions.forEach { address ->
                        DropdownMenuItem(
                            text = { Text(address, color = Color.Black) },
                            onClick = {
                                addressText = address
                                isAddressDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(
                R.string.general_description)) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = detailedDescription,
                onValueChange = { detailedDescription = it },
                label = { Text("Detailed Scope of Work (Visible on PDF)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )
            Spacer(Modifier.height(8.dp))
            TextField(value = "", onValueChange = {}, readOnly = true, enabled = false, label = { Text("Estimate Number (auto-generated)") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            Text(stringResource(R.string.line_items), style = MaterialTheme.typography.titleLarge)
            lineItems.forEachIndexed { index, item ->
                LineItemRow(item = item, onItemChange = { updatedItem -> lineItems[index] = updatedItem }, onRemoveClick = { lineItems.removeAt(index) })
            }

            Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(
                    expanded = isProductDropdownExpanded,
                    onExpandedChange = { isProductDropdownExpanded = !it }
                ) {
                    TextField(
                        value = productSearchQuery,
                        onValueChange = {
                            productSearchQuery = it
                            isProductDropdownExpanded = it.isNotBlank()
                        },
                        label = { Text(stringResource(R.string.search_products_and_services)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .focusRequester(productFocusRequester),
                        colors = textFieldColors
                    )
                    if (productSearchResults.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = isProductDropdownExpanded,
                            onDismissRequest = { isProductDropdownExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            productSearchResults.forEach { product ->
                                DropdownMenuItem(
                                    text = { Text(product.name, color = Color.Black) },
                                    onClick = {
                                        lineItems.add(EstimateLineItem(
                                            name = product.name,
                                            description = product.description,
                                            quantity = 1.0,
                                            unitPrice = product.price,
                                            cost = product.cost,
                                            type = product.type,
                                            isTaxable = product.isTaxable
                                        )
                                        )
                                        productSearchQuery = ""
                                        isProductDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                TextButton(onClick = { lineItems.add(EstimateLineItem()) }) {
                    Text(stringResource(R.string.add_line_item))
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- MODIFIED: Totals section with Checkbox ---
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Subtotal:", textAlign = TextAlign.End)
                    Text("$%.2f".format(subtotal))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Tax (${companyTaxRate}%):", textAlign = TextAlign.End)
                    Text("$%.2f".format(taxAmount))
                }
                HorizontalDivider(modifier = Modifier.width(150.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.End)
                    Text("$%.2f".format(totalAmount), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedCustomer != null && !isSaving) {
                        isSaving = true
                        coroutineScope.launch {
                            val companyRef = firestore.collection("companies").document(companyName)
                            val newEstimateRef = companyRef.collection("estimates").document()
                            val counterRef = companyRef.collection("metadata").document("estimateCounter")
                            try {
                                // 👇 1. Capture the new number from the transaction
                                val newEstimateNumber = firestore.runTransaction { transaction ->
                                    val counterDoc = transaction.get(counterRef)
                                    val lastNumber = counterDoc.getLong("lastEstimateNumber") ?: 9999L
                                    val newNumber = lastNumber + 1

                                    val newEstimate = Estimate(
                                        id = newEstimateRef.id,
                                        estimateNumber = newNumber.toString(),
                                        customerId = selectedCustomer!!.id,
                                        customerName = selectedCustomer!!.name,
                                        customerEmail = customerEmail,
                                        address = addressText,
                                        description = description,
                                        detailedDescription = detailedDescription,
                                        totalAmount = totalAmount,
                                        taxRate = companyTaxRate,
                                        taxAmount = taxAmount,
                                        subtotal = subtotal,
                                        creatorId = currentUserId,
                                        creatorName = currentUserName
                                    )
                                    transaction.set(newEstimateRef, newEstimate)

                                    if (counterDoc.exists()) {
                                        transaction.update(counterRef, "lastEstimateNumber", newNumber)
                                    } else {
                                        transaction.set(counterRef, mapOf("lastEstimateNumber" to newNumber))
                                    }

                                    // 👇 2. Return it here!
                                    newNumber
                                }.await()

                                val batch = firestore.batch()
                                val lineItemsCollection = newEstimateRef.collection("lineItems")
                                lineItems.forEach { lineItem ->
                                    if (lineItem.name.isNotBlank()) {
                                        val newLineItemRef = lineItemsCollection.document()
                                        batch.set(newLineItemRef, lineItem)
                                    }
                                }
                                // 👇 1. FIRE THE LOG FIRST 👇
                                logCompanyActivity(
                                    firestore = firestore,
                                    companyName = companyName,
                                    userId = currentUserId,
                                    userName = currentUserName,
                                    action = "CREATE_ESTIMATE",
                                    details = "Created Estimate #$newEstimateNumber for ${selectedCustomer!!.name} totaling $${"%.2f".format(totalAmount)}"
                                )

                                // 👇 2. THEN COMMIT THE ESTIMATE 👇
                                batch.commit().await()
                                Toast.makeText(context, "Estimate saved!", Toast.LENGTH_SHORT).show()

                                // 👇 3. THEN NAVIGATE AWAY 👇
                                onEstimateCreated()
                            } catch (e: Exception) {
                                Log.e("NEW_ESTIMATE", "Error saving estimate", e)
                                Toast.makeText(context, "Error saving estimate: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isSaving = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCustomer != null && !isSaving
            ) {
                if (isSaving) { CircularProgressIndicator(modifier = Modifier.height(24.dp)) } else { Text(stringResource(R.string.save)) }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateCard(
    estimate: Estimate,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConvertToInvoiceClick: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = getStatusColor(estimate.status).copy(alpha = 0.15f)
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(end = 40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = estimate.description.ifBlank {
                            stringResource(R.string.estimate_number_label, estimate.estimateNumber)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "$%.2f".format(estimate.totalAmount),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Text(
                    text = estimate.customerName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    Text(
                        text = dateFormat.format(estimate.creationDate.toDate()),
                        style = MaterialTheme.typography.bodySmall, color = Color.Gray
                    )
                    Text(
                        text = estimate.status,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = getStatusColor(estimate.status)
                    )
                }
            }

            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, "More options", tint = Color.Gray)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit", color = Color.Black) },
                        onClick = { expanded = false; onEditClick() }
                    )
                    DropdownMenuItem(
                        text = { Text("Convert to Invoice", color = Color.Black) },
                        onClick = { expanded = false; onConvertToInvoiceClick() },
                        enabled = estimate.invoiceId == null
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = Color.Black) },
                        onClick = { expanded = false; onDeleteClick() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInvoiceScreen(
    onBackToInvoices: () -> Unit,
    onInvoiceCreated: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String,
    currentUserName: String,
    taskToPrePopulate: FirestoreTask? = null
) {
    var customerSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCustomer by remember { mutableStateOf<CustomerInfo?>(null) }
    var customerEmail by rememberSaveable { mutableStateOf("") }
    var addressText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var isCustomerDropdownExpanded by remember { mutableStateOf(false) }
    var customerSearchResults by remember { mutableStateOf<List<CustomerInfo>>(emptyList()) }
    val customerFocusRequester = remember { FocusRequester() }
    var detailedDescription by rememberSaveable { mutableStateOf("") }
    var addressOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isAddressDropdownExpanded by remember { mutableStateOf(false) }

    var applyCreditCardFee by rememberSaveable { mutableStateOf(false) }
    var companyCcFeeRate by remember { mutableStateOf(0.0) }

    // 👇 NEW: State for Fixed Fee 👇
    var companyCcFeeFixed by remember { mutableStateOf(0.0) }

    val defaultDueDate = remember { Calendar.getInstance().apply { add(Calendar.DATE, 30) }.time }
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    var dueDateText by rememberSaveable { mutableStateOf(dateFormat.format(defaultDueDate)) }
    var companyTaxRate by remember { mutableStateOf(0.0) }
    var amountPaid by rememberSaveable { mutableDoubleStateOf(0.0) }

    val lineItems = rememberSaveable(saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })) { mutableStateListOf<EstimateLineItem>() }
    var productSearchQuery by rememberSaveable { mutableStateOf("") }
    var isProductDropdownExpanded by remember { mutableStateOf(false) }
    var allProductsAndServices by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
    val productFocusRequester = remember { FocusRequester() }

    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black
    )

    LaunchedEffect(selectedCustomer) {
        val customer = selectedCustomer
        if (customer != null) {
            val combinedAddresses = mutableListOf<String>()
            if (customer.address.isNotBlank()) combinedAddresses.add(customer.address)
            combinedAddresses.addAll(customer.jobsiteAddresses)
            addressOptions = combinedAddresses.distinct()
            addressText = customer.address
            customerEmail = customer.email
        } else {
            addressOptions = emptyList()
        }
    }

    LaunchedEffect(customerSearchQuery) {
        if (customerSearchQuery.isNotBlank() && selectedCustomer == null) {
            delay(300)
            firestore.collection("companies").document(companyName)
                .collection("customers").orderBy("searchableName")
                .whereGreaterThanOrEqualTo("searchableName", customerSearchQuery.lowercase())
                .whereLessThanOrEqualTo("searchableName", customerSearchQuery.lowercase() + '\uf8ff')
                .limit(10).get().addOnSuccessListener { result ->
                    customerSearchResults = result.documents.mapNotNull { it.toObject(CustomerInfo::class.java)?.copy(id = it.id) }
                    isCustomerDropdownExpanded = customerSearchResults.isNotEmpty()
                }
        } else {
            customerSearchResults = emptyList()
            isCustomerDropdownExpanded = false
        }
    }

    LaunchedEffect(Unit) {
        customerFocusRequester.requestFocus()
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).get().addOnSuccessListener { doc ->
                companyTaxRate = doc.getDouble("taxRate") ?: 0.0
                companyCcFeeRate = doc.getDouble("ccFeeRate") ?: 2.9
                // 👇 NEW: Fetch Fixed Fee 👇
                companyCcFeeFixed = doc.getDouble("ccFeeFixed") ?: 0.30
            }
            firestore.collection("companies").document(companyName)
                .collection("productsAndServices").orderBy("name").get()
                .addOnSuccessListener { result ->
                    allProductsAndServices = result.documents.mapNotNull { it.toObject(ProductOrService::class.java)?.copy(id = it.id) }
                }
        }
    }

    LaunchedEffect(taskToPrePopulate) {
        if (taskToPrePopulate != null) {
            description = taskToPrePopulate.jobDescription
            detailedDescription = taskToPrePopulate.detailedDescription
            addressText = taskToPrePopulate.address

            if (companyName.isNotBlank() && taskToPrePopulate.customerId.isNotBlank()) {
                firestore.collection("companies").document(companyName)
                    .collection("customers").document(taskToPrePopulate.customerId).get().addOnSuccessListener { doc ->
                        val customer = doc.toObject(CustomerInfo::class.java)?.copy(id = doc.id)
                        if (customer != null) {
                            selectedCustomer = customer
                            customerSearchQuery = customer.name
                        }
                    }
            }
        }
    }

    val productSearchResults = remember(allProductsAndServices, productSearchQuery) {
        if (productSearchQuery.isBlank()) emptyList()
        else allProductsAndServices.filter { product ->
            product.name.contains(productSearchQuery, ignoreCase = true) || product.description.contains(productSearchQuery, ignoreCase = true)
        }
    }

    val subtotal = remember(lineItems.toList()) { lineItems.sumOf { it.quantity * it.unitPrice } }

    val taxAmount = remember(lineItems.toList(), companyTaxRate) {
        val taxableSubtotal = lineItems.filter { it.isTaxable }.sumOf { it.quantity * it.unitPrice }
        ((taxableSubtotal * (companyTaxRate / 100.0)) * 100).toLong() / 100.0
    }

    val ccFeeAmount = remember(subtotal, taxAmount, amountPaid, applyCreditCardFee, companyCcFeeRate, companyCcFeeFixed) {
        val unpaidBase = (subtotal + taxAmount - amountPaid).coerceAtLeast(0.0)
        if (applyCreditCardFee && unpaidBase > 0) {
            val percentageFee = unpaidBase * (companyCcFeeRate / 100.0)
            ((percentageFee + companyCcFeeFixed) * 100).toLong() / 100.0
        } else 0.0
    }

    val totalAmount = remember(subtotal, taxAmount, ccFeeAmount) { subtotal + taxAmount + ccFeeAmount }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("New Invoice") }, navigationIcon = { IconButton(onClick = onBackToInvoices) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ExposedDropdownMenuBox(expanded = isCustomerDropdownExpanded, onExpandedChange = { isCustomerDropdownExpanded = !it }) {
                TextField(
                    value = customerSearchQuery, onValueChange = { customerSearchQuery = it; selectedCustomer = null; addressText = ""; customerEmail = "" },
                    label = { Text("Customer") }, modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .focusRequester(customerFocusRequester),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCustomerDropdownExpanded) }, colors = textFieldColors
                )
                if (customerSearchResults.isNotEmpty()) {
                    ExposedDropdownMenu(expanded = isCustomerDropdownExpanded, onDismissRequest = { isCustomerDropdownExpanded = false }, modifier = Modifier.background(Color.White)) {
                        customerSearchResults.forEach { customer ->
                            DropdownMenuItem(text = { Text(customer.name, color = Color.Black) }, onClick = { selectedCustomer = customer; customerSearchQuery = customer.name; isCustomerDropdownExpanded = false })
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            TextField(value = customerEmail, onValueChange = {}, label = { Text("Customer Email") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, readOnly = true)
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = isAddressDropdownExpanded, onExpandedChange = { if (addressOptions.size > 1) { isAddressDropdownExpanded = !isAddressDropdownExpanded } }) {
                TextField(
                    value = addressText, onValueChange = { addressText = it }, readOnly = addressOptions.isNotEmpty(),
                    label = { Text("Address") }, trailingIcon = { if (addressOptions.size > 1) { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAddressDropdownExpanded) } },
                    colors = textFieldColors, modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = isAddressDropdownExpanded, onDismissRequest = { isAddressDropdownExpanded = false }, modifier = Modifier.background(Color.White)) {
                    addressOptions.forEach { address -> DropdownMenuItem(text = { Text(address, color = Color.Black) }, onClick = { addressText = address; isAddressDropdownExpanded = false }) }
                }
            }
            Spacer(Modifier.height(8.dp))
            TextField(value = description, onValueChange = { description = it }, label = { Text("General Description") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = detailedDescription, onValueChange = { detailedDescription = it }, label = { Text("Detailed Scope of Work (Visible on PDF)") }, modifier = Modifier
                .fillMaxWidth()
                .height(120.dp), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
            Spacer(Modifier.height(8.dp))
            TextField(value = "", onValueChange = {}, readOnly = true, enabled = false, label = { Text("Invoice Number (auto-generated)") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(Modifier.height(8.dp))
            TextField(value = dueDateText, onValueChange = { dueDateText = it }, label = { Text("Due Date") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            Text("Line Items", style = MaterialTheme.typography.titleLarge)

            lineItems.forEachIndexed { index, item ->
                LineItemRow(item = item, onItemChange = { updatedItem -> lineItems[index] = updatedItem }, onRemoveClick = { lineItems.removeAt(index) })
            }

            Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(expanded = isProductDropdownExpanded, onExpandedChange = { isProductDropdownExpanded = !it }) {
                    TextField(
                        value = productSearchQuery, onValueChange = { productSearchQuery = it; isProductDropdownExpanded = it.isNotBlank() },
                        label = { Text("Search Products and Services") }, modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .focusRequester(productFocusRequester), colors = textFieldColors
                    )
                    if (productSearchResults.isNotEmpty()) {
                        ExposedDropdownMenu(expanded = isProductDropdownExpanded, onDismissRequest = { isProductDropdownExpanded = false }, modifier = Modifier.background(Color.White)) {
                            productSearchResults.forEach { product ->
                                DropdownMenuItem(
                                    text = { Text(product.name, color = Color.Black) },
                                    onClick = {
                                        lineItems.add(EstimateLineItem(
                                            name = product.name,
                                            description = product.description,
                                            quantity = 1.0,
                                            unitPrice = product.price,
                                            cost = product.cost,
                                            type = product.type,
                                            isTaxable = product.isTaxable
                                        ))
                                        productSearchQuery = ""
                                        isProductDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                TextButton(onClick = { lineItems.add(EstimateLineItem()) }) { Text("Add Line Item") }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Subtotal:",
                        textAlign = TextAlign.End
                    );
                    Text("$%.2f".format(subtotal)
                    ) }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tax (${companyTaxRate}%):", textAlign = TextAlign.End)
                    Text("$%.2f".format(taxAmount)
                    )
                }

                // 👇 NEW UI FOR CHECKBOX 👇
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = applyCreditCardFee,
                        onCheckedChange = { applyCreditCardFee = it }
                    )
                    Text("CC Fee (${companyCcFeeRate}% + $${companyCcFeeFixed}):", textAlign = TextAlign.End)
                    Text("$%.2f".format(ccFeeAmount))
                }

                HorizontalDivider(modifier = Modifier.width(150.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.End)
                    Text("$%.2f".format(totalAmount), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedCustomer != null && !isSaving) {
                        isSaving = true
                        coroutineScope.launch {
                            val companyRef = firestore.collection("companies").document(companyName)
                            val newInvoiceRef = companyRef.collection("invoices").document()
                            val counterRef = companyRef.collection("metadata").document("invoiceCounter")
                            try {
                                val parsedDueDate = try { dateFormat.parse(dueDateText) } catch (_: Exception) { defaultDueDate }

                                // 👇 1. Capture the new number from the transaction
                                val newInvoiceNumber = firestore.runTransaction { transaction ->
                                    val counterDoc = transaction.get(counterRef)
                                    val lastNumber = counterDoc.getLong("lastInvoiceNumber") ?: 9999L
                                    val newNumber = lastNumber + 1

                                    val newInvoice = Invoice(
                                        id = newInvoiceRef.id,
                                        invoiceNumber = newNumber.toString(),
                                        customerId = selectedCustomer!!.id,
                                        customerName = selectedCustomer!!.name,
                                        customerEmail = customerEmail,
                                        address = addressText,
                                        description = description,
                                        detailedDescription = detailedDescription,
                                        dueDate = Timestamp(parsedDueDate ?: defaultDueDate),
                                        subtotal = subtotal,
                                        taxAmount = taxAmount,
                                        taxRate = companyTaxRate,
                                        applyCreditCardFee = applyCreditCardFee,
                                        creditCardFeeAmount = ccFeeAmount,
                                        totalAmount = totalAmount,
                                        creatorId = currentUserId,
                                        creatorName = currentUserName
                                    )
                                    transaction.set(newInvoiceRef, newInvoice)

                                    if(counterDoc.exists()){ transaction.update(counterRef, "lastInvoiceNumber", newNumber) }
                                    else { transaction.set(counterRef, mapOf("lastInvoiceNumber" to newNumber)) }

                                    // 👇 2. Return it here!
                                    newNumber
                                }.await()

                                val batch = firestore.batch()
                                val lineItemsCollection = newInvoiceRef.collection("lineItems")
                                lineItems.forEach { lineItem ->
                                    if (lineItem.name.isNotBlank()) {
                                        val newLineItemRef = lineItemsCollection.document()
                                        batch.set(newLineItemRef, lineItem)
                                    }
                                }
                                logCompanyActivity(
                                    firestore = firestore,
                                    companyName = companyName,
                                    userId = currentUserId,
                                    userName = currentUserName,
                                    action = "CREATE_INVOICE",
                                    details = "Created Invoice #$newInvoiceNumber for ${selectedCustomer!!.name} totaling $${"%.2f".format(totalAmount)}"
                                )

                                // 👇 2. THEN COMMIT THE INVOICE 👇
                                batch.commit().await()
                                Toast.makeText(context, "Invoice saved!", Toast.LENGTH_SHORT).show()

                                // 👇 3. THEN NAVIGATE AWAY 👇
                                onInvoiceCreated()
                            } catch (e: Exception) {
                                Log.e("NEW_INVOICE", "Error saving invoice", e)
                                Toast.makeText(context, "Error saving invoice: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isSaving = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCustomer != null && !isSaving
            ) {
                if (isSaving) { CircularProgressIndicator(modifier = Modifier.height(24.dp)) } else { Text("Save") }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditInvoiceScreen(
    invoiceId: String,
    onBackToInvoices: () -> Unit,
    onInvoiceSaved: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    var customerName by rememberSaveable { mutableStateOf("") }
    var addressText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var invoiceNumber by rememberSaveable { mutableStateOf("") }
    var dueDateText by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val lineItems = rememberSaveable(
        saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })
    ) { mutableStateListOf<EstimateLineItem>() }

    var detailedDescription by rememberSaveable { mutableStateOf("") }
    var taxRate by rememberSaveable { mutableStateOf(0.0) }
    var applyCreditCardFee by rememberSaveable { mutableStateOf(false) }
    var companyCcFeeRate by remember { mutableStateOf(0.0) }
    var amountPaid by rememberSaveable { mutableDoubleStateOf(0.0) }
    var companyCcFeeFixed by remember { mutableStateOf(0.0) }

    var productSearchQuery by rememberSaveable { mutableStateOf("") }
    var isProductDropdownExpanded by remember { mutableStateOf(false) }
    var allProductsAndServices by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
    val productFocusRequester = remember { FocusRequester() }

    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    LaunchedEffect(invoiceId) {
        if (companyName.isNotBlank()) {
            coroutineScope.launch {
                try {
                    val companyDoc = firestore.collection("companies").document(companyName).get().await()
                    companyCcFeeRate = companyDoc.getDouble("ccFeeRate") ?: 2.9
                    companyCcFeeFixed = companyDoc.getDouble("ccFeeFixed") ?: 0.30

                    val invoiceRef = firestore.collection("companies").document(companyName)
                        .collection("invoices").document(invoiceId)
                    val invoiceData = invoiceRef.get().await().toObject(Invoice::class.java)

                    if (invoiceData != null) {
                        customerName = invoiceData.customerName
                        addressText = invoiceData.address
                        description = invoiceData.description
                        detailedDescription = invoiceData.detailedDescription
                        invoiceNumber = invoiceData.invoiceNumber
                        dueDateText = dateFormat.format(invoiceData.dueDate.toDate())
                        taxRate = invoiceData.taxRate
                        applyCreditCardFee = invoiceData.applyCreditCardFee
                        amountPaid = invoiceData.amountPaid
                    }

                    val lineItemsSnapshot = invoiceRef.collection("lineItems").get().await()
                    lineItems.clear()
                    lineItems.addAll(lineItemsSnapshot.documents.mapNotNull {
                        it.toObject(EstimateLineItem::class.java)?.copy(id = it.id)
                    })

                    val result = firestore.collection("companies").document(companyName)
                        .collection("productsAndServices").orderBy("name").get().await()
                    allProductsAndServices = result.documents.mapNotNull {
                        it.toObject(ProductOrService::class.java)?.copy(id = it.id)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    val subtotal = remember(lineItems.toList()) {
        lineItems.sumOf { it.quantity * it.unitPrice }
    }
    val taxAmount = remember(lineItems.toList(), taxRate) {
        val taxableSubtotal = lineItems.filter { it.isTaxable }.sumOf { it.quantity * it.unitPrice }
        ((taxableSubtotal * (taxRate / 100.0)) * 100).toLong() / 100.0
    }
    val ccFeeAmount = remember(subtotal, taxAmount, amountPaid, applyCreditCardFee, companyCcFeeRate, companyCcFeeFixed) {
        val unpaidBase = (subtotal + taxAmount - amountPaid).coerceAtLeast(0.0)
        if (applyCreditCardFee && unpaidBase > 0) {
            ((unpaidBase * (companyCcFeeRate / 100.0) + companyCcFeeFixed) * 100).toLong() / 100.0
        } else {
            0.0
        }
    }
    val totalAmount = remember(subtotal, taxAmount, ccFeeAmount) {
        subtotal + taxAmount + ccFeeAmount
    }

    val productSearchResults = remember(allProductsAndServices, productSearchQuery) {
        if (productSearchQuery.isBlank()) {
            emptyList()
        } else {
            allProductsAndServices.filter { p ->
                p.name.contains(productSearchQuery, ignoreCase = true) ||
                        p.description.contains(productSearchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Invoice") },
                navigationIcon = {
                    IconButton(onClick = onBackToInvoices) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 16.dp, color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackToInvoices,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (!isSaving) {
                                isSaving = true
                                coroutineScope.launch {
                                    try {
                                        val invoiceRef = firestore.collection("companies")
                                            .document(companyName).collection("invoices").document(invoiceId)
                                        val batch = firestore.batch()
                                        val parsedDueDate = try { dateFormat.parse(dueDateText) } catch (_: Exception) { Date() }

                                        batch.update(invoiceRef, mapOf(
                                            "address" to addressText,
                                            "description" to description,
                                            "detailedDescription" to detailedDescription,
                                            "dueDate" to Timestamp(parsedDueDate ?: Date()),
                                            "subtotal" to subtotal,
                                            "taxRate" to taxRate,
                                            "taxAmount" to taxAmount,
                                            "applyCreditCardFee" to applyCreditCardFee,
                                            "creditCardFeeAmount" to ccFeeAmount,
                                            "totalAmount" to totalAmount,
                                            "needsSync" to true
                                        ))

                                        val oldLineItems = invoiceRef.collection("lineItems").get().await()
                                        oldLineItems.documents.forEach { doc -> batch.delete(doc.reference) }

                                        lineItems.forEach { lineItem ->
                                            if (lineItem.name.isNotBlank()) {
                                                batch.set(invoiceRef.collection("lineItems").document(), lineItem)
                                            }
                                        }

                                        batch.commit().await()
                                        Toast.makeText(context, "Invoice updated!", Toast.LENGTH_SHORT).show()
                                        onInvoiceSaved()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isSaving = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        } else {
                            Text("Save")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = customerName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Customer") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = addressText,
                    onValueChange = { addressText = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("General Description") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = detailedDescription,
                    onValueChange = { detailedDescription = it },
                    label = { Text("Scope of Work (Visible on PDF)") },
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )

                Spacer(Modifier.height(8.dp))

                // Invoice # and Due Date Side-by-Side
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = invoiceNumber,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Invoice #") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = dueDateText,
                        onValueChange = { dueDateText = it },
                        label = { Text("Due Date") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text("Line Items", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                // Compact Line Items
                lineItems.forEachIndexed { index, item ->
                    LineItemRow(
                        item = item,
                        onItemChange = { updatedItem -> lineItems[index] = updatedItem },
                        onRemoveClick = { lineItems.removeAt(index) }
                    )
                }

                Column(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = isProductDropdownExpanded,
                        onExpandedChange = { isProductDropdownExpanded = !it }
                    ) {
                        OutlinedTextField(
                            value = productSearchQuery,
                            onValueChange = { productSearchQuery = it; isProductDropdownExpanded = it.isNotBlank() },
                            label = { Text("Search Products and Services") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable).focusRequester(productFocusRequester),
                            singleLine = true
                        )
                        if (productSearchResults.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = isProductDropdownExpanded,
                                onDismissRequest = { isProductDropdownExpanded = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                productSearchResults.forEach { product ->
                                    DropdownMenuItem(
                                        text = { Text(product.name, color = Color.Black) },
                                        onClick = {
                                            lineItems.add(
                                                EstimateLineItem(
                                                    name = product.name,
                                                    description = product.description,
                                                    quantity = 1.0,
                                                    unitPrice = product.price,
                                                    cost = product.cost,
                                                    type = product.type,
                                                    isTaxable = product.isTaxable
                                                )
                                            )
                                            productSearchQuery = ""
                                            isProductDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    TextButton(onClick = { lineItems.add(EstimateLineItem()) }) {
                        Text("+ Add Custom Line Item")
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // Totals Area
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Subtotal:", textAlign = TextAlign.End)
                        Text("$%.2f".format(subtotal))
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Tax (${taxRate}%):", textAlign = TextAlign.End)
                        Text("$%.2f".format(taxAmount))
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = applyCreditCardFee,
                            onCheckedChange = { applyCreditCardFee = it },
                            modifier = Modifier.size(20.dp)
                        )
                        Text("CC Fee:", textAlign = TextAlign.End)
                        Text("$%.2f".format(ccFeeAmount))
                    }

                    HorizontalDivider(modifier = Modifier.width(150.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.End)
                        Text("$%.2f".format(totalAmount), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }

                // Extra space to prevent the sticky bottom bar from covering content
                Spacer(Modifier.height(120.dp))
            }
        }
    }
}
@Composable
fun LineItemRow(
    item: EstimateLineItem,
    onItemChange: (EstimateLineItem) -> Unit,
    onRemoveClick: () -> Unit
) {
    var quantityText by remember { mutableStateOf(item.quantity.formattedQuantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // TOP ROW: Name & Remove Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = item.name,
                    onValueChange = { newItemName -> onItemChange(item.copy(name = newItemName)) },
                    label = { Text("Item Name / Description") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = onRemoveClick) {
                    Icon(Icons.Default.Close, "Remove", tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // BOTTOM ROW: Qty, Price, Tax
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { newQty ->
                        quantityText = newQty
                        onItemChange(item.copy(quantity = newQty.toDoubleOrNull() ?: 1.0))
                    },
                    label = { Text("Qty") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                OutlinedTextField(
                    value = item.unitPrice.toString(),
                    onValueChange = { newPrice ->
                        onItemChange(item.copy(unitPrice = newPrice.toDoubleOrNull() ?: 0.0))
                    },
                    label = { Text("Price") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    leadingIcon = { Text("$", color = Color.Gray) }
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tax", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Checkbox(
                        checked = item.isTaxable,
                        onCheckedChange = { isChecked -> onItemChange(item.copy(isTaxable = isChecked)) },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductServiceDialog(
    item: ProductOrService?,
    onDismiss: () -> Unit,
    onItemSaved: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    val isEditing = item != null

    // Basic Info
    var type by rememberSaveable { mutableStateOf(item?.type ?: "Non-inventory") }
    var name by rememberSaveable { mutableStateOf(item?.name ?: "") }
    var sku by rememberSaveable { mutableStateOf(item?.sku ?: "") }
    var category by rememberSaveable { mutableStateOf(item?.category ?: "") }

    // Sales Info
    var description by rememberSaveable { mutableStateOf(item?.description ?: "") }
    var price by rememberSaveable { mutableStateOf(item?.price?.let { if (it == 0.0) "" else it.toString() } ?: "") }
    var isTaxable by rememberSaveable { mutableStateOf(item?.isTaxable ?: true) }

    // Purchasing Info
    var isPurchased by rememberSaveable { mutableStateOf(item?.isPurchased ?: false) }
    var purchasingDescription by rememberSaveable { mutableStateOf(item?.purchasingDescription ?: "") }
    var cost by rememberSaveable { mutableStateOf(item?.cost?.let { if (it == 0.0) "" else it.toString() } ?: "") }
    var preferredVendor by rememberSaveable { mutableStateOf(item?.preferredVendor ?: "") }

    // --- NEW: ACCOUNT DROPDOWN STATE ---
    var incomeAccount by rememberSaveable { mutableStateOf(item?.incomeAccount?.takeIf { it.isNotBlank() } ?: "Sales") }
    var expenseAccount by rememberSaveable { mutableStateOf(item?.expenseAccount?.takeIf { it.isNotBlank() } ?: "Cost of Goods Sold") }

    var incomeAccountsList by rememberSaveable { mutableStateOf(listOf("Sales", "Services", "Billable Expense Income")) }
    var expenseAccountsList by rememberSaveable { mutableStateOf(listOf("Cost of Goods Sold", "Purchases", "Supplies & Materials")) }

    var incomeExpanded by rememberSaveable { mutableStateOf(false) }
    var expenseExpanded by rememberSaveable { mutableStateOf(false) }

    // --- NEW: ADD ACCOUNT DIALOG STATE ---
    var showNewAccountDialog by rememberSaveable { mutableStateOf(false) }
    var newAccountType by rememberSaveable { mutableStateOf("") } // "Income" or "Expense"
    var newAccountName by rememberSaveable { mutableStateOf("") }

    // Ensure the current item's custom accounts are in the lists!
    LaunchedEffect(item) {
        if (item != null) {
            if (item.incomeAccount.isNotBlank() && !incomeAccountsList.contains(item.incomeAccount)) {
                incomeAccountsList = incomeAccountsList + item.incomeAccount
            }
            if (item.expenseAccount.isNotBlank() && !expenseAccountsList.contains(item.expenseAccount)) {
                expenseAccountsList = expenseAccountsList + item.expenseAccount
            }
        }
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    // --- ADD NEW ACCOUNT WARNING DIALOG ---
    if (showNewAccountDialog) {
        AlertDialog(
            onDismissRequest = { showNewAccountDialog = false },
            title = { Text("Add New $newAccountType Account", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "WARNING: The name you type here must EXACTLY match an existing account in your QuickBooks Chart of Accounts, or the sync will fail!",
                        color = Color(0xFFB00020), // Red Warning
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = newAccountName,
                        onValueChange = { newAccountName = it },
                        label = { Text("Exact QuickBooks Account Name") },
                        singleLine = true,
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C)),
                    onClick = {
                        if (newAccountName.isNotBlank()) {
                            if (newAccountType == "Income") {
                                incomeAccountsList = incomeAccountsList + newAccountName
                                incomeAccount = newAccountName
                            } else {
                                expenseAccountsList = expenseAccountsList + newAccountName
                                expenseAccount = newAccountName
                            }
                            showNewAccountDialog = false
                        }
                    }) { Text("Add & Select") }
            },
            dismissButton = {
                TextButton(onClick = { showNewAccountDialog = false }) { Text("Cancel", color = Color.Gray) }
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = if (isEditing) "Product/Service information" else "New Product/Service",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(16.dp))

                // TYPE SELECTOR
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Non-inventory", "Service").forEach { itemType ->
                        Row(
                            Modifier
                                .selectable(
                                    selected = (type == itemType),
                                    onClick = { type = itemType })
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (type == itemType), onClick = { type = itemType })
                            Text(itemType, color = Color.Black)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                // BASIC INFO
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name*") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = sku, onValueChange = { sku = it }, label = { Text("SKU") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // SALES INFO
                Text("Description", fontWeight = FontWeight.Bold, color = Color.Black)
                TextField(value = description, onValueChange = { description = it }, placeholder = { Text("Sales description") }, modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(value = price, onValueChange = { price = it }, label = { Text("Sales price/rate") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), colors = textFieldColors)

                    // INCOME ACCOUNT DROPDOWN
                    ExposedDropdownMenuBox(
                        expanded = incomeExpanded,
                        onExpandedChange = { incomeExpanded = !incomeExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        TextField(
                            value = incomeAccount,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Income account") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = incomeExpanded) },
                            colors = textFieldColors,
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = incomeExpanded, onDismissRequest = { incomeExpanded = false }) {
                            incomeAccountsList.forEach { acc ->
                                DropdownMenuItem(text = { Text(acc) }, onClick = { incomeAccount = acc; incomeExpanded = false })
                            }
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("+ Add New", color = Color(0xFF2CA01C), fontWeight = FontWeight.Bold) },
                                onClick = {
                                    incomeExpanded = false
                                    newAccountType = "Income"
                                    newAccountName = ""
                                    showNewAccountDialog = true
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isTaxable, onCheckedChange = { isTaxable = it })
                    Text("Is taxable", color = Color.Black)
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // PURCHASING INFO
                Text("Purchasing information", fontWeight = FontWeight.Bold, color = Color.Black)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isPurchased, onCheckedChange = { isPurchased = it })
                    Text("I purchase this product/service from a vendor.", color = Color.Black)
                }

                if (isPurchased) {
                    Spacer(Modifier.height(8.dp))
                    TextField(value = purchasingDescription, onValueChange = { purchasingDescription = it }, placeholder = { Text("Description on purchase forms") }, modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), colors = textFieldColors)
                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextField(value = cost, onValueChange = { cost = it }, label = { Text("Cost") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), colors = textFieldColors)

                        // EXPENSE ACCOUNT DROPDOWN
                        ExposedDropdownMenuBox(
                            expanded = expenseExpanded,
                            onExpandedChange = { expenseExpanded = !expenseExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            TextField(
                                value = expenseAccount,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Expense account") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expenseExpanded) },
                                colors = textFieldColors,
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(expanded = expenseExpanded, onDismissRequest = { expenseExpanded = false }) {
                                expenseAccountsList.forEach { acc ->
                                    DropdownMenuItem(text = { Text(acc) }, onClick = { expenseAccount = acc; expenseExpanded = false })
                                }
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("+ Add New", color = Color(0xFF2CA01C), fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        expenseExpanded = false
                                        newAccountType = "Expense"
                                        newAccountName = ""
                                        showNewAccountDialog = true
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    TextField(value = preferredVendor, onValueChange = { preferredVendor = it }, label = { Text("Preferred Vendor") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                }

                Spacer(Modifier.height(24.dp))

                // SAVE / CANCEL BUTTONS
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C)),
                        onClick = {
                            if (name.isBlank()) {
                                Toast.makeText(context, "Item name cannot be empty", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val itemData = ProductOrService(
                                name = name,
                                sku = sku,
                                category = category,
                                description = description,
                                price = price.toDoubleOrNull() ?: 0.0,
                                incomeAccount = incomeAccount,
                                isTaxable = isTaxable,
                                type = type,
                                isPurchased = isPurchased,
                                purchasingDescription = purchasingDescription,
                                cost = cost.toDoubleOrNull() ?: 0.0,
                                expenseAccount = expenseAccount,
                                preferredVendor = preferredVendor
                            )

                            coroutineScope.launch {
                                try {
                                    val collection = firestore.collection("companies").document(companyName).collection("productsAndServices")
                                    if (isEditing) {
                                        // Keep the original QBO ID, but flag it as needing an update!
                                        collection.document(item!!.id).set(itemData.copy(qboId = item.qboId, needsSync = true)).await()
                                        Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show()
                                    } else {
                                        collection.add(itemData.copy(needsSync = true)).await()
                                        Toast.makeText(context, "Item saved", Toast.LENGTH_SHORT).show()
                                    }
                                    onItemSaved()
                                } catch (e: Exception) {
                                    Log.e("FIRESTORE_ITEM", "Error saving item", e)
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    ) {
                        Text("Save and close", color = Color.White)
                    }
                }
            }
        }
    }
}
            @Composable
            fun ProductServiceCard(
                item: ProductOrService,
                onEditClick: () -> Unit,
                onDeleteClick: () -> Unit,
                onSyncClick: () -> Unit,
                isSyncing: Boolean
            ) {
                var expanded by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier
                            .padding(16.dp)
                            .padding(end = 40.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = item.name, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                                if (item.description.isNotBlank()) {
                                    Text(text = item.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }
                            Text(text = "$%.2f".format(item.price), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Box(modifier = Modifier.align(Alignment.TopEnd)) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = stringResource(
                                    R.string.more_options), tint = Color.Gray)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                                DropdownMenuItem(text = { Text(stringResource(R.string.edit), color = Color.Black) }, onClick = { expanded = false; onEditClick() })
                                DropdownMenuItem(
                                    text = {
                                        if (isSyncing) Text("Syncing...", color = Color.Gray)
                                        else if (item.qboId != null && !item.needsSync) Text("✓ Synced to QBO", color = Color(0xFF2CA01C))
                                        else if (item.qboId != null && item.needsSync) Text("Update in QBO", color = Color.Black)
                                        else Text("Sync to QBO", color = Color.Black)
                                    },
                                    onClick = { expanded = false; onSyncClick() },
                                    // Enable the button if it has no ID, OR if it needs a sync!
                                    enabled = !isSyncing && (item.qboId == null || item.needsSync)
                                )
                                DropdownMenuItem(text = { Text(stringResource(R.string.delete), color = Color.Black) }, onClick = { expanded = false; onDeleteClick() })
                            }
                        }
                    }
                }
            }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsAndServicesScreen(
    onBackToMenu: () -> Unit,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    companyName: String,
    userRole: String
) {
    var itemList by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
    var refreshTrigger by remember { mutableStateOf(0) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isBatchSyncing by remember { mutableStateOf(false) }

    // --- ROTATION PROOF DIALOG STATES ---
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var selectedItemId by rememberSaveable { mutableStateOf<String?>(null) }
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }
    val selectedItem = itemList.find { it.id == selectedItemId }

    // --- NEW: TAB STATE ---
    // 0 = Products (Non-inventory), 1 = Services
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Products", "Services")
    // ----------------------

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(companyName, refreshTrigger) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName)
                .collection("productsAndServices").orderBy("name").get()
                .addOnSuccessListener { result ->
                    itemList = result.documents.mapNotNull { doc ->
                        doc.toObject(ProductOrService::class.java)?.copy(id = doc.id)
                    }
                }
                .addOnFailureListener { e -> Log.w("ProductsScreen", "Error getting items.", e) }
        }
    }

    // 1. Filter by Search Query
    val searchFilteredList = remember(itemList, searchQuery) {
        if (searchQuery.isBlank()) itemList
        else itemList.filter { item ->
            item.name.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true)
        }
    }

    // 2. Filter by Selected Tab!
    val finalDisplayList = remember(searchFilteredList, selectedTabIndex) {
        searchFilteredList.filter { item ->
            if (selectedTabIndex == 0) item.type == "Non-inventory" else item.type == "Service"
        }
    }

    // --- DIALOGS ---
    if (showDialog) {
        AddEditProductServiceDialog(
            item = selectedItem,
            onDismiss = { showDialog = false },
            onItemSaved = { showDialog = false; refreshTrigger++ },
            firestore = firestore,
            companyName = companyName
        )
    }

    if (showDeleteConfirmation && selectedItem != null) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_deletion),
            message = stringResource(R.string.confirm_delete_item, selectedItem.name),
            onConfirm = {
                coroutineScope.launch {
                    try {
                        firestore.collection("companies").document(companyName)
                            .collection("productsAndServices").document(selectedItem!!.id)
                            .delete().await()
                        Toast.makeText(context, R.string.toast_item_deleted, Toast.LENGTH_SHORT).show()
                        refreshTrigger++
                    } finally {
                        showDeleteConfirmation = false
                    }
                }
            },
            onDismiss = { showDeleteConfirmation = false }
        )
    }

    Scaffold(
        floatingActionButton = {
            if (userRole == "Manager") {
                FloatingActionButton(
                    onClick = { selectedItemId = null; showDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_item))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.products_and_services), fontSize = 26.sp, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            // --- SEARCH BAR ---
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(stringResource(R.string.search_products_services)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- NEW: TABS ---
            TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.Transparent) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // -----------------

            // --- BATCH SYNC BUTTON ---
            // Notice this still looks at `itemList` so it counts EVERYTHING across both tabs!
            val unsyncedCount = itemList.count { it.qboId == null || it.needsSync }
            if (unsyncedCount > 0) {
                Button(
                    onClick = {
                        isBatchSyncing = true
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser == null) {
                            isBatchSyncing = false
                            Toast.makeText(context, "Error: User logged out.", Toast.LENGTH_LONG).show()
                            return@Button
                        }

                        val freshFunctionsInstance = FirebaseFunctions.getInstance()

                        currentUser.getIdToken(true).addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val rawToken = tokenTask.result?.token
                                val data = hashMapOf("companyId" to companyName, "authToken" to rawToken)

                                freshFunctionsInstance.getHttpsCallable("batchSyncItemsToQBO").call(data)
                                    .addOnSuccessListener { result ->
                                        isBatchSyncing = false
                                        val responseData = result.data as? Map<*, *>
                                        val successes = responseData?.get("successCount") ?: 0
                                        val failures = responseData?.get("failCount") ?: 0

                                        if (failures == 0) {
                                            Toast.makeText(context, "Successfully synced $successes items!", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, "Synced $successes items. ($failures failed)", Toast.LENGTH_LONG).show()
                                        }
                                        refreshTrigger++
                                    }
                                    .addOnFailureListener { e ->
                                        isBatchSyncing = false
                                        Toast.makeText(context, "Batch Sync Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                isBatchSyncing = false
                                Toast.makeText(context, "Auth Error: Could not refresh token.", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isBatchSyncing,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                ) {
                    if (isBatchSyncing) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Syncing $unsyncedCount items...")
                    } else {
                        Text("Sync $unsyncedCount Pending Changes to QBO")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- LIST OF ITEMS ---
            if (finalDisplayList.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (searchQuery.isBlank()) "No ${tabs[selectedTabIndex].lowercase()} found."
                        else stringResource(R.string.no_items_match_search),
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(finalDisplayList, key = { it.id }) { item ->
                        var isSingleSyncing by remember { mutableStateOf(false) }

                        ProductServiceCard(
                            item = item,
                            onEditClick = { if (userRole == "Manager") { selectedItemId = item.id; showDialog = true } },
                            onDeleteClick = { if (userRole == "Manager") { selectedItemId = item.id; showDeleteConfirmation = true } },
                            isSyncing = isSingleSyncing,
                            onSyncClick = {
                                isSingleSyncing = true
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                if (currentUser != null) {
                                    val freshFunctionsInstance = FirebaseFunctions.getInstance()
                                    currentUser.getIdToken(true).addOnCompleteListener { tokenTask ->
                                        if (tokenTask.isSuccessful) {
                                            val rawToken = tokenTask.result?.token
                                            val data = hashMapOf("companyId" to companyName, "itemId" to item.id, "authToken" to rawToken)
                                            freshFunctionsInstance.getHttpsCallable("syncItemToQBO").call(data)
                                                .addOnSuccessListener {
                                                    isSingleSyncing = false
                                                    Toast.makeText(context, "Item synced to QuickBooks!", Toast.LENGTH_SHORT).show()
                                                    refreshTrigger++
                                                }
                                                .addOnFailureListener { e ->
                                                    isSingleSyncing = false
                                                    Toast.makeText(context, "Sync Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                                }
                                        } else {
                                            isSingleSyncing = false
                                            Toast.makeText(context, "Auth Error.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)) {
                Button(onClick = onBackToMenu) {
                    Text(stringResource(R.string.back))
                }
            }
        }
    }
}
@Parcelize data class UserSelectionInfo(
    val id: String,
    val name: String
): Parcelable
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun NewTaskScreen(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    editingTask: FirestoreTask? = null,
    firebaseAnalytics: FirebaseAnalytics? = null,
    onTaskCreated: () -> Unit = {},
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String,
    currentUserName: String
) {
    var detailedDescription by rememberSaveable { mutableStateOf(editingTask?.detailedDescription ?: "") }
    var notes by rememberSaveable { mutableStateOf(editingTask?.notes ?: "") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
// State for Customer Search
    var customerSearchQuery by rememberSaveable { mutableStateOf(editingTask?.customerName ?: "") }
    var selectedCustomer by remember { mutableStateOf<CustomerInfo?>(null) }
    var isCustomerDropdownExpanded by remember { mutableStateOf(false) }
    var customerSearchResults by remember { mutableStateOf<List<CustomerInfo>>(emptyList()) }
    val customerFocusRequester = remember { FocusRequester() }

// State for Task Details
    var addressText by rememberSaveable { mutableStateOf(editingTask?.address ?: "") }
    var showAddCustomerDialog by remember { mutableStateOf(false) }
    val timeWindows = listOf("8am-10am", "10am-12pm", "12pm-2pm", "2pm-4pm", "All Day")
    var selectedTimeWindow by rememberSaveable { mutableStateOf(editingTask?.timeWindow ?: "") }
    var jobDescription by rememberSaveable { mutableStateOf(editingTask?.jobDescription ?: "") }
    var isSaving by remember { mutableStateOf(false) }

// State for Address Dropdown
    var addressOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isAddressDropdownExpanded by remember { mutableStateOf(false) }

// State for Date Range
    val initialDate = remember {
        Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
    }
    var startDate by rememberSaveable {
        mutableStateOf<LocalDate?>(
            editingTask?.startDate?.let {
                Instant.ofEpochMilli(it.toDate().time).atZone(ZoneId.systemDefault()).toLocalDate()
            } ?: initialDate
        )
    }
    var endDate by rememberSaveable {
        mutableStateOf<LocalDate?>(
            editingTask?.endDate?.let {
                Instant.ofEpochMilli(it.toDate().time).atZone(ZoneId.systemDefault()).toLocalDate()
            } ?: initialDate
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val dateRangeText = when {
        startDate == null -> "Select Date(s)"
        endDate == null || startDate == endDate -> startDate!!.format(dateFormatter)
        else -> "${startDate!!.format(dateFormatter)} - ${endDate!!.format(dateFormatter)}"
    }

// State for User Assignment
    var companyUsers by remember { mutableStateOf<List<UserSelectionInfo>>(emptyList()) }
    val selectedUsers = rememberSaveable(saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })) {
        mutableStateListOf<UserSelectionInfo>()
    }
    var isUserDropdownExpanded by remember { mutableStateOf(false) }

// State for Customer Estimates and Jobs
    var estimates by remember { mutableStateOf<List<Estimate>>(emptyList()) }
    var isLoadingEstimates by remember { mutableStateOf(false) }
    var selectedEstimate by remember { mutableStateOf<Estimate?>(null) }
    var jobs by remember { mutableStateOf<List<FirestoreTask>>(emptyList()) }
    var isLoadingJobs by remember { mutableStateOf(false) }
    var selectedJob by remember { mutableStateOf<FirestoreTask?>(null) }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color(0xFFF0F0F0),
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    if (showDatePicker) {
        DateRangeCalendarDialog(
            onDismiss = { showDatePicker = false },
            onDateRangeSelected = { start, end ->
                startDate = start
                endDate = end
                showDatePicker = false
            },
            initialStartDate = startDate,
            initialEndDate = endDate
        )
    }

// Firestore Customer Search Logic
    LaunchedEffect(customerSearchQuery) {
        if (customerSearchQuery.isNotBlank() && selectedCustomer == null) {
            delay(300)
            firestore.collection("companies").document(companyName).collection("customers")
                .orderBy("searchableName")
                .whereGreaterThanOrEqualTo("searchableName", customerSearchQuery.lowercase())
                .whereLessThanOrEqualTo("searchableName", customerSearchQuery.lowercase() + '\uf8ff')
                .limit(10)
                .get()
                .addOnSuccessListener { result ->
                    customerSearchResults = result.documents.mapNotNull {
                        it.toObject(CustomerInfo::class.java)?.copy(id = it.id)
                    }
                    // 👇 CHANGE THIS TO ALWAYS BE TRUE 👇
                    isCustomerDropdownExpanded = true
                }
        } else {
            customerSearchResults = emptyList()
            isCustomerDropdownExpanded = false
        }
    }

// Fetch Users and Pre-select on Edit
    LaunchedEffect(companyName, editingTask) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).collection("users")
                .get()
                .addOnSuccessListener { result ->
                    companyUsers = result.documents.mapNotNull { doc ->
                        val firstName = doc.getString("firstName") ?: ""
                        val lastName = doc.getString("lastName") ?: ""
                        UserSelectionInfo(id = doc.id, name = "$firstName $lastName".trim())
                    }
                    editingTask?.let { task ->
                        val initialSelection = task.assignedUserIds.zip(task.assignedUserNames)
                            .map { (id, name) -> UserSelectionInfo(id, name) }
                        selectedUsers.addAll(initialSelection)
                    }
                }
        }
    }

// Handles fetching data when a customer is selected
    LaunchedEffect(selectedCustomer) {
        val customer = selectedCustomer
        if (customer != null) {
            selectedEstimate = null
            selectedJob = null
            // 1. Populate address options
            val combinedAddresses = mutableListOf<String>()
            if (customer.address.isNotBlank()) combinedAddresses.add(customer.address)
            combinedAddresses.addAll(customer.jobsiteAddresses)
            addressOptions = combinedAddresses.distinct()
            addressText = customer.address

            // 2. Fetch estimates
            isLoadingEstimates = true
            firestore.collection("companies").document(companyName).collection("estimates")
                .whereEqualTo("customerId", customer.id)
                .orderBy("creationDate", Query.Direction.DESCENDING).get()
                .addOnSuccessListener { result ->
                    estimates = result.documents.mapNotNull { it.toObject(Estimate::class.java)?.copy(id = it.id) }
                    isLoadingEstimates = false
                }.addOnFailureListener { isLoadingEstimates = false }

            // 3. Fetch jobs
            isLoadingJobs = true
            firestore.collection("companies").document(companyName).collection("tasks")
                .whereEqualTo("customerId", customer.id)
                .orderBy("startDate", Query.Direction.DESCENDING).limit(5).get()
                .addOnSuccessListener { result ->
                    jobs = result.documents.mapNotNull { it.toObject(FirestoreTask::class.java)?.copy(id = it.id) }
                    isLoadingJobs = false
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to load jobs.", Toast.LENGTH_SHORT).show()
                    isLoadingJobs = false
                }
        } else {
            estimates = emptyList()
            jobs = emptyList()
            addressOptions = emptyList()
        }
    }

// Pre-fills form when an existing job is selected
    LaunchedEffect(selectedJob) {
        selectedJob?.let { job ->
            jobDescription = job.jobDescription
            addressText = job.address
            selectedTimeWindow = job.timeWindow
            val preSelectedUsers = job.assignedUserIds.zip(job.assignedUserNames)
                .map { (id, name) -> UserSelectionInfo(id, name) }
            selectedUsers.clear()
            selectedUsers.addAll(preSelectedUsers)
        }
    }

// Pre-fills form when an estimate is selected
    LaunchedEffect(selectedEstimate) {
        selectedEstimate?.let { estimate ->
            jobDescription = estimate.description
            addressText = estimate.address
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showAddCustomerDialog) {
            AddCustomerDialog(
                onDismiss = { showAddCustomerDialog = false },
                onCustomerSaved = { newCustomer ->
                    showAddCustomerDialog = false
                    selectedCustomer = newCustomer
                    customerSearchQuery = newCustomer.name
                    // The existing LaunchedEffect(selectedCustomer) will automatically
                    // handle populating the address fields for you!
                },
                firestore = firestore,
                companyName = companyName,
                currentUserId = currentUserId,
                currentUserName = currentUserName
            )
        }

        ExposedDropdownMenuBox(
            expanded = isCustomerDropdownExpanded,
            onExpandedChange = { isCustomerDropdownExpanded = !isCustomerDropdownExpanded }
        ) {
            TextField(
                value = customerSearchQuery,
                onValueChange = {
                    customerSearchQuery = it
                    selectedCustomer = null
                    addressText = ""
                },
                label = { Text("Search Customer") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryEditable)
                    .focusRequester(customerFocusRequester),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCustomerDropdownExpanded) },
                colors = textFieldColors
            )

            // Always show the menu if expanded so they see the Add button
            ExposedDropdownMenu(
                expanded = isCustomerDropdownExpanded,
                onDismissRequest = { isCustomerDropdownExpanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                // 👇 THE NEW ADD CUSTOMER BUTTON 👇
                DropdownMenuItem(
                    text = { Text("+ Add New Customer", color = Color(0xFF2CA01C), fontWeight = FontWeight.Bold) },
                    onClick = {
                        isCustomerDropdownExpanded = false
                        showAddCustomerDialog = true
                    }
                )

                if (customerSearchResults.isNotEmpty()) {
                    HorizontalDivider()
                    customerSearchResults.forEach { customer ->
                        DropdownMenuItem(
                            text = { Text(customer.name, color = Color.Black) },
                            onClick = {
                                selectedCustomer = customer
                                customerSearchQuery = customer.name
                                isCustomerDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isAddressDropdownExpanded,
            onExpandedChange = { if (addressOptions.size > 1) isAddressDropdownExpanded = !isAddressDropdownExpanded }
        ) {
            TextField(
                value = addressText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Address")
                        },
                trailingIcon = {
                    if (addressOptions.size > 1) ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAddressDropdownExpanded) },
                colors = textFieldColors, modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isAddressDropdownExpanded,
                onDismissRequest = { isAddressDropdownExpanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                addressOptions.forEach { address ->
                    DropdownMenuItem(text = { Text(address, color = Color.Black) }, onClick = {
                        addressText = address
                        isAddressDropdownExpanded = false
                    })
                }
            }
        }

        if (isLoadingEstimates) {
            Box(modifier = Modifier.padding(vertical = 16.dp)) { CircularProgressIndicator() }
        } else if (estimates.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Recent Estimates (Tap to Select)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    estimates.forEachIndexed { index, estimate ->
                        EstimateInfoRow(estimate = estimate, isSelected = selectedEstimate?.id == estimate.id, onEstimateClick = {
                            selectedEstimate = if (selectedEstimate?.id == estimate.id) null else estimate
                            if (selectedEstimate != null) selectedJob = null // Deselect job if estimate is selected
                        })
                        if (index < estimates.lastIndex) HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }

        if (isLoadingJobs) {
            Box(modifier = Modifier.padding(vertical = 16.dp)) { CircularProgressIndicator() }
        } else if (jobs.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Recent Jobs (Tap to Select)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    jobs.forEachIndexed { index, job ->
                        JobInfoRow(task = job, isSelected = selectedJob?.id == job.id, onJobClick = {
                            selectedJob = if (selectedJob?.id == job.id) null else job
                            if (selectedJob != null) selectedEstimate = null // Deselect estimate if job is selected
                        })
                        if (index < jobs.lastIndex) HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }) {
            TextField(
                value = dateRangeText, onValueChange = {}, label = { Text("Date(s)") },
                trailingIcon = { Icon(Icons.Default.Edit, "Select date range") },
                colors = textFieldColors, enabled = false, modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isUserDropdownExpanded,
            onExpandedChange = { isUserDropdownExpanded = !isUserDropdownExpanded }
        ) {
            TextField(
                value = if (
                    selectedUsers.isNotEmpty()) selectedUsers.joinToString
                { it.name } else "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Assign To")
                        },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isUserDropdownExpanded) },
                colors = textFieldColors, modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isUserDropdownExpanded,
                onDismissRequest = { isUserDropdownExpanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                companyUsers.forEach { user ->
                    val isSelected = selectedUsers.any { it.id == user.id }
                    DropdownMenuItem(
                        text = { Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isSelected, onCheckedChange = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(user.name, color = Color.Black)
                        }},
                        onClick = {
                            if (isSelected) selectedUsers.removeIf { it.id == user.id } else selectedUsers.add(user)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var timeExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = timeExpanded, onExpandedChange = { timeExpanded = it }) {
            TextField(
                value = selectedTimeWindow,
                onValueChange = {},
                readOnly = true,
                label = { Text("Time window") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeExpanded) },
                colors = textFieldColors,
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = timeExpanded,
                onDismissRequest = { timeExpanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                timeWindows.forEach { window ->
                    DropdownMenuItem(text = { Text(window, color = Color.Black) }, onClick = {
                        selectedTimeWindow = window
                        timeExpanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = jobDescription, onValueChange = {
            jobDescription = it },
            label = { Text("Job Description") },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = detailedDescription,
            onValueChange = { detailedDescription = it },
            label = { Text("Detailed Job Description (Optional)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextField(value = notes, onValueChange = { notes = it },
            label = { Text("Internal Notes") },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isSaving) {
                    isSaving = true
                    coroutineScope.launch {
                        try {
                            val startTimestamp = startDate?.let { Timestamp(Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())) }
                            val endTimestamp = endDate?.let { Timestamp(Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())) }

                            if (editingTask == null) {
                                val counterRef = firestore.collection("companies").document(companyName).collection("metadata").document("jobCounter")
                                val newTaskRef = firestore.collection("companies").document(companyName).collection("tasks").document()

                                // 👇 1. Capture the new job number from the transaction 👇
                                val newJobNumber = firestore.runTransaction { transaction ->
                                    val counterDoc = transaction.get(counterRef)
                                    val lastNumber = counterDoc.getLong("lastJobNumber") ?: 9999L
                                    val newNumber = lastNumber + 1

                                    val taskData = FirestoreTask(
                                        id = newTaskRef.id,
                                        jobNumber = newNumber.toString(),
                                        customerId = selectedCustomer!!.id,
                                        customerName = customerSearchQuery,
                                        customerCompanyName = selectedCustomer?.companyName ?: "",
                                        address = addressText,
                                        startDate = startTimestamp,
                                        endDate = endTimestamp,
                                        timeWindow = selectedTimeWindow,
                                        jobDescription = jobDescription,
                                        detailedDescription = detailedDescription,
                                        notes = notes,
                                        assignedUserIds = selectedUsers.map { it.id },
                                        assignedUserNames = selectedUsers.map { it.name }
                                    )

                                    transaction.set(newTaskRef, taskData)

                                    if (counterDoc.exists()) {
                                        transaction.update(counterRef, "lastJobNumber", newNumber)
                                    } else {
                                        transaction.set(counterRef, mapOf("lastJobNumber" to newNumber))
                                    }

                                    // 👇 2. Return the number here 👇
                                    newNumber
                                }.await()

                                firebaseAnalytics?.logEvent("task_created", null)
                                Toast.makeText(context, "Task Scheduled!", Toast.LENGTH_SHORT).show()

                                // 👇 3. LOG THE CREATION 👇
                                logCompanyActivity(
                                    firestore = firestore,
                                    companyName = companyName,
                                    userId = currentUserId,
                                    userName = currentUserName,
                                    action = "CREATE_JOB",
                                    details = "Scheduled Job #$newJobNumber for $customerSearchQuery"
                                )

                            } else {
                                val taskData = FirestoreTask(
                                    id = editingTask.id,
                                    jobNumber = editingTask.jobNumber,
                                    customerId = selectedCustomer?.id ?: editingTask.customerId,
                                    customerName = customerSearchQuery,
                                    customerCompanyName = selectedCustomer?.companyName ?: editingTask.customerCompanyName,
                                    address = addressText,
                                    startDate = startTimestamp,
                                    endDate = endTimestamp,
                                    timeWindow = selectedTimeWindow,
                                    jobDescription = jobDescription,
                                    detailedDescription = detailedDescription,
                                    notes = notes,
                                    assignedUserIds = selectedUsers.map { it.id },
                                    assignedUserNames = selectedUsers.map { it.name }
                                )
                                firestore.collection("companies").document(companyName).collection("tasks").document(editingTask.id).set(taskData).await()
                                Toast.makeText(context, "Task Updated!", Toast.LENGTH_SHORT).show()

                                // 👇 4. LOG THE UPDATE 👇
                                logCompanyActivity(
                                    firestore = firestore,
                                    companyName = companyName,
                                    userId = currentUserId,
                                    userName = currentUserName,
                                    action = "UPDATE_JOB",
                                    details = "Updated Job #${editingTask.jobNumber} for $customerSearchQuery"
                                )
                            }

                            onTaskCreated()

                        } catch (e: Exception) {
                            Log.e("FIRESTORE_TASK", "Error saving task", e)
                            Toast.makeText(context, "Error saving task: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isSaving = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = (selectedCustomer != null || editingTask != null) && startDate != null && endDate != null && selectedTimeWindow.isNotBlank() && selectedUsers.isNotEmpty() && !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.height(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(if (editingTask == null) "Schedule Task" else "Update Task")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
@Composable fun JobInfoRow(
    task: FirestoreTask,
    isSelected: Boolean,    // --- ADD THIS PARAMETER ---
    onJobClick: () -> Unit  // --- ADD THIS PARAMETER --- )
    ) {
        val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
        val dateDisplay = task.startDate?.let { dateFormat.format(it.toDate()) } ?: "No date"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onJobClick() }
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Job #${task.jobNumber}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = task.status, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
        if (task.jobDescription.isNotBlank()) {
            Text(text = task.jobDescription, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic, color = Color.Gray)
        }
        Text(text = "Scheduled: $dateDisplay", style = MaterialTheme.typography.bodySmall)
    }
}
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ViewCustomerScreen(
    customerId: String,
    companyName: String,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    onBack: () -> Unit,
    onEstimateClick: (String) -> Unit,
    onInvoiceClick: (String) -> Unit,
    onJobClick: (FirestoreTask) -> Unit,
    onDuplicateEstimateClick: (estimateId: String, onComplete: () -> Unit) -> Unit
) {
    var customer by remember { mutableStateOf<CustomerInfo?>(null) }
    var estimates by remember { mutableStateOf<List<Estimate>>(emptyList()) }
    var invoices by remember { mutableStateOf<List<Invoice>>(emptyList()) }
    var jobs by remember { mutableStateOf<List<FirestoreTask>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var isSyncing by remember { mutableStateOf(false) }

    LaunchedEffect(customerId, companyName, refreshTrigger) {
        if (companyName.isBlank() || customerId.isBlank()) {
            isLoading = false
            return@LaunchedEffect
        }
        isLoading = true
        try {
            val customerDeferred = async {
                firestore.collection("companies").document(companyName)
                    .collection("customers").document(customerId).get().await()
            }
            val estimatesDeferred = async {
                firestore.collection("companies").document(companyName)
                    .collection("estimates").whereEqualTo("customerId", customerId)
                    .orderBy("creationDate", Query.Direction.DESCENDING).get().await()
            }
            val invoicesDeferred = async {
                firestore.collection("companies").document(companyName)
                    .collection("invoices").whereEqualTo("customerId", customerId)
                    .orderBy("creationDate", Query.Direction.DESCENDING).get().await()
            }
            val jobsDeferred = async {
                firestore.collection("companies").document(companyName)
                    .collection("tasks").whereEqualTo("customerId", customerId)
                    .orderBy("startDate", Query.Direction.DESCENDING).get().await()
            }
            customer = customerDeferred.await().toObject(CustomerInfo::class.java)?.copy(id = customerId)

            estimates = estimatesDeferred.await().documents.mapNotNull { doc ->
                doc.toObject(Estimate::class.java)?.copy(id = doc.id)
            }
            invoices = invoicesDeferred.await().documents.mapNotNull { doc ->
                doc.toObject(Invoice::class.java)?.copy(id = doc.id)
            }
            jobs = jobsDeferred.await().documents.mapNotNull { doc ->
                doc.toObject(FirestoreTask::class.java)?.copy(id = doc.id)
            }

        } catch (e: Exception) {
            Log.e("ViewCustomer", "Error fetching customer details", e)
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(customer?.name ?: "Customer Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (customer == null) {
            Box(Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Customer not found.")
            }
        } else {
            val tabTitles = listOf("Details", "Estimates", "Invoices", "Jobs")
            val pagerState = rememberPagerState { tabTitles.size }

            Column(modifier = Modifier.padding(innerPadding)) {
                PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(text = "$title (${when(index){0 -> "" ; 1 -> estimates.size; 2 -> invoices.size; 3 -> jobs.size; else -> "" }})".trim()) }
                        )
                    }
                }
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxHeight()) { page ->
                    when (page) {
                        0 -> CustomerDetailsTab(
                            customer = customer!!,
                            isSyncing = isSyncing,
                            onSyncClick = {
                                isSyncing = true
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                if (currentUser == null) {
                                    isSyncing = false
                                    Toast.makeText(context, "Error: User logged out.", Toast.LENGTH_LONG).show()
                                    return@CustomerDetailsTab
                                }

                                currentUser.getIdToken(true).addOnCompleteListener { tokenTask ->
                                    if (tokenTask.isSuccessful) {
                                        val data = hashMapOf(
                                            "companyId" to companyName,
                                            "customerId" to customer!!.id,
                                            "authToken" to tokenTask.result?.token
                                        )

                                        functions.getHttpsCallable("syncCustomerToQBO")
                                            .call(data)
                                            .addOnSuccessListener {
                                                isSyncing = false
                                                Toast.makeText(context, "Customer Synced to QuickBooks!", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener { e ->
                                                isSyncing = false
                                                Toast.makeText(context, "Sync Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                            }
                                    } else {
                                        isSyncing = false
                                    }
                                }
                            }
                        )
                        1 -> CustomerEstimatesTab(
                            items = estimates,
                            onItemClick = onEstimateClick,
                            onDuplicateClick = { estimateId ->
                                onDuplicateEstimateClick(estimateId) {
                                    refreshTrigger++ // Refresh the screen after duplicating
                                }
                            }
                        )
                        2 -> CustomerInvoicesTab(invoices, onItemClick = onInvoiceClick)
                        3 -> CustomerRelatedItemsTab(jobs) { SummaryJobCard(it, onClick = { onJobClick(it) }) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEstimateScreen(
    estimateId: String,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    companyName: String,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onConvertToInvoiceClick: () -> Unit,
    onEmailClick: (Estimate, List<EstimateLineItem>) -> Unit,
    onViewPdfClick: (Estimate, List<EstimateLineItem>) -> Unit,
    onDeleteClick: () -> Unit
) {
    var estimate by remember { mutableStateOf<Estimate?>(null) }
    var lineItems by remember { mutableStateOf<List<EstimateLineItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // UI States
    var isSyncing by remember { mutableStateOf(false) }
    var showSignatureDialog by remember { mutableStateOf(false) }
    var isUploadingSignature by remember { mutableStateOf(false) }
    var hideItemization by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) } // 👈 3-dot menu state

    LaunchedEffect(estimateId, companyName) {
        if (companyName.isNotBlank() && estimateId.isNotBlank()) {
            isLoading = true
            try {
                val companyDoc = firestore.collection("companies").document(companyName).get().await()
                hideItemization = companyDoc.getBoolean("hideItemization") ?: false

                val estimateRef = firestore.collection("companies").document(companyName)
                    .collection("estimates").document(estimateId)

                estimateRef.addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null && snapshot.exists()) {
                        estimate = snapshot.toObject(Estimate::class.java)?.copy(id = snapshot.id)
                    }
                }

                val lineItemsSnapshot = estimateRef.collection("lineItems").get().await()
                lineItems = lineItemsSnapshot.toObjects(EstimateLineItem::class.java)

            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estimate #${estimate?.estimateNumber ?: ""}", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                // 👇 3-DOT MENU (ADMIN ACTIONS) 👇
                actions = {
                    IconButton(onClick = { if (estimate != null) expandedMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Color.Black
                        )
                    }

                    if (estimate != null) {
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(text = { Text("Edit Estimate", color = Color.Black) }, onClick = { expandedMenu = false; onEditClick() })
                            DropdownMenuItem(text = { Text("View PDF", color = Color.Black) }, onClick = { expandedMenu = false; onViewPdfClick(estimate!!, lineItems) })
                            DropdownMenuItem(text = { Text("Send Email", color = Color.Black) }, onClick = { expandedMenu = false; onEmailClick(estimate!!, lineItems) })
                            HorizontalDivider()

                            DropdownMenuItem(
                                text = { Text(if (isSyncing) "Syncing..." else if (estimate?.qboId != null && estimate?.needsSync == false) "✓ QBO Synced" else "Sync to QBO", color = if (estimate?.qboId != null && estimate?.needsSync == false) Color(0xFF2CA01C) else Color.Black) },
                                onClick = {
                                    expandedMenu = false
                                    isSyncing = true
                                    FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                                        if (tokenTask.isSuccessful) {
                                            functions.getHttpsCallable("syncEstimateToQBO").call(
                                                hashMapOf("companyId" to companyName, "estimateId" to estimate!!.id, "authToken" to tokenTask.result?.token)
                                            )
                                                .addOnSuccessListener { isSyncing = false; Toast.makeText(context, "Synced to QBO!", Toast.LENGTH_SHORT).show() }
                                                .addOnFailureListener { e -> isSyncing = false; Toast.makeText(context, "Sync Failed: ${e.message}", Toast.LENGTH_LONG).show() }
                                        }
                                    }
                                },
                                enabled = !isSyncing && (estimate?.qboId == null || estimate?.needsSync == true)
                            )
                            HorizontalDivider()
                            DropdownMenuItem(text = { Text("Delete", color = Color.Red) }, onClick = { expandedMenu = false; onDeleteClick() })
                        }
                    }
                }
            )
        },
        // 👇 BOTTOM BAR (PRIMARY ACTIONS ONLY) 👇
        bottomBar = {
            if (!isLoading && estimate != null && estimate!!.invoiceId == null) {
                Surface(shadowElevation = 16.dp, color = Color.White) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        if (estimate!!.signatureUrl == null) {
                            Button(
                                onClick = { showSignatureDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                            ) { Text("Sign & Accept") }
                        }

                        Button(
                            onClick = onConvertToInvoiceClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)) // Purple for convert
                        ) { Text("Convert to Invoice") }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else if (estimate == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) { Text("Estimate not found.") }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // HEADER CARD (Total Amount)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = if(estimate!!.status == "Approved" || estimate!!.status == "Converted") Color(0xFFE8F5E9) else MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(if (estimate!!.status == "Converted") "CONVERTED" else if(estimate!!.status == "Approved") "APPROVED" else "ESTIMATE TOTAL", color = if(estimate!!.status == "Approved" || estimate!!.status == "Converted") Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Text(
                                "$%.2f".format(estimate!!.totalAmount),
                                fontSize = 40.sp, fontWeight = FontWeight.Black, color = if(estimate!!.status == "Approved" || estimate!!.status == "Converted") Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            Text("Created on ${dateFormat.format(estimate!!.creationDate.toDate())}", color = if(estimate!!.status == "Approved" || estimate!!.status == "Converted") Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                // CUSTOMER INFO
                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Prepared For", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text(estimate!!.customerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(estimate!!.address, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                // SCOPE OF WORK
                if (estimate!!.detailedDescription.isNotBlank() || estimate!!.description.isNotBlank()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Scope of Work", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(estimate!!.detailedDescription.ifBlank { estimate!!.description }, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                // LINE ITEMS
                item {
                    Text("Line Items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    if (hideItemization) {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Services Rendered", fontWeight = FontWeight.SemiBold)
                                Text("$%.2f".format(estimate!!.subtotal))
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            lineItems.forEach { item ->
                                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(item.name, fontWeight = FontWeight.SemiBold)
                                            Text("${item.quantity.formattedQuantity} x $%.2f".format(item.unitPrice), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                        }
                                        Text("$%.2f".format(item.quantity * item.unitPrice), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // TOTALS
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) { Text("Subtotal:", color = Color.Gray) ; Text("$%.2f".format(estimate!!.subtotal)) }
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) { Text("Tax:", color = Color.Gray) ; Text("$%.2f".format(estimate!!.taxAmount)) }
                        HorizontalDivider(modifier = Modifier.width(150.dp).padding(vertical = 8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text("$%.2f".format(estimate!!.totalAmount), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Spacer(Modifier.height(100.dp)) // Prevent bottom bar from covering content
                }
            }
        }

        // SIGNATURE DIALOG
        if (showSignatureDialog && estimate != null) {
            SignatureDialog(
                onDismiss = { showSignatureDialog = false },
                onSave = { bitmap ->
                    showSignatureDialog = false
                    isUploadingSignature = true
                    coroutineScope.launch {
                        try {
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                            val data = baos.toByteArray()
                            val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                            val storageRef = Firebase.storage.reference.child("companies/$safeCompany/estimates/${estimate!!.id}/signature.png")
                            storageRef.putBytes(data).await()
                            val downloadUrl = storageRef.downloadUrl.await().toString()

                            firestore.collection("companies").document(companyName).collection("estimates").document(estimate!!.id).update(
                                mapOf("signatureUrl" to downloadUrl, "status" to "Approved", "acceptedDate" to FieldValue.serverTimestamp())
                            ).await()
                            Toast.makeText(context, "Estimate Signed & Approved!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) { Toast.makeText(context, "Failed to save signature", Toast.LENGTH_LONG).show() }
                        finally { isUploadingSignature = false }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewInvoiceScreen(
    invoiceId: String,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    companyName: String,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onEmailClick: (Invoice, List<EstimateLineItem>) -> Unit,
    onViewPdfClick: (Invoice, List<EstimateLineItem>) -> Unit,
    onDeleteClick: () -> Unit
) {
    var invoice by remember { mutableStateOf<Invoice?>(null) }
    var lineItems by remember { mutableStateOf<List<EstimateLineItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // UI States
    var isSyncing by remember { mutableStateOf(false) }
    var showSignatureDialog by remember { mutableStateOf(false) }
    var isUploadingSignature by remember { mutableStateOf(false) }
    var hideItemization by remember { mutableStateOf(false) }

    // 👇 The 3-Dot Menu State 👇
    var expandedMenu by remember { mutableStateOf(false) }

    // Payment States
    var isPreparingPayment by remember { mutableStateOf(false) }
    var isSendingQboEmail by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var paymentAmountText by remember { mutableStateOf("") }
    var isSubmittingPayment by remember { mutableStateOf(false) }
    var companyCcFeeRate by remember { mutableDoubleStateOf(2.9) }
    var companyCcFeeFixed by remember { mutableDoubleStateOf(0.30) }

    val paymentSheet = rememberPaymentSheet { paymentResult ->
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                val fullAmount = invoice?.totalAmount ?: 0.0
                val stripeAmount = (fullAmount - (invoice?.amountPaid ?: 0.0)).coerceAtLeast(0.0)
                val newPayment = PaymentRecord(id = UUID.randomUUID().toString(), amount = stripeAmount, method = "Credit Card", date = Timestamp.now())

                firestore.collection("companies").document(companyName).collection("invoices").document(invoiceId)
                    .update(mapOf("status" to "Paid", "amountPaid" to fullAmount, "needsSync" to true, "paymentHistory" to FieldValue.arrayUnion(newPayment)))
                Toast.makeText(context, "Payment Successful!", Toast.LENGTH_LONG).show()
            }
            is PaymentSheetResult.Canceled -> Toast.makeText(context, "Payment Canceled", Toast.LENGTH_SHORT).show()
            is PaymentSheetResult.Failed -> Toast.makeText(context, "Payment Failed: ${paymentResult.error.message}", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(invoiceId, companyName) {
        if (companyName.isNotBlank() && invoiceId.isNotBlank()) {
            isLoading = true
            try {
                val companyDoc = firestore.collection("companies").document(companyName).get().await()
                hideItemization = companyDoc.getBoolean("hideItemization") ?: false
                companyCcFeeRate = companyDoc.getDouble("ccFeeRate") ?: 2.9
                companyCcFeeFixed = companyDoc.getDouble("ccFeeFixed") ?: 0.30

                val invoiceRef = firestore.collection("companies").document(companyName).collection("invoices").document(invoiceId)
                invoiceRef.addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null && snapshot.exists()) {
                        invoice = snapshot.toObject(Invoice::class.java)?.copy(id = snapshot.id)
                    }
                }
                val lineItemsSnapshot = invoiceRef.collection("lineItems").get().await()
                lineItems = lineItemsSnapshot.toObjects(EstimateLineItem::class.java)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally { isLoading = false }
        } else { isLoading = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invoice #${invoice?.invoiceNumber ?: ""}", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                },
                // 👇 THE BULLETPROOF 3-DOT MENU 👇
                actions = {
                    // 1. The icon is drawn NO MATTER WHAT so it can never be hidden
                    IconButton(onClick = { if (invoice != null) expandedMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Color.Black // Hardcoded to black to ensure visibility
                        )
                    }

                    // 2. The dropdown menu logic
                    DropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        if (invoice != null) {
                            DropdownMenuItem(text = { Text("Edit Invoice", color = Color.Black) }, onClick = { expandedMenu = false; onEditClick() })
                            DropdownMenuItem(text = { Text("View PDF", color = Color.Black) }, onClick = { expandedMenu = false; onViewPdfClick(invoice!!, lineItems) })
                            DropdownMenuItem(text = { Text("Share PDF", color = Color.Black) }, onClick = { expandedMenu = false; onEmailClick(invoice!!, lineItems) })
                            HorizontalDivider()

                            DropdownMenuItem(
                                text = { Text(if (isSendingQboEmail) "Sending..." else "Email via QBO", color = Color.Black) },
                                onClick = {
                                    expandedMenu = false
                                    isSendingQboEmail = true
                                    functions.getHttpsCallable("sendInvoiceEmailViaQBO").call(hashMapOf("companyId" to companyName, "invoiceId" to invoice!!.id))
                                        .addOnSuccessListener { isSendingQboEmail = false; Toast.makeText(context, "Emailed via QuickBooks!", Toast.LENGTH_LONG).show() }
                                        .addOnFailureListener { e -> isSendingQboEmail = false; Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show() }
                                },
                                enabled = invoice?.qboId != null && !isSendingQboEmail
                            )
                            DropdownMenuItem(
                                text = { Text(if (isSyncing) "Syncing..." else if (invoice?.qboId != null && invoice?.needsSync == false) "✓ QBO Synced" else "Sync to QBO", color = if (invoice?.qboId != null && invoice?.needsSync == false) Color(0xFF2CA01C) else Color.Black) },
                                onClick = {
                                    expandedMenu = false
                                    isSyncing = true
                                    FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                                        if (tokenTask.isSuccessful) {
                                            functions.getHttpsCallable("syncInvoiceToQBO").call(hashMapOf("companyId" to companyName, "invoiceId" to invoice!!.id, "authToken" to tokenTask.result?.token))
                                                .addOnSuccessListener { isSyncing = false; Toast.makeText(context, "Synced to QBO!", Toast.LENGTH_SHORT).show() }
                                                .addOnFailureListener { e -> isSyncing = false; Toast.makeText(context, "Sync Failed: ${e.message}", Toast.LENGTH_LONG).show() }
                                        }
                                    }
                                },
                                enabled = !isSyncing && (invoice?.qboId == null || invoice?.needsSync == true)
                            )
                            HorizontalDivider()
                            DropdownMenuItem(text = { Text("Delete", color = Color.Red) }, onClick = { expandedMenu = false; onDeleteClick() })
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!isLoading && invoice != null) {
                Surface(shadowElevation = 16.dp, color = Color.White) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        if (invoice!!.status != "Paid") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { showPaymentDialog = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                                ) { Text("Log Cash") }

                                Button(
                                    onClick = {
                                        isPreparingPayment = true
                                        functions.getHttpsCallable("createStripePaymentIntent").call(hashMapOf("companyId" to companyName, "invoiceId" to invoice!!.id))
                                            .addOnSuccessListener { result ->
                                                isPreparingPayment = false
                                                val clientSecret = (result.data as? Map<*, *>)?.get("clientSecret") as? String
                                                if (clientSecret != null) paymentSheet.presentWithPaymentIntent(clientSecret, PaymentSheet.Configuration(merchantDisplayName = companyName))
                                            }
                                            .addOnFailureListener { isPreparingPayment = false; Toast.makeText(context, "Failed to prep payment.", Toast.LENGTH_SHORT).show() }
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = !isPreparingPayment,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6772E5))
                                ) {
                                    if (isPreparingPayment) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White) else Text("Take Card")
                                }
                            }
                        }

                        if (invoice!!.signatureUrl == null) {
                            Button(
                                onClick = { showSignatureDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                            ) { Text("Sign & Accept") }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else if (invoice == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) { Text("Invoice not found.") }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // HEADER CARD (Balance Due)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = if(invoice!!.status == "Paid") Color(0xFFE8F5E9) else MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(if (invoice!!.status == "Paid") "PAID IN FULL" else "BALANCE DUE", color = if(invoice!!.status == "Paid") Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Text(
                                "$%.2f".format((invoice!!.totalAmount - invoice!!.amountPaid).coerceAtLeast(0.0)),
                                fontSize = 40.sp, fontWeight = FontWeight.Black, color = if(invoice!!.status == "Paid") Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            Text("Due on ${dateFormat.format(invoice!!.dueDate.toDate())}", color = if(invoice!!.status == "Paid") Color(0xFF2E7D32) else MaterialTheme.colorScheme.onPrimaryContainer, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                // CUSTOMER INFO
                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Billed To", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text(invoice!!.customerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(invoice!!.address, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                // SCOPE OF WORK
                if (invoice!!.detailedDescription.isNotBlank() || invoice!!.description.isNotBlank()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Scope of Work", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                Text(invoice!!.detailedDescription.ifBlank { invoice!!.description }, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                // LINE ITEMS
                item {
                    Text("Line Items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    if (hideItemization) {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Services Rendered", fontWeight = FontWeight.SemiBold)
                                Text("$%.2f".format(invoice!!.subtotal))
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            lineItems.forEach { item ->
                                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(item.name, fontWeight = FontWeight.SemiBold)
                                            Text("${item.quantity.formattedQuantity} x $%.2f".format(item.unitPrice), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                        }
                                        Text("$%.2f".format(item.quantity * item.unitPrice), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // TOTALS
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) { Text("Subtotal:", color = Color.Gray) ; Text("$%.2f".format(invoice!!.subtotal)) }
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) { Text("Tax:", color = Color.Gray) ; Text("$%.2f".format(invoice!!.taxAmount)) }
                        if (invoice!!.applyCreditCardFee) {
                            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) { Text("CC Fee:", color = Color.Gray) ; Text("$%.2f".format(invoice!!.creditCardFeeAmount)) }
                        }
                        HorizontalDivider(modifier = Modifier.width(150.dp).padding(vertical = 8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text("$%.2f".format(invoice!!.totalAmount), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Spacer(Modifier.height(80.dp))
                }
                //google review
                item {
                    var reviewLink by remember { mutableStateOf("") }

                    // Fetch the link from company settings
                    LaunchedEffect(companyName) {
                        firestore.collection("companies").document(companyName).get()
                            .addOnSuccessListener { reviewLink = it.getString("googleReviewLink") ?: "" }
                    }

                    if (reviewLink.isNotBlank()) {
                        Spacer(Modifier.height(24.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                            border = BorderStroke(1.dp, Color(0xFFE9ECEF))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Happy with our service?", fontWeight = FontWeight.Bold)
                                Text("Scan to leave us a review on Google!", fontSize = 12.sp, color = Color.Gray)
                                Spacer(Modifier.height(12.dp))

                                val qrBitmap = remember(reviewLink) { generateQrCodeBitmap(reviewLink) }
                                qrBitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Google Review QR Code",
                                        modifier = Modifier.size(120.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- DIALOGS (Payment, Signature) ---
        if (showPaymentDialog && invoice != null) {
            AlertDialog(
                onDismissRequest = { showPaymentDialog = false },
                title = { Text("Log Manual Payment", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Enter the payment amount collected (e.g. Check or Cash).", color = Color.Gray)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = paymentAmountText,
                            onValueChange = { paymentAmountText = it },
                            label = { Text("Amount Received") },
                            leadingIcon = { Text("$") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amount = paymentAmountText.toDoubleOrNull() ?: 0.0
                            if (amount > 0) {
                                isSubmittingPayment = true
                                coroutineScope.launch {
                                    try {
                                        val baseTotal = invoice!!.subtotal + invoice!!.taxAmount
                                        val newAmountPaid = invoice!!.amountPaid + amount
                                        val unpaidBase = (baseTotal - newAmountPaid).coerceAtLeast(0.0)
                                        val newCcFee = if (invoice!!.applyCreditCardFee && unpaidBase > 0) {
                                            val percentageFee = unpaidBase * (companyCcFeeRate / 100.0)
                                            ((percentageFee + companyCcFeeFixed) * 100).toLong() / 100.0
                                        } else 0.0
                                        val newTotalAmount = baseTotal + newCcFee
                                        val newStatus = if (newAmountPaid >= newTotalAmount - 0.01) "Paid" else "Partial Payment"
                                        val newPayment = PaymentRecord(id = UUID.randomUUID().toString(), amount = amount, method = "Check/Cash", date = Timestamp.now())

                                        firestore.collection("companies").document(companyName).collection("invoices").document(invoice!!.id)
                                            .update(mapOf("amountPaid" to newAmountPaid, "status" to newStatus, "totalAmount" to newTotalAmount, "creditCardFeeAmount" to newCcFee, "needsSync" to true, "paymentHistory" to FieldValue.arrayUnion(newPayment))).await()
                                        Toast.makeText(context, "Payment Logged!", Toast.LENGTH_SHORT).show()
                                        showPaymentDialog = false
                                    } catch (e: Exception) { Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
                                    finally { isSubmittingPayment = false }
                                }
                            }
                        },
                        enabled = !isSubmittingPayment,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                    ) { if (isSubmittingPayment) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White) else Text("Save Payment") }
                },
                dismissButton = { TextButton(onClick = { showPaymentDialog = false }) { Text("Cancel", color = Color.Gray) } }
            )
        }

        // SIGNATURE DIALOG
        if (showSignatureDialog && invoice != null) {
            SignatureDialog(
                onDismiss = { showSignatureDialog = false },
                onSave = { bitmap ->
                    showSignatureDialog = false
                    isUploadingSignature = true
                    coroutineScope.launch {
                        try {
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                            val data = baos.toByteArray()
                            val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                            val storageRef = Firebase.storage.reference.child("companies/$safeCompany/invoices/${invoice!!.id}/signature.png")
                            storageRef.putBytes(data).await()
                            val downloadUrl = storageRef.downloadUrl.await().toString()
                            firestore.collection("companies").document(companyName).collection("invoices").document(invoice!!.id).update(mapOf("signatureUrl" to downloadUrl, "status" to "Approved", "acceptedDate" to FieldValue.serverTimestamp())).await()
                            Toast.makeText(context, "Invoice Signed!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) { Toast.makeText(context, "Failed to save signature", Toast.LENGTH_LONG).show() }
                        finally { isUploadingSignature = false }
                    }
                }
            )
        }
    }
}
fun generateQrCodeBitmap(content: String): Bitmap? {
    if (content.isBlank()) return null
    val result = QRCode.ofSquares().build(content).render()
    val bytes = result.getBytes()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}


@Composable
fun CustomerDetailsTab(
    customer: CustomerInfo,
    isSyncing: Boolean,       // <-- 1. Add parameter
    onSyncClick: () -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { DetailItem(label = "Name", value = customer.name) }
        if (customer.companyName.isNotBlank()) {
            item { DetailItem(label = "Company", value = customer.companyName) }
        }
        if (customer.phone.isNotBlank()) {
            item {
                DetailItem(label = "Phone", value = customer.phone, onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, "tel:${customer.phone}".toUri())
                    context.startActivity(intent)
                })
            }
        }
        if (customer.email.isNotBlank()) {
            item { DetailItem(label = "Email", value = customer.email) }
        }
        if (customer.address.isNotBlank()) {
            item {
                DetailItem(label = "Billing Address", value = customer.address, onClick = {
                    val gmmIntentUri = "geo:0,0?q=${Uri.encode(customer.address)}".toUri()
                    context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri).setPackage("com.google.android.apps.maps"))
                })
            }
        }
        if (customer.jobsiteAddresses.isNotEmpty()) {
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
            item { Text("Jobsite Addresses", style = MaterialTheme.typography.titleMedium) }
            items(customer.jobsiteAddresses) { address ->
                DetailItem(label = "Jobsite", value = address, onClick = {
                    val gmmIntentUri = "geo:0,0?q=${Uri.encode(address)}".toUri()
                    context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri).setPackage("com.google.android.apps.maps"))
                })
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onSyncClick,
                enabled = !isSyncing,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSyncing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Syncing...")
                } else {
                    Text("Sync Customer to QuickBooks")
                }
            }
        }
    }
}

@Composable
fun <T> CustomerRelatedItemsTab(items: List<T>, itemContent: @Composable (T) -> Unit) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No items found.", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.size) { index ->
                itemContent(items[index])
            }
        }
    }
}

@Composable
fun SummaryEstimateCard(
    estimate: Estimate,
    onClick: () -> Unit,
    onDuplicateClick: () -> Unit // New parameter
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        // --- THIS IS THE MAGIC LINE ---
        colors = CardDefaults.cardColors(
            containerColor = getStatusColor(estimate.status).copy(alpha = 0.15f)
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Existing Column for estimate details
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    // Add padding to the end to avoid overlapping the icon
                    .padding(end = 40.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#${estimate.estimateNumber}", fontWeight = FontWeight.Bold)
                    Text(
                        text = estimate.status,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = getStatusColor(estimate.status) // <-- CHANGED THIS
                    )
                }
                if (estimate.description.isNotBlank()) {
                    Text(
                        estimate.description,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        dateFormat.format(estimate.creationDate.toDate()),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text("$%.2f".format(estimate.totalAmount), fontWeight = FontWeight.Bold)
                }
            }

            // --- NEW: Dropdown Menu ---
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Duplicate") },
                        onClick = {
                            expanded = false
                            onDuplicateClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryInvoiceCard(invoice: Invoice, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick, // Make the card clickable
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        // --- THIS IS THE MAGIC LINE ---
        colors = CardDefaults.cardColors(
            containerColor = getStatusColor(invoice.status).copy(alpha = 0.15f)
        )
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("#${invoice.invoiceNumber}", fontWeight = FontWeight.Bold)
                Text(
                    text = invoice.status,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = getStatusColor(invoice.status) // <-- CHANGED THIS
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Due: ${dateFormat.format(invoice.dueDate.toDate())}", style = MaterialTheme.typography.bodyMedium)
                Text("$%.2f".format(invoice.totalAmount), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CustomerEstimatesTab(
    items: List<Estimate>,
    onItemClick: (String) -> Unit,
    onDuplicateClick: (String) -> Unit
) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No estimates found.", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.id }) { estimate ->
                SummaryEstimateCard(
                    estimate = estimate,
                    onClick = { onItemClick(estimate.id) },
                    onDuplicateClick = { onDuplicateClick(estimate.id) } // <-- Pass the action up
                )
            }
        }
    }
}

@Composable
fun CustomerInvoicesTab(
    items: List<Invoice>,
    onItemClick: (String) -> Unit
) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No invoices found.", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.id }) { invoice ->
                SummaryInvoiceCard(invoice) { onItemClick(invoice.id) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryJobCard(job: FirestoreTask, onClick: () -> Unit) { // <-- 1. Add onClick parameter
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick // <-- 2. Add onClick to the Card
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(if(job.jobNumber.isNotBlank()) "Job #${job.jobNumber}" else "Job", fontWeight = FontWeight.Bold)
                Text(job.status, style = MaterialTheme.typography.bodySmall)
            }
            if(job.jobDescription.isNotBlank()){
                Text(job.jobDescription, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, color = Color.Gray)
            }
            job.startDate?.let {
                Text("Scheduled: ${dateFormat.format(it.toDate())}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
            @Composable
            fun EstimateInfoRow(
                estimate: Estimate,
                isSelected: Boolean, // This parameter is needed
                onEstimateClick: () -> Unit // This parameter is needed
            ) {
                val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        // Use the isSelected parameter to conditionally show a border
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        // Use isSelected to conditionally change the background
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        // Use the onEstimateClick lambda to make the whole item clickable
                        .clickable { onEstimateClick() }
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Est #${estimate.estimateNumber}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$%.2f".format(estimate.totalAmount),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    if (estimate.description.isNotBlank()) {
                        Text(
                            text = estimate.description,
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = "Created: ${dateFormat.format(estimate.creationDate.toDate())}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            @Composable
            fun AddCustomerDialog(
                onDismiss: () -> Unit,
                onCustomerSaved: (CustomerInfo) -> Unit,
                firestore: FirebaseFirestore,
                companyName: String,
                currentUserId: String,
                currentUserName: String
            ) {
                var name by rememberSaveable { mutableStateOf("") }
                var customerCompanyName by rememberSaveable { mutableStateOf("") }
                var phone by rememberSaveable { mutableStateOf("") }
                var email by rememberSaveable { mutableStateOf("") }
                var address by rememberSaveable { mutableStateOf("") } // Billing Address

                // State for the dynamic list of jobsite addresses, starting with one empty field
                val jobsiteAddresses = remember { mutableStateListOf("") }

                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                val textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ) {
                            Text("Add New Customer", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Customer Name") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = customerCompanyName,
                                onValueChange = { customerCompanyName = it },
                                label = { Text("Company Name (Optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors
                            )
                            TextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Phone Number") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email Address") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors
                            )
                            Spacer(Modifier.height(8.dp))
                            AddressAutocompleteTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = "Billing Address",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        Text("Jobsite Addresses", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            jobsiteAddresses.forEachIndexed { index, jobsiteAddress ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AddressAutocompleteTextField(
                                        value = jobsiteAddress,
                                        onValueChange = { jobsiteAddresses[index] = it },
                                        label = "Jobsite Address ${index + 1}",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    if (jobsiteAddresses.size > 1) {
                                        IconButton(onClick = { jobsiteAddresses.removeAt(index) }) {
                                            Icon(Icons.Default.Close, contentDescription = "Remove Address")
                                        }
                                    }
                                }
                            }
                        }
                        TextButton(onClick = { jobsiteAddresses.add("") }) {
                            Text("+ Add Jobsite Address")
                        }

                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) { Text("Cancel") }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (name.isBlank()) {
                                        Toast.makeText(context, "Customer name cannot be empty.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val customerData = hashMapOf(
                                        "name" to name,
                                        "searchableName" to name.lowercase(),
                                        "companyName" to customerCompanyName,
                                        "phone" to phone,
                                        "email" to email,
                                        "address" to address,
                                        "jobsiteAddresses" to jobsiteAddresses.filter { it.isNotBlank() }
                                    )
                                    coroutineScope.launch {
                                        try {
                                            // 👇 2. UPDATE THE SAVE LOGIC TO RETURN THE CUSTOMER 👇
                                            val docRef = firestore.collection("companies").document(companyName)
                                                .collection("customers").add(customerData).await()
                                            // 👇 ADD THE AUDIT LOG HERE 👇
                                            logCompanyActivity(
                                                firestore = firestore,
                                                companyName = companyName,
                                                userId = currentUserId,
                                                userName = currentUserName,
                                                action = "CREATE_CUSTOMER",
                                                details = "Created new customer: $name (via Dialog)"
                                            )

                                            val newCustomer = CustomerInfo(
                                                id = docRef.id,
                                                name = name,
                                                companyName = customerCompanyName,
                                                phone = phone,
                                                email = email,
                                                address = address,
                                                jobsiteAddresses = jobsiteAddresses.filter { it.isNotBlank() },
                                                searchableName = name.lowercase()
                                            )

                                            Toast.makeText(context, "Customer saved!", Toast.LENGTH_SHORT).show()
                                            onCustomerSaved(newCustomer) // Pass it back!
                                        } catch (e: Exception) {
                                            Log.e("Firestore", "Error saving customer", e)
                                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            ) { Text("Save") }
                        }
                    }
                }
            }

            @Composable
            fun EditCustomerDialog(
                customer: CustomerInfo,
                onDismiss: () -> Unit,
                onCustomerSaved: () -> Unit,
                firestore: FirebaseFirestore,
                companyName: String
            ) {
                var name by rememberSaveable { mutableStateOf(customer.name) }
                var phone by rememberSaveable { mutableStateOf(customer.phone) }
                var email by rememberSaveable { mutableStateOf(customer.email) }
                var address by rememberSaveable { mutableStateOf(customer.address) }

                // Initialize the list with the customer's existing jobsite addresses
                val jobsiteAddresses = remember { customer.jobsiteAddresses.toMutableStateList() }

                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                val textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ) {
                            Text("Edit Customer", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
                            Spacer(Modifier.height(16.dp))
                            TextField(value = name, onValueChange = { name = it }, label = { Text("Customer Name") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                            Spacer(Modifier.height(8.dp))
                            TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                            Spacer(Modifier.height(8.dp))
                            TextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                            Spacer(Modifier.height(8.dp))
                            AddressAutocompleteTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = "Billing Address",
                                modifier = Modifier.fillMaxWidth())

                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        Text("Jobsite Addresses", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            jobsiteAddresses.forEachIndexed { index, jobsiteAddress ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AddressAutocompleteTextField(
                                        value = jobsiteAddress,
                                        onValueChange = { jobsiteAddresses[index] = it },
                                        label = "Jobsite Address ${index + 1}"
                                    )
                                    IconButton(onClick = { jobsiteAddresses.removeAt(index) }) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove Address")
                                    }
                                }
                            }
                        }
                        TextButton(onClick = { jobsiteAddresses.add("") }) {
                            Text("+ Add Jobsite Address")
                        }

                        Spacer(Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = onDismiss) { Text("Cancel") }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                val updatedData = mapOf(
                                    "name" to name,
                                    "searchableName" to name.lowercase(),
                                    "phone" to phone,
                                    "email" to email,
                                    "address" to address,
                                    "jobsiteAddresses" to jobsiteAddresses.filter { it.isNotBlank() }
                                )
                                coroutineScope.launch {
                                    try {
                                        firestore.collection("companies").document(companyName)
                                            .collection("customers").document(customer.id)
                                            .update(updatedData).await()
                                        Toast.makeText(context, "Customer updated!", Toast.LENGTH_SHORT).show()
                                        onCustomerSaved()
                                    } catch (e: Exception) {
                                        Log.e("Firestore", "Error updating customer", e)
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }) { Text("Save") }
                        }
                    }
                }
            }

            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun CustomersScreen(
                onBackToMenu: () -> Unit = {},
                onCustomerClick: (String) -> Unit,
                firestore: FirebaseFirestore,
                companyName: String,
                currentUserId: String,
                currentUserName: String
            ) {
                var customerList by remember { mutableStateOf<List<CustomerInfo>>(emptyList()) }
                var searchQuery by rememberSaveable { mutableStateOf("") }
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                var refreshTrigger by remember { mutableStateOf(0) }

                // State for Dialogs
                var showAddCustomerDialog by remember { mutableStateOf(false) }
                var showEditDialog by remember { mutableStateOf(false) }
                var showDeleteConfirmation by remember { mutableStateOf(false) }
                var selectedCustomer by remember { mutableStateOf<CustomerInfo?>(null) }

                // NOTE: The filePickerLauncher and import state variables have been removed.

                LaunchedEffect(companyName, searchQuery, refreshTrigger) {
                    if (companyName.isNotBlank()) {
                        delay(300)
                        val customersCollection = firestore.collection("companies").document(companyName)
                            .collection("customers")
                        val searchQueryLower = searchQuery.lowercase()

                        val firestoreQuery = if (searchQuery.isBlank()) {
                            // When search is empty, order by the display name
                            customersCollection.orderBy("name")
                        } else {
                            // --- FIX: Query against the searchableName field ---
                            customersCollection.orderBy("searchableName")
                                .whereGreaterThanOrEqualTo("searchableName", searchQueryLower)
                                .whereLessThanOrEqualTo("searchableName", searchQueryLower + '\uf8ff')
                        }

                        firestoreQuery.get()
                            .addOnSuccessListener { result ->
                                customerList = result.documents.mapNotNull { doc ->
                                    doc.toObject(CustomerInfo::class.java)?.copy(id = doc.id)
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error getting customers.", e)
                                Toast.makeText(context, "Failed to load customers.", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                // --- Dialogs for Add, Edit, Delete ---
                if (showAddCustomerDialog) {
                    AddCustomerDialog(
                        onDismiss = { showAddCustomerDialog = false },
                        onCustomerSaved = { _ -> // 👇 UPDATE THIS LINE 👇
                            showAddCustomerDialog = false
                            refreshTrigger++ // Refresh the customer list
                        },
                        firestore = firestore,
                        companyName = companyName,
                        currentUserId = currentUserId,
                        currentUserName = currentUserName,
                    )
                }
                if (showEditDialog && selectedCustomer != null) {
                    EditCustomerDialog(
                        customer = selectedCustomer!!,
                        onDismiss = { showEditDialog = false },
                        onCustomerSaved = {
                            showEditDialog = false
                            refreshTrigger++ // Refresh the customer list
                        },
                        firestore = firestore,
                        companyName = companyName
                    ) }
                if (showDeleteConfirmation && selectedCustomer != null) {
                    ConfirmationDialog(
                        title = stringResource(R.string.confirm_deletion),
                        message = stringResource(R.string.confirm_delete_customer, selectedCustomer!!.name),
                        onConfirm = {
                            coroutineScope.launch {
                                try {
                                    firestore.collection("companies").document(companyName)
                                        .collection("customers").document(selectedCustomer!!.id)
                                        .delete().await()
                                    Toast.makeText(context, R.string.toast_customer_deleted, Toast.LENGTH_SHORT).show()
                                    refreshTrigger++
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    showDeleteConfirmation = false
                                }
                            }
                        },
                        onDismiss = { showDeleteConfirmation = false }
                    ) }

                // NOTE: The dialogs for the import process have been removed.

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showAddCustomerDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(
                                R.string.add_customer))
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // --- REMOVED: The Row containing the import button is gone ---
                        // Replaced with a simple Text element for the title.
                        Text(
                            text = stringResource(R.string.customers),
                            fontSize = 26.sp,
                            modifier = Modifier.fillMaxWidth() // Aligns to the start by default
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text(stringResource(R.string.search_customers)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(
                                R.string.search_customers)) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (customerList.isEmpty()) {
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (searchQuery.isBlank()) stringResource(
                                        R.string.no_customers_found)
                                    else stringResource(R.string.no_customers_match_search),
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(customerList, key = { it.id }) { customer ->
                                    CustomerCard(
                                        customer = customer,
                                        onClick = { onCustomerClick(customer.id) }, // Pass the customer ID up
                                        onEditClick = {
                                            selectedCustomer = customer
                                            showEditDialog = true
                                        },
                                        onDeleteClick = {
                                            selectedCustomer = customer
                                            showDeleteConfirmation = true
                                        }
                                    )
                                }
                            }
                        }

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp), horizontalArrangement = Arrangement.Start) {
                            Button(onClick = onBackToMenu) {
                                Text(stringResource(R.string.back))
                            }
                        }
                    }
                }
            }
@Composable
fun CustomerCard(
    customer: CustomerInfo,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        onClick = onClick, // <-- THIS IS THE FIX
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(end = 40.dp), // Make space for the icon
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = customer.name, style = MaterialTheme.typography.titleLarge, color = Color.Black)
                if (customer.companyName.isNotBlank()) {
                    Text(text = customer.companyName, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                }
                if (customer.phone.isNotBlank()) {
                    Text(
                        text = customer.phone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                if (customer.email.isNotBlank()) {
                    Text(text = customer.email, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                }

                // Display Billing Address with a label
                if (customer.address.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Billing: ${customer.address}", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.SemiBold)
                }

                // Display the Jobsite Addresses
                if (customer.jobsiteAddresses.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    customer.jobsiteAddresses.forEach { jobsite ->
                        Text(text = "Jobsite: $jobsite", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                }
            }

            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.more_options),
                        tint = Color.Gray
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.edit), color = Color.Black) },
                        onClick = {
                            expanded = false
                            onEditClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.delete), color = Color.Black) },
                        onClick = {
                            expanded = false
                            onDeleteClick()
                        }
                    )
                }
            }
        }
    }
}

            @Composable fun InvoiceCard(
                invoice: Invoice,
                onClick: () -> Unit,
                onEditClick: () -> Unit,
                onDeleteClick: () -> Unit,
                firestore: FirebaseFirestore,
                companyName: String ) {
                var expanded by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope() // <-- THE FIX IS HERE
                Card(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = getStatusColor(invoice.status).copy(alpha = 0.15f)
                    )
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .padding(end = 40.dp), // Make space for the 3-dot menu icon
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // TOP ROW: Description/Number & Total Amount
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = invoice.description.ifBlank {
                                        stringResource(R.string.invoice_number_label, invoice.invoiceNumber)
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                )
                                Text(
                                    text = "$%.2f".format(invoice.totalAmount),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // MIDDLE ROW: Customer Name
                            Text(
                                text = invoice.customerName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )

                            // BOTTOM ROW: Due Date & Status Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                Text(
                                    text = "Due: ${dateFormat.format(invoice.dueDate.toDate())}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = invoice.status,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = getStatusColor(invoice.status) // Color-coded badge!
                                )
                            }
                        }

                        // 3-DOT DROPDOWN MENU (Top Right)
                        Box(modifier = Modifier.align(Alignment.TopEnd)) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, "More options", tint = Color.Gray)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit", color = Color.Black) },
                                    onClick = { expanded = false; onEditClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("Send Email", color = Color.Black) },
                                    onClick = {
                                        expanded = false
                                        Toast.makeText(context, "Open the invoice to email it.", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete", color = Color.Black) },
                                    onClick = { expanded = false; onDeleteClick() }
                                )
                            }
                        }
                    }
                }
            }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesScreen(
    onBackToMenu: () -> Unit,
    onNewInvoiceClick: () -> Unit,
    onInvoiceClick: (String) -> Unit,
    onEditInvoiceClick: (String) -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    var invoiceList by remember { mutableStateOf<List<Invoice>>(emptyList()) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // --- NEW: SEARCH AND TAB STATE ---
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("All", "Due", "Sent", "Paid")

    LaunchedEffect(companyName, refreshTrigger) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).collection("invoices")
                .orderBy("creationDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    invoiceList = result.documents.mapNotNull {
                        it.toObject(Invoice::class.java)?.copy(id = it.id)
                    }
                }
                .addOnFailureListener { e -> Log.w("InvoicesScreen", "Error getting invoices.", e) }
        }
    }

    // --- NEW: FILTER LOGIC (Applies Search AND Tab Selection) ---
    val filteredInvoices = remember(invoiceList, searchQuery, selectedTabIndex) {
        invoiceList.filter { invoice ->
            val matchesSearch = invoice.customerName.contains(searchQuery, ignoreCase = true) ||
                    invoice.invoiceNumber.contains(searchQuery, ignoreCase = true)

            val matchesTab = when (selectedTabIndex) {
                0 -> true // All
                1 -> invoice.status.equals("Due", ignoreCase = true)
                2 -> invoice.status.equals("Sent", ignoreCase = true)
                3 -> invoice.status.equals("Paid", ignoreCase = true) || invoice.status.equals("Approved", ignoreCase = true)
                else -> true
            }

            matchesSearch && matchesTab
        }
    }

    if (showDeleteConfirmation && selectedInvoice != null) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_deletion),
            message = stringResource(R.string.confirm_delete_invoice, selectedInvoice!!.invoiceNumber),
            onConfirm = {
                coroutineScope.launch {
                    try {
                        firestore.collection("companies").document(companyName)
                            .collection("invoices").document(selectedInvoice!!.id)
                            .delete().await()
                        Toast.makeText(context, R.string.toast_invoice_deleted, Toast.LENGTH_SHORT).show()
                        refreshTrigger++
                    } finally {
                        showDeleteConfirmation = false
                    }
                }
            },
            onDismiss = { showDeleteConfirmation = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Accounts Receivable") }, // Upgraded title!
                navigationIcon = {
                    IconButton(onClick = onBackToMenu) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNewInvoiceClick, containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_invoice))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Professional light gray background
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- NEW: SEARCH BAR ---
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by customer or #") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- NEW: STATUS TABS ---
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                divider = { HorizontalDivider(color = Color.LightGray) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (filteredInvoices.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (searchQuery.isBlank()) "No invoices found in this tab." else "No invoices match your search.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Extra breathing room
                ) {
                    items(filteredInvoices, key = { it.id }) { invoice ->
                        InvoiceCard(
                            invoice = invoice,
                            onClick = { onInvoiceClick(invoice.id) },
                            onEditClick = { onEditInvoiceClick(invoice.id) },
                            onDeleteClick = { selectedInvoice = invoice; showDeleteConfirmation = true },
                            firestore = firestore,
                            companyName = companyName
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeClockScreen(
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit = {},
    firestore: FirebaseFirestore,
    companyName: String,
    userRole: String,
    userId: String,
    userName: String
) {
    val TAG = "TimeClockDebug"
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var payPeriods by remember { mutableStateOf<List<Pair<Timestamp, Timestamp>>>(emptyList()) }
    var selectedPayPeriod by remember { mutableStateOf<Pair<Timestamp, Timestamp>?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingTimeEntry by remember { mutableStateOf<TimeEntry?>(null) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var entryToDelete by remember { mutableStateOf<TimeEntry?>(null) }

    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName)
                .addSnapshotListener { snap, err ->
                    if (snap != null && snap.exists()) {
                        val refDate = snap.getString("payPeriodReferenceDate")
                        val cycle = snap.getLong("payPeriodCycleDays")?.toInt() ?: 14

                        val newPeriods = generatePayPeriods(refDate, cycle)
                        payPeriods = newPeriods
                        if (selectedPayPeriod == null || !newPeriods.contains(selectedPayPeriod)) {
                            selectedPayPeriod = newPeriods.firstOrNull()
                        }
                    }
                }
        }
    }

    // Google Play Prominent Disclosure State
    var showLocationDisclosure by remember { mutableStateOf(false) }

    fun getMyTimeEntriesCollection(): CollectionReference {
        return firestore.collection("companies").document(companyName)
            .collection("users").document(userId).collection("time_entries")
    }

    val isClockedIn by produceState(initialValue = false, userId, companyName) {
        val listener = getMyTimeEntriesCollection().whereEqualTo("clockOut", null).limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("TimeClockListener", "Clock-in status listen failed.", error)
                    return@addSnapshotListener
                }
                value = snapshot != null && !snapshot.isEmpty
            }
        awaitDispose { listener.remove() }
    }

    val timeEntries by produceState<List<TimeEntry>>(initialValue = emptyList(), userId, companyName, selectedPayPeriod) {
        if (selectedPayPeriod != null) {
            val (startDate, endDate) = selectedPayPeriod!!
            val listener = getMyTimeEntriesCollection()
                .whereGreaterThanOrEqualTo("clockIn", startDate)
                .whereLessThanOrEqualTo("clockIn", endDate)
                .orderBy("clockIn", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("TimeClockListener", "Time entry listen failed.", error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        value = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(TimeEntry::class.java)?.copy(id = doc.id)
                        }
                    }
                }
            awaitDispose { listener.remove() }
        } else {
            value = emptyList()
        }
    }

    val totalHoursWorked = remember(timeEntries) {
        val totalMillis = timeEntries.sumOf { entry ->
            if (entry.clockIn != null && entry.clockOut != null) {
                entry.clockOut.toDate().time - entry.clockIn.toDate().time
            } else 0L
        }
        val decimalHours = totalMillis / (1000.0 * 60.0 * 60.0)
        String.format(Locale.getDefault(), "%.1f hours", decimalHours)
    }

    val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
    val payPeriodFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    val performClockIn = {
        val batch = firestore.batch()
        val newEntryRef = getMyTimeEntriesCollection().document()
        val newEntry = hashMapOf(
            "userId" to userId,
            "companyName" to companyName,
            "clockIn" to FieldValue.serverTimestamp(),
            "clockOut" to null
        )
        batch.set(newEntryRef, newEntry)

        val userRef = firestore.collection("companies").document(companyName).collection("users").document(userId)
        batch.update(userRef, "isClockedIn", true)

        batch.commit()
            .addOnSuccessListener {
                selectedPayPeriod = payPeriods.firstOrNull()
                context.startService(Intent(context, LocationService::class.java))
                logCompanyActivity(firestore, companyName, userId, userName, "CLOCK_IN", "$userName clocked in.")
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(context, "Clock in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
            val coarseLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)

            if (fineLocationGranted || coarseLocationGranted) {
                performClockIn()
            } else {
                Toast.makeText(context, "Location permission is required to clock in.", Toast.LENGTH_LONG).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }

    if (showLocationDisclosure) {
        AlertDialog(
            onDismissRequest = { showLocationDisclosure = false },
            title = { Text("Location Tracking Required", fontWeight = FontWeight.Bold) },
            text = { Text("CrewQ collects location data to enable the 'Team Map' and to verify 'Time Clock' punches even when the app is closed or not in use.\n\nThis allows your manager to see your location while you are clocked in.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLocationDisclosure = false
                        locationPermissionLauncher.launch(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("I Agree") }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDisclosure = false }) { Text("Decline", color = Color.Gray) }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = stringResource(R.string.time_clock), fontSize = 26.sp)
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = selectedPayPeriod?.let {
                    "${payPeriodFormat.format(it.first.toDate())} - ${payPeriodFormat.format(it.second.toDate())}"
                } ?: stringResource(R.string.select_pay_period),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.pay_period)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                payPeriods.forEach { period ->
                    DropdownMenuItem(
                        text = { Text("${payPeriodFormat.format(period.first.toDate())} - ${payPeriodFormat.format(period.second.toDate())}") },
                        onClick = { selectedPayPeriod = period; expanded = false }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = dateTimeFormat.format(Date(currentTime)).split(" ")[0], fontSize = 24.sp)
        Text(text = timeFormat.format(Date(currentTime)), fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    val hasFineLocation = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    val hasCoarseLocation = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasFineLocation || hasCoarseLocation) {
                        performClockIn()
                    } else {
                        showLocationDisclosure = true
                    }
                },
                enabled = !isClockedIn,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
            ) { Text("Clock In") }

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val openEntries = getMyTimeEntriesCollection().whereEqualTo("clockOut", null).limit(1).get().await()
                            if (!openEntries.isEmpty) {
                                val docId = openEntries.documents[0].id
                                val batch = firestore.batch()
                                val entryRef = getMyTimeEntriesCollection().document(docId)
                                batch.update(entryRef, "clockOut", FieldValue.serverTimestamp())
                                val userRef = firestore.collection("companies").document(companyName).collection("users").document(userId)
                                batch.update(userRef, "isClockedIn", false)
                                batch.commit().await()
                                context.stopService(Intent(context, LocationService::class.java))
                                logCompanyActivity(firestore, companyName, userId, userName, "CLOCK_OUT", "$userName clocked out.")
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Clock out failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = isClockedIn,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Clock Out") }
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (isClockedIn) {
            Text("Status: Clocked In (Location Tracking Active)", color = Color(0xFF2CA01C), fontWeight = FontWeight.Bold)
        } else {
            Text("Status: Clocked Out", color = Color.Red, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Total Hours for Period: $totalHoursWorked", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(timeEntries, key = { it.id }) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("In: ${entry.clockIn?.toDate()?.let { dateTimeFormat.format(it) } ?: "N/A"}", color = Color.Black)
                        Text("Out: ${entry.clockOut?.toDate()?.let { dateTimeFormat.format(it) } ?: "N/A"}", color = Color.Black)
                        if (userRole == "Manager") {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                                Button(onClick = { editingTimeEntry = entry; showEditDialog = true }) { Text("Edit") }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { entryToDelete = entry; showDeleteConfirmationDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) { Text("Delete") }
                            }
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Button(onClick = onBackToMenu) { Text("Back") }
        }
    }

    if (showEditDialog && editingTimeEntry != null) {
        TimeEntryEditDialog(
            timeEntry = editingTimeEntry!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedEntry ->
                val updates = mapOf("clockIn" to updatedEntry.clockIn, "clockOut" to updatedEntry.clockOut)
                getMyTimeEntriesCollection().document(updatedEntry.id).update(updates)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Entry updated", Toast.LENGTH_SHORT).show()
                        showEditDialog = false
                    }
                    .addOnFailureListener { e: Exception -> Toast.makeText(context, "Error updating: ${e.message}", Toast.LENGTH_SHORT).show() }
            }
        )
    }

    if (showDeleteConfirmationDialog && entryToDelete != null) {
        ConfirmationDialog(
            title = "Confirm Deletion",
            message = "Are you sure you want to delete this time entry?",
            onConfirm = {
                getMyTimeEntriesCollection().document(entryToDelete!!.id).delete()
                    .addOnSuccessListener { Toast.makeText(context, "Entry deleted", Toast.LENGTH_SHORT).show() }
                showDeleteConfirmationDialog = false
                entryToDelete = null
            },
            onDismiss = { showDeleteConfirmationDialog = false; entryToDelete = null }
        )
    }
}
@Composable
fun EditScheduleDialog(
    user: CompanyUser,
    onDismiss: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    // Load existing schedule or default to empty strings
    val scheduleState = remember {
        mutableStateMapOf<String, String>().apply {
            daysOfWeek.forEach { day ->
                this[day] = user.weeklySchedule[day] ?: ""
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text("Edit Schedule: ${user.firstName}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.Black)
                Text("Enter shift times (e.g., '8am - 5pm' or 'Off')", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(daysOfWeek) { day ->
                        TextField(
                            value = scheduleState[day] ?: "",
                            onValueChange = { scheduleState[day] = it },
                            label = { Text(day) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(scheduleState.toMap()) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Save Schedule") }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPeriodSettingsDialog(
    currentRefDate: String?,
    currentCycleDays: Int,
    onDismiss: () -> Unit,
    onSave: (String, Int) -> Unit
) {
    var cycleDays by remember { mutableIntStateOf(currentCycleDays) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Convert current string to millis for the DatePicker
    val initialMillis = remember {
        try {
            if (!currentRefDate.isNullOrBlank()) {
                val date = LocalDate.parse(currentRefDate, DateTimeFormatter.ISO_LOCAL_DATE)
                date.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
            } else System.currentTimeMillis()
        } catch (e: Exception) { System.currentTimeMillis() }
    }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    // Format the currently selected date from the picker for display
    val displayDate = remember(datePickerState.selectedDateMillis) {
        val millis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
        val localCalendar = Calendar.getInstance().apply {
            set(utcCalendar.get(Calendar.YEAR), utcCalendar.get(Calendar.MONTH), utcCalendar.get(Calendar.DAY_OF_MONTH))
        }
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(localCalendar.time)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = { showDatePicker = false }) { Text("OK") } }
        ) { DatePicker(state = datePickerState) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()) {
                Text("Pay Period Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Configure how timesheets are generated.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(16.dp))

                Text("Pay Cycle Length", fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = cycleDays == 7, onClick = { cycleDays = 7 })
                    Text("Weekly (7 Days)")
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = cycleDays == 14, onClick = { cycleDays = 14 })
                    Text("Bi-Weekly (14 Days)")
                }

                Spacer(Modifier.height(16.dp))
                Text("Anchor Date (Start of a Pay Period)", fontWeight = FontWeight.SemiBold)
                OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(displayDate)
                }

                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val millis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
                        // Format specifically as YYYY-MM-DD for stable parsing
                        val isoString = String.format(Locale.US, "%04d-%02d-%02d",
                            utcCal.get(Calendar.YEAR), utcCal.get(Calendar.MONTH) + 1, utcCal.get(Calendar.DAY_OF_MONTH)
                        )
                        onSave(isoString, cycleDays)
                    }) { Text("Save Settings") }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamTimeClockScreen(
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit = {},
    firestore: FirebaseFirestore,
    companyName: String,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // --- PAY PERIOD SETTINGS STATE ---
    var payPeriodRefDate by remember { mutableStateOf<String?>(null) }
    var payPeriodCycleDays by remember { mutableIntStateOf(14) }
    var showPayPeriodSettings by remember { mutableStateOf(false) }

    // --- TIME CLOCK STATE ---
    var userDetails by remember { mutableStateOf<List<UserDetails>>(emptyList()) }
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
    val payPeriodFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    // Dynamic pay period state
    var payPeriods by remember { mutableStateOf<List<Pair<Timestamp, Timestamp>>>(emptyList()) }
    var selectedPayPeriod by remember { mutableStateOf<Pair<Timestamp, Timestamp>?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingTeamTimeEntry by remember { mutableStateOf<TeamTimeEntry?>(null) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var teamEntryToDelete by remember { mutableStateOf<TeamTimeEntry?>(null) }

    // --- SCHEDULE & TAB STATE ---
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Timesheets", "Work Schedules")
    var companyUsers by remember { mutableStateOf<List<CompanyUser>>(emptyList()) }
    var userToEditSchedule by remember { mutableStateOf<CompanyUser?>(null) }

    // Tracks which employee cards are currently expanded to show punches
    var expandedUserCards by remember { mutableStateOf(setOf<String>()) }

    fun calculateTotalHoursForTeam(entries: List<TeamTimeEntry>): String {
        var totalMillis: Long = 0
        for (entry in entries) {
            if (entry.clockIn != null && entry.clockOut != null) {
                totalMillis += entry.clockOut.toDate().time - entry.clockIn.toDate().time
            }
        }
        val decimalHours = totalMillis / (1000.0 * 60.0 * 60.0)
        return String.format(Locale.getDefault(), "%.1f hours", decimalHours)
    }

    fun fetchTeamTimeEntries(startDate: Timestamp, endDate: Timestamp) {
        if (companyName.isBlank()) return
        coroutineScope.launch {
            try {
                val usersSnapshot = firestore.collection("companies").document(companyName).collection("users").get().await()

                // Save full user objects for the Schedule Tab
                companyUsers = usersSnapshot.documents.mapNotNull { it.toObject(CompanyUser::class.java)?.copy(id = it.id) }

                val usersMap = usersSnapshot.documents.associate { doc ->
                    doc.id to "${doc.getString("firstName") ?: ""} ${doc.getString("lastName") ?: ""}".trim()
                }

                val timeEntriesSnapshot = firestore.collectionGroup("time_entries")
                    .whereEqualTo("companyName", companyName)
                    .whereGreaterThanOrEqualTo("clockIn", startDate)
                    .whereLessThanOrEqualTo("clockIn", endDate)
                    .orderBy("clockIn", Query.Direction.DESCENDING)
                    .get().await()

                val entries = timeEntriesSnapshot.documents.mapNotNull { doc ->
                    val userId = doc.getString("userId")
                    if (userId != null) {
                        TeamTimeEntry(
                            id = doc.id,
                            userId = userId,
                            userName = usersMap[userId] ?: "Unknown User",
                            clockIn = doc.getTimestamp("clockIn"),
                            clockOut = doc.getTimestamp("clockOut")
                        )
                    } else null
                }
                val groupedEntries = entries.groupBy { it.userName }
                userDetails = groupedEntries.map { (userName, userEntries) ->
                    UserDetails(userName = userName, totalHours = calculateTotalHoursForTeam(userEntries), entries = userEntries)
                }.sortedBy { it.userName } // Alphabetical order
            } catch (e: Exception) {
                Log.e("TeamTimeClock", "Error fetching data", e)
            }
        }
    }

    // --- FETCH COMPANY SETTINGS FOR PAY PERIODS ---
    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName)
                .addSnapshotListener { snap, err ->
                    if (snap != null && snap.exists()) {
                        payPeriodRefDate = snap.getString("payPeriodReferenceDate")
                        payPeriodCycleDays = snap.getLong("payPeriodCycleDays")?.toInt() ?: 14

                        // Generate the periods based on the fetched settings
                        val newPeriods = generatePayPeriods(payPeriodRefDate, payPeriodCycleDays)
                        payPeriods = newPeriods

                        // Set default selection if none exists or if old one is no longer valid
                        if (selectedPayPeriod == null || !newPeriods.contains(selectedPayPeriod)) {
                            selectedPayPeriod = newPeriods.firstOrNull()
                        }
                    }
                }
        }
    }

    LaunchedEffect(companyName, selectedPayPeriod) {
        selectedPayPeriod?.let { fetchTeamTimeEntries(it.first, it.second) }
    }

    // --- DIALOGS ---

    // 1. Pay Period Settings Dialog
    if (showPayPeriodSettings) {
        PayPeriodSettingsDialog(
            currentRefDate = payPeriodRefDate,
            currentCycleDays = payPeriodCycleDays,
            onDismiss = { showPayPeriodSettings = false },
            onSave = { newDateStr, newCycle ->
                coroutineScope.launch {
                    try {
                        firestore.collection("companies").document(companyName)
                            .update(
                                mapOf(
                                    "payPeriodReferenceDate" to newDateStr,
                                    "payPeriodCycleDays" to newCycle
                                )
                            ).await()
                        Toast.makeText(context, "Settings saved!", Toast.LENGTH_SHORT).show()
                        showPayPeriodSettings = false
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    // 2. Schedule Edit Dialog
    userToEditSchedule?.let { user ->
        EditScheduleDialog(
            user = user,
            onDismiss = { userToEditSchedule = null },
            onSave = { newSchedule ->
                coroutineScope.launch {
                    try {
                        firestore.collection("companies").document(companyName)
                            .collection("users").document(user.id)
                            .update("weeklySchedule", newSchedule).await()
                        Toast.makeText(context, "Schedule saved!", Toast.LENGTH_SHORT).show()
                        userToEditSchedule = null
                        // Refresh the users list
                        selectedPayPeriod?.let { fetchTeamTimeEntries(it.first, it.second) }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    // 3. Time Entry Edit Dialog
    if (showEditDialog && editingTeamTimeEntry != null) {
        TeamTimeEntryEditDialog(
            timeEntry = editingTeamTimeEntry!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedEntry ->
                val entryRef = firestore.collection("companies").document(companyName)
                    .collection("users").document(updatedEntry.userId)
                    .collection("time_entries").document(updatedEntry.id)

                entryRef.update(
                    "clockIn", updatedEntry.clockIn,
                    "clockOut", updatedEntry.clockOut
                ).addOnSuccessListener {
                    Toast.makeText(context, "Entry updated", Toast.LENGTH_SHORT).show()
                    selectedPayPeriod?.let { fetchTeamTimeEntries(it.first, it.second) }
                    showEditDialog = false
                }
            }
        )
    }

    // 4. Time Entry Delete Dialog
    if (showDeleteConfirmationDialog && teamEntryToDelete != null) {
        ConfirmationDialog(
            title = "Confirm Deletion",
            message = "Are you sure you want to delete this team time entry?",
            onConfirm = {
                firestore.collection("companies").document(companyName)
                    .collection("users").document(teamEntryToDelete!!.userId)
                    .collection("time_entries").document(teamEntryToDelete!!.id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Entry deleted", Toast.LENGTH_SHORT).show()
                        selectedPayPeriod?.let { fetchTeamTimeEntries(it.first, it.second) }
                    }
                showDeleteConfirmationDialog = false
            },
            onDismiss = { showDeleteConfirmationDialog = false }
        )
    }

    // --- UI ---
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackToMenu) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                Text("Team Time & Schedule", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
            // Settings Gear Icon
            IconButton(onClick = { showPayPeriodSettings = true }) {
                Icon(Icons.Default.Build, contentDescription = "Pay Period Settings", tint = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // TABS
        TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.Transparent) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTabIndex == 0) {
            // --- TAB 1: TIMESHEETS ---
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                TextField(
                    value = selectedPayPeriod?.let { "${payPeriodFormat.format(it.first.toDate())} - ${payPeriodFormat.format(it.second.toDate())}" } ?: "Select Pay Period",
                    onValueChange = {}, readOnly = true, label = { Text("Pay Period") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                    payPeriods.forEach { period ->
                        DropdownMenuItem(
                            text = { Text("${payPeriodFormat.format(period.first.toDate())} - ${payPeriodFormat.format(period.second.toDate())}") },
                            onClick = { selectedPayPeriod = period; expanded = false }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (userDetails.isEmpty()) {
                    item { Text("No time entries for this pay period.", color = Color.Gray) }
                }
                items(userDetails, key = { it.userName }) { user ->
                    val isExpanded = expandedUserCards.contains(user.userName)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedUserCards =
                                    if (isExpanded) expandedUserCards - user.userName else expandedUserCards + user.userName
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(user.userName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text("Total: ${user.totalHours}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                }
                                Icon(if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = "Expand")
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(modifier = Modifier.padding(top = 16.dp)) {
                                    HorizontalDivider()
                                    Spacer(modifier = Modifier.height(8.dp))
                                    user.entries.forEach { entry ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("In: ${entry.clockIn?.toDate()?.let { dateTimeFormat.format(it) } ?: "N/A"}", fontSize = 12.sp)
                                                Text("Out: ${entry.clockOut?.toDate()?.let { dateTimeFormat.format(it) } ?: "N/A"}", fontSize = 12.sp)
                                            }
                                            Row {
                                                IconButton(onClick = { editingTeamTimeEntry = entry; showEditDialog = true }) {
                                                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(20.dp))
                                                }
                                                IconButton(onClick = { teamEntryToDelete = entry; showDeleteConfirmationDialog = true }) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
                                                }
                                            }
                                        }
                                        HorizontalDivider(color = Color(0xFFF0F0F0))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // --- TAB 2: SCHEDULES ---
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(companyUsers, key = { it.id }) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("${user.firstName} ${user.lastName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                OutlinedButton(onClick = { userToEditSchedule = user }) { Text("Edit Schedule") }
                            }

                            Spacer(Modifier.height(8.dp))

                            // Display the schedule if they have one
                            if (user.weeklySchedule.isNotEmpty()) {
                                listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").forEach { day ->
                                    val shift = user.weeklySchedule[day]
                                    if (!shift.isNullOrBlank()) {
                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(day, color = Color.Gray, fontSize = 14.sp)
                                            Text(shift, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                        }
                                    }
                                }
                            } else {
                                Text("No schedule assigned.", color = Color.Gray, fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CompanySettingsDialog(
    onDismiss: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    var taxRateText by rememberSaveable { mutableStateOf("") }
    var displayName by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var cityStateZip by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var hideItemization by rememberSaveable { mutableStateOf(false) }
    var ccFeeRateText by rememberSaveable { mutableStateOf("") }
    var enableDispatchFee by rememberSaveable { mutableStateOf(false) }
    var dispatchFeeAmountText by rememberSaveable { mutableStateOf("") }

    // 👇 NEW: Fixed Fee State 👇
    var ccFeeFixedText by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var googleReviewLink by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).get()
                .addOnSuccessListener { doc ->
                    taxRateText = (doc.getDouble("taxRate") ?: 0.0).toString()
                    displayName = doc.getString("displayName") ?: companyName
                    address = doc.getString("address") ?: ""
                    cityStateZip = doc.getString("cityStateZip") ?: ""
                    phone = doc.getString("phone") ?: ""
                    email = doc.getString("email") ?: ""
                    hideItemization = doc.getBoolean("hideItemization") ?: false
                    ccFeeRateText = (doc.getDouble("ccFeeRate") ?: 2.9).toString()
                    enableDispatchFee = doc.getBoolean("enableDispatchFee") ?: false
                    dispatchFeeAmountText = (doc.getDouble("dispatchFeeAmount") ?: 99.0).toString()

                    // 👇 NEW: Load Fixed Fee (Default 0.30) 👇
                    ccFeeFixedText = (doc.getDouble("ccFeeFixed") ?: 0.30).toString()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to load settings.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Company Settings", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
                Spacer(Modifier.height(16.dp))

                val colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black
                )

                TextField(value = displayName, onValueChange = { displayName = it }, label = { Text("Display Name (For PDFs)") }, modifier = Modifier.fillMaxWidth(), colors = colors)
                Spacer(Modifier.height(8.dp))
                TextField(value = address, onValueChange = { address = it }, label = { Text("Address (Line 1)") }, modifier = Modifier.fillMaxWidth(), colors = colors)
                Spacer(Modifier.height(8.dp))
                TextField(value = cityStateZip, onValueChange = { cityStateZip = it }, label = { Text("City, State Zip") }, modifier = Modifier.fillMaxWidth(), colors = colors)
                Spacer(Modifier.height(8.dp))
                TextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), colors = colors)
                Spacer(Modifier.height(8.dp))
                TextField(value = email, onValueChange = { email = it }, label = { Text("Public Email") }, modifier = Modifier.fillMaxWidth(), colors = colors)
                Spacer(Modifier.height(8.dp))
                TextField(value = taxRateText, onValueChange = { taxRateText = it }, label = { Text("Default Sales Tax Rate (%)") }, modifier = Modifier.fillMaxWidth(), colors = colors)
                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(value = ccFeeRateText, onValueChange = { ccFeeRateText = it }, label = { Text("CC Fee (%)") }, modifier = Modifier.weight(1f), colors = colors)

                    // 👇 NEW: Fixed Fee TextField 👇
                    TextField(value = ccFeeFixedText, onValueChange = { ccFeeFixedText = it }, label = { Text("Fixed Fee ($)") }, modifier = Modifier.weight(1f), colors = colors)
                }
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = hideItemization, onCheckedChange = { hideItemization = it })
                    Text("Hide Line Items on PDFs", color = Color.Black)
                }
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(Modifier.height(16.dp))

                Text("Pre-Work Agreement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = enableDispatchFee, onCheckedChange = { enableDispatchFee = it })
                    Text("Require Dispatch Fee signature before jobs", color = Color.Black)
                }

                if (enableDispatchFee) {
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = dispatchFeeAmountText,
                        onValueChange = { dispatchFeeAmountText = it },
                        label = { Text("Dispatch Fee Amount ($)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = colors
                    )
                }
                TextField(
                    value = googleReviewLink,
                    onValueChange = { googleReviewLink = it },
                    label = { Text("Google Review Link") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("https://g.page/r/YOUR_CODE/review") }
                )

                Spacer(Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val rate = taxRateText.toDoubleOrNull() ?: 0.0
                        coroutineScope.launch {
                            try {
                                val updates = mapOf(
                                    "taxRate" to rate,
                                    "displayName" to displayName,
                                    "address" to address,
                                    "cityStateZip" to cityStateZip,
                                    "phone" to phone,
                                    "email" to email,
                                    "hideItemization" to hideItemization,
                                    "ccFeeRate" to (ccFeeRateText.toDoubleOrNull() ?: 0.0),
                                    "ccFeeFixed" to (ccFeeFixedText.toDoubleOrNull() ?: 0.0),
                                    "enableDispatchFee" to enableDispatchFee,
                                    "dispatchFeeAmount" to (dispatchFeeAmountText.toDoubleOrNull() ?: 0.0),
                                    "googleReviewLink" to googleReviewLink
                                )
                                firestore.collection("companies").document(companyName).update(updates).await()
                                Toast.makeText(context, "Settings Saved!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) { Text("Save") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit,
    subStatus: String,
    daysLeft: Int?,
    isTrial: Boolean,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    companyName: String,
    userRole: String,
    onAddingUserChange: (Boolean) -> Unit,
    onNavigateToAuditLog: () -> Unit
) {

    var showCompanySettingsDialog by remember { mutableStateOf(false) }
    var showUserManagementDialog by remember { mutableStateOf(false) }
    var showAddUserDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var companyLogoUrl by remember { mutableStateOf<String?>(null) }
    var isQuickBooksConnected by remember { mutableStateOf(false) }
    var showDisconnectQboDialog by remember { mutableStateOf(false) }

    var isImportingQboCustomers by remember { mutableStateOf(false) }
    var isImportingQboProducts by remember { mutableStateOf(false) }

    var subStatus by remember { mutableStateOf("Loading...") }
    var trialDaysLeft by remember { mutableStateOf<Int?>(null) }
    var ownerUid by remember { mutableStateOf("") }
    var isUpgrading by remember { mutableStateOf(false) }

    // 👇 NEW STRIPE CONNECT STATES 👇
    var stripeAccountId by remember { mutableStateOf<String?>(null) }
    val isStripeConnected = stripeAccountId != null
    var isConnectingStripe by remember { mutableStateOf(false) }

    // Listen for QuickBooks Connection
    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            val qboRef = firestore.collection("companies").document(companyName)
                .collection("integrations").document("quickbooks")
            val listener = qboRef.addSnapshotListener { snapshot, error ->
                if (error == null) { isQuickBooksConnected = snapshot != null && snapshot.exists() }
            }
            try {
                delay(Long.MAX_VALUE)
            } finally { listener.remove() }
        }
    }

    // 👇 NEW: Listen for Stripe Account Connection 👇
    DisposableEffect(companyName) {
        var listener: ListenerRegistration? = null

        if (companyName.isNotBlank()) {
            listener = firestore.collection("companies").document(companyName)
                .addSnapshotListener { snap, _ ->
                    if (snap != null && snap.exists()) {
                        val newId = snap.getString("stripeAccountId")
                        // Ensure an accidental empty string isn't treated as a connection
                        stripeAccountId = if (newId.isNullOrBlank()) null else newId
                    }
                }
        }

        onDispose {
            listener?.remove()
        }
    }

    // Load Company and Subscription Data
    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            try {
                val doc = firestore.collection("companies").document(companyName).get().await()
                companyLogoUrl = doc.getString("logoUrl")
                val status = doc.getString("subscriptionStatus") ?: "trial"
                ownerUid = doc.getString("ownerUid") ?: auth.currentUser?.uid ?: ""
                val endDate = doc.getTimestamp("trialEndDate")?.toDate()

                val listener = firestore.collection("user_profiles").document(ownerUid)
                    .collection("subscriptions")
                    .whereIn("status", listOf("trialing", "active"))
                    .limit(1)
                    .addSnapshotListener { subSnap, error ->
                        if (error != null) {
                            Log.e("Settings", "Subscription listen failed.", error)
                            return@addSnapshotListener
                        }

                        if (subSnap != null && !subSnap.isEmpty) {
                            val activeSubs = subSnap.documents.sortedByDescending {
                                it.getTimestamp("created")?.toDate()?.time ?: 0L
                            }
                            val sub = activeSubs.first()
                            val items = sub.get("items") as? List<Map<String, Any>>
                            val priceMap = items?.firstOrNull()?.get("price") as? Map<String, Any>
                            val stripePriceId = priceMap?.get("id") as? String

                            val planName = when (stripePriceId) {
                                "price_1TIDuBHefrvyxRLzOSKSCvwq" -> "Starter"
                                "price_1TJOp0HefrvyxRLzik8egYWS" -> "Pro"
                                "price_1TJOriHefrvyxRLzOdOmySGr" -> "Unlimited"
                                else -> "Premium"
                            }

                            subStatus = "Active ($planName)"
                            trialDaysLeft = null
                        } else {
                            if (status == "trial") {
                                if (endDate != null) {
                                    val diff = endDate.time - System.currentTimeMillis()
                                    val days = TimeUnit.MILLISECONDS.toDays(diff).toInt()
                                    trialDaysLeft = maxOf(0, days)
                                    subStatus = if (days > 0) "Free Trial" else "Trial Expired"
                                } else {
                                    subStatus = "Free Trial"
                                    trialDaysLeft = 14
                                }
                            } else {
                                subStatus = status.replaceFirstChar { it.uppercase() }
                            }
                        }
                    }

                try {
                    awaitCancellation()
                } finally {
                    listener.remove()
                }

            } catch (e: Exception) {
                Log.e("Settings", "Error loading settings", e)
            }
        }
    }

    var isUploadingLogo by remember { mutableStateOf(false) }
    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                isUploadingLogo = true
                coroutineScope.launch {
                    try {
                        val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                        val storageRef = Firebase.storage.reference.child("companies/$safeCompany/logo.jpg")
                        storageRef.putFile(it).await()
                        val downloadUrl = storageRef.downloadUrl.await().toString()

                        firestore.collection("companies").document(companyName)
                            .update("logoUrl", downloadUrl).await()
                        companyLogoUrl = downloadUrl
                        Toast.makeText(context, "Company logo updated!", Toast.LENGTH_SHORT).show()
                    } catch(e: Exception) {
                        Toast.makeText(context, "Failed to upload logo: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isUploadingLogo = false
                    }
                }
            }
        }
    )

    var isImportingCustomers by remember { mutableStateOf(false) }
    var customerImportResult by remember { mutableStateOf<Pair<Int, String?>?>(null) }
    val customerFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                isImportingCustomers = true
                coroutineScope.launch {
                    val mimeType = context.contentResolver.getType(it)
                    val result = if (mimeType == "text/csv" || mimeType == "text/comma-separated-values") {
                        parseCsvAndImportCustomers(context, it, firestore, companyName)
                    } else { Pair(0, "Unsupported file type. Please select a .csv file.") }
                    customerImportResult = result
                    isImportingCustomers = false
                }
            }
        }
    )

    var isImportingProducts by remember { mutableStateOf(false) }
    var productImportResult by remember { mutableStateOf<Pair<Int, String?>?>(null) }
    val productFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                isImportingProducts = true
                coroutineScope.launch {
                    val mimeType = context.contentResolver.getType(it)
                    val result = if (mimeType == "text/csv" || mimeType == "text/comma-separated-values") {
                        parseCsvAndImportProducts(context, it, firestore, companyName)
                    } else { Pair(0, "Unsupported file type. Please select a .csv file.") }
                    productImportResult = result
                    isImportingProducts = false
                }
            }
        }
    )

    // 👇 NEW STRIPE CONNECT HANDLER 👇
    val handleStripeConnectToggle = {
        if (isStripeConnected) {
            AlertDialog.Builder(context)
                .setTitle("Disconnect Stripe?")
                .setMessage("You will no longer be able to accept credit cards on invoices.")
                .setPositiveButton("Disconnect") { _, _ ->
                    firestore.collection("companies").document(companyName)
                        .update("stripeAccountId", null)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Stripe disconnected.", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            isConnectingStripe = true
            coroutineScope.launch {
                try {
                    val user = auth.currentUser
                    val tokenResult = user?.getIdToken(true)?.await()
                    val token = tokenResult?.token

                    val data = hashMapOf(
                        "companyId" to companyName,
                        "authToken" to token
                    )

                    // Call the exact same Cloud Function!
                    val result = functions.getHttpsCallable("createStripeConnectAccount")
                        .call(data)
                        .await()

                    val resultMap = result.data as? Map<*, *>
                    val error = resultMap?.get("error") as? String
                    val url = resultMap?.get("url") as? String

                    if (error != null) {
                        Toast.makeText(context, "Stripe Error: $error", Toast.LENGTH_LONG).show()
                    } else if (url != null) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to connect: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                } finally {
                    isConnectingStripe = false
                }
            }
        }
    }

    // Dialogs
    if (showCompanySettingsDialog) CompanySettingsDialog(onDismiss = { showCompanySettingsDialog = false }, firestore = firestore, companyName = companyName)
    if (showUserManagementDialog) UserManagementDialog(onDismiss = { showUserManagementDialog = false }, firestore = firestore, companyName = companyName)
    if (showAddUserDialog) AddUserDialog(
        onDismiss = { showAddUserDialog = false },
        onUserAdded = { showAddUserDialog = false },
        auth = auth,
        firestore = firestore,
        companyName = companyName,
        onAddingUserChange = onAddingUserChange,
        ownerUid = ownerUid
    )
    if (showChangePasswordDialog) ChangePasswordDialog(onDismiss = { showChangePasswordDialog = false }, onPasswordChanged = { showChangePasswordDialog = false }, auth = auth, coroutineScope = coroutineScope)

    if (isImportingCustomers) Dialog(onDismissRequest = {}) { Card { Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator(); Spacer(Modifier.height(16.dp)); Text("Importing CSV customers...") } } }
    if (isImportingProducts) Dialog(onDismissRequest = {}) { Card { Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator(); Spacer(Modifier.height(16.dp)); Text("Importing CSV products...") } } }

    if (showDisconnectQboDialog) {
        ConfirmationDialog(
            title = "Disconnect QuickBooks?",
            message = "Are you sure you want to disconnect QuickBooks? Invoices will no longer sync automatically.",
            onConfirm = {
                firestore.collection("companies").document(companyName).collection("integrations").document("quickbooks").delete()
                    .addOnSuccessListener { Toast.makeText(context, "QuickBooks Disconnected", Toast.LENGTH_SHORT).show() }
                showDisconnectQboDialog = false
            },
            onDismiss = { showDisconnectQboDialog = false }
        )
    }

    customerImportResult?.let { (count, error) ->
        AlertDialog(onDismissRequest = { customerImportResult = null }, title = { Text(if (error == null) "Import Successful" else "Import Failed") }, text = { Text(if (error == null) "$count customers imported." else "Error: $error") }, confirmButton = { TextButton(onClick = { customerImportResult = null }) { Text("OK") } })
    }
    productImportResult?.let { (count, error) ->
        AlertDialog(onDismissRequest = { productImportResult = null }, title = { Text(if (error == null) "Import Successful" else "Import Failed") }, text = { Text(if (error == null) "$count products imported." else "Error: $error") }, confirmButton = { TextButton(onClick = { productImportResult = null }) { Text("OK") } })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToMenu) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (userRole == "Manager") {

            Text(
                "COMPANY PROFILE",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                logoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isUploadingLogo) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else if (companyLogoUrl != null) {
                                AsyncImage(
                                    model = companyLogoUrl,
                                    contentDescription = "Logo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = "Add Logo",
                                    tint = Color.Gray
                                )
                            }
                        }
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                "Company Logo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Tap to upload or change logo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    SettingsActionRow(
                        title = "Company Details",
                        subtitle = "Edit business name, address, and default tax",
                        icon = Icons.Default.Edit,
                        onClick = { showCompanySettingsDialog = true }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "INTEGRATIONS",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isQuickBooksConnected) {
                                    showDisconnectQboDialog = true
                                } else {
                                    val url =
                                        "https://us-central1-studio-5370490774-c0517.cloudfunctions.net/qboLogin?companyId=${companyName}"
                                    val intent = CustomTabsIntent.Builder().build()
                                    intent.launchUrl(context, Uri.parse(url))
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isQuickBooksConnected) Color(0xFFE8F5E9) else Color(
                                        0xFFE3F2FD
                                    )
                                ), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isQuickBooksConnected) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = null,
                                tint = if (isQuickBooksConnected) Color(0xFF2CA01C) else Color(
                                    0xFF1976D2
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        ) {
                            Text(
                                "QuickBooks Online",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isQuickBooksConnected) Color(0xFF2CA01C) else Color.Black
                            )
                            Text(
                                if (isQuickBooksConnected) "Connected. Tap to disconnect." else "Tap to connect your account",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    if (isQuickBooksConnected) {
                        HorizontalDivider(color = Color(0xFFF0F0F0))

                        if (isImportingQboCustomers) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(16.dp))
                                Text("Importing Customers from QBO...", color = Color.Gray)
                            }
                        } else {
                            SettingsActionRow(
                                title = "Import QBO Customers",
                                subtitle = "Pull your QuickBooks customer list into CrewQ",
                                icon = Icons.Default.PersonAdd,
                                onClick = {
                                    isImportingQboCustomers = true
                                    val currentUser = FirebaseAuth.getInstance().currentUser
                                    if (currentUser != null) {
                                        val freshFunctions =
                                            FirebaseFunctions.getInstance()
                                        currentUser.getIdToken(true)
                                            .addOnCompleteListener { tokenTask ->
                                                if (tokenTask.isSuccessful) {
                                                    val data = hashMapOf(
                                                        "companyId" to companyName,
                                                        "authToken" to tokenTask.result?.token
                                                    )
                                                    freshFunctions.getHttpsCallable("importCustomersFromQBO")
                                                        .call(data)
                                                        .addOnSuccessListener { result ->
                                                            isImportingQboCustomers = false
                                                            val responseMap =
                                                                result.data as? Map<*, *>
                                                            val added =
                                                                responseMap?.get("added") ?: 0
                                                            val updated =
                                                                responseMap?.get("updated") ?: 0
                                                            Toast.makeText(
                                                                context,
                                                                "Success! Added $added, Updated $updated.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            isImportingQboCustomers = false
                                                            Toast.makeText(
                                                                context,
                                                                "Import Failed: ${e.message}",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                } else {
                                                    isImportingQboCustomers = false
                                                    Toast.makeText(
                                                        context,
                                                        "Auth error.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    }
                                }
                            )
                        }

                        HorizontalDivider(color = Color(0xFFF0F0F0))

                        if (isImportingQboProducts) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(16.dp))
                                Text("Importing Products from QBO...", color = Color.Gray)
                            }
                        } else {
                            SettingsActionRow(
                                title = "Import QBO Products & Services",
                                subtitle = "Pull your QuickBooks item list into CrewQ",
                                icon = Icons.Default.CloudDownload,
                                onClick = {
                                    isImportingQboProducts = true
                                    val currentUser = FirebaseAuth.getInstance().currentUser
                                    if (currentUser != null) {
                                        val freshFunctions =
                                            FirebaseFunctions.getInstance()
                                        currentUser.getIdToken(true)
                                            .addOnCompleteListener { tokenTask ->
                                                if (tokenTask.isSuccessful) {
                                                    val data = hashMapOf(
                                                        "companyId" to companyName,
                                                        "authToken" to tokenTask.result?.token
                                                    )
                                                    freshFunctions.getHttpsCallable("importItemsFromQBO")
                                                        .call(data)
                                                        .addOnSuccessListener { result ->
                                                            isImportingQboProducts = false
                                                            val responseMap =
                                                                result.data as? Map<*, *>
                                                            val added =
                                                                responseMap?.get("added") ?: 0
                                                            val updated =
                                                                responseMap?.get("updated") ?: 0
                                                            Toast.makeText(
                                                                context,
                                                                "Success! Added $added, Updated $updated.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            isImportingQboProducts = false
                                                            Toast.makeText(
                                                                context,
                                                                "Import Failed: ${e.message}",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                }
                                            }
                                    }
                                }
                            )
                        }
                    }

                    // 👇 NEW: STRIPE CONNECT INTEGRATION ROW 👇
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    SettingsActionRow(
                        title = "Stripe Payments",
                        subtitle = if (isStripeConnected) "Bank connected. Ready for payouts." else "Connect your bank to accept credit cards.",
                        icon = if (isStripeConnected) Icons.Default.Check else Icons.Default.Add,
                        onClick = {
                            if (!isConnectingStripe) handleStripeConnectToggle()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "TEAM MANAGEMENT",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    SettingsActionRow(
                        title = "Manage Users",
                        subtitle = "Edit roles or remove employees",
                        icon = Icons.Default.PersonSearch,
                        onClick = { showUserManagementDialog = true })
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    SettingsActionRow(
                        title = "Add New User",
                        subtitle = "Create a new login for an employee",
                        icon = Icons.Default.PersonAdd,
                        onClick = { showAddUserDialog = true })
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    SettingsActionRow(
                        title = "Audit Log",
                        subtitle = "Track all employee actions and changes",
                        icon = Icons.Default.Search,
                        onClick = { onNavigateToAuditLog() }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "DATA IMPORT (CSV)",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    SettingsActionRow(
                        title = "Import Customers",
                        subtitle = "Upload a CSV file of clients",
                        icon = Icons.Default.CloudUpload,
                        onClick = { customerFilePickerLauncher.launch("text/csv") })
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    SettingsActionRow(
                        title = "Import Products",
                        subtitle = "Upload a CSV file of items",
                        icon = Icons.Default.CloudUpload,
                        onClick = { productFilePickerLauncher.launch("text/csv") })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text("SUBSCRIPTION", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Plan Status",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                            Text(
                                subStatus,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        val badgeColor =
                            if (subStatus.contains("Active")) Color(0xFFE8F5E9) else Color(
                                0xFFFFF3E0
                            )
                        val textColor =
                            if (subStatus.contains("Active")) Color(0xFF2E7D32) else Color(
                                0xFFE65100
                            )

                        Surface(color = badgeColor, shape = RoundedCornerShape(16.dp)) {
                            Text(
                                text = if (isTrial) "TRIAL" else "PRO",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black, color = textColor
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // THE DAYS REMAINING TEXT
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = daysLeft?.toString() ?: "0",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = " Days Remaining",
                            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray
                        )
                    }

                    // THE PROGRESS BAR
                    val progress = remember(daysLeft) {
                        val max = if (isTrial) 14f else 30f
                        ((daysLeft ?: 0) / max).coerceIn(0f, 1f)
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = if ((daysLeft ?: 0) < 5) Color.Red else Color(0xFF2CA01C),
                        trackColor = Color(0xFFF5F5F5)
                    )

                    // UPGRADE/MANAGE BILLING BUTTON
                    if (userRole == "Manager") {
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (subStatus.contains("Active")) {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://billing.stripe.com/p/login/test_5kQ9AT0ON1RsfRt7CKdUY00")
                                    )
                                    context.startActivity(intent)
                                } else {
                                    isUpgrading = true
                                    val sessionRef =
                                        firestore.collection("user_profiles").document(ownerUid)
                                            .collection("checkout_sessions").document()
                                    coroutineScope.launch {
                                        sessionRef.set(
                                            mapOf(
                                                "price" to "price_1TIDuBHefrvyxRLzOSKSCvwq",
                                                "success_url" to "https://crewq.com/success",
                                                "cancel_url" to "https://crewq.com/cancel",
                                                "customer" to ownerUid
                                            )
                                        ).await()
                                        sessionRef.addSnapshotListener { snap, _ ->
                                            val url = snap?.getString("url")
                                            if (url != null) {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(url)
                                                    )
                                                )
                                                isUpgrading = false
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (subStatus.contains(
                                        "Active"
                                    )
                                ) Color.DarkGray else Color(0xFF2CA01C)
                            )
                        ) {
                            if (isUpgrading) CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                            else Text(
                                if (subStatus.contains("Active")) "Manage Billing" else "Upgrade to Pro",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Text("MY ACCOUNT", style = MaterialTheme.typography.labelMedium, color = Color.Gray, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp))
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
                SettingsActionRow(title = "Change Password", subtitle = "Update your login password", icon = Icons.Default.Lock, onClick = { showChangePasswordDialog = true })
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Composable
fun SettingsActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Column(modifier = Modifier
            .padding(start = 16.dp)
            .weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}
            @Composable
            fun UserManagementDialog(
                onDismiss: () -> Unit,
                firestore: FirebaseFirestore,
                companyName: String
            ) {
                var userList by remember { mutableStateOf<List<CompanyUser>>(emptyList()) }
                var isLoading by remember { mutableStateOf(true) }
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                var selectedUser by remember { mutableStateOf<CompanyUser?>(null) }
                var showEditDialog by remember { mutableStateOf(false) }
                var showDeleteConfirmation by remember { mutableStateOf(false) }
                var refreshTrigger by remember { mutableIntStateOf(0) }

                LaunchedEffect(companyName, refreshTrigger) {
                    if (companyName.isNotBlank()) {
                        isLoading = true
                        firestore.collection("companies").document(companyName).collection("users")
                            .get()
                            .addOnSuccessListener { result ->
                                userList = result.documents.mapNotNull { doc ->
                                    CompanyUser(
                                        id = doc.id,
                                        firstName = doc.getString("firstName") ?: "",
                                        lastName = doc.getString("lastName") ?: "",
                                        email = doc.getString("email") ?: "",
                                        role = doc.getString("role") ?: "Technician",
                                        canCreateDeleteEstimates = doc.getBoolean("canCreateDeleteEstimates") ?: true,
                                        canCreateDeleteInvoices = doc.getBoolean("canCreateDeleteInvoices") ?: true,
                                        canAddDeleteJobs = doc.getBoolean("canAddDeleteJobs") ?: false,
                                        canAddDeleteItems = doc.getBoolean("canAddDeleteItems") ?: false,
                                        canCollectDispatchFee = doc.getBoolean("canCollectDispatchFee") ?: true,
                                        canEditTimeClock = doc.getBoolean("canEditTimeClock") ?: false
                                    )
                                }
                                isLoading = false
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to load users.", Toast.LENGTH_SHORT).show()
                                isLoading = false
                            }
                    } else {
                        isLoading = false
                    }
                }

                if (showEditDialog && selectedUser != null) {
                    EditUserDialog(
                        user = selectedUser!!,
                        onDismiss = { showEditDialog = false },
                        onUserSaved = {
                            showEditDialog = false
                            refreshTrigger++
                        },
                        firestore = firestore,
                        companyName = companyName
                    )
                }

                if (showDeleteConfirmation && selectedUser != null) {
                    ConfirmationDialog(
                        title = "Confirm Deletion",
                        message = "Are you sure you want to delete ${selectedUser!!.firstName} ${selectedUser!!.lastName}?\n\nThis will permanently delete the user's data and their login account.",
                        onConfirm = {
                            coroutineScope.launch {
                                try {
                                    val userToDeleteId = selectedUser!!.id

                                    // Step 1: Delete the Firestore document. This part is fine.
                                    firestore.collection("companies").document(companyName)
                                        .collection("users").document(userToDeleteId)
                                        .delete().await()

                                    // Step 2: TEMPORARILY DISABLED the Cloud Function call.
                                    // TODO: Re-enable the cloud function call once the build error is fixed.
                                    Log.d("DELETE_USER", "Cloud function call is disabled for debugging.")
                                    Toast.makeText(context, "User data deleted. Account deletion is disabled.", Toast.LENGTH_LONG).show()

                                    refreshTrigger++

                                } catch (e: Exception) {
                                    Log.e("FIRESTORE_DELETE", "Error during user deletion process", e)
                                    Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    showDeleteConfirmation = false
                                }
                            }
                        },
                        onDismiss = { showDeleteConfirmation = false }
                    )
                }

                Dialog(onDismissRequest = onDismiss) {
                    Card(
                        modifier = Modifier.padding(vertical = 32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(
                                text = "User Management",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                                    items(userList, key = { it.id }) { user ->
                                        UserManagementRow(
                                            user = user,
                                            onEditClick = {
                                                selectedUser = user
                                                showEditDialog = true
                                            },
                                            onDeleteClick = {
                                                selectedUser = user
                                                showDeleteConfirmation = true
                                            }
                                        )
                                        HorizontalDivider()
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = onDismiss) {
                                    Text("Close")
                                }
                            }
                        }
                    }
                }
            }
            @Composable
            fun UserManagementRow(
                user: CompanyUser,
                onEditClick: () -> Unit,
                onDeleteClick: () -> Unit
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${user.firstName} ${user.lastName}", // Construct full name here
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = user.role,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row {
                        IconButton(onClick = onEditClick) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit User")
                        }
                        IconButton(onClick = onDeleteClick) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete User", tint = Color.Red)
                        }
                    }
                }
            }
@Composable
fun EditUserDialog(
    user: CompanyUser,
    onDismiss: () -> Unit,
    onUserSaved: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var firstName by rememberSaveable { mutableStateOf(user.firstName) }
    var lastName by rememberSaveable { mutableStateOf(user.lastName) }
    val roles = listOf("Manager", "Technician")
    var selectedRole by rememberSaveable { mutableStateOf(user.role) }

    // 👇 6 PERMISSION STATES 👇
    var canCreateDeleteEstimates by rememberSaveable { mutableStateOf(user.canCreateDeleteEstimates) }
    var canCreateDeleteInvoices by rememberSaveable { mutableStateOf(user.canCreateDeleteInvoices) }
    var canAddDeleteJobs by rememberSaveable { mutableStateOf(user.canAddDeleteJobs) }
    var canAddDeleteItems by rememberSaveable { mutableStateOf(user.canAddDeleteItems) }
    var canCollectDispatchFee by rememberSaveable { mutableStateOf(user.canCollectDispatchFee) }
    var canEditTimeClock by rememberSaveable { mutableStateOf(user.canEditTimeClock) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text("Edit User", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
                Spacer(Modifier.height(8.dp))
                TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
                Spacer(Modifier.height(16.dp))

                Text("Role", fontWeight = FontWeight.Bold)
                Row {
                    roles.forEach { role ->
                        Row(Modifier.selectable(selected = (role == selectedRole), onClick = { selectedRole = role }).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = (role == selectedRole), onClick = { selectedRole = role })
                            Text(role)
                        }
                    }
                }

                // 👇 DYNAMIC PERMISSIONS MENU 👇
                if (selectedRole == "Technician") {
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(Modifier.height(16.dp))
                    Text("Technician Permissions", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canCreateDeleteEstimates, onCheckedChange = { canCreateDeleteEstimates = it })
                        Text("Create & Delete Estimates")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canCreateDeleteInvoices, onCheckedChange = { canCreateDeleteInvoices = it })
                        Text("Create & Delete Invoices")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canAddDeleteJobs, onCheckedChange = { canAddDeleteJobs = it })
                        Text("Create & Delete Jobs")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canAddDeleteItems, onCheckedChange = { canAddDeleteItems = it })
                        Text("Create & Delete Catalog Items")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canCollectDispatchFee, onCheckedChange = { canCollectDispatchFee = it })
                        Text("Collect Dispatch Signatures")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canEditTimeClock, onCheckedChange = { canEditTimeClock = it })
                        Text("Edit Time Clock Entries")
                    }
                }

                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            try {
                                val updatedData = mapOf(
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "role" to selectedRole,
                                    // Save the permissions! (Managers always get true)
                                    "canCreateDeleteEstimates" to if (selectedRole == "Manager") true else canCreateDeleteEstimates,
                                    "canCreateDeleteInvoices" to if (selectedRole == "Manager") true else canCreateDeleteInvoices,
                                    "canAddDeleteJobs" to if (selectedRole == "Manager") true else canAddDeleteJobs,
                                    "canAddDeleteItems" to if (selectedRole == "Manager") true else canAddDeleteItems,
                                    "canCollectDispatchFee" to if (selectedRole == "Manager") true else canCollectDispatchFee,
                                    "canEditTimeClock" to if (selectedRole == "Manager") true else canEditTimeClock
                                )
                                firestore.collection("companies").document(companyName)
                                    .collection("users").document(user.id)
                                    .update(updatedData).await()
                                Toast.makeText(context, "User updated!", Toast.LENGTH_SHORT).show()
                                onUserSaved()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
@Composable
fun AddUserDialog(
    onDismiss: () -> Unit,
    onUserAdded: () -> Unit,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    companyName: String,
    onAddingUserChange: (Boolean) -> Unit,
    ownerUid: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val roles = listOf("Manager", "Technician")
    var selectedRole by remember { mutableStateOf(roles[1]) } // Default to tech

    // 👇 6 PERMISSION STATES (With safe defaults) 👇
    var canCreateDeleteEstimates by remember { mutableStateOf(true) }
    var canCreateDeleteInvoices by remember { mutableStateOf(true) }
    var canAddDeleteJobs by remember { mutableStateOf(false) }
    var canAddDeleteItems by remember { mutableStateOf(false) }
    var canCollectDispatchFee by remember { mutableStateOf(true) }
    var canEditTimeClock by remember { mutableStateOf(false) }

    var isSaving by remember { mutableStateOf(false) }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Add New User", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, colors = textFieldColors, enabled = !isSaving, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, colors = textFieldColors, enabled = !isSaving, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, colors = textFieldColors, enabled = !isSaving, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), colors = textFieldColors, enabled = !isSaving, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Role", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Row(modifier = Modifier.fillMaxWidth()) {
                    roles.forEach { role ->
                        Row(Modifier.selectable(selected = (role == selectedRole), onClick = { if (!isSaving) selectedRole = role }).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = (role == selectedRole), onClick = { if (!isSaving) selectedRole = role }, enabled = !isSaving)
                            Text(text = role)
                        }
                    }
                }

                // 👇 DYNAMIC PERMISSIONS MENU 👇
                if (selectedRole == "Technician") {
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(Modifier.height(16.dp))
                    Text("Technician Permissions", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.Start))
                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canCreateDeleteEstimates, onCheckedChange = { canCreateDeleteEstimates = it }, enabled = !isSaving)
                        Text("Create & Delete Estimates")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canCreateDeleteInvoices, onCheckedChange = { canCreateDeleteInvoices = it }, enabled = !isSaving)
                        Text("Create & Delete Invoices")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canAddDeleteJobs, onCheckedChange = { canAddDeleteJobs = it }, enabled = !isSaving)
                        Text("Create & Delete Jobs")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canAddDeleteItems, onCheckedChange = { canAddDeleteItems = it }, enabled = !isSaving)
                        Text("Create & Delete Catalog Items")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canCollectDispatchFee, onCheckedChange = { canCollectDispatchFee = it }, enabled = !isSaving)
                        Text("Collect Dispatch Signatures")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = canEditTimeClock, onCheckedChange = { canEditTimeClock = it }, enabled = !isSaving)
                        Text("Edit Time Clock Entries")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss, enabled = !isSaving) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        enabled = !isSaving,
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()) {
                                isSaving = true
                                coroutineScope.launch {
                                    var userCreatedSuccessfully = false

                                    if (companyName.isBlank()) {
                                        Toast.makeText(context, "Error: Company name is not set.", Toast.LENGTH_LONG).show()
                                        isSaving = false
                                        return@launch
                                    }

                                    try {
                                        // A. Count current users
                                        val usersSnap = firestore.collection("companies").document(companyName).collection("users").get().await()
                                        val currentUserCount = usersSnap.size()

                                        // B. Find out what subscription plan they have
                                        val subSnap = firestore.collection("user_profiles").document(ownerUid).collection("subscriptions").whereIn("status", listOf("trialing", "active")).limit(1).get().await()

                                        var maxUsersAllowed = 3 // Default
                                        if (subSnap != null && !subSnap.isEmpty) {
                                            val activeSubs = subSnap.documents.sortedByDescending { it.getTimestamp("created")?.toDate()?.time ?: 0L }
                                            val sub = activeSubs.first()
                                            val items = sub.get("items") as? List<Map<String, Any>>
                                            val priceMap = items?.firstOrNull()?.get("price") as? Map<String, Any>
                                            val stripePriceId = priceMap?.get("id") as? String

                                            maxUsersAllowed = when (stripePriceId) {
                                                "price_1TIDuBHefrvyxRLzOSKSCvwq" -> 3
                                                "price_1TJOp0HefrvyxRLzik8egYWS" -> 10
                                                "price_1TJOriHefrvyxRLzOdOmySGr" -> 50
                                                else -> 3
                                            }
                                        }

                                        if (currentUserCount >= maxUsersAllowed) {
                                            Toast.makeText(context, "User limit reached ($currentUserCount/$maxUsersAllowed). Please upgrade your plan.", Toast.LENGTH_LONG).show()
                                            isSaving = false
                                            return@launch
                                        }

                                        onAddingUserChange(true)
                                        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                                        val newUser = authResult.user

                                        if (newUser != null) {
                                            val batch = firestore.batch()
                                            val userRef = firestore.collection("companies").document(companyName).collection("users").document(newUser.uid)

                                            // 👇 SAVE THE 6 PERMISSIONS 👇
                                            val userData = hashMapOf(
                                                "uid" to newUser.uid,
                                                "firstName" to firstName,
                                                "lastName" to lastName,
                                                "email" to email,
                                                "role" to selectedRole,
                                                "companyName" to companyName,
                                                "canCreateDeleteEstimates" to if (selectedRole == "Manager") true else canCreateDeleteEstimates,
                                                "canCreateDeleteInvoices" to if (selectedRole == "Manager") true else canCreateDeleteInvoices,
                                                "canAddDeleteJobs" to if (selectedRole == "Manager") true else canAddDeleteJobs,
                                                "canAddDeleteItems" to if (selectedRole == "Manager") true else canAddDeleteItems,
                                                "canCollectDispatchFee" to if (selectedRole == "Manager") true else canCollectDispatchFee,
                                                "canEditTimeClock" to if (selectedRole == "Manager") true else canEditTimeClock
                                            )
                                            batch.set(userRef, userData)

                                            val profileRef = firestore.collection("user_profiles").document(newUser.uid)
                                            val profileData = hashMapOf("companyName" to companyName)
                                            batch.set(profileRef, profileData)

                                            batch.commit().await()
                                            userCreatedSuccessfully = true
                                            Toast.makeText(context, "User added successfully!", Toast.LENGTH_SHORT).show()
                                            onUserAdded()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to create user: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        if (userCreatedSuccessfully) auth.signOut()
                                        onAddingUserChange(false)
                                        isSaving = false
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        if (isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        else Text("Save")
                    }
                }
            }
        }
    }
}
            @Composable
            fun ConfirmationDialog(
                title: String,
                message: String,
                onConfirm: () -> Unit,
                onDismiss: () -> Unit
            ) {
                Dialog(onDismissRequest = onDismiss) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(onClick = onDismiss, colors = ButtonDefaults.outlinedButtonColors()) {
                                    Text("Cancel")
                                }
                                Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
            @Composable
            fun TimeEntryEditDialog(
                timeEntry: TimeEntry,
                onDismiss: () -> Unit,
                onSave: (TimeEntry) -> Unit
            ) {
                val context = LocalContext.current
                val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())

                var clockInStr by remember { mutableStateOf(timeEntry.clockIn?.toDate()?.let { dateTimeFormat.format(it) } ?: "") }
                var clockOutStr by remember { mutableStateOf(timeEntry.clockOut?.toDate()?.let { dateTimeFormat.format(it) } ?: "") }

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Edit Time Entry", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            TextField(
                                value = clockInStr,
                                onValueChange = { clockInStr = it },
                                label = { Text("Clock In") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = clockOutStr,
                                onValueChange = { clockOutStr = it },
                                label = { Text("Clock Out") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            Row {
                                Button(onClick = onDismiss) {
                                    Text("Cancel")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    try {
                                        val newClockIn = dateTimeFormat.parse(clockInStr)?.let { Timestamp(it) }
                                        val newClockOut = clockOutStr.takeIf { it.isNotBlank() }?.let { dateString -> dateTimeFormat.parse(dateString)?.let { Timestamp(it) } }
                                        onSave(timeEntry.copy(clockIn = newClockIn, clockOut = newClockOut))
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error parsing date in TimeEntryEditDialog", e)
                                        Toast.makeText(context, "Invalid date format: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            }

            @Composable
            fun TeamTimeEntryEditDialog(
                timeEntry: TeamTimeEntry,
                onDismiss: () -> Unit,
                onSave: (TeamTimeEntry) -> Unit
            ) {
                val context = LocalContext.current
                val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())

                var clockInStr by remember { mutableStateOf(timeEntry.clockIn?.toDate()?.let { dateTimeFormat.format(it) } ?: "") }
                var clockOutStr by remember { mutableStateOf(timeEntry.clockOut?.toDate()?.let { dateTimeFormat.format(it) } ?: "") }

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Edit Team Time Entry", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            TextField(
                                value = clockInStr,
                                onValueChange = { clockInStr = it },
                                label = { Text("Clock In") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = clockOutStr,
                                onValueChange = { clockOutStr = it },
                                label = { Text("Clock Out") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                )
                            )
                            Spacer(Modifier.height(16.dp))
                            Row {
                                Button(onClick = onDismiss) {
                                    Text("Cancel")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    try {
                                        val newClockIn = dateTimeFormat.parse(clockInStr)?.let { Timestamp(it) }
                                        val newClockOut = clockOutStr.takeIf { it.isNotBlank() }?.let { dateString -> dateTimeFormat.parse(dateString)?.let { Timestamp(it) } }
                                        onSave(timeEntry.copy(clockIn = newClockIn, clockOut = newClockOut))
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error parsing date in TeamTimeEntryEditDialog", e)
                                        Toast.makeText(context, "Invalid date format: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            }


            @Composable
            fun ChangePasswordDialog(
                onDismiss: () -> Unit,
                onPasswordChanged: () -> Unit,
                auth: FirebaseAuth,
                coroutineScope: CoroutineScope) {
                val context = LocalContext.current
                var newPassword by remember { mutableStateOf("") }
                var confirmPassword by remember { mutableStateOf("") }

                val isSaveEnabled = remember(newPassword, confirmPassword) {
                    newPassword.isNotBlank() && newPassword == confirmPassword
                }

                val textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Change Password", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = newPassword,
                                onValueChange = { newPassword = it },
                                label = { Text("New Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                colors = textFieldColors,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = { Text("Confirm New Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                colors = textFieldColors,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                Button(onClick = onDismiss) {
                                    Text("Cancel")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (newPassword.isNotBlank() && newPassword == confirmPassword) {
                                            val user = auth.currentUser
                                            if (user != null) {
                                                coroutineScope.launch {
                                                    try {
                                                        user.updatePassword(newPassword).await()
                                                        Toast.makeText(
                                                            context,
                                                            "Password updated successfully!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        onPasswordChanged()
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            context,
                                                            "Error updating password: ${e.message}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "User not logged in.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Please ensure passwords match and are not blank.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    enabled = isSaveEnabled
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            }

            @Preview(showBackground = true)
            @Composable
            fun SignInPreview() {
                JobTrackerTheme {
                    SignInScreen()
                }
            }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UpcomingJobsScreen(
    modifier: Modifier = Modifier,
    onJobClick: (FirestoreTask) -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String
) {
    var jobs by remember { mutableStateOf<List<FirestoreTask>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(companyName, currentUserId) {
        if (companyName.isNotBlank()) {
            // 1. Get the start of today
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfToday = Timestamp(calendar.time)

            // 2. Fetch future jobs
            firestore.collection("companies").document(companyName).collection("tasks")
                .whereGreaterThanOrEqualTo("startDate", startOfToday)
                .whereArrayContains("assignedUserIds", currentUserId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Toast.makeText(context, "Error loading jobs", Toast.LENGTH_SHORT).show()
                        isLoading = false
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        // 3. Filter and Sort
                        jobs = snapshot.documents.mapNotNull { it.toObject(FirestoreTask::class.java)?.copy(id = it.id) }
                            .filter { it.status != "Completed" && it.status != "Canceled" && it.status != "In Progress" }
                            .sortedBy { it.startDate?.toDate()?.time ?: Long.MAX_VALUE }
                    }
                    isLoading = false
                }
        }
    }

    // --- NEW: Group the jobs by Date ---
    val groupedJobs = remember(jobs) {
        val dateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())
        jobs.groupBy { job ->
            job.startDate?.let { dateFormat.format(it.toDate()) } ?: "Unscheduled / No Date"
        }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (jobs.isEmpty()) {
            Text("No upcoming jobs scheduled.", color = Color.Gray, modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // --- NEW: Iterate through the groups and add Sticky Headers ---
                groupedJobs.forEach { (dateString, jobsForDate) ->

                    // The Sticky Header (The Date)
                    stickyHeader {
                        Text(
                            text = dateString,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background) // Prevents overlapping text when scrolling
                                .padding(vertical = 8.dp)
                        )
                    }

                    // The Jobs for that specific Date
                    items(jobsForDate, key = { it.id }) { job ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onJobClick(job) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (job.customerName.isNotBlank()) job.customerName else "Unknown Customer",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                val safeDescription = job.jobDescription ?: ""
                                if (safeDescription.isNotBlank()) {
                                    Text(safeDescription, color = Color.DarkGray)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Show the specific time window since the Date is already in the header
                                Text("Time: ${job.timeWindow.ifBlank { "TBD" }}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InProgressJobsScreen(
    modifier: Modifier = Modifier,
    onJobClick: (FirestoreTask) -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String
) {
    var jobs by remember { mutableStateOf<List<FirestoreTask>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(companyName, currentUserId) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).collection("tasks")
                .whereEqualTo("status", "In Progress") // Only get active jobs
                .whereArrayContains("assignedUserIds", currentUserId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Toast.makeText(context, "Error loading jobs", Toast.LENGTH_SHORT).show()
                        isLoading = false
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        jobs = snapshot.documents.mapNotNull { it.toObject(FirestoreTask::class.java)?.copy(id = it.id) }
                            .sortedByDescending { it.startDate?.toDate()?.time ?: 0L }
                    }
                    isLoading = false
                }
        }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (jobs.isEmpty()) {
            Text("No jobs currently in progress.", color = Color.Gray, modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(jobs, key = { it.id }) { job ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onJobClick(job) }, // <-- Safer click handling
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // Light blue to show it's active
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (job.customerName.isNotBlank()) job.customerName else "Unknown Customer",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )

                            // 2. Bulletproof the job description (Firebase sometimes sends null instead of "")
                            val safeDescription = job.jobDescription ?: ""
                            if (safeDescription.isNotBlank()) {
                                Text(safeDescription, color = Color.DarkGray)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
                            val dateStr = job.startDate?.let { dateFormat.format(it.toDate()) } ?: "No Date"
                            Text("Started: $dateStr", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountingScreen(
    onBackToMenu: () -> Unit,
    onEstimateClick: (String) -> Unit,
    onInvoiceClick: (String) -> Unit,
    firestore: FirebaseFirestore,
    functions: FirebaseFunctions,
    companyName: String,
    userRole: String
) {
    var unsyncedEstimates by remember { mutableStateOf<List<Estimate>>(emptyList()) }
    var unsyncedInvoices by remember { mutableStateOf<List<Invoice>>(emptyList()) }
    var unsyncedProducts by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }

    var isLoading by remember { mutableStateOf(true) }
    var isSyncingEstimates by remember { mutableStateOf(false) }
    var isSyncingInvoices by remember { mutableStateOf(false) }
    var isSyncingProducts by remember { mutableStateOf(false) }

    var refreshTrigger by remember { mutableStateOf(0) }
    val context = LocalContext.current

    // --- TAB STATE ---
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Invoices", "Estimates", "Products")

    // --- FETCH DATA ---
    LaunchedEffect(companyName, refreshTrigger) {
        if (companyName.isNotBlank()) {
            isLoading = true
            try {
                val estSnap = firestore.collection("companies").document(companyName).collection("estimates").get().await()
                unsyncedEstimates = estSnap.documents.mapNotNull { it.toObject(Estimate::class.java)?.copy(id = it.id) }
                    .filter { it.qboId == null || it.needsSync }
                    .sortedByDescending { it.creationDate.toDate().time }

                val invSnap = firestore.collection("companies").document(companyName).collection("invoices").get().await()
                unsyncedInvoices = invSnap.documents.mapNotNull { it.toObject(Invoice::class.java)?.copy(id = it.id) }
                    .filter { it.qboId == null || it.needsSync }
                    .sortedByDescending { it.creationDate.toDate().time }

                val prodSnap = firestore.collection("companies").document(companyName).collection("productsAndServices").get().await()
                unsyncedProducts = prodSnap.documents.mapNotNull { it.toObject(ProductOrService::class.java)?.copy(id = it.id) }
                    .filter { it.qboId == null || it.needsSync }
                    .sortedBy { it.name }

            } catch (e: Exception) {
                Log.e("AccountingScreen", "Error loading items", e)
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Accounting Sync", fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                // --- TABS ---
                TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.Transparent) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                val count = when(index) {
                                    0 -> unsyncedInvoices.size
                                    1 -> unsyncedEstimates.size
                                    else -> unsyncedProducts.size
                                }
                                Text("$title ($count)", fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // --- TAB CONTENT ---
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTabIndex) {
                        0 -> { // INVOICES TAB
                            Column(modifier = Modifier.fillMaxSize()) {
                                if (unsyncedInvoices.isNotEmpty()) {
                                    Button(
                                        onClick = {
                                            isSyncingInvoices = true
                                            val currentUser = FirebaseAuth.getInstance().currentUser
                                            if (currentUser != null) {
                                                val freshFunctions = FirebaseFunctions.getInstance()
                                                currentUser.getIdToken(true).addOnCompleteListener { tokenTask ->
                                                    if (tokenTask.isSuccessful) {
                                                        val data = hashMapOf("companyId" to companyName, "authToken" to tokenTask.result?.token)
                                                        freshFunctions.getHttpsCallable("batchSyncInvoicesToQBO").call(data)
                                                            .addOnSuccessListener {
                                                                isSyncingInvoices = false
                                                                Toast.makeText(context, "Invoices Synced!", Toast.LENGTH_SHORT).show()
                                                                refreshTrigger++
                                                            }
                                                            .addOnFailureListener { e ->
                                                                isSyncingInvoices = false
                                                                Toast.makeText(context, "Sync Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                                            }
                                                    }
                                                }
                                            }
                                        },
                                        enabled = !isSyncingInvoices,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                                    ) {
                                        if (isSyncingInvoices) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                                        else Text("Sync ${unsyncedInvoices.size} Invoices to QBO")
                                    }
                                    Spacer(Modifier.height(16.dp))

                                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        items(unsyncedInvoices, key = { it.id }) { invoice ->
                                            SummaryInvoiceCard(invoice = invoice, onClick = { onInvoiceClick(invoice.id) })
                                        }
                                    }
                                } else {
                                    Text("All Invoices are synced!", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                                }
                            }
                        }

                        1 -> { // ESTIMATES TAB
                            Column(modifier = Modifier.fillMaxSize()) {
                                if (unsyncedEstimates.isNotEmpty()) {
                                    Button(
                                        onClick = {
                                            isSyncingEstimates = true
                                            val currentUser = FirebaseAuth.getInstance().currentUser
                                            if (currentUser != null) {
                                                val freshFunctions = FirebaseFunctions.getInstance()
                                                currentUser.getIdToken(true).addOnCompleteListener { tokenTask ->
                                                    if (tokenTask.isSuccessful) {
                                                        val data = hashMapOf("companyId" to companyName, "authToken" to tokenTask.result?.token)
                                                        freshFunctions.getHttpsCallable("batchSyncEstimatesToQBO").call(data)
                                                            .addOnSuccessListener {
                                                                isSyncingEstimates = false
                                                                Toast.makeText(context, "Estimates Synced!", Toast.LENGTH_SHORT).show()
                                                                refreshTrigger++
                                                            }
                                                            .addOnFailureListener { e ->
                                                                isSyncingEstimates = false
                                                                Toast.makeText(context, "Sync Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                                            }
                                                    }
                                                }
                                            }
                                        },
                                        enabled = !isSyncingEstimates,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                                    ) {
                                        if (isSyncingEstimates) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                                        else Text("Sync ${unsyncedEstimates.size} Estimates to QBO")
                                    }
                                    Spacer(Modifier.height(16.dp))

                                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        items(unsyncedEstimates, key = { it.id }) { estimate ->
                                            SummaryEstimateCard(
                                                estimate = estimate,
                                                onClick = { onEstimateClick(estimate.id) },
                                                onDuplicateClick = { } // Accounting screen doesn't need duplicate function
                                            )
                                        }
                                    }
                                } else {
                                    Text("All Estimates are synced!", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                                }
                            }
                        }

                        2 -> { // PRODUCTS TAB
                            Column(modifier = Modifier.fillMaxSize()) {
                                if (unsyncedProducts.isNotEmpty()) {
                                    Button(
                                        onClick = {
                                            isSyncingProducts = true
                                            val currentUser = FirebaseAuth.getInstance().currentUser
                                            if (currentUser != null) {
                                                val freshFunctions = FirebaseFunctions.getInstance()
                                                currentUser.getIdToken(true).addOnCompleteListener { tokenTask ->
                                                    if (tokenTask.isSuccessful) {
                                                        val data = hashMapOf("companyId" to companyName, "authToken" to tokenTask.result?.token)
                                                        freshFunctions.getHttpsCallable("batchSyncItemsToQBO").call(data)
                                                            .addOnSuccessListener {
                                                                isSyncingProducts = false
                                                                Toast.makeText(context, "Products Synced!", Toast.LENGTH_SHORT).show()
                                                                refreshTrigger++
                                                            }
                                                            .addOnFailureListener { e ->
                                                                isSyncingProducts = false
                                                                Toast.makeText(context, "Sync Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                                            }
                                                    }
                                                }
                                            }
                                        },
                                        enabled = !isSyncingProducts,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                                    ) {
                                        if (isSyncingProducts) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                                        else Text("Sync ${unsyncedProducts.size} Products to QBO")
                                    }
                                    Spacer(Modifier.height(16.dp))

                                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        items(unsyncedProducts, key = { it.id }) { item ->
                                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                    Text(item.name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                                    Text("$%.2f".format(item.price))
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Text("All Products are synced!", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(onClick = onBackToMenu) {
                    Text("Back")
                }
            }
        }
    }
}

@Composable fun SignatureDialog(
    title: String = "Customer Signature",
    subtitle: String = "Please sign below to approve.",
    onDismiss: () -> Unit,
    onSave: (Bitmap) -> Unit )
{
    var strokes by remember { mutableStateOf(listOf<List<Offset>>()) }
    var currentStroke by remember { mutableStateOf(listOf<Offset>()) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(subtitle, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))

                // THE DRAWING PAD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .background(Color(0xFFF9F9F9))
                        .onSizeChanged { size -> canvasSize = size }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset -> currentStroke = listOf(offset) },
                                onDrag = { change, _ ->
                                    currentStroke = currentStroke + change.position
                                },
                                onDragEnd = {
                                    strokes = strokes + listOf(currentStroke)
                                    currentStroke = emptyList()
                                }
                            )
                        }
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        // Draw all finished strokes
                        strokes.forEach { stroke ->
                            if (stroke.isNotEmpty()) {
                                val path = Path().apply {
                                    moveTo(stroke.first().x, stroke.first().y)
                                    for (i in 1 until stroke.size) lineTo(stroke[i].x, stroke[i].y)
                                }
                                drawPath(
                                    path = path,
                                    color = Color.Black,
                                    style = Stroke(
                                        width = 6f,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }
                        // Draw the stroke currently being drawn
                        if (currentStroke.isNotEmpty()) {
                            val path = Path().apply {
                                moveTo(currentStroke.first().x, currentStroke.first().y)
                                for (i in 1 until currentStroke.size) lineTo(currentStroke[i].x, currentStroke[i].y)
                            }
                            drawPath(
                                path = path,
                                color = Color.Black,
                                style = Stroke(
                                    width = 6f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { strokes = emptyList(); currentStroke = emptyList() }) {
                        Text("Clear", color = Color.Red)
                    }
                    Row {
                        TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C)),
                            onClick = {
                                // Check if they actually drew something
                                if (strokes.isNotEmpty() && canvasSize.width > 0) {
                                    val bmp = Bitmap.createBitmap(canvasSize.width, canvasSize.height, Bitmap.Config.ARGB_8888)
                                    val canvas = Canvas(bmp)
                                    canvas.drawColor(android.graphics.Color.TRANSPARENT)
                                    val paint = Paint().apply {
                                        color = android.graphics.Color.BLACK
                                        style = Paint.Style.STROKE
                                        strokeWidth = 6f
                                        strokeJoin = Paint.Join.ROUND
                                        strokeCap = Paint.Cap.ROUND
                                        isAntiAlias = true
                                    }
                                    strokes.forEach { stroke ->
                                        if (stroke.isNotEmpty()) {
                                            val path = android.graphics.Path()
                                            path.moveTo(stroke.first().x, stroke.first().y)
                                            for (i in 1 until stroke.size) path.lineTo(stroke[i].x, stroke[i].y)
                                            canvas.drawPath(path, paint)
                                        }
                                    }
                                    onSave(bmp)
                                } else {
                                    // 👇 THIS POPS UP IF THEY DIDN'T DRAW ANYTHING 👇
                                    Toast.makeText(context, "Please provide a signature.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) { Text("Accept & Sign") }
                    }
                }
            }
        }
    }
}

fun compressImageFromUri(context: Context, uri: Uri): ByteArray? {
    return try {
        // 1. Get original bitmap safely through the ContentResolver
        val inputStream1 = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream1)

        // THIS MUST BE ON ITS OWN LINE 👇
        inputStream1?.close()

        if (originalBitmap == null) return null

        // 2. Read the hidden EXIF Rotation Metadata
        val inputStream2 = context.contentResolver.openInputStream(uri)
        val exif = inputStream2?.let { ExifInterface(it) }
        val orientation = exif?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        ) ?: ExifInterface.ORIENTATION_NORMAL

        // THIS MUST BE ON ITS OWN LINE 👇
        inputStream2?.close()

        // 3. Create a Matrix to fix the rotation
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        // 4. Scale down if it's huge (max 1024px on the longest side)
        val maxDim = 1024f
        val scale = minOf(maxDim / originalBitmap.width, maxDim / originalBitmap.height)

        val scaledBitmap = if (scale < 1f) {
            Bitmap.createScaledBitmap(
                originalBitmap,
                (originalBitmap.width * scale).toInt(),
                (originalBitmap.height * scale).toInt(),
                true
            )
        } else {
            originalBitmap
        }

        // 5. Apply the rotation fix to the newly scaled bitmap
        val rotatedBitmap = Bitmap.createBitmap(
            scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true
        )

        // 6. Compress to JPEG
        val baos = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        baos.toByteArray()
    } catch (e: Exception) {
        Log.e("ImageCompression", "Failed to compress image", e)
        null
    }
}

fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt( activity, executor, object : BiometricPrompt.AuthenticationCallback()
    { override fun onAuthenticationError(
        errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        onError(errString.toString())
    }
    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        onSuccess()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
    }
}
)

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Unlock CrewQ")
        .setSubtitle("Confirm your identity to access your account")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        .build()

    biometricPrompt.authenticate(promptInfo)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun CompanyChatScreen(
    onBack: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    currentUserId: String,
    currentUserName: String
) {
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var newMessageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
// Listen for new messages in real-time
    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("ChatScreen", "Listen failed.", error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        messages = snapshot.documents.mapNotNull {
                            it.toObject(ChatMessage::class.java)?.copy(id = it.id)
                        }
                        // Automatically scroll to the bottom when a new message arrives
                        coroutineScope.launch {
                            if (messages.isNotEmpty()) {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Company Chat") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            // 1. The Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    val isMe = message.senderId == currentUserId

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        // Align my messages to the right, others to the left
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Column(
                            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
                            modifier = Modifier.fillMaxWidth(0.8f) // Don't let messages stretch all the way across
                        ) {
                            // Only show the sender's name if it's someone else
                            Text(
                                text = message.senderName,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 2.dp, start = 4.dp, end = 4.dp)
                            )

                            // The Message Bubble
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isMe) MaterialTheme.colorScheme.primary else Color.White,
                                    contentColor = if (isMe) MaterialTheme.colorScheme.onPrimary else Color.Black
                                ),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isMe) 16.dp else 4.dp,
                                    bottomEnd = if (isMe) 4.dp else 16.dp
                                ),
                                elevation = CardDefaults.cardElevation(1.dp)
                            ) {
                                Text(
                                    text = message.text,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            // Timestamp
                            val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
                            Text(
                                text = timeFormat.format(message.timestamp.toDate()),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.LightGray,
                                modifier = Modifier.padding(top = 2.dp, end = 4.dp)
                            )
                        }
                    }
                }
            }

            // 2. The Input Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .navigationBarsPadding() // Keep it above the phone's swipe bar
                        .imePadding(), // Push it up when the keyboard opens!
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newMessageText,
                        onValueChange = { newMessageText = it },
                        placeholder = { Text("Message team...") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF0F0F0),
                            unfocusedContainerColor = Color(0xFFF0F0F0),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4 // Let them type a long message if needed
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button
                    IconButton(
                        onClick = {
                            if (newMessageText.isNotBlank()) {
                                val messageData = ChatMessage(
                                    senderId = currentUserId,
                                    senderName = currentUserName,
                                    text = newMessageText.trim()
                                )

                                // Instantly clear the box so it feels snappy
                                val textToSend = newMessageText.trim()
                                newMessageText = ""

                                coroutineScope.launch {
                                    try {
                                        firestore.collection("companies").document(companyName)
                                            .collection("messages").add(messageData).await()
                                    } catch (e: Exception) {
                                        Log.e("ChatScreen", "Error sending message", e)
                                        // Put the text back if it failed!
                                        newMessageText = textToSend
                                        Toast.makeText(context, "Failed to send", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
@Composable fun PaywallScreen(
    userRole: String,
    companyName: String,
    firestore: FirebaseFirestore,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Account Locked",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Subscription Inactive",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (userRole == "Manager") {
            Text(
                text = "Your company's CrewQ subscription has expired or payment failed. Please update your billing information to restore access for your entire team.",
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (!isLoading && companyName.isNotBlank()) {
                        isLoading = true

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser == null) {
                            Toast.makeText(context, "Error: User logged out", Toast.LENGTH_SHORT).show()
                            isLoading = false
                            return@Button
                        }

                        // 1. Tell the Stripe Extension to create a Checkout Session
                        val sessionData = hashMapOf(
                            "price" to "price_1TIDuBHefrvyxRLzOSKSCvwq",
                            "success_url" to "https://crewq.com/success",
                            "cancel_url" to "https://crewq.com/cancel",
                            // 👇 THE FIX: Remove client: mobile. Add customer ID. 👇
                            "customer" to currentUser.uid
                        )

                        // 👇 Use the user_profiles collection 👇
                        val sessionRef = firestore.collection("user_profiles").document(currentUser.uid)
                            .collection("checkout_sessions").document()

                        sessionRef.set(sessionData)

                        // 2. Wait for the Extension to give us the secure URL
                        val listener = sessionRef.addSnapshotListener { snap, err ->
                            val url = snap?.getString("url")

                            // Safely unpack the error object
                            val errorObj = snap?.get("error") as? Map<*, *>
                            val error = errorObj?.get("message") as? String

                            if (url != null) {
                                // 3. Open the phone's browser to the Stripe Checkout page!
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                                isLoading = false
                            } else if (error != null) {
                                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Subscribe / Upgrade Now", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link to the Customer Portal so they can cancel or update their credit card later
            TextButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://billing.stripe.com/p/login/test_5kQ9AT0ON1RsfRt7CKdUY00"))
                context.startActivity(intent)
            }) {
                Text("Manage Existing Billing")
            }

        } else {
            Text(
                text = "Your company's CrewQ subscription is currently inactive. Please contact your manager to restore access.",
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onSignOut) {
            Text("Sign Out", color = Color.Red)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    onBackToMenu: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    userRole: String,
    currentUserId: String,
    currentUserName: String
) {
    var toolsList by remember { mutableStateOf<List<Tool>>(emptyList()) }
    var companyUsers by remember { mutableStateOf<List<CompanyUser>>(emptyList()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Dialog States
    var showAddDialog by remember { mutableStateOf(false) }
    var toolToEdit by remember { mutableStateOf<Tool?>(null) }
    var toolToCheckout by remember { mutableStateOf<Tool?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Fetch Tools & Users
    LaunchedEffect(companyName, refreshTrigger) {
        if (companyName.isNotBlank()) {
            isLoading = true
            try {
                val toolsSnap = firestore.collection("companies").document(companyName)
                    .collection("tools").orderBy("name").get().await()
                toolsList = toolsSnap.documents.mapNotNull { it.toObject(Tool::class.java)?.copy(id = it.id) }

                val usersSnap = firestore.collection("companies").document(companyName)
                    .collection("users").get().await()
                companyUsers = usersSnap.documents.mapNotNull { it.toObject(CompanyUser::class.java)?.copy(id = it.id) }
            } catch (e: Exception) {
                Log.e("ToolsScreen", "Error loading tools", e)
            } finally {
                isLoading = false
            }
        }
    }

    val filteredTools = remember(toolsList, searchQuery) {
        if (searchQuery.isBlank()) toolsList
        else toolsList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.serialNumber.contains(searchQuery, ignoreCase = true) ||
                    it.brandModel.contains(searchQuery, ignoreCase = true)
        }
    }

    // --- ADD / EDIT DIALOG ---
    if (showAddDialog || toolToEdit != null) {
        val isEditing = toolToEdit != null
        var name by rememberSaveable { mutableStateOf(toolToEdit?.name ?: "") }
        var brandModel by rememberSaveable { mutableStateOf(toolToEdit?.brandModel ?: "") }
        var serialNumber by rememberSaveable { mutableStateOf(toolToEdit?.serialNumber ?: "") }
        var notes by rememberSaveable { mutableStateOf(toolToEdit?.notes ?: "") }
        var status by rememberSaveable { mutableStateOf(toolToEdit?.status ?: "Available") }

        Dialog(onDismissRequest = { showAddDialog = false; toolToEdit = null }) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())) {
                    Text(if (isEditing) "Edit Tool" else "Add New Tool", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))

                    TextField(value = name, onValueChange = { name = it }, label = { Text("Tool Name") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
                    Spacer(Modifier.height(8.dp))
                    TextField(value = brandModel, onValueChange = { brandModel = it }, label = { Text("Brand / Model") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
                    Spacer(Modifier.height(8.dp))
                    TextField(value = serialNumber, onValueChange = { serialNumber = it }, label = { Text("Serial Number") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
                    Spacer(Modifier.height(8.dp))
                    TextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes / Condition") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))

                    if (isEditing) {
                        Spacer(Modifier.height(16.dp))
                        Text("Status", style = MaterialTheme.typography.labelMedium)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            listOf("Available", "Maintenance", "Lost").forEach { s ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { status = s }) {
                                    RadioButton(selected = status == s, onClick = { status = s })
                                    Text(s, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddDialog = false; toolToEdit = null }) { Text("Cancel") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            coroutineScope.launch {
                                val toolData = Tool(
                                    id = toolToEdit?.id ?: "",
                                    name = name,
                                    brandModel = brandModel,
                                    serialNumber = serialNumber,
                                    notes = notes,
                                    status = status,
                                    assignedUserId = if (status == "Available") null else toolToEdit?.assignedUserId,
                                    assignedUserName = if (status == "Available") null else toolToEdit?.assignedUserName
                                )
                                val collection = firestore.collection("companies").document(companyName).collection("tools")
                                if (isEditing) collection.document(toolData.id).set(toolData).await()
                                else collection.add(toolData).await()

                                Toast.makeText(context, "Tool saved!", Toast.LENGTH_SHORT).show()
                                showAddDialog = false; toolToEdit = null; refreshTrigger++
                            }
                        }) { Text("Save Tool") }
                    }
                }
            }
        }
    }

    // --- CHECKOUT DIALOG ---
    if (toolToCheckout != null) {
        var expanded by remember { mutableStateOf(false) }
        var selectedUser by remember { mutableStateOf<CompanyUser?>(companyUsers.find { it.id == currentUserId }) }

        AlertDialog(
            onDismissRequest = { toolToCheckout = null },
            title = { Text("Check Out Tool") },
            text = {
                Column {
                    Text("Who is taking '${toolToCheckout!!.name}'?", color = Color.Gray)
                    Spacer(Modifier.height(16.dp))
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        TextField(
                            value = selectedUser?.let { "${it.firstName} ${it.lastName}" } ?: "Select Employee",
                            onValueChange = {}, readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            companyUsers.forEach { user ->
                                DropdownMenuItem(
                                    text = { Text("${user.firstName} ${user.lastName}") },
                                    onClick = { selectedUser = user; expanded = false }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (selectedUser != null) {
                        coroutineScope.launch {
                            firestore.collection("companies").document(companyName)
                                .collection("tools").document(toolToCheckout!!.id)
                                .update(mapOf(
                                    "status" to "Checked Out",
                                    "assignedUserId" to selectedUser!!.id,
                                    "assignedUserName" to "${selectedUser!!.firstName} ${selectedUser!!.lastName}",
                                    "checkoutDate" to FieldValue.serverTimestamp()
                                )).await()
                            Toast.makeText(context, "Tool Checked Out!", Toast.LENGTH_SHORT).show()
                            toolToCheckout = null; refreshTrigger++
                        }
                    }
                }) { Text("Check Out") }
            },
            dismissButton = { TextButton(onClick = { toolToCheckout = null }) { Text("Cancel") } }
        )
    }

    // --- MAIN UI ---
    Scaffold(
        floatingActionButton = {
            if (userRole == "Manager") {
                FloatingActionButton(onClick = { showAddDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                    Icon(Icons.Default.Add, contentDescription = "Add Tool", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackToMenu) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                Text("Tool Catalog", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = searchQuery, onValueChange = { searchQuery = it },
                label = { Text("Search tools or serial #") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, null) },
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (filteredTools.isEmpty()) {
                Box(Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center) { Text("No tools found.", color = Color.Gray) }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredTools, key = { it.id }) { tool ->
                        var expandedMenu by remember { mutableStateOf(false) }

                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier
                                    .padding(16.dp)
                                    .padding(end = 32.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(tool.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                                        // Status Badge
                                        val statusColor = when (tool.status) {
                                            "Available" -> Color(0xFF2CA01C)
                                            "Checked Out" -> Color(0xFFFF9800)
                                            else -> Color.Red
                                        }
                                        Text(tool.status, color = statusColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                    }

                                    if (tool.brandModel.isNotBlank()) Text("Model: ${tool.brandModel}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    if (tool.serialNumber.isNotBlank()) Text("S/N: ${tool.serialNumber}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                                    if (tool.status == "Checked Out" && tool.assignedUserName != null) {
                                        Spacer(Modifier.height(8.dp))
                                        Text("Currently with: ${tool.assignedUserName}", color = Color.Black, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                        val dateFormat = remember { SimpleDateFormat("MM/dd/yy", Locale.getDefault()) }
                                        tool.checkoutDate?.let { Text("Since: ${dateFormat.format(it.toDate())}", style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    // Action Buttons
                                    if (tool.status == "Available") {
                                        Button(onClick = { toolToCheckout = tool }, modifier = Modifier.fillMaxWidth()) { Text("Check Out") }
                                    } else if (tool.status == "Checked Out") {
                                        OutlinedButton(onClick = {
                                            coroutineScope.launch {
                                                firestore.collection("companies").document(companyName)
                                                    .collection("tools").document(tool.id)
                                                    .update(mapOf("status" to "Available", "assignedUserId" to null, "assignedUserName" to null, "checkoutDate" to null)).await()
                                                Toast.makeText(context, "Tool Returned to Shop", Toast.LENGTH_SHORT).show()
                                                refreshTrigger++
                                            }
                                        }, modifier = Modifier.fillMaxWidth()) { Text("Return to Shop") }
                                    }
                                }

                                if (userRole == "Manager") {
                                    Box(modifier = Modifier.align(Alignment.TopEnd)) {
                                        IconButton(onClick = { expandedMenu = true }) { Icon(Icons.Default.MoreVert, "Options", tint = Color.Gray) }
                                        DropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }, modifier = Modifier.background(Color.White)) {
                                            DropdownMenuItem(text = { Text("Edit") }, onClick = { toolToEdit = tool; expandedMenu = false })
                                            DropdownMenuItem(text = { Text("Delete", color = Color.Red) }, onClick = {
                                                expandedMenu = false
                                                coroutineScope.launch {
                                                    firestore.collection("companies").document(companyName).collection("tools").document(tool.id).delete().await()
                                                    refreshTrigger++
                                                }
                                            })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MileageScreen(
    onBackToMenu: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String,
    userRole: String, // <-- Added userRole parameter
    userId: String,
    userName: String
) {
    var mileageList by remember { mutableStateOf<List<MileageEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }

    // 👇 NEW: State for Editing and Deleting 👇
    var entryToEdit by remember { mutableStateOf<MileageEntry?>(null) }
    var entryToDelete by remember { mutableStateOf<MileageEntry?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dateFormat = remember { SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()) }

    // Fetch the logged-in user's mileage
    LaunchedEffect(companyName, userId) {
        if (companyName.isNotBlank() && userId.isNotBlank()) {
            val listener = firestore.collection("companies").document(companyName)
                .collection("users").document(userId)
                .collection("mileage")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("MileageScreen", "Error loading mileage", error)
                        isLoading = false
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        mileageList = snapshot.documents.mapNotNull {
                            it.toObject(MileageEntry::class.java)?.copy(id = it.id)
                        }
                    }
                    isLoading = false
                }
        }
    }

    // --- REUSABLE DIALOG FOR ADD / EDIT ---
    if (showAddDialog || entryToEdit != null) {
        AddMileageDialog(
            editingEntry = entryToEdit, // Pass the entry if we are editing!
            onDismiss = {
                showAddDialog = false
                entryToEdit = null
            },
            onSave = { entry ->
                coroutineScope.launch {
                    try {
                        val collection = firestore.collection("companies").document(companyName)
                            .collection("users").document(userId).collection("mileage")

                        if (entry.id.isNotBlank()) {
                            // Update existing entry
                            collection.document(entry.id).set(entry).await()
                            Toast.makeText(context, "Mileage Updated!", Toast.LENGTH_SHORT).show()
                        } else {
                            // Create new entry
                            collection.add(entry).await()
                            Toast.makeText(context, "Mileage Logged!", Toast.LENGTH_SHORT).show()
                        }

                        showAddDialog = false
                        entryToEdit = null
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            companyName = companyName,
            userId = userId,
            userName = userName
        )
    }

    // --- CONFIRMATION DIALOG FOR DELETE ---
    if (entryToDelete != null) {
        ConfirmationDialog(
            title = "Delete Mileage",
            message = "Are you sure you want to delete this mileage entry?",
            onConfirm = {
                firestore.collection("companies").document(companyName)
                    .collection("users").document(userId)
                    .collection("mileage").document(entryToDelete!!.id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Entry deleted.", Toast.LENGTH_SHORT).show()
                    }
                entryToDelete = null
            },
            onDismiss = { entryToDelete = null }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log Mileage")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackToMenu) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                Text("Mileage Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            val totalMilesLogged = mileageList.sumOf { it.totalMiles }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Miles Tracked", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = String.format(Locale.US, "%.1f mi", totalMilesLogged),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (mileageList.isEmpty()) {
                Box(Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No mileage logged yet.", color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(mileageList, key = { it.id }) { entry ->
                        val isOdoOnly = entry.totalMiles == 0.0 && entry.startOdometer != null

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dateFormat.format(entry.date.toDate()),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    if (isOdoOnly) {
                                        Text(
                                            text = "Odometer Check",
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    } else {
                                        Text(
                                            text = "${entry.totalMiles} mi",
                                            color = Color(0xFF2CA01C),
                                            fontWeight = FontWeight.ExtraBold,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }

                                if (entry.vehicle.isNotBlank()) {
                                    Text("Vehicle: ${entry.vehicle}", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                                }

                                if (isOdoOnly) {
                                    Text("Odometer Reading: ${entry.startOdometer}", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.SemiBold)
                                } else if (entry.startOdometer != null && entry.endOdometer != null) {
                                    Text("Odometer: ${entry.startOdometer} → ${entry.endOdometer}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }

                                if (entry.notes.isNotBlank()) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(entry.notes, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
                                }

                                // 👇 NEW: Manager Edit & Delete Buttons 👇
                                if (userRole == "Manager") {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(onClick = { entryToEdit = entry }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit Entry", modifier = Modifier.size(20.dp))
                                        }
                                        IconButton(onClick = { entryToDelete = entry }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete Entry", tint = Color.Red, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMileageDialog(
    editingEntry: MileageEntry? = null, // 👇 THIS IS THE PARAMETER IT WAS MISSING 👇
    onDismiss: () -> Unit,
    onSave: (MileageEntry) -> Unit,
    companyName: String,
    userId: String,
    userName: String
) {
    // Pre-fill states if editingEntry is provided
    var vehicle by rememberSaveable { mutableStateOf(editingEntry?.vehicle ?: "") }
    var startOdoText by rememberSaveable {
        mutableStateOf(editingEntry?.startOdometer?.let {
            if (it % 1 == 0.0) it.toInt().toString() else it.toString()
        } ?: "")
    }
    var endOdoText by rememberSaveable {
        mutableStateOf(editingEntry?.endOdometer?.let {
            if (it % 1 == 0.0) it.toInt().toString() else it.toString()
        } ?: "")
    }
    var manualTotalText by rememberSaveable {
        mutableStateOf(editingEntry?.totalMiles?.let { if (it == 0.0) "" else it.toString() } ?: "")
    }
    var notes by rememberSaveable { mutableStateOf(editingEntry?.notes ?: "") }

    var isOdoReadingOnly by rememberSaveable {
        mutableStateOf(editingEntry != null && editingEntry.totalMiles == 0.0 && editingEntry.startOdometer != null)
    }

    val startOdo = startOdoText.toDoubleOrNull()
    val endOdo = endOdoText.toDoubleOrNull()
    val autoTotal =
        if (!isOdoReadingOnly && startOdo != null && endOdo != null && endOdo >= startOdo) {
            endOdo - startOdo
        } else null

    LaunchedEffect(autoTotal) {
        if (autoTotal != null) {
            manualTotalText = String.format(Locale.US, "%.1f", autoTotal)
        }
    }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (editingEntry != null) "Edit Mileage" else if (isOdoReadingOnly) "Log Odometer" else "Log Mileage",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isOdoReadingOnly,
                        onCheckedChange = { isChecked ->
                            isOdoReadingOnly = isChecked
                            endOdoText = ""
                            manualTotalText = ""
                        }
                    )
                    Text(
                        "Log single odometer reading (e.g., Start of Year)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(Modifier.height(16.dp))

                TextField(
                    value = vehicle, onValueChange = { vehicle = it },
                    label = { Text("Vehicle (Optional)") },
                    modifier = Modifier.fillMaxWidth(), colors = textFieldColors
                )
                Spacer(Modifier.height(8.dp))

                if (isOdoReadingOnly) {
                    TextField(
                        value = startOdoText, onValueChange = { startOdoText = it },
                        label = { Text("Odometer Reading*") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = textFieldColors
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextField(
                            value = startOdoText, onValueChange = { startOdoText = it },
                            label = { Text("Start Odo") }, modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = textFieldColors
                        )
                        TextField(
                            value = endOdoText, onValueChange = { endOdoText = it },
                            label = { Text("End Odo") }, modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = textFieldColors
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = manualTotalText, onValueChange = { manualTotalText = it },
                        label = { Text("Total Miles*") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = textFieldColors
                    )
                }

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("Notes / Destination") },
                    modifier = Modifier.fillMaxWidth(), colors = textFieldColors
                )

                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))

                    val isSaveEnabled = if (isOdoReadingOnly) {
                        startOdoText.toDoubleOrNull() != null
                    } else {
                        (manualTotalText.toDoubleOrNull() ?: 0.0) > 0
                    }

                    Button(
                        onClick = {
                            val finalTotal =
                                if (isOdoReadingOnly) 0.0 else manualTotalText.toDoubleOrNull()
                                    ?: 0.0

                            onSave(
                                MileageEntry(
                                    id = editingEntry?.id ?: "",
                                    date = editingEntry?.date ?: Timestamp.now(),
                                    userId = userId,
                                    userName = userName,
                                    companyName = companyName,
                                    vehicle = vehicle,
                                    startOdometer = startOdoText.toDoubleOrNull(),
                                    endOdometer = if (isOdoReadingOnly) null else endOdoText.toDoubleOrNull(),
                                    totalMiles = finalTotal,
                                    notes = notes
                                )
                            )
                        },
                        enabled = isSaveEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2CA01C))
                    ) {
                        Text(if (editingEntry != null) "Update Entry" else "Save Entry")
                    }
                }
            }
        }
    }
}
fun logCompanyActivity(
    firestore: FirebaseFirestore,
    companyName: String,
    userId: String,
    userName: String,
    action: String,
    details: String
) {
    // If these are blank, we want to know about it, but we still want to log it to trace the bug!
    val safeCompany = companyName.ifBlank { "UnknownCompany" }
    val safeUserId = userId.ifBlank { "UnknownUser" }

    val logEntry = hashMapOf(
        "userId" to safeUserId,
        "userName" to userName,
        "action" to action,
        "details" to details,
        "timestamp" to FieldValue.serverTimestamp()
    )

    Log.d("AuditLog", "Attempting to log: $action for $safeCompany")

    // Force the write to happen
    firestore.collection("companies").document(safeCompany)
        .collection("activity_logs").add(logEntry)
        .addOnSuccessListener {
            Log.d("AuditLog", "Successfully wrote log to database!")
        }
        .addOnFailureListener { e ->
            Log.e("AuditLog", "Failed to write log", e)
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLogScreen(
    onBackToMenu: () -> Unit,
    firestore: FirebaseFirestore,
    companyName: String
) {
    var logs by remember { mutableStateOf<List<ActivityLogEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName)
                .collection("activity_logs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(200) // Only load the 200 most recent events to save data
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        isLoading = false
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        logs = snapshot.documents.mapNotNull {
                            it.toObject(ActivityLogEntry::class.java)?.copy(id = it.id)
                        }
                    }
                    isLoading = false
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audit Log") },
                navigationIcon = {
                    IconButton(onClick = onBackToMenu) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (logs.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No activity logged yet.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(logs, key = { it.id }) { log ->
                        val dateFormat = remember { SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault()) }

                        // Determine the icon and color based on the action
                        val iconColor = when {
                            log.action.contains("DELETE") -> Color(0xFFE53935) // Red
                            log.action.contains("CREATE") -> Color(0xFF43A047) // Green
                            log.action.contains("TIME") || log.action.contains("CLOCK") -> Color(0xFF1E88E5) // Blue
                            log.action.contains("EDIT") || log.action.contains("UPDATE") -> Color(0xFFFB8C00) // Orange
                            else -> Color.Gray
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Visual Indicator
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(iconColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = log.userName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = log.timestamp?.toDate()?.let { dateFormat.format(it) } ?: "Just now",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = log.details,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}