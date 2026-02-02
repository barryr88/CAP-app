package com.example.jobtracker

import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.jobtracker.data.AppDatabase
import com.example.jobtracker.data.Customer
import com.example.jobtracker.data.Task
import com.example.jobtracker.ui.theme.JobTrackerTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner

private const val TAG = "JobTrackerDB"

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        enableEdgeToEdge()
        setContent {
            JobTrackerTheme {
                MainApp(firebaseAnalytics, lifecycleScope)
            }
        }
    }
}

@Composable
fun MainApp(firebaseAnalytics: FirebaseAnalytics, activityScope: CoroutineScope) {
    val currentScreen = remember { mutableStateOf("signIn") }
    val loggedInUser = remember { mutableStateOf<String?>(null) }
    val selectedDate = remember {
        mutableLongStateOf(
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        when (currentScreen.value) {
            "signIn" -> SignInScreen(
                modifier = screenModifier,
                onSignInSuccess = { username ->
                    loggedInUser.value = username
                    currentScreen.value = "mainMenu"
                    Log.d(TAG, "MainApp: Navigating to MainMenu for user: $username")
                    firebaseAnalytics.logEvent("sign_in", null)
                },
                activityScope = activityScope,
                firebaseAnalytics = firebaseAnalytics
            )
            "mainMenu" -> MainMenu(
                modifier = screenModifier,
                username = loggedInUser.value ?: "User",
                onSignOut = {
                    loggedInUser.value = null
                    currentScreen.value = "signIn"
                },
                onScheduleClick = { currentScreen.value = "schedule" },
                onCustomersClick = { currentScreen.value = "customers" },
                onTimeClockClick = { currentScreen.value = "timeClock" }
            )
            "schedule" -> ScheduleScreen(
                modifier = screenModifier,
                selectedDate = selectedDate.longValue,
                onDateSelected = { selectedDate.longValue = it },
                onBackToMenu = { currentScreen.value = "mainMenu" },
                onNewTaskClick = { currentScreen.value = "newTask" }
            )
            "newTask" -> NewTaskScreen(
                modifier = screenModifier,
                selectedDate = selectedDate.longValue,
                firebaseAnalytics = firebaseAnalytics,
                activityScope = activityScope,
                onBackToSchedule = { currentScreen.value = "schedule" },
                onTaskCreated = { currentScreen.value = "schedule" }
            )
            "customers" -> CustomersScreen(
                modifier = screenModifier,
                activityScope = activityScope,
                onBackToMenu = { currentScreen.value = "mainMenu" }
            )
            "timeClock" -> TimeClockScreen(
                modifier = screenModifier,
                onBackToMenu = { currentScreen.value = "mainMenu" }
            )
        }
    }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInSuccess: (String) -> Unit = {},
    activityScope: CoroutineScope,
    firebaseAnalytics: FirebaseAnalytics
) {
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.cap_construction),
            contentDescription = "Cap Construction Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 32.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sign In", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("391755660486-sgarg7eloridtbj5e12pq0hekagnelaq.apps.googleusercontent.com")
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .setCredentialOptions(listOf(googleIdOption))
                .build()

            activityScope.launch {
                Log.d(TAG, "SignInScreen: Attempting Google Sign-In")
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                    val credential = result.credential
                    Log.d(TAG, "SignInScreen: Received credential of type: ${credential::class.simpleName}")
                    if (credential is GoogleIdTokenCredential) {
                        onSignInSuccess(credential.displayName ?: credential.id)
                        Toast.makeText(context, "Google Sign-In Success!", Toast.LENGTH_SHORT).show()
                        firebaseAnalytics.logEvent("sign_in") { 
                            param("method", "google")
                        }

                    } else {
                        Log.e(TAG, "SignInScreen: Google Sign-In failed, credential is not GoogleIdTokenCredential. Actual type: ${credential::class.simpleName}")
                        Toast.makeText(context, "Google Sign-In failed: Invalid credential type.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("Auth", "Google Sign-In failed: ${e.message}", e)
                    Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Sign In with Google")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    username: String,
    onSignOut: () -> Unit,
    onScheduleClick: () -> Unit,
    onCustomersClick: () -> Unit = {},
    onTimeClockClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Main Menu", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopStart))
        
        Image(
            painter = painterResource(id = R.drawable.cap_construction),
            contentDescription = "Cap Construction Logo",
            modifier = Modifier.size(100.dp).align(Alignment.TopEnd),
            contentScale = ContentScale.Fit
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.Center) 
                .fillMaxWidth(), 
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome, $username!", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onScheduleClick) { Text("Schedule") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onCustomersClick) { Text("Customers") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onTimeClockClick) { Text("Time Clock") }
        }

        Button(onClick = onSignOut, modifier = Modifier.align(Alignment.BottomEnd)) {
            Text("Sign Out")
        }
    }
}

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier, 
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    onBackToMenu: () -> Unit,
    onNewTaskClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    var tasks by remember { mutableStateOf(listOf<Task>()) }

    LaunchedEffect(selectedDate) {
        tasks = db.taskDao().getTasksForDate(selectedDate)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Schedule", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopStart))
            Button(onClick = onNewTaskClick, modifier = Modifier.align(Alignment.TopEnd)) {
                Text("New Task")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AndroidView(
            factory = { viewContext ->
                CalendarView(viewContext).apply {
                    setViewTreeLifecycleOwner(lifecycleOwner)
                    setOnDateChangeListener { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth, 0, 0, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        onDateSelected(calendar.timeInMillis)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            update = { view ->
                if (view.date != selectedDate) {
                    view.date = selectedDate
                }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tasks for this date:", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Customer: ${task.customer}", fontWeight = FontWeight.Bold)
                        Text(text = "Time: ${task.timeWindow}")
                        Text(text = "Description: ${task.jobDescription}")
                    }
                }
            }
            if (tasks.isEmpty()) {
                item {
                    Text(text = "No tasks scheduled for this date.", modifier = Modifier.padding(16.dp), color = Color.Gray)
                }
            }
        }

        Button(onClick = onBackToMenu, modifier = Modifier.align(Alignment.Start)) {
            Text("Back")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(
    modifier: Modifier = Modifier, 
    selectedDate: Long,
    firebaseAnalytics: FirebaseAnalytics,
    activityScope: CoroutineScope,
    onBackToSchedule: () -> Unit = {},
    onTaskCreated: () -> Unit = {}
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val customerExpanded = remember { mutableStateOf(false) }
    var customers by remember { mutableStateOf(listOf<Customer>()) }
    var selectedCustomer by remember { mutableStateOf("") }

    val timeExpanded = remember { mutableStateOf(false) }
    val timeWindows = listOf("8am-10am", "10am-12pm", "12pm-2pm", "2pm-4pm")
    var selectedTimeWindow by remember { mutableStateOf("") }

    var jobDescription by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        customers = db.customerDao().getAllCustomers()
    }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    )

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "New Task", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopStart))
        }

        Spacer(modifier = Modifier.height(32.dp))

        ExposedDropdownMenuBox(
            expanded = customerExpanded.value,
            onExpandedChange = { customerExpanded.value = it }
        ) {
            TextField(
                value = selectedCustomer,
                onValueChange = {},
                readOnly = true,
                label = { Text("Customer") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerExpanded.value) },
                colors = textFieldColors,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = customerExpanded.value,
                onDismissRequest = { customerExpanded.value = false },
                modifier = Modifier.background(Color.White)
            ) {
                customers.forEach { customer ->
                    DropdownMenuItem(
                        text = { Text(customer.name) },
                        onClick = {
                            selectedCustomer = customer.name
                            customerExpanded.value = false
                        },
                        modifier = Modifier.background(Color.White)
                    )
                }
                if (customers.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No customers found") },
                        onClick = { customerExpanded.value = false },
                        modifier = Modifier.background(Color.White)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = timeExpanded.value,
            onExpandedChange = { timeExpanded.value = it }
        ) {
            TextField(
                value = selectedTimeWindow,
                onValueChange = {},
                readOnly = true,
                label = { Text("Time window") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeExpanded.value) },
                colors = textFieldColors,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = timeExpanded.value,
                onDismissRequest = { timeExpanded.value = false },
                modifier = Modifier.background(Color.White)
            ) {
                timeWindows.forEach { window ->
                    DropdownMenuItem(
                        text = { Text(window) },
                        onClick = {
                            selectedTimeWindow = window
                            timeExpanded.value = false
                        },
                        modifier = Modifier.background(Color.White)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = jobDescription,
            onValueChange = { jobDescription = it },
            label = { Text("Job Description") },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (selectedCustomer.isNotBlank() && selectedTimeWindow.isNotBlank() && jobDescription.isNotBlank()) {
                activityScope.launch {
                    try {
                        val task = Task(
                            date = selectedDate,
                            customer = selectedCustomer,
                            timeWindow = selectedTimeWindow,
                            jobDescription = jobDescription
                        )
                        db.taskDao().insertTask(task)
                        firebaseAnalytics.logEvent("task_scheduled") {
                            param("customer", selectedCustomer)
                            param("time_window", selectedTimeWindow)
                            param("description_length", jobDescription.length.toLong())
                        }
                        Toast.makeText(context.applicationContext, "Task Scheduled!", Toast.LENGTH_SHORT).show()
                        onTaskCreated()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.e(TAG, "Error scheduling task", e)
                    }
                }
            }
        }) {
            Text("Schedule")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onBackToSchedule, modifier = Modifier.align(Alignment.Start)) {
            Text("Back")
        }
    }
}

