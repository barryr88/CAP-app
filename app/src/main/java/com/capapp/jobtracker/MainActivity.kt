package com.capapp.jobtracker


import android.app.NotificationManager
import android.app.Service
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
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.capapp.jobtracker.ui.theme.JobTrackerTheme
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
import java.time.DayOfWeek
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
import androidx.core.graphics.scale
import androidx.core.graphics.withTranslation
import androidx.core.net.toUri
import com.google.firebase.firestore.SetOptions
import android.provider.OpenableColumns
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.AttachFile
import com.google.maps.android.compose.Polyline

data class MapUser(
    val name: String,
    val position: LatLng
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun AddressAutocompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
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
                    predictions = response.autocompletePredictions
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
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        if (predictions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                predictions.forEach { prediction ->
                    DropdownMenuItem(
                        text = { Text(prediction.getFullText(null).toString()) },
                        onClick = {
                            onValueChange(prediction.getFullText(null).toString())
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

@Composable
fun TeamMapScreen(
    firestore: FirebaseFirestore,
    companyName: String
) {
    var usersOnMap by remember { mutableStateOf<List<MapUser>>(emptyList()) }
    var userPaths by remember { mutableStateOf<List<UserPath>>(emptyList()) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(39.8283, -98.5795), 4f)
    }

    // Colors to distinguish different users on the map
    val pathColors = listOf(Color.Blue, Color.Red, Color.Green, Color.Magenta, Color.Cyan, Color(0xFFFF5722))

    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            // 1. Fetch Users (Current Location)
            firestore.collection("companies").document(companyName).collection("users")
                .whereNotEqualTo("location", null)
                .addSnapshotListener { result, e ->
                    if (e != null || result == null) return@addSnapshotListener

                    val userList = mutableListOf<MapUser>()
                    val pathsList = mutableListOf<UserPath>()

                    // Calculate start of today to filter history
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    val startOfToday = Timestamp(calendar.time)

                    result.documents.forEachIndexed { index, doc ->
                        val geoPoint = doc.getGeoPoint("location")
                        val firstName = doc.getString("firstName") ?: "Unknown"
                        val lastName = doc.getString("lastName") ?: ""
                        val userId = doc.id

                        // Assign a consistent color to this user
                        val userColor = pathColors[index % pathColors.size]

                        if (geoPoint != null) {
                            userList.add(
                                MapUser(
                                    name = "$firstName $lastName",
                                    position = LatLng(geoPoint.latitude, geoPoint.longitude)
                                )
                            )

                            // 2. Fetch History for this user (Nested Listener)
                            // Note: For production, consider optimizing this to not fetch inside a loop
                            // if you have many users.
                            doc.reference.collection("location_history")
                                .whereGreaterThan("timestamp", startOfToday)
                                .orderBy("timestamp", Query.Direction.ASCENDING)
                                .get()
                                .addOnSuccessListener { historyResult ->
                                    val points = historyResult.documents.mapNotNull { historyDoc ->
                                        val histGeo = historyDoc.getGeoPoint("location")
                                        if (histGeo != null) LatLng(histGeo.latitude, histGeo.longitude) else null
                                    }

                                    if (points.isNotEmpty()) {
                                        // Update the specific path for this user
                                        userPaths = userPaths.filter { it.userId != userId } +
                                                UserPath(userId, userColor, points)
                                    }
                                }
                        }
                    }
                    usersOnMap = userList

                    // Center camera on first user if just loaded
                    if (usersOnMap.isNotEmpty() && cameraPositionState.position.zoom == 4f) {
                        usersOnMap.firstOrNull()?.let {
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(it.position, 12f)
                        }
                    }
                }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Draw the Paths (History)
        userPaths.forEach { path ->
            Polyline(
                points = path.points,
                color = path.color,
                width = 10f
            )
        }

        // Draw the Markers (Current Location)
        usersOnMap.forEach { user ->
            Marker(
                state = MarkerState(position = user.position),
                title = user.name,
                snippet = "Current Location"
            )
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
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your app's icon
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
        if (user == null) return

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

                        // 1. Update Current Location (Existing logic)
                        val locationData = mapOf(
                            "location" to geoPoint,
                            "lastLocationUpdate" to now
                        )
                        userRef.update(locationData)

                        // 2. ADD HISTORY ENTRY (New Logic)
                        // We save a new document for every update to build the path
                        val historyData = mapOf(
                            "location" to geoPoint,
                            "timestamp" to now
                        )
                        userRef.collection("location_history").add(historyData)
                            .addOnFailureListener { e -> Log.e(TAG, "Failed to save history", e) }
                    }
                }
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
data class PdfEstimateData(
    val estimate: Estimate,
    val lineItems: List<EstimateLineItem>
)
data class PdfInvoiceData(
    val invoice: Invoice,
    val lineItems: List<EstimateLineItem>
)
fun generateAndEmailInvoice(
    context: Context, data: PdfInvoiceData) {
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
    var yPos = topMargin

    // --- HELPER TO DRAW THE REPEATING HEADER ---
    fun drawPageHeader(): Float {
        // Draw Logo
        var logoX = pageWidth - margin
        try {
            val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.cap_construction)
            val scaledBitmap = originalBitmap.scale(180, 90, true)
            logoX = pageWidth - scaledBitmap.width - margin
            canvas.drawBitmap(scaledBitmap, logoX, topMargin - 40, null)
        } catch (e: Exception) { /* Logo not found */ }

        // Draw Company Info
        canvas.drawText("CAP CONSTRUCTION LLC", margin, topMargin, headerPaint)
        canvas.drawText("7008 N Fischer Ct", margin, topMargin + 20, normalPaint)
        canvas.drawText("Spokane, WA 99208", margin, topMargin + 40, normalPaint)
        canvas.drawText("+15097681167", margin, topMargin + 60, normalPaint)
        canvas.drawText("capconstructionllc@icloud.com", margin, topMargin + 80, normalPaint)

        // Draw "Invoice" Title & Number
        canvas.drawText("Invoice", 300f, 80f, titlePaint)
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
        val issueDateStr = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(data.invoice.creationDate.toDate())
        canvas.drawRect(dateBoxRect, grayFillPaint)
        canvas.drawText("DATE", dateBoxRect.centerX() - 20, dateBoxRect.top + 20, smallPaint)
        canvas.drawText(issueDateStr, dateBoxRect.centerX() - 40, dateBoxRect.top + 45, normalPaint)
        canvas.drawRect(totalBoxRect, blackFillPaint)
        canvas.drawText("TOTAL", totalBoxRect.centerX() - 25, totalBoxRect.top + 20, whiteTextPaint)
        canvas.drawText("$%.2f".format(data.invoice.totalAmount), totalBoxRect.centerX() - 40, totalBoxRect.top + 45, whiteTextPaint)

        val dueDateStr = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(data.invoice.dueDate.toDate())
        canvas.drawText("Due Date: $dueDateStr", margin, addressBottomY + 20, headerPaint)

        return addressBottomY + 60
    }

    // --- DRAW INITIAL HEADER ---
    yPos = drawPageHeader()

    // --- DRAW LINE ITEMS TABLE ---
    val activityColX = margin + 80
    val descColX = activityColX + 180
    val qtyColX = descColX + 270
    val rateColX = qtyColX + 60
    val amountColX = rateColX + 80

    fun drawLineItemHeader(startY: Float): Float {
        var currentY = startY
        canvas.drawText("DATE", margin, currentY, tableHeaderPaint)
        canvas.drawText("ACTIVITY", activityColX, currentY, tableHeaderPaint)
        canvas.drawText("DESCRIPTION", descColX, currentY, tableHeaderPaint)
        canvas.drawText("QTY", qtyColX, currentY, tableHeaderPaint)
        canvas.drawText("RATE", rateColX, currentY, tableHeaderPaint)
        canvas.drawText("AMOUNT", amountColX, currentY, tableHeaderPaint)
        currentY += 15
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
        currentY += 25
        return currentY
    }

    yPos = drawLineItemHeader(yPos)

    for (item in data.lineItems) {
        val issueDateStr = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(data.invoice.creationDate.toDate())
        val activityLayout = StaticLayout.Builder.obtain(item.name, 0, item.name.length, headerPaint, (descColX - activityColX - 15).toInt()).build()
        val descLayout = StaticLayout.Builder.obtain(item.description, 0, item.description.length, normalPaint, (qtyColX - descColX - 15).toInt()).build()
        val rowHeight = maxOf(activityLayout.height, descLayout.height).toFloat()

        // *** NEW: Check if the item fits on the current page ***
        if (yPos + rowHeight > pageHeight - bottomMargin) {
            pdfDocument.finishPage(page)
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            val headerBottomY = drawPageHeader() // Redraw header
            yPos = drawLineItemHeader(headerBottomY) // Redraw table header
        }

        val itemStartY = yPos
        val textVerticalOffset = if (rowHeight > 20) rowHeight / 2 - 10 else 0f

        canvas.drawText(issueDateStr, margin, itemStartY + textVerticalOffset, normalPaint)
        canvas.drawText(item.quantity.toString(), qtyColX, itemStartY + textVerticalOffset, normalPaint)
        canvas.drawText("%.2f".format(item.unitPrice), rateColX, itemStartY + textVerticalOffset, normalPaint)
        canvas.drawText("%.2f".format(item.unitPrice * item.quantity), amountColX, itemStartY + textVerticalOffset, normalPaint)

        canvas.withTranslation(x = activityColX, y = itemStartY) { activityLayout.draw(this) }
        canvas.withTranslation(x = descColX, y = itemStartY) { descLayout.draw(this) }
        yPos += rowHeight + rowPadding
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
    yPos += 15
    canvas.drawLine(totalsLabelX - 20, yPos, pageWidth - margin, yPos, linePaint)
    yPos += 25
    canvas.drawText("TOTAL", totalsLabelX, yPos, headerPaint)
    canvas.drawText("$%.2f".format(data.invoice.totalAmount), totalsValueX, yPos, headerPaint)
    yPos += 25
    canvas.drawText("THANK YOU.", totalsLabelX + 50, yPos, smallPaint)

    // --- FINISH AND SAVE ---
    pdfDocument.finishPage(page)
    val file = File(context.cacheDir, "Invoice_${data.invoice.invoiceNumber}.pdf")
    try {
        FileOutputStream(file).use { pdfDocument.writeTo(it) }
        pdfDocument.close()
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(data.invoice.customerEmail))
            putExtra(Intent.EXTRA_SUBJECT, "Service Invoice #${data.invoice.invoiceNumber}")
            putExtra(Intent.EXTRA_TEXT, "Please find your invoice attached.")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(emailIntent, "Send Invoice via..."))
    } catch (e: Exception) {
        Log.e("PDF_GEN", "Failed to generate or send Invoice PDF", e)
    }
}
fun generateAndEmailEstimate(
    context: Context, data: PdfEstimateData) {
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
    val contentWidth = pageWidth - (2 * margin)

    // --- PAGE SETUP ---
    var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas: Canvas = page.canvas
    var yPos = topMargin

    // --- HELPER TO DRAW THE REPEATING HEADER ---
    fun drawPageHeader(): Float {
        // Draw Logo
        var logoX = pageWidth - margin
        try {
            val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.cap_construction)
            val scaledBitmap = originalBitmap.scale(180, 90, true)
            logoX = pageWidth - scaledBitmap.width - margin
            canvas.drawBitmap(scaledBitmap, logoX, topMargin - 40, null)
        } catch (e: Exception) { /* Logo not found */ }

        // Draw Company Info
        canvas.drawText("CAP CONSTRUCTION LLC", margin, topMargin, headerPaint)
        canvas.drawText("7008 N Fischer Ct", margin, topMargin + 20, normalPaint)
        canvas.drawText("Spokane, WA 99208", margin, topMargin + 40, normalPaint)
        canvas.drawText("+15097681167", margin, topMargin + 60, normalPaint)
        canvas.drawText("capconstructionllc@icloud.com", margin, topMargin + 80, normalPaint)

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

    // --- DRAW INITIAL HEADER ---
    yPos = drawPageHeader()

    // --- DRAW LINE ITEMS TABLE ---
    val activityColX = margin + 80
    val descColX = activityColX + 180
    val qtyColX = descColX + 270
    val rateColX = qtyColX + 60
    val amountColX = rateColX + 80

    // Helper to draw the table header
    fun drawLineItemHeader(startY: Float): Float {
        var currentY = startY
        canvas.drawText("DATE", margin, currentY, tableHeaderPaint)
        canvas.drawText("ACTIVITY", activityColX, currentY, tableHeaderPaint)
        canvas.drawText("DESCRIPTION", descColX, currentY, tableHeaderPaint)
        canvas.drawText("QTY", qtyColX, currentY, tableHeaderPaint)
        canvas.drawText("RATE", rateColX, currentY, tableHeaderPaint)
        canvas.drawText("AMOUNT", amountColX, currentY, tableHeaderPaint)
        currentY += 15
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
        currentY += 25
        return currentY
    }

    yPos = drawLineItemHeader(yPos)

    for (item in data.lineItems) {
        val dateStr = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(data.estimate.creationDate.toDate())
        // Pre-calculate the height of the row to check for page breaks
        val activityLayout = StaticLayout.Builder.obtain(item.name, 0, item.name.length, headerPaint, (descColX - activityColX - 15).toInt()).build()
        val descLayout = StaticLayout.Builder.obtain(item.description, 0, item.description.length, normalPaint, (qtyColX - descColX - 15).toInt()).build()
        val rowHeight = maxOf(activityLayout.height, descLayout.height).toFloat()

        // *** NEW: Check if the item fits on the current page ***
        if (yPos + rowHeight > pageHeight - bottomMargin) {
            pdfDocument.finishPage(page)
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            val headerBottomY = drawPageHeader() // Redraw header on new page
            yPos = drawLineItemHeader(headerBottomY) // Redraw table header on new page
        }

        val itemStartY = yPos
        val textVerticalOffset = if (rowHeight > 20) rowHeight / 2 - 10 else 0f

        canvas.drawText(dateStr, margin, itemStartY + textVerticalOffset, normalPaint)
        canvas.drawText(item.quantity.toString(), qtyColX, itemStartY + textVerticalOffset, normalPaint)
        canvas.drawText("%.2f".format(item.unitPrice), rateColX, itemStartY + textVerticalOffset, normalPaint)
        canvas.drawText("%.2f".format(item.unitPrice * item.quantity), amountColX, itemStartY + textVerticalOffset, normalPaint)

        canvas.withTranslation(x = activityColX, y = itemStartY) { activityLayout.draw(this) }
        canvas.withTranslation(x = descColX, y = itemStartY) { descLayout.draw(this) }

        yPos += rowHeight + rowPadding
    }
    canvas.drawLine(margin, yPos - 15, pageWidth - margin, yPos - 15, linePaint)

    // --- DRAW FOOTER AND SIGNATURE (check for page break first) ---
    val footerHeight = 220f
    if (yPos + footerHeight > pageHeight - bottomMargin) {
        pdfDocument.finishPage(page)
        pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
        page = pdfDocument.startPage(pageInfo)
        canvas = page.canvas
        yPos = drawPageHeader() // Redraw header on new page for context
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

    yPos = pageHeight - 100f
    canvas.drawText("Accepted By", margin, yPos, normalPaint)
    canvas.drawLine(margin + 100, yPos, margin + 300, yPos, linePaint)
    canvas.drawText("Accepted Date", margin + 350, yPos, normalPaint)
    canvas.drawLine(margin + 480, yPos, margin + 680, yPos, linePaint)


    // --- FINISH AND SAVE ---
    pdfDocument.finishPage(page) // Finish the last page
    val file = File(context.cacheDir, "Estimate_${data.estimate.estimateNumber}.pdf")
    try {
        FileOutputStream(file).use { outputStream -> pdfDocument.writeTo(outputStream) }
        pdfDocument.close()
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(data.estimate.customerEmail))
            putExtra(Intent.EXTRA_SUBJECT, "Service Estimate #${data.estimate.estimateNumber}")
            putExtra(Intent.EXTRA_TEXT, "Please find your estimate attached.")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(emailIntent, "Send Estimate via..."))
    } catch (e: Exception) {
        Log.e("PDF_GEN", "Failed to generate or send PDF", e)
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


class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        auth = Firebase.auth
        firestore = Firebase.firestore
        enableEdgeToEdge()
        setContent {
            JobTrackerTheme {
                MainApp(firebaseAnalytics, auth, firestore)
            }
        }
    }
}

@Parcelize
data class JobDocument(
    val name: String = "",
    val url: String = "",
    val type: String = "" // Optional: to show different icons (pdf vs doc)
) : Parcelable

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
    val searchableName: String = ""
)
data class ProductOrService(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val type: String = "Service" // "Service" or "Product"
)

data class TeamTimeEntry(
    val id: String,
    val userId: String,
    val userName: String,
    val clockIn: Timestamp?,
    val clockOut: Timestamp?
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
    val creatorName: String? = null
)
data class Invoice(
    val id: String = "",
    val invoiceNumber: String = "",
    val customerId: String = "",
    val customerName: String = "", // Denormalized for easy list display
    val customerEmail: String = "", // <-- THE FIX IS HERE
    val address: String = "",
    val description: String = "",
    val creationDate: Timestamp = Timestamp.now(),
    val dueDate: Timestamp = Timestamp(Calendar.getInstance().apply { add(Calendar.DATE, 30) }.time),
    val status: String = "Due",
    val totalAmount: Double = 0.0,
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val taxRate: Double = 0.0,
    val estimateId: String? = null,
    val creatorId: String? = null,
    val creatorName: String? = null
)
data class CompanyUser(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: String = ""
)
@Parcelize
data class EstimateLineItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val quantity: Double = 1.0,
    val unitPrice: Double = 0.0

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
    val notes: String = "",
    val assignedUserIds: List<String> = emptyList(),
    val assignedUserNames: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val documents: List<JobDocument> = emptyList()
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
    firestore: FirebaseFirestore
) {
    var viewingEstimateId by rememberSaveable { mutableStateOf<String?>(null) }
    var viewingInvoiceId by rememberSaveable { mutableStateOf<String?>(null) }
    var editingTask by remember { mutableStateOf<FirestoreTask?>(null) }
    var selectedTaskForDetails by rememberSaveable { mutableStateOf<FirestoreTask?>(null) }
    var selectedCustomerId by rememberSaveable { mutableStateOf<String?>(null) } // Add this state
    var currentScreen by rememberSaveable { mutableStateOf("signIn") }
    var refreshTrigger by remember { mutableStateOf(0) }
    var loggedInUser by rememberSaveable { mutableStateOf<String?>(null) }
    var editingInvoiceId by rememberSaveable { mutableStateOf<String?>(null) }
    var editingEstimateId by rememberSaveable { mutableStateOf<String?>(null) }
    var isAddingUser by remember { mutableStateOf(false) }
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
    var currentUser by remember { mutableStateOf(auth.currentUser) }

    var taskForConversion by rememberSaveable { mutableStateOf<FirestoreTask?>(null) }
    var isConverting by remember { mutableStateOf(false) }

    var showDeleteEstimateDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteInvoiceDialog by rememberSaveable { mutableStateOf(false) }
    var isDuplicating by remember { mutableStateOf(false) }


    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
        }
        auth.addAuthStateListener(authStateListener)

        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    LaunchedEffect(currentUser) {
        if (isAddingUser) {
            return@LaunchedEffect
        }
        if (currentUser != null) {
            try {
                val userQuery = firestore.collectionGroup("users").whereEqualTo("uid", currentUser!!.uid).limit(1).get().await()
                if (!userQuery.isEmpty) {
                    val userDoc = userQuery.documents[0]
                    val firstName = userDoc.getString("firstName") ?: ""
                    val lastName = userDoc.getString("lastName") ?: ""
                    loggedInUser = "$firstName $lastName".trim().ifBlank { currentUser!!.email }
                    userRole = userDoc.getString("role") ?: ""
                    companyName = userDoc.getString("companyName") ?: ""
                } else {
                    Log.e(AUTH_TAG, "User document not found for UID: ${currentUser!!.uid}")
                    loggedInUser = currentUser!!.email // fallback
                }
                if (currentScreen == "signIn") {
                    currentScreen = "mainMenu"
                }

            } catch (e: Exception) {
                Log.e(AUTH_TAG, "Error fetching user data", e)
                Toast.makeText(context, "Error fetching user data: ${e.message}", Toast.LENGTH_LONG).show()
                loggedInUser = currentUser!!.email // fallback
                if (currentScreen == "signIn") {
                    currentScreen = "mainMenu" // Still navigate on error if coming from sign-in
                }
            }
        } else {
            loggedInUser = null
            userRole = ""
            companyName = ""
            if (currentScreen != "register") {
                currentScreen = "signIn"
            }
        }
    }

    val showNavigationDrawer = currentScreen !in listOf("signIn", "register")

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showNavigationDrawer,
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
                val screensWithOwnAppBar = listOf("jobDetails")
                val canGoBack = currentScreen in listOf("newTask")
                val showTopBar = currentScreen !in listOf("signIn", "register") + screensWithOwnAppBar
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
                    },
                    onRegisterClick = { currentScreen = "register" },
                    auth = auth
                )

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

                "mainMenu" -> MainMenu(
                    modifier = screenModifier,
                    username = loggedInUser ?: "User",
                    firestore = firestore,
                    companyName = companyName
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

                "jobDetails" -> {
                    selectedTaskForDetails?.let { task ->
                        JobDetailsScreen(
                            task = task,
                            modifier = screenModifier,
                            firestore = firestore,
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
                                currentScreen = "viewEstimate"
                            },
                            onInvoiceClick = { invoiceId ->
                                // Set the customer context so the back button from ViewInvoiceScreen works correctly
                                selectedCustomerId = task.customerId
                                viewingInvoiceId = invoiceId
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
                    onBackToSchedule = { currentScreen = "schedule" },
                    onTaskCreated = {
                        editingTask = null
                        currentScreen = "schedule"
                    },
                    firestore = firestore,
                    companyName = companyName
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
                    firestore = firestore,
                    companyName = companyName
                )

                "estimates" -> EstimatesScreen(
                    onBackToMenu = { currentScreen = "mainMenu" },
                    onNewEstimateClick = { currentScreen = "newEstimate" },
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
                    auth = auth,
                    firestore = firestore,
                    companyName = companyName,
                    userRole = userRole,
                    onAddingUserChange = { isAddingUser = it }
                )

                "customers" -> {
                    if (companyName.isNotBlank()) {
                        CustomersScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            onCustomerClick = { customerId ->
                                selectedCustomerId = customerId
                                currentScreen = "viewCustomer"
                            },
                            firestore = firestore,
                            companyName = companyName
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
                            companyName = companyName,
                            onBack = { currentScreen = "customers" },
                            onEstimateClick = { estimateId ->
                                viewingEstimateId = estimateId
                                currentScreen = "viewEstimate"
                            },
                            onInvoiceClick = { invoiceId ->
                                viewingInvoiceId = invoiceId
                                currentScreen = "viewInvoice"
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
                            companyName = companyName,
                            onBack = { currentScreen = "viewCustomer" },
                            onEditClick = {
                                editingEstimateId = estimateId
                                currentScreen = "editEstimate"
                            },
                            onConvertToInvoiceClick = {
                                scope.launch {
                                    isConverting = true
                                    try {
                                        // 1. Fetch the full estimate object
                                        val estimateDoc = firestore.collection("companies").document(companyName)
                                            .collection("estimates").document(estimateId).get().await()
                                        val estimate = estimateDoc.toObject(Estimate::class.java)?.copy(id = estimateDoc.id)

                                        if (estimate == null || estimate.invoiceId != null) {
                                            Toast.makeText(context, "Cannot convert this estimate.", Toast.LENGTH_SHORT).show()
                                            return@launch
                                        }

                                        // 2. Reuse conversion logic (transaction + batch write)
                                        val companyRef = firestore.collection("companies").document(companyName)
                                        val newInvoiceRef = companyRef.collection("invoices").document()
                                        val counterRef = companyRef.collection("metadata").document("invoiceCounter")

                                        firestore.runTransaction { transaction ->
                                            val counterDoc = transaction.get(counterRef)
                                            val lastNumber = counterDoc.getLong("lastInvoiceNumber") ?: 9999L
                                            val newInvoiceNumber = lastNumber + 1

                                            val newInvoice = Invoice(
                                                id = newInvoiceRef.id,
                                                invoiceNumber = newInvoiceNumber.toString(),
                                                customerId = estimate.customerId,
                                                customerName = estimate.customerName,
                                                customerEmail = estimate.customerEmail,
                                                address = estimate.address,
                                                description = estimate.description,
                                                subtotal = estimate.subtotal,
                                                taxAmount = estimate.taxAmount,
                                                taxRate = estimate.taxRate,
                                                totalAmount = estimate.totalAmount,
                                                estimateId = estimate.id,
                                                creatorId = auth.currentUser?.uid ?: "",
                                                creatorName = loggedInUser ?: "Unknown"
                                            )
                                            transaction.set(newInvoiceRef, newInvoice)

                                            if(counterDoc.exists()) {
                                                transaction.update(counterRef, "lastInvoiceNumber", newInvoiceNumber)
                                            } else {
                                                transaction.set(counterRef, mapOf("lastInvoiceNumber" to newInvoiceNumber))
                                            }
                                            null
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

                                        // 3. Navigate to the invoices screen
                                        currentScreen = "invoices"
                                        viewingEstimateId = null // clear state

                                    } catch (e: Exception) {
                                        Log.e("CONVERSION", "Error converting from view screen: ${e.message}", e)
                                        Toast.makeText(context, "Conversion failed: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isConverting = false
                                    }
                                }
                            },
                                    onDeleteClick = { showDeleteEstimateDialog = true }
                        )
                    } ?: run {
                        Text("Error: No Estimate ID provided. Navigating back.")
                        LaunchedEffect(Unit) {
                            delay(2000)
                            currentScreen = "customers"
                        }
                    }
                }

                "viewInvoice" -> {
                    viewingInvoiceId?.let { invoiceId ->
                        ViewInvoiceScreen(
                            invoiceId = invoiceId,
                            firestore = firestore,
                            companyName = companyName,
                            onBack = { currentScreen = "viewCustomer" },
                            // --- NEW: Handle the edit button click ---
                            onEditClick = {
                                editingInvoiceId = invoiceId
                                currentScreen = "editInvoice"
                            },
                            // --- NEW: Handle the email button click ---
                            onEmailClick = { invoice, lineItems ->
                                scope.launch {
                                    try {
                                        val pdfData = PdfInvoiceData(
                                            invoice = invoice,
                                            lineItems = lineItems
                                        )
                                        generateAndEmailInvoice(context, pdfData)
                                    } catch (e: Exception) {
                                        Log.e("PDF_GEN_INVOICE", "Failed to generate/send PDF", e)
                                        Toast.makeText(
                                            context,
                                            "Error creating PDF: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
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
                "productsAndServices" -> {
                    if (companyName.isNotBlank()) {
                        ProductsAndServicesScreen(
                            onBackToMenu = { currentScreen = "mainMenu" },
                            firestore = firestore,
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
                            onBackToEstimates = { currentScreen = "estimates" },
                            onEstimateSaved = { currentScreen = "estimates" },
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
                            onBackToInvoices = { currentScreen = "invoices" },
                            onInvoiceSaved = { currentScreen = "invoices" },
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
            label = { Text("Time Clock") },
            selected = currentScreen == "timeClock",
            onClick = { onScreenSelected("timeClock") }
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
        }
        NavigationDrawerItem(
            label = { Text("Settings") },
            selected = currentScreen == "settings",
            onClick = { onScreenSelected("settings") }
        )
        // This Spacer takes up all remaining vertical space in the drawer's column,
        // pushing the item below it to the bottom.
        Spacer(modifier = Modifier.weight(1f))
        NavigationDrawerItem(
            label = { Text("Sign Out") },
            selected = false,
            onClick = onSignOut
        )
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

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.cap_construction),
            contentDescription = "Cap Construction Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Sign In", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }

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
            .padding(32.dp),
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
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val companiesRef = firestore.collection("companies")
                        val query = companiesRef.whereEqualTo("name", companyName.lowercase()).limit(1)
                        val companyDoc = query.get().await()

                        if (!companyDoc.isEmpty) {
                            Toast.makeText(context, "Company name already exists. Please choose another.", Toast.LENGTH_LONG).show()
                            return@launch
                        }

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    val user = auth.currentUser
                                    if (user == null) {
                                        Toast.makeText(context, "Registration failed: Could not get user.", Toast.LENGTH_LONG).show()
                                        return@addOnCompleteListener
                                    }

                                    val userData = hashMapOf(
                                        "uid" to user.uid,
                                        "firstName" to firstName,
                                        "lastName" to lastName,
                                        "email" to email,
                                        "role" to "Manager",
                                        "companyName" to companyName
                                    )

                                    firestore.collection("companies").document(companyName).set(mapOf("name" to companyName.lowercase()))
                                        .continueWithTask {
                                            firestore.collection("companies").document(companyName).collection("users").document(user.uid).set(userData)
                                        }
                                        .addOnCompleteListener { firestoreTask ->
                                            if (firestoreTask.isSuccessful) {
                                                Toast.makeText(context, "Registration successful! Please sign in.", Toast.LENGTH_LONG).show()
                                                onRegistrationSuccess()
                                            } else {
                                                Toast.makeText(context, "Firestore Error: ${firestoreTask.exception?.message}", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(context, "Registration Failed: ${authTask.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.e(AUTH_TAG, "Error checking for company", e)
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
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
    companyName: String
) {
    var developmentNotes by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isInEditMode by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Load notes from Firestore when the composable is first displayed
    LaunchedEffect(companyName) {
        if (companyName.isNotBlank()) {
            isLoading = true
            try {
                val doc = firestore.collection("companies").document(companyName).get().await()
                developmentNotes = doc.getString("developmentNotes") ?: ""
            } catch (e: Exception) {
                Log.e("MainMenuNotes", "Error loading development notes", e)
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Welcome, $username!", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.cap_construction),
            contentDescription = "Cap Construction Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        // This Spacer pushes the notes section to the bottom half
        Spacer(modifier = Modifier.weight(1f))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            // Title and Edit/Save button for the notes section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "App Development Ideas",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = {
                    if (isInEditMode) {
                        // If we were in edit mode, this click is the "Save" action
                        coroutineScope.launch {
                            try {
                                firestore.collection("companies").document(companyName)
                                    .set(
                                        mapOf("developmentNotes" to developmentNotes),
                                        SetOptions.merge()
                                    ).await()
                                Toast.makeText(context, "Notes Saved!", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Log.e("MainMenuNotes", "Error saving notes", e)
                                Toast.makeText(context, "Error saving notes.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    // Toggle edit mode state
                    isInEditMode = !isInEditMode
                }) {
                    Icon(
                        imageVector = if (isInEditMode) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = if (isInEditMode) "Save Notes" else "Edit Notes"
                    )
                }
            }

            OutlinedTextField(
                value = developmentNotes,
                onValueChange = { developmentNotes = it },
                readOnly = !isInEditMode, // Make text field read-only when not in edit mode
                placeholder = { Text("Jot down your ideas here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f) // Occupies half of the remaining space
            )
        }
    }
}
/**
 * Generates a list of 14-day, bi-weekly pay periods.
 * Each period starts on a Friday and ends on the second following Thursday.
 *
 * @return A list of start and end Timestamps for the current and three past pay periods.
 */
private fun generatePayPeriods(): List<Pair<Timestamp, Timestamp>> {
    val periods = mutableListOf<Pair<Timestamp, Timestamp>>()

    // A known Thursday that was the end of a pay period.
    // This acts as a fixed anchor to calculate all other periods.
    // You can change this to any real payday Thursday from the past.
    val referencePayday = LocalDate.of(2024, 1, 4)

    // 1. Find the next Thursday from today.
    var upcomingThursday = LocalDate.now()
    while (upcomingThursday.dayOfWeek != DayOfWeek.THURSDAY) {
        upcomingThursday = upcomingThursday.plusDays(1)
    }

    // 2. Check if this Thursday is a payday.
    // Paydays happen every two weeks. We check if the number of weeks
    // between our reference payday and the upcoming Thursday is an even number.
    val weeksBetween = ChronoUnit.WEEKS.between(referencePayday, upcomingThursday)
    if (weeksBetween % 2 != 0L) {
        // If it's an odd number of weeks, the real payday is next week's Thursday.
        upcomingThursday = upcomingThursday.plusWeeks(1)
    }

    // This is the correct end date for the pay period we are currently in.
    val currentPayPeriodEndDate = upcomingThursday

    // 3. Generate the current period and 3 past periods.
    for (i in 0..3) {
        // Calculate the end date for the current iteration by subtracting 2 weeks each time.
        val endDate = currentPayPeriodEndDate.minusWeeks(i * 2L)
        // The start date is 13 days before the end date (making a 14-day period inclusive).
        val startDate = endDate.minusDays(13)

        // Convert LocalDate to Timestamp, setting the time correctly.
        val startTimestamp = Timestamp(
            Date.from(
                startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            )
        )
        val endTimestamp = Timestamp(
            Date.from(
                endDate.atTime(23, 59, 59, 999_000_000)
                    .atZone(ZoneId.systemDefault()).toInstant()
            )
        )

        periods.add(Pair(startTimestamp, endTimestamp))
    }

    // The list is already generated in descending order (newest first).
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
                        documentSnapshot.toObject(FirestoreTask::class.java)?.copy(id = documentSnapshot.id)
                    }
                        // 2. Filter in client-side code for tasks that END on or after the start of the selected day
                        .filter { task ->
                            // If endDate is null, assume it's a single-day task using startDate
                            val taskEndDate = task.endDate?.toDate()?.time ?: task.startDate!!.toDate().time
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
            // --- THIS IS THE FIX ---
            // Use the smart-cast local variable here, removing the need for '!!'
            message = "Are you sure you want to delete the task for ${currentTaskToDelete.customerName}?",
            onConfirm = {
                coroutineScope.launch {
                    try {
                        firestore.collection("companies").document(companyName)
                            .collection("tasks").document(currentTaskToDelete.id) // Also use it here
                            .delete().await()

                        Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                        onRefresh() // Trigger a data refresh
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                    }
                }
            },
            onDismiss = {
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

    var isStatusInEditMode by rememberSaveable { mutableStateOf(false) }
    var editedStatus by rememberSaveable { mutableStateOf(task.status) }
    var isStatusDropdownExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Unassigned", "Assigned", "In Progress", "Completed", "Canceled")

    // --- STATE: Customer Info ---
    var customerPhoneNumber by remember { mutableStateOf<String?>(null) }

    // --- STATE: Files & Photos (Local Mutable State for Immediate UI Updates) ---
    var isUploading by remember { mutableStateOf(false) }
    // Initialize with task data, but allow local modification
    var imageUrls by remember { mutableStateOf(task.imageUrls) }
    var jobDocuments by remember { mutableStateOf(task.documents) }

    // --- STATE: Deletion & Viewing ---
    var selectedImageUrl by remember { mutableStateOf<String?>(null) } // For full screen view
    var photoUrlToDelete by remember { mutableStateOf<String?>(null) }
    var documentToDelete by remember { mutableStateOf<JobDocument?>(null) }

    // --- HELPER: Delete Logic ---
    fun deleteFile(url: String, isDocument: Boolean, docObject: JobDocument?) {
        coroutineScope.launch {
            try {
                // 1. Delete from Firebase Storage (The actual file)
                val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance().getReferenceFromUrl(url)
                storageRef.delete().await()

                // 2. Remove reference from Firestore
                val taskRef = firestore.collection("companies").document(companyName)
                    .collection("tasks").document(task.id)

                if (isDocument && docObject != null) {
                    taskRef.update("documents", FieldValue.arrayRemove(docObject)).await()
                    // 3. Update Local State
                    jobDocuments = jobDocuments.filter { it != docObject }
                } else {
                    taskRef.update("imageUrls", FieldValue.arrayRemove(url)).await()
                    // 3. Update Local State
                    imageUrls = imageUrls.filter { it != url }
                }

                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("DeleteFile", "Error deleting file", e)
                Toast.makeText(context, "Error deleting: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- LAUNCHER: Document Picker ---
    val docPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                isUploading = true
                coroutineScope.launch {
                    try {
                        val fileName = getFileName(context, it)
                        val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                        val safeTaskId = task.id.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }

                        // Upload to Storage
                        val storageRef = Firebase.storage.reference
                            .child("companies/$safeCompany/tasks/$safeTaskId/docs/$fileName")
                        storageRef.putFile(it).await()
                        val downloadUrl = storageRef.downloadUrl.await().toString()

                        // Update Firestore
                        val newDoc = JobDocument(name = fileName, url = downloadUrl)
                        val taskRef = firestore.collection("companies").document(companyName)
                            .collection("tasks").document(task.id)
                        taskRef.update("documents", FieldValue.arrayUnion(newDoc)).await()

                        // Update Local State
                        jobDocuments = jobDocuments + newDoc
                        Toast.makeText(context, "Document uploaded!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("DocUpload", "Error", e)
                        Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isUploading = false
                    }
                }
            }
        }
    )

    // --- LAUNCHER: Photo Picker ---
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                isUploading = true
                coroutineScope.launch {
                    try {
                        val safeCompany = companyName.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                        val safeTaskId = task.id.filter { c -> c.isLetterOrDigit() }.ifBlank { "unknown" }
                        val imageFileName = "${UUID.randomUUID()}.jpg"

                        val storageRef = Firebase.storage.reference
                            .child("companies/$safeCompany/tasks/$safeTaskId/$imageFileName")
                        storageRef.putFile(it).await()
                        val downloadUrl = storageRef.downloadUrl.await().toString()

                        val taskRef = firestore.collection("companies").document(companyName)
                            .collection("tasks").document(task.id)
                        taskRef.update("imageUrls", FieldValue.arrayUnion(downloadUrl)).await()

                        imageUrls = imageUrls + downloadUrl
                        Toast.makeText(context, "Photo uploaded!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("PhotoUpload", "Error", e)
                        Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isUploading = false
                    }
                }
            }
        }
    )

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
                estimates = estimatesDeferred.await().documents.mapNotNull { it.toObject(Estimate::class.java)?.copy(id = it.id) }
                invoices = invoicesDeferred.await().documents.mapNotNull { it.toObject(Invoice::class.java)?.copy(id = it.id) }
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

    // --- UI CONTENT ---
    if (selectedImageUrl != null) {
        FullScreenImageDialog(imageUrl = selectedImageUrl!!, onDismiss = { selectedImageUrl = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                }
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
                    catch (e: Exception) { Toast.makeText(context, "Cannot open dialer", Toast.LENGTH_SHORT).show() }
                })
            }

            // 2. STATUS EDIT
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Status", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    if (isStatusInEditMode) {
                        ExposedDropdownMenuBox(
                            expanded = isStatusDropdownExpanded,
                            onExpandedChange = { isStatusDropdownExpanded = !isStatusDropdownExpanded }
                        ) {
                            TextField(
                                value = editedStatus, onValueChange = {}, readOnly = true,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStatusDropdownExpanded) }
                            )
                            ExposedDropdownMenu(
                                expanded = isStatusDropdownExpanded,
                                onDismissRequest = { isStatusDropdownExpanded = false }
                            ) {
                                statusOptions.forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status) },
                                        onClick = { editedStatus = status; isStatusDropdownExpanded = false }
                                    )
                                }
                            }
                        }
                    } else {
                        Text(task.status, style = MaterialTheme.typography.titleMedium)
                    }
                }
                IconButton(onClick = {
                    if (isStatusInEditMode) {
                        if (editedStatus != task.status) {
                            coroutineScope.launch {
                                try {
                                    firestore.collection("companies").document(companyName)
                                        .collection("tasks").document(task.id).update("status", editedStatus).await()
                                    onStatusUpdated(editedStatus)
                                    isStatusInEditMode = false
                                    Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) { Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
                            }
                        } else { isStatusInEditMode = false }
                    } else { isStatusInEditMode = true }
                }) {
                    Icon(if (isStatusInEditMode) Icons.Default.Check else Icons.Default.Edit, contentDescription = "Edit Status")
                }
            }

            HorizontalDivider()
            DetailItem(label = "Scheduled Date", value = displayDate)
            DetailItem(label = "Address", value = task.address, onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "geo:0,0?q=${Uri.encode(task.address)}".toUri())
                intent.setPackage("com.google.android.apps.maps")
                try { context.startActivity(intent) } catch (e: Exception) { Toast.makeText(context, "Maps not available", Toast.LENGTH_SHORT).show() }
            })

            HorizontalDivider()

            // 3. DOCUMENTS SECTION
            Text("Documents", style = MaterialTheme.typography.titleLarge)
            if (jobDocuments.isEmpty()) {
                Text("No documents attached.", color = Color.Gray)
            } else {
                jobDocuments.forEach { doc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(doc.url)
                                        )
                                    )
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No app found to open file.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, "File", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(12.dp))
                            Text(doc.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            IconButton(onClick = { documentToDelete = doc }) {
                                Icon(Icons.Default.Delete, "Delete", tint = Color.Red)
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = { docPickerLauncher.launch("*/*") },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isUploading) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else {
                    Icon(Icons.Default.AttachFile, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Attach Document")
                }
            }

            // 4. PHOTOS SECTION
            Text("Photos", style = MaterialTheme.typography.titleLarge)
            if (imageUrls.isEmpty()) {
                Text("No photos added.", color = Color.Gray)
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(imageUrls) { url ->
                        Box(modifier = Modifier.size(100.dp)) {
                            Card(
                                onClick = { selectedImageUrl = url },
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.matchParentSize()
                            ) {
                                AsyncImage(
                                    model = url, contentDescription = "Job Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            IconButton(
                                onClick = { photoUrlToDelete = url },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isUploading) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else {
                    Icon(Icons.Default.AddAPhoto, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add Job Photo")
                }
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

            // 6. INTERNAL NOTES
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Internal Notes", style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = {
                    if (isInEditMode) {
                        coroutineScope.launch {
                            try {
                                firestore.collection("companies").document(companyName)
                                    .collection("tasks").document(task.id).update("notes", editedNotes).await()
                                onNotesUpdated(editedNotes)
                                isInEditMode = false
                                Toast.makeText(context, "Notes saved", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) { Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show() }
                        }
                    } else { isInEditMode = true }
                }) {
                    Icon(if (isInEditMode) Icons.Default.Check else Icons.Default.Edit, contentDescription = "Edit Notes")
                }
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
                confirmButton = {
                    Button(onClick = {
                        deleteFile(documentToDelete!!.url, true, documentToDelete)
                        documentToDelete = null
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Delete") }
                },
                dismissButton = { TextButton(onClick = { documentToDelete = null }) { Text("Cancel") } }
            )
        }
        if (photoUrlToDelete != null) {
            AlertDialog(
                onDismissRequest = { photoUrlToDelete = null },
                title = { Text("Delete Photo?") },
                text = { Text("Are you sure you want to delete this photo?") },
                confirmButton = {
                    Button(onClick = {
                        deleteFile(photoUrlToDelete!!, false, null)
                        photoUrlToDelete = null
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Delete") }
                },
                dismissButton = { TextButton(onClick = { photoUrlToDelete = null }) { Text("Cancel") } }
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
                "JobTracker/${title}"
            )
            .setAllowedOverMetered(true)

        downloadManager.enqueue(request)
        Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to start download: ${e.message}", Toast.LENGTH_LONG).show()
        Log.e("ImageDownload", "Error starting download", e)
    }
}
@Composable
fun FullScreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Full screen job photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onDismiss() },
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = {
                    val fileName = "JobImage_${System.currentTimeMillis()}.jpg"
                    downloadImage(
                        context = context,
                        imageUrl = imageUrl,
                        title = fileName,
                        description = "Downloading job photo..."
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Download,
                    contentDescription = "Download Photo",
                    tint = MaterialTheme.colorScheme.onSurface
                )
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
                Modifier.clickable { onClick?.invoke() }
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
                .addOnFailureListener { e -> Log.w(TAG, "Error getting estimates.", e) }
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
                title = { Text(stringResource(R.string.estimates)) },
                navigationIcon = {
                    IconButton(onClick = onBackToMenu) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNewEstimateClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_estimate))
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
            Spacer(modifier = Modifier.height(16.dp))

            if (estimateList.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_estimates_found), color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(estimateList, key = { it.id }) { estimate ->
                        EstimateCard(
                            estimate = estimate,
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
                                        firestore.runTransaction { transaction ->
                                            val counterDoc = transaction.get(counterRef)
                                            val lastNumber = counterDoc.getLong("lastInvoiceNumber") ?: 9999L
                                            val newInvoiceNumber = lastNumber + 1

                                            val newInvoice = Invoice(
                                                id = newInvoiceRef.id,
                                                invoiceNumber = newInvoiceNumber.toString(),
                                                customerId = estimate.customerId,
                                                customerName = estimate.customerName,
                                                customerEmail = estimate.customerEmail,
                                                address = estimate.address,
                                                description = estimate.description,
                                                subtotal = estimate.subtotal,
                                                taxAmount = estimate.taxAmount,
                                                taxRate = estimate.taxRate,
                                                totalAmount = estimate.totalAmount,
                                                estimateId = estimate.id,
                                                creatorId = currentUserId,
                                                creatorName = currentUserName
                                            )
                                            transaction.set(newInvoiceRef, newInvoice)

                                            if(counterDoc.exists()) {
                                                transaction.update(counterRef, "lastInvoiceNumber", newInvoiceNumber)
                                            } else {
                                                transaction.set(counterRef, mapOf("lastInvoiceNumber" to newInvoiceNumber))
                                            }
                                            null
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
                                        onEstimateConverted()
                                        refreshTrigger++
                                    } catch (e: Exception) {
                                        Log.e("CONVERSION", "Error: ${e.message}")
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
    // --- STATE MANAGEMENT ---
    var customerName by rememberSaveable { mutableStateOf("") }
    var customerEmail by rememberSaveable { mutableStateOf("") }
    var addressText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var estimateNumber by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val lineItems = rememberSaveable(saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })) { mutableStateListOf<EstimateLineItem>() }

    // --- NEW: State for the tax checkbox ---
    var isTaxable by rememberSaveable { mutableStateOf(true) }

    // --- State for tax calculation ---
    var taxRate by rememberSaveable { mutableStateOf(0.0) }

    var productSearchQuery by rememberSaveable { mutableStateOf("") }
    var isProductDropdownExpanded by remember { mutableStateOf(false) }
    var allProductsAndServices by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
    val productFocusRequester = remember { FocusRequester() }

    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
        disabledContainerColor = Color(0xFFF0F0F0),
        focusedTextColor = Color.Black, unfocusedTextColor = Color.Black
    )

    // --- DATA FETCHING ---
    LaunchedEffect(estimateId) {
        if (companyName.isNotBlank()) {
            coroutineScope.launch {
                try {
                    val estimateRef = firestore.collection("companies").document(companyName)
                        .collection("estimates").document(estimateId)

                    val estimateDoc = estimateRef.get().await()
                    val estimateData = estimateDoc.toObject(Estimate::class.java)
                    if (estimateData != null) {
                        customerName = estimateData.customerName
                        customerEmail = estimateData.customerEmail
                        addressText = estimateData.address
                        description = estimateData.description
                        estimateNumber = estimateData.estimateNumber
                        taxRate = estimateData.taxRate
                        // --- NEW: Initialize checkbox based on loaded data ---
                        // If tax amount was > 0, the box should be checked.
                        isTaxable = estimateData.taxAmount > 0
                    }

                    val lineItemsSnapshot = estimateRef.collection("lineItems").get().await()
                    lineItems.addAll(lineItemsSnapshot.documents.mapNotNull {
                        it.toObject(EstimateLineItem::class.java)?.copy(id = it.id)
                    })

                    firestore.collection("companies").document(companyName)
                        .collection("productsAndServices").orderBy("name").get()
                        .addOnSuccessListener { result ->
                            allProductsAndServices = result.documents.mapNotNull {
                                it.toObject(ProductOrService::class.java)?.copy(id = it.id)
                            }
                        }
                } catch (e: Exception) {
                    Log.e("FIRESTORE_EDIT_ESTIMATE", "Error fetching estimate data", e)
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    // --- MODIFIED: Automatic tax recalculation ---
    val subtotal = remember(lineItems.toList()) {
        lineItems.sumOf { it.quantity * it.unitPrice }
    }
    // If taxable, use the rate from the estimate; otherwise, use 0.
    val appliedTaxRate = if (isTaxable) taxRate else 0.0
    val taxAmount = remember(subtotal, appliedTaxRate) {
        (subtotal * (appliedTaxRate / 100.0) * 100).toLong() / 100.0
    }
    val totalAmount = remember(subtotal, taxAmount) {
        subtotal + taxAmount
    }

    val productSearchResults = remember(allProductsAndServices, productSearchQuery) {
        if (productSearchQuery.isBlank()) {
            emptyList()
        } else {
            allProductsAndServices.filter { product ->
                product.name.contains(productSearchQuery, ignoreCase = true) ||
                        product.description.contains(productSearchQuery, ignoreCase = true)
            }
        }
    }
    LaunchedEffect(productSearchResults) {
        if (productSearchResults.isNotEmpty()) productFocusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Estimate") },
                navigationIcon = { IconButton(onClick = onBackToEstimates) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)) {

                // Header fields...
                TextField(value = customerName, onValueChange = {}, readOnly = true, label = { Text(stringResource(R.string.customer)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = customerEmail, onValueChange = {}, readOnly = true, label = { Text("Customer Email") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = addressText, onValueChange = { addressText = it }, label = { Text(stringResource(R.string.address)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.general_description)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = estimateNumber, onValueChange = {}, readOnly = true, label = { Text(stringResource(R.string.estimate_number)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text(stringResource(R.string.line_items), style = MaterialTheme.typography.titleLarge)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(lineItems) { index, item ->
                        LineItemRow(item = item, onItemChange = { updatedItem -> lineItems[index] = updatedItem }, onRemoveClick = { lineItems.removeAt(index) })
                    }
                    item {
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
                                        .menuAnchor()
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
                                                    lineItems.add(EstimateLineItem(name = product.name, description = product.description, quantity = 1.0, unitPrice = product.price))
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
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                // --- MODIFIED: Totals section with Checkbox ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Subtotal:", textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        Text("$%.2f".format(subtotal))
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isTaxable,
                            onCheckedChange = { isTaxable = it }
                        )
                        Text("Tax (${taxRate}%):", textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        Text("$%.2f".format(taxAmount))
                    }
                    HorizontalDivider(modifier = Modifier.width(150.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        Text(
                            "$%.2f".format(totalAmount),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!isSaving) {
                            isSaving = true
                            coroutineScope.launch {
                                try {
                                    val estimateRef = firestore.collection("companies").document(companyName).collection("estimates").document(estimateId)
                                    val batch = firestore.batch()

                                    // --- MODIFIED: Use appliedTaxRate when saving ---
                                    val updatedEstimateData = mapOf(
                                        "address" to addressText,
                                        "description" to description,
                                        "subtotal" to subtotal,
                                        "taxRate" to appliedTaxRate,
                                        "taxAmount" to taxAmount,
                                        "totalAmount" to totalAmount
                                    )
                                    batch.update(estimateRef, updatedEstimateData)

                                    val oldLineItems = estimateRef.collection("lineItems").get().await()
                                    oldLineItems.documents.forEach { doc -> batch.delete(doc.reference) }

                                    lineItems.forEach { lineItem ->
                                        if (lineItem.name.isNotBlank()) {
                                            val newLineItemRef = estimateRef.collection("lineItems").document()
                                            batch.set(newLineItemRef, lineItem)
                                        }
                                    }

                                    batch.commit().await()
                                    Toast.makeText(context, "Estimate updated!", Toast.LENGTH_SHORT).show()
                                    onEstimateSaved()
                                } catch (e: Exception) {
                                    Log.e("FIRESTORE_UPDATE_ESTIMATE", "Error updating estimate", e)
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    isSaving = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSaving
                ) {
                    if (isSaving) { CircularProgressIndicator(modifier = Modifier.height(24.dp)) } else { Text(stringResource(R.string.save)) }
                }
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

    // --- NEW: State for the tax checkbox ---
    var isTaxable by rememberSaveable { mutableStateOf(true) }

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
    val appliedTaxRate = if (isTaxable) companyTaxRate else 0.0
    val subtotal = remember(lineItems.toList()) {
        lineItems.sumOf { it.quantity * it.unitPrice }
    }
    val taxAmount = remember(subtotal, appliedTaxRate) {
        (subtotal * (appliedTaxRate / 100.0) * 100).toLong() / 100.0
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
                        .menuAnchor()
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
                        .menuAnchor()
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
            TextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.general_description)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
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
                            .menuAnchor()
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
                                        lineItems.add(EstimateLineItem(name = product.name, description = product.description, quantity = 1.0, unitPrice = product.price))
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTaxable,
                        onCheckedChange = { isTaxable = it }
                    )
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
                            // ... (transaction logic remains the same)
                            val companyRef = firestore.collection("companies").document(companyName)
                            val newEstimateRef = companyRef.collection("estimates").document()
                            val counterRef = companyRef.collection("metadata").document("estimateCounter")
                            try {
                                firestore.runTransaction { transaction ->
                                    val counterDoc = transaction.get(counterRef)
                                    val lastNumber = counterDoc.getLong("lastEstimateNumber") ?: 9999L
                                    val newEstimateNumber = lastNumber + 1

                                    // --- MODIFIED: Use appliedTaxRate when saving ---
                                    val newEstimate = Estimate(
                                        id = newEstimateRef.id,
                                        estimateNumber = newEstimateNumber.toString(),
                                        customerId = selectedCustomer!!.id,
                                        customerName = selectedCustomer!!.name,
                                        customerEmail = customerEmail,
                                        address = addressText,
                                        description = description,
                                        totalAmount = totalAmount,
                                        taxRate = appliedTaxRate, // Save the applied rate
                                        taxAmount = taxAmount,
                                        subtotal = subtotal,
                                        creatorId = currentUserId,
                                        creatorName = currentUserName
                                    )
                                    transaction.set(newEstimateRef, newEstimate)

                                    if (counterDoc.exists()) {
                                        transaction.update(counterRef, "lastEstimateNumber", newEstimateNumber)
                                    } else {
                                        transaction.set(counterRef, mapOf("lastEstimateNumber" to newEstimateNumber))
                                    }
                                    null
                                }.await()

                                val batch = firestore.batch()
                                val lineItemsCollection = newEstimateRef.collection("lineItems")
                                lineItems.forEach { lineItem ->
                                    if (lineItem.name.isNotBlank()) {
                                        val newLineItemRef = lineItemsCollection.document()
                                        batch.set(newLineItemRef, lineItem)
                                    }
                                }
                                batch.commit().await()

                                Toast.makeText(context, "Estimate saved!", Toast.LENGTH_SHORT).show()
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
            @Composable
            fun EstimateCard(
                estimate: Estimate,
                onEditClick: () -> Unit,
                onDeleteClick: () -> Unit,
                onConvertToInvoiceClick: () -> Unit,
                // Add parameters to access Firestore
                firestore: FirebaseFirestore,
                companyName: String
            ) {
                var expanded by remember { mutableStateOf(false) }
                val context = LocalContext.current // Correct way to get context
                val coroutineScope = rememberCoroutineScope()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .padding(end = 40.dp), // Space for the menu icon
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // ... (Your existing UI for displaying estimate info)
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
                                    color = Color.Black
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
                                    text = estimate.status, style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold, color = Color.Black
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
                                    enabled = estimate.invoiceId == null // Disable if already converted
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete", color = Color.Black) },
                                    onClick = { expanded = false; onDeleteClick() }
                                )
                                // --- FIXED EMAIL MENU ITEM ---
                                DropdownMenuItem(
                                    text = { Text("Send Email", color = Color.Black) },
                                    onClick = {
                                        expanded = false
                                        coroutineScope.launch {
                                            try {
                                                // Fetch line items from the subcollection
                                                val lineItemsSnapshot = firestore.collection("companies").document(companyName)
                                                    .collection("estimates").document(estimate.id)
                                                    .collection("lineItems").get().await()

                                                val lineItems = lineItemsSnapshot.toObjects(EstimateLineItem::class.java)

                                                // Create the data bundle
                                                val pdfData = PdfEstimateData(
                                                    estimate = estimate,
                                                    lineItems = lineItems
                                                )

                                                // Call the new, powerful PDF generator
                                                generateAndEmailEstimate(context, pdfData)

                                            } catch (e: Exception) {
                                                Log.e("PDF_GEN", "Failed to generate estimate PDF", e)
                                                Toast.makeText(context, "Error: Could not generate PDF.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
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
    // STATE FOR THE INVOICE HEADER
    var customerSearchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCustomer by remember { mutableStateOf<CustomerInfo?>(null) }
    var customerEmail by rememberSaveable { mutableStateOf("") }
    var addressText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var isCustomerDropdownExpanded by remember { mutableStateOf(false) }
    var customerSearchResults by remember { mutableStateOf<List<CustomerInfo>>(emptyList()) }
    val customerFocusRequester = remember { FocusRequester() }

    // STATE FOR ADDRESS DROPDOWN
    var addressOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isAddressDropdownExpanded by remember { mutableStateOf(false) }

    // --- NEW: State for the tax checkbox ---
    var isTaxable by rememberSaveable { mutableStateOf(true) }

    // Invoice-specific state
    val defaultDueDate = remember { Calendar.getInstance().apply { add(Calendar.DATE, 30) }.time }
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    var dueDateText by rememberSaveable { mutableStateOf(dateFormat.format(defaultDueDate)) }
    var companyTaxRate by remember { mutableStateOf(0.0) }

    // STATE FOR LINE ITEMS & PRODUCT SEARCH
    val lineItems = rememberSaveable(saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() })) { mutableStateListOf<EstimateLineItem>() }
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

    // Handles customer search
    LaunchedEffect(customerSearchQuery) {
        if (customerSearchQuery.isNotBlank() && selectedCustomer == null) {
            delay(300)
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

    // DATA FETCHING (TAX RATE & PRODUCTS)
    LaunchedEffect(Unit) {
        customerFocusRequester.requestFocus()
        if (companyName.isNotBlank()) {
            firestore.collection("companies").document(companyName).get()
                .addOnSuccessListener { doc -> companyTaxRate = doc.getDouble("taxRate") ?: 0.0 }

            firestore.collection("companies").document(companyName)
                .collection("productsAndServices").orderBy("name").get()
                .addOnSuccessListener { result ->
                    allProductsAndServices = result.documents.mapNotNull {
                        it.toObject(ProductOrService::class.java)?.copy(id = it.id)
                    }
                }
        }
    }
    LaunchedEffect(taskToPrePopulate) {
        if (taskToPrePopulate != null) {
            description = taskToPrePopulate.jobDescription
            addressText = taskToPrePopulate.address

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
    val appliedTaxRate = if (isTaxable) companyTaxRate else 0.0
    val subtotal = remember(lineItems.toList()) { lineItems.sumOf { it.quantity * it.unitPrice } }
    val taxAmount = remember(subtotal, appliedTaxRate) { (subtotal * (appliedTaxRate / 100.0) * 100).toLong() / 100.0 }
    val totalAmount = remember(subtotal, taxAmount) { subtotal + taxAmount }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_invoice)) },
                navigationIcon = {
                    IconButton(onClick = onBackToInvoices) {
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
            // Header fields...
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
                        .menuAnchor()
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
            TextField(value = customerEmail, onValueChange = {}, label = { Text("Customer Email") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, readOnly = true)
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = isAddressDropdownExpanded,
                onExpandedChange = { if (addressOptions.size > 1) { isAddressDropdownExpanded = !isAddressDropdownExpanded } }
            ) {
                TextField(
                    value = addressText,
                    onValueChange = { addressText = it },
                    readOnly = addressOptions.isNotEmpty(),
                    label = { Text(stringResource(R.string.address)) },
                    trailingIcon = { if (addressOptions.size > 1) { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAddressDropdownExpanded) } },
                    colors = textFieldColors,
                    modifier = Modifier
                        .menuAnchor()
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
            TextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.general_description)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(Modifier.height(8.dp))
            TextField(value = "", onValueChange = {}, readOnly = true, enabled = false, label = { Text("Invoice Number (auto-generated)") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(Modifier.height(8.dp))
            TextField(value = dueDateText, onValueChange = { dueDateText = it }, label = { Text(stringResource(R.string.due_date)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            Text(stringResource(R.string.line_items), style = MaterialTheme.typography.titleLarge)

            lineItems.forEachIndexed { index, item ->
                LineItemRow(item = item, onItemChange = { updatedItem -> lineItems[index] = updatedItem }, onRemoveClick = { lineItems.removeAt(index) })
            }

            Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(expanded = isProductDropdownExpanded, onExpandedChange = { isProductDropdownExpanded = !it }) {
                    TextField(
                        value = productSearchQuery,
                        onValueChange = { productSearchQuery = it; isProductDropdownExpanded = it.isNotBlank() },
                        label = { Text(stringResource(R.string.search_products_and_services)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
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
                                        lineItems.add(EstimateLineItem(name = product.name, description = product.description, quantity = 1.0, unitPrice = product.price))
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTaxable,
                        onCheckedChange = { isTaxable = it }
                    )
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
                            val newInvoiceRef = companyRef.collection("invoices").document()
                            val counterRef = companyRef.collection("metadata").document("invoiceCounter")
                            try {
                                val parsedDueDate = try { dateFormat.parse(dueDateText) } catch (e: Exception) { defaultDueDate }

                                firestore.runTransaction { transaction ->
                                    val counterDoc = transaction.get(counterRef)
                                    val lastNumber = counterDoc.getLong("lastInvoiceNumber") ?: 9999L
                                    val newInvoiceNumber = lastNumber + 1

                                    // --- MODIFIED: Use appliedTaxRate when saving ---
                                    val newInvoice = Invoice(
                                        id = newInvoiceRef.id,
                                        invoiceNumber = newInvoiceNumber.toString(),
                                        customerId = selectedCustomer!!.id,
                                        customerName = selectedCustomer!!.name,
                                        customerEmail = customerEmail,
                                        address = addressText,
                                        description = description,
                                        dueDate = Timestamp(parsedDueDate ?: defaultDueDate),
                                        subtotal = subtotal,
                                        taxAmount = taxAmount,
                                        taxRate = appliedTaxRate, // Save the applied rate
                                        totalAmount = totalAmount,
                                        creatorId = currentUserId,
                                        creatorName = currentUserName
                                    )
                                    transaction.set(newInvoiceRef, newInvoice)

                                    if(counterDoc.exists()){
                                        transaction.update(counterRef, "lastInvoiceNumber", newInvoiceNumber)
                                    } else {
                                        transaction.set(counterRef, mapOf("lastInvoiceNumber" to newInvoiceNumber))
                                    }
                                    null
                                }.await()

                                val batch = firestore.batch()
                                val lineItemsCollection = newInvoiceRef.collection("lineItems")
                                lineItems.forEach { lineItem ->
                                    if (lineItem.name.isNotBlank()) {
                                        val newLineItemRef = lineItemsCollection.document()
                                        batch.set(newLineItemRef, lineItem)
                                    }
                                }
                                batch.commit().await()
                                Toast.makeText(context, "Invoice saved!", Toast.LENGTH_SHORT).show()
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
                if (isSaving) { CircularProgressIndicator(modifier = Modifier.height(24.dp)) } else { Text(stringResource(R.string.save)) }
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
    // --- STATE MANAGEMENT ---
    var customerName by rememberSaveable { mutableStateOf("") }
    var addressText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var invoiceNumber by rememberSaveable { mutableStateOf("") }
    var dueDateText by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val lineItems = rememberSaveable( saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() }) ) { mutableStateListOf<EstimateLineItem>() }

    // --- NEW: State for the tax checkbox ---
    var isTaxable by rememberSaveable { mutableStateOf(true) }

    // --- State for tax calculation ---
    var taxRate by rememberSaveable { mutableStateOf(0.0) }

    var productSearchQuery by rememberSaveable { mutableStateOf("") }
    var isProductDropdownExpanded by remember { mutableStateOf(false) }
    var allProductsAndServices by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
    val productFocusRequester = remember { FocusRequester() }

    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
        disabledContainerColor = Color(0xFFF0F0F0),
        focusedTextColor = Color.Black, unfocusedTextColor = Color.Black
    )
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    // --- DATA FETCHING ---
    LaunchedEffect(invoiceId) {
        if (companyName.isNotBlank()) {
            coroutineScope.launch {
                try {
                    val invoiceRef = firestore.collection("companies").document(companyName)
                        .collection("invoices").document(invoiceId)

                    val invoiceDoc = invoiceRef.get().await()
                    val invoiceData = invoiceDoc.toObject(Invoice::class.java)
                    if (invoiceData != null) {
                        customerName = invoiceData.customerName
                        addressText = invoiceData.address
                        description = invoiceData.description
                        invoiceNumber = invoiceData.invoiceNumber
                        dueDateText = dateFormat.format(invoiceData.dueDate.toDate())
                        taxRate = invoiceData.taxRate
                        // --- NEW: Initialize checkbox based on loaded data ---
                        isTaxable = invoiceData.taxAmount > 0
                    }

                    val lineItemsSnapshot = invoiceRef.collection("lineItems").get().await()
                    lineItems.addAll(lineItemsSnapshot.documents.mapNotNull {
                        it.toObject(EstimateLineItem::class.java)?.copy(id = it.id)
                    })

                    firestore.collection("companies").document(companyName)
                        .collection("productsAndServices").orderBy("name").get()
                        .addOnSuccessListener { result ->
                            allProductsAndServices = result.documents.mapNotNull {
                                it.toObject(ProductOrService::class.java)?.copy(id = it.id)
                            }
                        }
                } catch (e: Exception) {
                    Log.e("FIRESTORE_EDIT_INVOICE", "Error fetching invoice data", e)
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    // --- MODIFIED: Automatic tax recalculation ---
    val subtotal = remember(lineItems.toList()) {
        lineItems.sumOf { it.quantity * it.unitPrice }
    }
    val appliedTaxRate = if (isTaxable) taxRate else 0.0
    val taxAmount = remember(subtotal, appliedTaxRate) {
        (subtotal * (appliedTaxRate / 100.0) * 100).toLong() / 100.0
    }
    val totalAmount = remember(subtotal, taxAmount) {
        subtotal + taxAmount
    }

    val productSearchResults = remember(allProductsAndServices, productSearchQuery) {
        if (productSearchQuery.isBlank()) {
            emptyList()
        } else {
            allProductsAndServices.filter { product ->
                product.name.contains(productSearchQuery, ignoreCase = true) ||
                        product.description.contains(productSearchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Invoice") },
                navigationIcon = { IconButton(onClick = onBackToInvoices) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)) {
                // Header fields...
                TextField(value = customerName, onValueChange = {}, readOnly = true, label = { Text(stringResource(R.string.customer)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = addressText, onValueChange = { addressText = it }, label = { Text(stringResource(R.string.address)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.general_description)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = invoiceNumber, onValueChange = {}, readOnly = true, label = { Text(stringResource(R.string.invoice_number)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                Spacer(Modifier.height(8.dp))
                TextField(value = dueDateText, onValueChange = { dueDateText = it }, label = { Text(stringResource(R.string.due_date)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text(stringResource(R.string.line_items), style = MaterialTheme.typography.titleLarge)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(lineItems) { index, item ->
                        LineItemRow(item = item, onItemChange = { updatedItem -> lineItems[index] = updatedItem }, onRemoveClick = { lineItems.removeAt(index) })
                    }
                    item {
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
                                        .menuAnchor()
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
                                                    lineItems.add(EstimateLineItem(name = product.name, description = product.description, quantity = 1.0, unitPrice = product.price))
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
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                // --- MODIFIED: Totals section with Checkbox ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Subtotal:", textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        Text("$%.2f".format(subtotal))
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isTaxable,
                            onCheckedChange = { isTaxable = it }
                        )
                        Text("Tax (${taxRate}%):", textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        Text("$%.2f".format(taxAmount))
                    }
                    HorizontalDivider(modifier = Modifier.width(150.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        Text(
                            "$%.2f".format(totalAmount),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!isSaving) {
                            isSaving = true
                            coroutineScope.launch {
                                try {
                                    val invoiceRef = firestore.collection("companies").document(companyName)
                                        .collection("invoices").document(invoiceId)
                                    val batch = firestore.batch()

                                    val parsedDueDate = try { dateFormat.parse(dueDateText) } catch (e: Exception) { Date() }

                                    // --- MODIFIED: Use appliedTaxRate when saving ---
                                    val updatedInvoiceData = mapOf(
                                        "address" to addressText,
                                        "description" to description,
                                        "dueDate" to Timestamp(parsedDueDate ?: Date()),
                                        "subtotal" to subtotal,
                                        "taxRate" to appliedTaxRate,
                                        "taxAmount" to taxAmount,
                                        "totalAmount" to totalAmount
                                    )
                                    batch.update(invoiceRef, updatedInvoiceData)

                                    val oldLineItems = invoiceRef.collection("lineItems").get().await()
                                    oldLineItems.documents.forEach { doc -> batch.delete(doc.reference) }

                                    lineItems.forEach { lineItem ->
                                        if (lineItem.name.isNotBlank()) {
                                            val newLineItemRef = invoiceRef.collection("lineItems").document()
                                            batch.set(newLineItemRef, lineItem)
                                        }
                                    }

                                    batch.commit().await()
                                    Toast.makeText(context, "Invoice updated!", Toast.LENGTH_SHORT).show()
                                    onInvoiceSaved()
                                } catch (e: Exception) {
                                    Log.e("FIRESTORE_UPDATE_INVOICE", "Error updating invoice", e)
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    isSaving = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSaving
                ) {
                    if (isSaving) { CircularProgressIndicator(modifier = Modifier.height(24.dp)) }
                    else { Text(stringResource(R.string.save)) }
                }
            }
        }
    }
}
            @Composable fun LineItemRow(
                item: EstimateLineItem,
                onItemChange: (EstimateLineItem) -> Unit,
                onRemoveClick: () -> Unit ) { // --- Define the TextField colors for consistency ---
                var quantityText by remember(item.quantity) {
                    mutableStateOf(item.quantity.toString())
                }
                val textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onRemoveClick) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.remove_item))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        TextField(
                            value = item.name,
                            onValueChange = { newItemName -> onItemChange(item.copy(name = newItemName)) },
                            label = { Text(stringResource(R.string.item_details)) },
                            modifier = Modifier.fillMaxWidth(),
                            // --- APPLY COLORS ---
                            colors = textFieldColors
                        )
                    }
                    TextField(
                        value = quantityText,
                        onValueChange = { newQty ->
                            quantityText = newQty
                            onItemChange(item.copy(quantity = newQty.toDoubleOrNull() ?: 1.0))
                        },
                        label = { Text(stringResource(R.string.quantity)) },
                        modifier = Modifier.width(80.dp),
                        // --- APPLY COLORS ---
                        colors = textFieldColors
                    )
                    TextField(
                        value = item.unitPrice.toString(),
                        onValueChange = { newPrice ->
                            onItemChange(item.copy(unitPrice = newPrice.toDoubleOrNull() ?: 0.0))
                        },
                        label = { Text(stringResource(R.string.price)) },
                        modifier = Modifier.width(100.dp),
                        // --- APPLY COLORS ---
                        colors = textFieldColors
                    )
                }
            }
            @Composable
            fun AddEditProductServiceDialog(
                item: ProductOrService?, // Null for adding, non-null for editing
                onDismiss: () -> Unit,
                onItemSaved: () -> Unit,
                firestore: FirebaseFirestore,
                companyName: String
            ) {
                val isEditing = item != null
                var name by rememberSaveable { mutableStateOf(item?.name ?: "") }
                var description by rememberSaveable { mutableStateOf(item?.description ?: "") }
                var price by rememberSaveable { mutableStateOf(item?.price?.toString() ?: "") }
                var type by rememberSaveable { mutableStateOf(item?.type ?: "Service") }
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()

                val itemTypes = listOf("Service", "Non-inventory")

                val textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()) {
                            Text(
                                text = if (isEditing) stringResource(R.string.edit_item) else stringResource(R.string.add_new_item),
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black
                            )
                            Spacer(Modifier.height(16.dp))
                            TextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.item_name)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                            Spacer(Modifier.height(8.dp))
                            TextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.item_description)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                            Spacer(Modifier.height(8.dp))
                            TextField(value = price, onValueChange = { price = it }, label = { Text(stringResource(R.string.item_price)) }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
                            Spacer(Modifier.height(16.dp))

                            Text(stringResource(R.string.item_type), color = Color.Black)
                            Row(modifier = Modifier.fillMaxWidth()) {
                                itemTypes.forEach { itemType ->
                                    Row(Modifier
                                        .selectable(
                                            selected = (type == itemType),
                                            onClick = { type = itemType })
                                        .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(selected = (type == itemType), onClick = { type = itemType })
                                        Text(itemType, color = Color.Black)
                                    }
                                }
                            }
                            Spacer(Modifier.height(24.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    if (name.isBlank()) {
                                        Toast.makeText(context, R.string.toast_item_name_empty, Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val priceDouble = price.toDoubleOrNull() ?: 0.0
                                    val itemData = ProductOrService(name = name, description = description, price = priceDouble, type = type)

                                    coroutineScope.launch {
                                        try {
                                            val collection = firestore.collection("companies").document(companyName).collection("productsAndServices")
                                            if (isEditing) {
                                                collection.document(item!!.id).set(itemData).await()
                                                Toast.makeText(context, R.string.toast_item_updated, Toast.LENGTH_SHORT).show()
                                            } else {
                                                collection.add(itemData).await()
                                                Toast.makeText(context, R.string.toast_item_saved, Toast.LENGTH_SHORT).show()
                                            }
                                            onItemSaved()
                                        } catch (e: Exception) {
                                            Log.e("FIRESTORE_ITEM", "Error saving item", e)
                                            Toast.makeText(context, context.getString(R.string.error_colon, e.message), Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }) {
                                    Text(stringResource(R.string.save))
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
                onDeleteClick: () -> Unit
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
                                Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.more_options), tint = Color.Gray)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                                DropdownMenuItem(text = { Text(stringResource(R.string.edit), color = Color.Black) }, onClick = { expanded = false; onEditClick() })
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
                companyName: String,
                userRole: String
            ) {
                var itemList by remember { mutableStateOf<List<ProductOrService>>(emptyList()) }
                var refreshTrigger by remember { mutableStateOf(0) }
                var showDialog by remember { mutableStateOf(false) }
                var selectedItem by remember { mutableStateOf<ProductOrService?>(null) }
                var showDeleteConfirmation by remember { mutableStateOf(false) }
                val coroutineScope = rememberCoroutineScope()
                val context = LocalContext.current
                var searchQuery by rememberSaveable { mutableStateOf("") }

                // This block performs the "contains" search on the downloaded list.
// It will re-run efficiently whenever the search query or the item list changes.
                LaunchedEffect(companyName, refreshTrigger) {
                    if (companyName.isNotBlank()) {
                        firestore.collection("companies").document(companyName)
                            .collection("productsAndServices").orderBy("name").get()
                            .addOnSuccessListener { result ->
                                itemList = result.documents.mapNotNull { doc ->
                                    doc.toObject(ProductOrService::class.java)?.copy(id = doc.id)
                                }
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error getting items.", e) }
                    }
                }
                // This block performs the "contains" search on the downloaded list.
// It will re-run efficiently whenever the search query or the item list changes.
                val filteredList = remember(itemList, searchQuery) {
                    if (searchQuery.isBlank()) {
                        itemList
                    } else {
                        itemList.filter { item ->
                            item.name.contains(searchQuery, ignoreCase = true) ||
                                    item.description.contains(searchQuery, ignoreCase = true)
                        }
                    }
                }
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
                        message = stringResource(R.string.confirm_delete_item, selectedItem!!.name),
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
                            FloatingActionButton(onClick = { selectedItem = null; showDialog = true }) {
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
                        Spacer(modifier = Modifier.height(16.dp))


                        if (filteredList.isEmpty()) {
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (searchQuery.isBlank()) stringResource(R.string.no_items_found)
                                    else stringResource(R.string.no_items_match_search),
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                // --- FIX: The LazyColumn now displays the filteredList, not the original itemList ---
                                items(filteredList, key = { it.id }) { item ->
                                    ProductServiceCard(
                                        item = item,
                                        onEditClick = { if (userRole == "Manager") { selectedItem = item; showDialog = true } },
                                        onDeleteClick = { if (userRole == "Manager") { selectedItem = item; showDeleteConfirmation = true } }
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
    onBackToSchedule: () -> Unit = {},
    onTaskCreated: () -> Unit = {},
    firestore: FirebaseFirestore,
    companyName: String
) {
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
                    .menuAnchor()
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

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isAddressDropdownExpanded,
            onExpandedChange = { if (addressOptions.size > 1) isAddressDropdownExpanded = !isAddressDropdownExpanded }
        ) {
            TextField(
                value = addressText, onValueChange = {}, readOnly = true, label = { Text("Address") },
                trailingIcon = { if (addressOptions.size > 1) ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAddressDropdownExpanded) },
                colors = textFieldColors, modifier = Modifier
                    .menuAnchor()
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
                value = if (selectedUsers.isNotEmpty()) selectedUsers.joinToString { it.name } else "",
                onValueChange = {}, readOnly = true, label = { Text("Assign To") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isUserDropdownExpanded) },
                colors = textFieldColors, modifier = Modifier
                    .menuAnchor()
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
                value = selectedTimeWindow, onValueChange = {}, readOnly = true, label = { Text("Time window") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeExpanded) },
                colors = textFieldColors, modifier = Modifier
                    .menuAnchor()
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
        TextField(value = jobDescription, onValueChange = { jobDescription = it }, label = { Text("Job Description") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        TextField(value = notes, onValueChange = { notes = it }, label = { Text("Internal Notes") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth())
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
                                firestore.runTransaction { transaction ->
                                    val counterDoc = transaction.get(counterRef)
                                    val lastNumber = counterDoc.getLong("lastJobNumber") ?: 9999L
                                    val newJobNumber = lastNumber + 1
                                    val taskData = FirestoreTask(
                                        id = newTaskRef.id, jobNumber = newJobNumber.toString(), customerId = selectedCustomer!!.id,
                                        customerName = customerSearchQuery, customerCompanyName = selectedCustomer?.companyName ?: "",
                                        address = addressText, startDate = startTimestamp, endDate = endTimestamp,
                                        timeWindow = selectedTimeWindow, jobDescription = jobDescription, notes = notes,
                                        assignedUserIds = selectedUsers.map { it.id }, assignedUserNames = selectedUsers.map { it.name }
                                    )
                                    transaction.set(newTaskRef, taskData)
                                    transaction.update(counterRef, "lastJobNumber", newJobNumber)
                                    null
                                }.await()
                                firebaseAnalytics?.logEvent("task_created", null)
                                Toast.makeText(context, "Task Scheduled!", Toast.LENGTH_SHORT).show()
                            } else {
                                val taskData = FirestoreTask(
                                    id = editingTask.id, jobNumber = editingTask.jobNumber, customerId = selectedCustomer?.id ?: editingTask.customerId,
                                    customerName = customerSearchQuery, customerCompanyName = selectedCustomer?.companyName ?: editingTask.customerCompanyName,
                                    address = addressText, startDate = startTimestamp, endDate = endTimestamp,
                                    timeWindow = selectedTimeWindow, jobDescription = jobDescription, notes = notes,
                                    assignedUserIds = selectedUsers.map { it.id }, assignedUserNames = selectedUsers.map { it.name }
                                )
                                firestore.collection("companies").document(companyName).collection("tasks").document(editingTask.id).set(taskData).await()
                                Toast.makeText(context, "Task Updated!", Toast.LENGTH_SHORT).show()
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
    onBack: () -> Unit,
    onEstimateClick: (String) -> Unit,
    onInvoiceClick: (String) -> Unit,
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
                        0 -> CustomerDetailsTab(customer!!)
                        1 -> CustomerEstimatesTab(
                            items = estimates,
                            onItemClick = onEstimateClick,
                            // Handle the duplicate click here
                            onDuplicateClick = { estimateId ->
                                // Call the lambda from MainApp, passing a function
                                // to refresh this screen by incrementing the trigger.
                                onDuplicateEstimateClick(estimateId) {
                                    refreshTrigger++
                                }
                            }
                        )
                        2 -> CustomerInvoicesTab(invoices, onItemClick = onInvoiceClick)
                        3 -> CustomerRelatedItemsTab(jobs) { SummaryJobCard(it) }
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
    companyName: String,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onConvertToInvoiceClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var estimate by remember { mutableStateOf<Estimate?>(null) }
    var lineItems by remember { mutableStateOf<List<EstimateLineItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(estimateId, companyName) {
        if (companyName.isNotBlank() && estimateId.isNotBlank()) {
            isLoading = true
            try {
                val estimateRef = firestore.collection("companies").document(companyName)
                    .collection("estimates").document(estimateId)

                val estimateDoc = estimateRef.get().await()
                estimate = estimateDoc.toObject(Estimate::class.java)?.copy(id = estimateDoc.id)

                val lineItemsSnapshot = estimateRef.collection("lineItems").get().await()
                lineItems = lineItemsSnapshot.toObjects(EstimateLineItem::class.java)

            } catch (e: Exception) {
                Log.e("ViewEstimateScreen", "Error fetching estimate", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        } else {
            Log.w("ViewEstimateScreen", "companyName or estimateId is blank. Cannot load data.")
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Estimate #${estimate?.estimateNumber ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            // Only show the action bar if loading is complete and we have an estimate
            if (!isLoading && estimate != null) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onEditClick,
                            // Disable editing if the estimate has been converted
                            enabled = estimate?.status != "Converted"
                        ) {
                            Text("Edit Estimate")
                        }
                        Button(
                            onClick = onConvertToInvoiceClick,
                            // Disable conversion if an invoice ID already exists
                            enabled = estimate?.invoiceId == null
                        ) {
                            Text("Convert to Invoice")
                        }
                        OutlinedButton(
                            onClick = onDeleteClick,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (estimate == null) {
            Box(Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Estimate not found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { DetailItem("Customer", estimate!!.customerName) }
                item { DetailItem("Address", estimate!!.address) }
                item { DetailItem("Status", estimate!!.status) }

                item { Spacer(Modifier.height(8.dp)); HorizontalDivider() }

                item {
                    Text(
                        "Line Items",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                items(lineItems) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold)
                            }
                            Text("${item.quantity} x $%.2f".format(item.unitPrice))
                            Text("$%.2f".format(item.quantity * item.unitPrice), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)); HorizontalDivider() }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Subtotal:")
                            Text("$%.2f".format(estimate!!.subtotal))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Tax (${estimate!!.taxRate}%):")
                            Text("$%.2f".format(estimate!!.taxAmount))
                        }
                        HorizontalDivider(modifier = Modifier.width(150.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "$%.2f".format(estimate!!.totalAmount),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun ViewInvoiceScreen(
    invoiceId: String,
    firestore: FirebaseFirestore,
    companyName: String,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onEmailClick: (Invoice, List<EstimateLineItem>) -> Unit,
    onDeleteClick: () -> Unit
) {
    var invoice by remember { mutableStateOf<Invoice?>(null) }
    var lineItems by remember { mutableStateOf<List<EstimateLineItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    LaunchedEffect(invoiceId, companyName) {
        if (companyName.isNotBlank() && invoiceId.isNotBlank()) {
            isLoading = true
            try {
                val invoiceRef = firestore.collection("companies").document(companyName)
                    .collection("invoices").document(invoiceId)

                val invoiceDoc = invoiceRef.get().await()
                invoice = invoiceDoc.toObject(Invoice::class.java)?.copy(id = invoiceDoc.id)

                val lineItemsSnapshot = invoiceRef.collection("lineItems").get().await()
                lineItems = lineItemsSnapshot.toObjects(EstimateLineItem::class.java)

            } catch (e: Exception) {
                Log.e("ViewInvoiceScreen", "Error fetching invoice", e)
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
                title = { Text("View Invoice #${invoice?.invoiceNumber ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (!isLoading && invoice != null) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = onEditClick) {
                            Text("Edit")
                        }
                        Button(onClick = {
                            // Pass the fully-loaded data up to the email handler
                            onEmailClick(invoice!!, lineItems)
                        }) {
                            Text("Email")
                        }
                        OutlinedButton(
                            onClick = onDeleteClick,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (invoice == null) {
            Box(Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Invoice not found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { DetailItem("Customer", invoice!!.customerName) }
                item { DetailItem("Address", invoice!!.address) }
                item { DetailItem("Status", invoice!!.status) }

                item { Spacer(Modifier.height(8.dp)); HorizontalDivider() }

                item {
                    Text(
                        "Line Items",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                items(lineItems) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold)
                            }
                            Text("${item.quantity} x $%.2f".format(item.unitPrice))
                            Text("$%.2f".format(item.quantity * item.unitPrice), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)); HorizontalDivider() }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Subtotal:")
                            Text("$%.2f".format(invoice!!.subtotal))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Tax (${invoice!!.taxRate}%):")
                            Text("$%.2f".format(invoice!!.taxAmount))
                        }
                        HorizontalDivider(modifier = Modifier.width(150.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Total:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "$%.2f".format(invoice!!.totalAmount),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerDetailsTab(customer: CustomerInfo) {
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
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${customer.phone}"))
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
                    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(customer.address)}")
                    context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri).setPackage("com.google.android.apps.maps"))
                })
            }
        }
        if (customer.jobsiteAddresses.isNotEmpty()) {
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
            item { Text("Jobsite Addresses", style = MaterialTheme.typography.titleMedium) }
            items(customer.jobsiteAddresses) { address ->
                DetailItem(label = "Jobsite", value = address, onClick = {
                    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
                    context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri).setPackage("com.google.android.apps.maps"))
                })
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
        onClick = onClick
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
                    Text(estimate.status, style = MaterialTheme.typography.bodySmall)
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
        onClick = onClick // Make the card clickable
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("#${invoice.invoiceNumber}", fontWeight = FontWeight.Bold)
                Text(invoice.status, style = MaterialTheme.typography.bodySmall)
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

@Composable
fun SummaryJobCard(job: FirestoreTask) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
                onCustomerSaved: () -> Unit,
                firestore: FirebaseFirestore,
                companyName: String
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
                                        // Filter out any empty strings from the list before saving
                                        "jobsiteAddresses" to jobsiteAddresses.filter { it.isNotBlank() }
                                    )
                                    coroutineScope.launch {
                                        try {
                                            firestore.collection("companies").document(companyName)
                                                .collection("customers").add(customerData).await()
                                            Toast.makeText(context, "Customer saved!", Toast.LENGTH_SHORT).show()
                                            onCustomerSaved()
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
                companyName: String
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
                        onCustomerSaved = {
                            showAddCustomerDialog = false
                            refreshTrigger++ // Refresh the customer list
                        },
                        firestore = firestore,
                        companyName = companyName
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
                                    Toast.makeText(context, context.getString(R.string.error_colon, e.message), Toast.LENGTH_LONG).show()
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
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_customer))
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
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_customers)) },
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
                                    text = if (searchQuery.isBlank()) stringResource(R.string.no_customers_found)
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
                onEditClick: () -> Unit,
                onDeleteClick: () -> Unit,
                firestore: FirebaseFirestore,
                companyName: String ) {
                var expanded by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope() // <-- THE FIX IS HERE
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                    text = invoice.description.ifBlank { stringResource(R.string.invoice_number_label, invoice.invoiceNumber) },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "$%.2f".format(invoice.totalAmount),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = invoice.customerName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            // Add more rows for due date, etc. as needed
                        }

                        Box(modifier = Modifier.align(Alignment.TopEnd)) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.more_options), tint = Color.Gray)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.edit), color = Color.Black) },
                                    onClick = { expanded = false; onEditClick() }
                                )
                                DropdownMenuItem(
                                    text = { Text("Send Email", color = Color.Black) },
                                    onClick = {
                                        expanded = false
                                        coroutineScope.launch {
                                            try {
                                                val lineItemsSnapshot = firestore.collection("companies").document(companyName)
                                                    .collection("invoices").document(invoice.id)
                                                    .collection("lineItems").get().await()

                                                val lineItems = lineItemsSnapshot.toObjects(EstimateLineItem::class.java)

                                                val pdfData = PdfInvoiceData(
                                                    invoice = invoice,
                                                    lineItems = lineItems
                                                )

                                                generateAndEmailInvoice(context, pdfData)
                                            } catch (e: Exception) {
                                                Log.e("PDF_GEN_INVOICE", "Failed to generate invoice PDF", e)
                                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.delete), color = Color.Black) },
                                    onClick = { expanded = false; onDeleteClick() }
                                )
                            }
                        }
                    }
                }
            }
            @OptIn(ExperimentalMaterial3Api::class)
            @Composable fun InvoicesScreen(
                onBackToMenu: () -> Unit,
                onNewInvoiceClick: () -> Unit,
                onEditInvoiceClick: (String) -> Unit,
                firestore: FirebaseFirestore,
                companyName: String ) {
                var invoiceList by remember { mutableStateOf<List<Invoice>>(emptyList()) }
                var refreshTrigger by remember { mutableStateOf(0) }
                val coroutineScope = rememberCoroutineScope()
                val context = LocalContext.current
                var selectedInvoice by remember { mutableStateOf<Invoice?>(null) }
                var showDeleteConfirmation by remember { mutableStateOf(false) }
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
                            .addOnFailureListener { e -> Log.w(TAG, "Error getting invoices.", e) }
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
                    floatingActionButton = {
                        FloatingActionButton(onClick = onNewInvoiceClick) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_invoice))
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
                        Text(stringResource(R.string.invoices), fontSize = 26.sp, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))

                        if (invoiceList.isEmpty()) {
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(stringResource(R.string.no_invoices_found), color = Color.Gray)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp))
                            {
                                items(invoiceList, key = { it.id }) { invoice ->
                                    InvoiceCard(
                                        invoice = invoice,
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
            @Composable fun TimeClockScreen(
                modifier: Modifier = Modifier,
                onBackToMenu: () -> Unit = {},
                firestore: FirebaseFirestore,
                companyName: String,
                userRole: String, // Kept for showing manager buttons
                userId: String,
                userName: String // Can be used for display if needed
            ) {
                val TAG = "TimeClockDebug"
                Log.d(TAG, "TimeClockScreen Composed. Parameters are:")
                Log.d(TAG, "  - companyName: '$companyName'")
                Log.d(TAG, "  - userId: '$userId'")
                Log.d(TAG, "  - userName: '$userName'")
                // The screen is guaranteed to have a valid userId and companyName
                // because of the guard clause in MainApp.
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
// --- STATE MANAGEMENT ---
                var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
                val payPeriods = remember { generatePayPeriods() }
                var selectedPayPeriod by remember { mutableStateOf(payPeriods.firstOrNull()) }
                var expanded by remember { mutableStateOf(false) }
                var showEditDialog by remember { mutableStateOf(false) }
                var editingTimeEntry by remember { mutableStateOf<TimeEntry?>(null) }
                var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
                var entryToDelete by remember { mutableStateOf<TimeEntry?>(null) }

// --- REAL-TIME DATA FROM FIRESTORE ---

                // This helper function is simple and safe because userId is a required parameter.
                fun getMyTimeEntriesCollection(): CollectionReference { // <-- THE FIX IS HERE
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

// --- DERIVED STATE & HELPERS ---
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

                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { permissions ->
                        if (permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false)) {
                            context.startService(Intent(context, LocationService::class.java))
                        } else {
                            Toast.makeText(context, "Location permission is required for tracking.", Toast.LENGTH_LONG).show()
                        }
                    }
                )

// --- EFFECTS ---
                LaunchedEffect(Unit) {
                    while (true) {
                        currentTime = System.currentTimeMillis()
                        delay(1000)
                    }
                }

// --- UI ---
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
                            modifier = Modifier.menuAnchor(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            payPeriods.forEach { period ->
                                DropdownMenuItem(
                                    text = { Text("${payPeriodFormat.format(period.first.toDate())} - ${payPeriodFormat.format(period.second.toDate())}") },
                                    onClick = {
                                        selectedPayPeriod = period
                                        expanded = false
                                    }
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
                                val newEntry = hashMapOf(
                                    "userId" to userId,
                                    "companyName" to companyName,
                                    "clockIn" to FieldValue.serverTimestamp(),
                                    "clockOut" to null
                                )
                                getMyTimeEntriesCollection().add(newEntry)
                                    .addOnSuccessListener {
                                        selectedPayPeriod = payPeriods.firstOrNull()
                                        locationPermissionLauncher.launch(
                                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                        )
                                    }
                                    .addOnFailureListener { e: Exception ->
                                        Toast.makeText(context, "Clock in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            },
                            enabled = !isClockedIn,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                        ) { Text("Clock In") }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val openEntries = getMyTimeEntriesCollection()
                                            .whereEqualTo("clockOut", null).limit(1).get().await()
                                        if (!openEntries.isEmpty) {
                                            val docId = openEntries.documents[0].id
                                            getMyTimeEntriesCollection().document(docId).update("clockOut", FieldValue.serverTimestamp()).await()
                                            context.stopService(Intent(context, LocationService::class.java))
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
                        Text("Status: Clocked In", color = Color.Green, fontWeight = FontWeight.Bold)
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
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 8.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
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

// --- DIALOGS ---
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
                                .addOnFailureListener { e: Exception ->
                                    Toast.makeText(context, "Error updating: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    )
                }

                if (showDeleteConfirmationDialog && entryToDelete != null) {
                    ConfirmationDialog(
                        title = "Confirm Deletion",
                        message = "Are you sure you want to delete this time entry?",
                        onConfirm = {
                            getMyTimeEntriesCollection().document(entryToDelete!!.id).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Entry deleted", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e: Exception ->
                                    Toast.makeText(context, "Error deleting: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            showDeleteConfirmationDialog = false
                            entryToDelete = null
                        },
                        onDismiss = {
                            showDeleteConfirmationDialog = false
                            entryToDelete = null
                        }
                    )
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
                var userDetails by remember { mutableStateOf<List<UserDetails>>(emptyList()) }
                val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
                val payPeriodFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                var payPeriods by remember { mutableStateOf<List<Pair<Timestamp, Timestamp>>>(emptyList()) }
                var selectedPayPeriod by remember { mutableStateOf<Pair<Timestamp, Timestamp>?>(null) }
                var expanded by remember { mutableStateOf(false) }
                var showEditDialog by remember { mutableStateOf(false) }
                var editingTeamTimeEntry by remember { mutableStateOf<TeamTimeEntry?>(null) }

                var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
                var teamEntryToDelete by remember { mutableStateOf<TeamTimeEntry?>(null) }
                // Removed the unused fetchEarliestClockInDate function.



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
                    if (companyName.isBlank()) {
                        Log.e(AUTH_TAG, "companyName is blank in fetchTeamTimeEntries. Aborting.")
                        return
                    }
                    Log.d(AUTH_TAG, "Fetching team time entries for company: '$companyName'")
                    Log.d(AUTH_TAG, "Pay Period Start: ${startDate.toDate()}, End: ${endDate.toDate()}")

                    // Calculate endDate


                    Log.d(AUTH_TAG, "Query range adjusted to: start ${startDate.toDate()} (inclusive), end ${endDate.toDate()} (exclusive)")

                    coroutineScope.launch {
                        try {
                            val usersSnapshot = firestore.collection("companies").document(companyName).collection("users").get().await()
                            val usersMap = usersSnapshot.documents.associate { doc ->
                                doc.id to "${doc.getString("firstName") ?: ""} ${doc.getString("lastName") ?: ""}".trim()
                            }
                            Log.d(AUTH_TAG, "Found ${usersMap.size} users in company '$companyName'.")

                            val timeEntriesSnapshot = firestore.collectionGroup("time_entries")
                                .whereEqualTo("companyName", companyName)
                                .whereGreaterThanOrEqualTo("clockIn", startDate)
                                .whereLessThanOrEqualTo("clockIn", endDate) // <-- FIX
                                .orderBy("clockIn", Query.Direction.DESCENDING)
                                .get()
                                .await()

                            Log.d(AUTH_TAG, "Firestore query returned ${timeEntriesSnapshot.size()} documents.")
                            if (timeEntriesSnapshot.isEmpty) {
                                Log.w(AUTH_TAG, "Query for time entries returned no results. Check Firestore data and indexes.")
                            }

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
                                } else {
                                    null
                                }
                            }
                            val groupedEntries = entries.groupBy { it.userName }
                            userDetails = groupedEntries.map { (userName, userEntries) ->
                                UserDetails(
                                    userName = userName,
                                    totalHours = calculateTotalHoursForTeam(userEntries),
                                    entries = userEntries
                                )
                            }
                            Log.d(AUTH_TAG, "Processed and grouped entries for ${userDetails.size} users.")

                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Log.e(AUTH_TAG, "Error fetching team time entries. This might be due to a missing Firestore index.", e)
                            Toast.makeText(context, "Error fetching entries: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    payPeriods = generatePayPeriods()
                    if (payPeriods.isNotEmpty()) {
                        selectedPayPeriod = payPeriods.first() // Select the most recent pay period initially
                    }
                }

                LaunchedEffect(companyName, selectedPayPeriod) {
                    selectedPayPeriod?.let { (startDate, endDate) ->
                        fetchTeamTimeEntries(startDate, endDate)
                    }
                }

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Team Time Clock", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopStart))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedPayPeriod?.let { "${payPeriodFormat.format(it.first.toDate())} - ${payPeriodFormat.format(it.second.toDate())}" } ?: "Select Pay Period",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pay Period") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            payPeriods.forEach { period ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${payPeriodFormat.format(period.first.toDate())} - ${payPeriodFormat.format(period.second.toDate())}") },
                                    onClick = {
                                        selectedPayPeriod = period
                                        expanded = false
                                    },
                                    modifier = Modifier.background(Color.White)
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        if (userDetails.isEmpty()){
                            item {
                                Text("No time entries for this pay period.")
                            }
                        }
                        userDetails.forEach { user ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = user.userName,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Total: ${user.totalHours}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            items(user.entries) { entry ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "In: ${entry.clockIn?.toDate()?.let { dateTimeFormat.format(it) } ?: "N/A"}", color = Color.Black)
                                        Text(text = "Out: ${entry.clockOut?.toDate()?.let { dateTimeFormat.format(it) } ?: "N/A"}", color = Color.Black)
                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Button(onClick = {
                                                editingTeamTimeEntry = entry
                                                showEditDialog = true
                                            }) {
                                                Text("Edit")
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Button(onClick = {
                                                teamEntryToDelete = entry
                                                showDeleteConfirmationDialog = true
                                            },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                            ) {
                                                Text("Delete")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Button(onClick = onBackToMenu) {
                            Text("Back")
                        }
                    }
                }
                if (showEditDialog && editingTeamTimeEntry != null) {
                    TeamTimeEntryEditDialog(
                        timeEntry = editingTeamTimeEntry!!,
                        onDismiss = { showEditDialog = false },
                        onSave = { updatedEntry ->
                            val entryRef = firestore.collection("companies").document(companyName)
                                .collection("users").document(updatedEntry.userId)
                                .collection("time_entries").document(updatedEntry.id)

                            val updates = hashMapOf<String, Any?>(
                                "clockIn" to updatedEntry.clockIn,
                                "clockOut" to updatedEntry.clockOut
                            )

                            entryRef.update(updates)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Entry updated", Toast.LENGTH_SHORT).show()
                                    selectedPayPeriod?.let { fetchTeamTimeEntries(it.first, it.second) }
                                    showEditDialog = false
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error updating entry: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    )
                }
                if (showDeleteConfirmationDialog && teamEntryToDelete != null) {
                    ConfirmationDialog(
                        title = "Confirm Deletion",
                        message = "Are you sure you want to delete this team time entry?",
                        onConfirm = {
                            // Perform deletion
                            if (companyName.isNotBlank() && teamEntryToDelete != null) {
                                firestore.collection("companies").document(companyName)
                                    .collection("users").document(teamEntryToDelete!!.userId)
                                    .collection("time_entries").document(teamEntryToDelete!!.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Entry deleted", Toast.LENGTH_SHORT).show()
                                        selectedPayPeriod?.let { fetchTeamTimeEntries(it.first, it.second) } // Refresh the list
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Error deleting entry: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            teamEntryToDelete = null // Clear the entry
                            showDeleteConfirmationDialog = false // Dismiss the dialog
                        },
                        onDismiss = {
                            teamEntryToDelete = null // Clear the entry
                            showDeleteConfirmationDialog = false // Dismiss the dialog
                        }
                    )
                }
            }
            @Composable fun CompanySettingsDialog(
                onDismiss: () -> Unit, firestore:
                FirebaseFirestore, companyName: String ) {
                var taxRateText by rememberSaveable { mutableStateOf("") }
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
// Fetch the current tax rate when the dialog opens
                LaunchedEffect(Unit) {
                    if (companyName.isNotBlank()) {
                        firestore.collection("companies").document(companyName).get()
                            .addOnSuccessListener { doc ->
                                val rate = doc.getDouble("taxRate") ?: 0.0
                                taxRateText = rate.toString()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to load settings.", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Company Settings", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
                            Spacer(Modifier.height(16.dp))

                            TextField(
                                value = taxRateText,
                                onValueChange = { taxRateText = it },
                                label = { Text("Default Sales Tax Rate (%)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                )
                            )
                            Spacer(Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = onDismiss) {
                                    Text("Cancel")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    val rate = taxRateText.toDoubleOrNull() ?: 0.0
                                    coroutineScope.launch {
                                        try {
                                            firestore.collection("companies").document(companyName)
                                                .update("taxRate", rate).await()
                                            Toast.makeText(context, "Settings Saved!", Toast.LENGTH_SHORT).show()
                                            onDismiss() // Close the dialog on success
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
            fun SettingsScreen(
                modifier: Modifier = Modifier,
                onBackToMenu: () -> Unit = {},
                auth: FirebaseAuth, firestore: FirebaseFirestore,
                companyName: String,
                userRole: String,
                onAddingUserChange: (Boolean) -> Unit) { // State to control the visibility of the new dialog
                var showCompanySettingsDialog by remember { mutableStateOf(false) }
                var showUserManagementDialog by remember { mutableStateOf(false) }
                var showAddUserDialog by remember { mutableStateOf(false) }
                var showChangePasswordDialog by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()

// State for Customer Import
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
                                } else {
                                    Pair(0, "Unsupported file type. Please select a .csv file.")
                                }
                                customerImportResult = result
                                isImportingCustomers = false
                            }
                        }
                    }
                )
                if (showUserManagementDialog) {
                    UserManagementDialog(
                        onDismiss = { showUserManagementDialog = false },
                        firestore = firestore,
                        companyName = companyName
                    )
                }

// State for Product & Service Import
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
                                } else {
                                    Pair(0, "Unsupported file type. Please select a .csv file.")
                                }
                                productImportResult = result
                                isImportingProducts = false
                            }
                        }
                    }
                )

// --- Show the new dialog when its state is true ---
                if (showCompanySettingsDialog) {
                    CompanySettingsDialog(
                        onDismiss = { showCompanySettingsDialog = false },
                        firestore = firestore,
                        companyName = companyName
                    )
                }

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Settings", fontSize = 26.sp, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Manager-specific settings ---
                    if (userRole == "Manager") {
                        Button(onClick = { showCompanySettingsDialog = true }) {
                            Text("Company Settings")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showAddUserDialog = true }) {
                            Text("Add Company User")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showUserManagementDialog = true }) {
                            Text("User Management")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // --- General settings for all users ---
                    Button(onClick = { showChangePasswordDialog = true }) {
                        Text("Change Password")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Import functionality ---
                    Button(onClick = { customerFilePickerLauncher.launch("text/csv") }) {
                        Text(stringResource(R.string.import_customers_from_csv))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { productFilePickerLauncher.launch("text/csv") }) {
                        Text(stringResource(R.string.import_products_from_csv))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // --- Back button at the bottom ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Button(onClick = onBackToMenu) { Text("Back") }
                    }
                }

                if (showAddUserDialog) {
                    AddUserDialog(
                        onDismiss = { showAddUserDialog = false },
                        onUserAdded = { showAddUserDialog = false },
                        auth = auth,
                        firestore = firestore,
                        companyName = companyName,
                        onAddingUserChange = onAddingUserChange
                    )
                }
                if (showChangePasswordDialog) {
                    ChangePasswordDialog(
                        onDismiss = { showChangePasswordDialog = false },
                        onPasswordChanged = { showChangePasswordDialog = false },
                        auth = auth,
                        coroutineScope = coroutineScope
                    )
                }

                if (isImportingCustomers) {
                    Dialog(onDismissRequest = {}) {
                        Card {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                                Spacer(Modifier.height(16.dp))
                                Text("Importing customers...")
                            }
                        }
                    }
                }
                customerImportResult?.let { (count, error) ->
                    AlertDialog(
                        onDismissRequest = { customerImportResult = null },
                        title = { Text(if (error == null) stringResource(R.string.import_successful) else stringResource(R.string.import_failed)) },
                        text = { Text(if (error == null) stringResource(R.string.import_count_message, count) else stringResource(R.string.error_colon, error ?: "Unknown error")) },
                        confirmButton = { TextButton(onClick = { customerImportResult = null }) { Text(stringResource(R.string.ok)) } }
                    )
                }

                if (isImportingProducts) {
                    Dialog(onDismissRequest = {}) {
                        Card {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                                Spacer(Modifier.height(16.dp))
                                Text(stringResource(R.string.importing_products))
                            }
                        }
                    }
                }
                productImportResult?.let { (count, error) ->
                    AlertDialog(
                        onDismissRequest = { productImportResult = null },
                        title = { Text(if (error == null) stringResource(R.string.import_successful) else stringResource(R.string.import_failed)) },
                        text = { Text(if (error == null) stringResource(R.string.product_import_count_message, count) else stringResource(R.string.error_colon, error ?: "Unknown error")) },
                        confirmButton = { TextButton(onClick = { productImportResult = null }) { Text(stringResource(R.string.ok)) } }
                    )
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
                var refreshTrigger by remember { mutableStateOf(0) }

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
                                        role = doc.getString("role") ?: "Technician"
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

                Dialog(onDismissRequest = onDismiss) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Edit User", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))
                            TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
                            Spacer(Modifier.height(8.dp))
                            TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
                            Spacer(Modifier.height(16.dp))
                            Text("Role")
                            Row {
                                roles.forEach { role ->
                                    Row(Modifier
                                        .selectable(
                                            selected = (role == selectedRole),
                                            onClick = { selectedRole = role })
                                        .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(selected = (role == selectedRole), onClick = { selectedRole = role })
                                        Text(role)
                                    }
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
                                                "role" to selectedRole
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
                onAddingUserChange: (Boolean) -> Unit // <-- FIX: New parameter
            ) {
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var firstName by remember { mutableStateOf("") }
                var lastName by remember { mutableStateOf("") }
                val roles = listOf("Manager", "Technician")
                var selectedRole by remember { mutableStateOf(roles[0]) }
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
                            Text(text = "Add New User", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = { Text("First Name") },
                                colors = textFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = { Text("Last Name") },
                                colors = textFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                colors = textFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                colors = textFieldColors
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Role")
                            Row {
                                roles.forEach { role ->
                                    Row(
                                        Modifier
                                            .selectable(
                                                selected = (role == selectedRole),
                                                onClick = { selectedRole = role }
                                            )
                                            .padding(horizontal = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = (role == selectedRole),
                                            onClick = { selectedRole = role }
                                        )
                                        Text(text = role)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                Button(onClick = onDismiss) {
                                    Text("Cancel")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    if (email.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()) {
                                        coroutineScope.launch {
                                            var userCreatedSuccessfully = false

                                            if (companyName.isBlank()) {
                                                Toast.makeText(context, "Error: Company name is not set.", Toast.LENGTH_LONG).show()
                                                return@launch
                                            }

                                            try {
                                                // FIX: Signal that the sensitive auth process is starting
                                                onAddingUserChange(true)

                                                // 1. Create the new user. This signs the manager out.
                                                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                                                val newUser = authResult.user

                                                if (newUser != null) {
                                                    // 2. Add the new user's data to Firestore
                                                    val userData = hashMapOf(
                                                        "uid" to newUser.uid,
                                                        "firstName" to firstName,
                                                        "lastName" to lastName,
                                                        "email" to email,
                                                        "role" to selectedRole,
                                                        "companyName" to companyName
                                                    )
                                                    firestore.collection("companies").document(companyName)
                                                        .collection("users").document(newUser.uid)
                                                        .set(userData).await()
                                                    userCreatedSuccessfully = true
                                                    Toast.makeText(context, "User added successfully!", Toast.LENGTH_SHORT).show()
                                                    onUserAdded()
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Failed to create user: ${e.message}", Toast.LENGTH_LONG).show()
                                            } finally {
                                                // 3. Sign out the newly created user, leaving no one logged in.
                                                // This triggers the navigation back to the sign-in screen.
                                                if (userCreatedSuccessfully) {
                                                    auth.signOut()
                                                }

                                                // FIX: Signal that the auth process is finished
                                                onAddingUserChange(false)
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
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