@Composable
fun CustomersScreen(
    modifier: Modifier = Modifier,
    activityScope: CoroutineScope,
    onBackToMenu: () -> Unit = {}
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    
    var customerName by remember { mutableStateOf("") }
    var customers by remember { mutableStateOf(listOf<Customer>()) }

    LaunchedEffect(Unit) {
        customers = db.customerDao().getAllCustomers()
    }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Customers", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopStart))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (customerName.isNotBlank()) {
                activityScope.launch {
                    try {
                        db.customerDao().insertCustomer(Customer(name = customerName))
                        customers = db.customerDao().getAllCustomers()
                        customerName = ""
                        Toast.makeText(context.applicationContext, "Customer Added!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Log.e(TAG, "Error adding customer", e)
                    }
                }
            }
        }) {
            Text("Add Customer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(customers) { customer ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Customer: ${customer.name}", fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (customers.isEmpty()) {
                item {
                    Text(text = "No customers found")
                }
            }
        }

        Button(onClick = onBackToMenu, modifier = Modifier.align(Alignment.Start)) {
            Text("Back")
        }
    }
}

@Composable
fun TimeClockScreen(modifier: Modifier = Modifier, onBackToMenu: () -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Time Clock", fontSize = 26.sp, modifier = Modifier.align(Alignment.TopStart))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Time Clock features coming soon!", fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onBackToMenu, modifier = Modifier.align(Alignment.Start)) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    JobTrackerTheme {
        MainApp(Firebase.analytics, rememberCoroutineScope())
    }
}