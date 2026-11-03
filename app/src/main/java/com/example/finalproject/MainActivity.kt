package com.example.ecoplmun

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

val EcoGreen = Color(0xFF008A55)
val EcoDarkGreen = Color(0xFF006B43)
val EcoLightGreen = Color(0xFFE8F7EF)
val EcoYellow = Color(0xFFFFF3CD)
val EcoBlue = Color(0xFFE4F1FF)

data class EnvironmentalReport(
    val id: Int,
    val issueType: String,
    val description: String,
    val location: String,
    val date: String,
    val status: String,
    val imageUri: String?
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoPLMUNTheme {
                EcoPLMUNApp()
            }
        }
    }
}

@Composable
fun EcoPLMUNTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

@Composable
fun EcoPLMUNApp() {
    var currentScreen by remember { mutableStateOf("splash") }
    var reports by remember { mutableStateOf(emptyList<EnvironmentalReport>()) }
    var selectedReport by remember { mutableStateOf<EnvironmentalReport?>(null) }
    var loggedInEmail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        delay(2000)
        currentScreen = "login"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        when (currentScreen) {
            "splash" -> SplashScreen()

            "login" -> LoginScreen(
                onLogin = {
                    loggedInEmail = it
                    currentScreen = "home"
                },
                onRegister = {
                    currentScreen = "register"
                }
            )

            "register" -> RegisterScreen(
                onBack = {
                    currentScreen = "login"
                },
                onRegister = {
                    loggedInEmail = it
                    currentScreen = "home"
                }
            )

            "home" -> HomeScreen(
                reports = reports,
                onNavigate = {
                    currentScreen = it
                }
            )

            "report" -> ReportScreen(
                onBack = {
                    currentScreen = "home"
                },
                onSubmit = {
                    reports = reports + it
                    currentScreen = "reports"
                }
            )

            "reports" -> ReportsScreen(
                reports = reports,
                onBack = {
                    currentScreen = "home"
                },
                onReportClick = {
                    selectedReport = it
                    currentScreen = "details"
                }
            )

            "details" -> selectedReport?.let {
                ReportDetailsScreen(
                    report = it,
                    onBack = {
                        currentScreen = "reports"
                    }
                )
            }

            "dashboard" -> DashboardScreen(
                reports = reports,
                onBack = {
                    currentScreen = "home"
                }
            )

            "information" -> EnvironmentalInformationScreen {
                currentScreen = "home"
            }

            "notifications" -> NotificationsScreen {
                currentScreen = "home"
            }

            "feedback" -> FeedbackScreen {
                currentScreen = "home"
            }

            "profile" -> ProfileScreen(
                email = loggedInEmail,
                onBack = {
                    currentScreen = "home"
                },
                onLogout = {
                    loggedInEmail = ""
                    currentScreen = "login"
                }
            )
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "EcoPLMUN Logo",
                modifier = Modifier.size(130.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "EcoPLMUN",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = EcoGreen
            )

            Spacer(Modifier.height(8.dp))

            Text("A Mobile Environmental", fontSize = 18.sp)
            Text("Monitoring System", fontSize = 18.sp)

            Spacer(Modifier.height(35.dp))

            Text(
                "PAMANTASAN NG LUNGSOD NG MUNTINLUPA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EcoDarkGreen
            )

            Spacer(Modifier.height(50.dp))

            Text(
                "A Cleaner Campus for a Greener Tomorrow",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LoginScreen(
    onLogin: (String) -> Unit,
    onRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoLightGreen)
            .padding(25.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = "EcoPLMUN Logo",
                    modifier = Modifier.size(90.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "EcoPLMUN",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = EcoGreen
                )

                Text(
                    "Environmental Monitoring System",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(25.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Email, null)
                    },
                    label = {
                        Text("Email")
                    },
                    placeholder = {
                        Text("Enter your email")
                    },
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, null)
                    },
                    label = {
                        Text("Password")
                    },
                    placeholder = {
                        Text("Enter your password")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        errorMessage,
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            onLogin(email)
                        } else {
                            errorMessage =
                                "Please enter your email and password."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "LOG IN",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Don't have an account?",
                        color = Color.Gray
                    )

                    TextButton(onClick = onRegister) {
                        Text(
                            "Register",
                            color = EcoGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegister: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Account")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Register for EcoPLMUN",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Create your student account.",
                    color = Color.Gray
                )

                Spacer(Modifier.height(15.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Full Name")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Person, null)
                    }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Email")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Email, null)
                    }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Password")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, null)
                    },
                    visualTransformation = PasswordVisualTransformation()
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Confirm Password")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, null)
                    },
                    visualTransformation = PasswordVisualTransformation()
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        when {
                            name.isBlank() ||
                                    email.isBlank() ||
                                    password.isBlank() -> {
                                errorMessage =
                                    "Please complete all fields."
                            }

                            password != confirmPassword -> {
                                errorMessage =
                                    "Passwords do not match."
                            }

                            else -> {
                                onRegister(email)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    )
                ) {
                    Text(
                        "CREATE ACCOUNT",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    reports: List<EnvironmentalReport>,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "EcoPLMUN",
                        fontWeight = FontWeight.Bold,
                        color = EcoGreen
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onNavigate("notifications")
                        }
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            "Notifications"
                        )
                    }

                    IconButton(
                        onClick = {
                            onNavigate("profile")
                        }
                    ) {
                        Icon(
                            Icons.Default.Person,
                            "Profile"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selected = "home",
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Hello, Student! 👋",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Help keep PLMUN clean and sustainable.",
                    color = Color.Gray
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        onNavigate("report")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, null)

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "REPORT ENVIRONMENTAL ISSUE",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    "Quick Access",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickCard(
                        "🌱",
                        "Eco Tips",
                        Modifier.weight(1f)
                    ) {
                        onNavigate("information")
                    }

                    QuickCard(
                        "📊",
                        "Dashboard",
                        Modifier.weight(1f)
                    ) {
                        onNavigate("dashboard")
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickCard(
                        "🔔",
                        "Notifications",
                        Modifier.weight(1f)
                    ) {
                        onNavigate("notifications")
                    }

                    QuickCard(
                        "💬",
                        "Feedback",
                        Modifier.weight(1f)
                    ) {
                        onNavigate("feedback")
                    }
                }

                Spacer(Modifier.height(15.dp))

                Text(
                    "Recent Reports",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (reports.isEmpty()) {
                item {
                    EmptyReportsCard()
                }
            } else {
                items(reports.takeLast(3).reversed()) { report ->
                    ReportCard(
                        report = report,
                        onClick = {
                            onNavigate("reports")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuickCard(
    emoji: String,
    title: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 30.sp)

            Spacer(Modifier.height(5.dp))

            Text(
                title,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EmptyReportsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7FAF8)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📄", fontSize = 45.sp)

            Spacer(Modifier.height(8.dp))

            Text(
                "No reports submitted yet.",
                fontWeight = FontWeight.Bold
            )

            Text(
                "Start monitoring environmental issues on your campus.",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onBack: () -> Unit,
    onSubmit: (EnvironmentalReport) -> Unit
) {
    var issueType by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val imagePicker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            imageUri = uri
        }

    val issueTypes = listOf(
        "Littering",
        "Improper Waste Disposal",
        "Overflowing Trash Bin",
        "Dirty Area",
        "Drainage Problem",
        "Plastic Waste",
        "Other Environmental Concern"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Report Issue")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "Environmental Issue",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "Issue Type",
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            expanded = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (issueType.isEmpty()) {
                                "Select Issue Type"
                            } else {
                                issueType
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        issueTypes.forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Text(type)
                                },
                                onClick = {
                                    issueType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Text(
                    "Description",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    placeholder = {
                        Text(
                            "Describe the environmental problem..."
                        )
                    }
                )

                Text(
                    "Photo Evidence",
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = {
                        imagePicker.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.PhotoLibrary,
                        null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        if (imageUri == null) {
                            "Upload Picture"
                        } else {
                            "Picture Selected ✓"
                        }
                    )
                }

                imageUri?.let { uri ->
                    Spacer(Modifier.height(8.dp))

                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                scaleType =
                                    ImageView.ScaleType.CENTER_CROP
                            }
                        },
                        update = {
                            it.setImageURI(uri)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                Text(
                    "Location",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            null
                        )
                    },
                    placeholder = {
                        Text("Enter location")
                    }
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        when {
                            issueType.isBlank() -> {
                                errorMessage =
                                    "Please select an issue type."
                            }

                            description.isBlank() -> {
                                errorMessage =
                                    "Please enter a description."
                            }

                            location.isBlank() -> {
                                errorMessage =
                                    "Please enter the location."
                            }

                            else -> {
                                val date =
                                    SimpleDateFormat(
                                        "MMMM dd, yyyy",
                                        Locale.getDefault()
                                    ).format(Date())

                                onSubmit(
                                    EnvironmentalReport(
                                        id = Random.nextInt(1000, 9999),
                                        issueType = issueType,
                                        description = description,
                                        location = location,
                                        date = date,
                                        status = "Pending",
                                        imageUri = imageUri?.toString()
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcoGreen
                    )
                ) {
                    Icon(Icons.Default.Send, null)

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "SUBMIT REPORT",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    reports: List<EnvironmentalReport>,
    onBack: () -> Unit,
    onReportClick: (EnvironmentalReport) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Reports")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📄", fontSize = 55.sp)

                    Text(
                        "No reports submitted yet.",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Submit an environmental issue to see it here.",
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reports.reversed()) { report ->
                    ReportCard(
                        report = report,
                        onClick = {
                            onReportClick(report)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReportCard(
    report: EnvironmentalReport,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    report.issueType,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Text(
                    report.status,
                    color = Color(0xFFF57C00),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            report.imageUri?.let { uriString ->
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType =
                                ImageView.ScaleType.CENTER_CROP
                        }
                    },
                    update = {
                        it.setImageURI(Uri.parse(uriString))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(Modifier.height(8.dp))
            }

            Text(report.description)

            Spacer(Modifier.height(8.dp))

            Row {
                Icon(
                    Icons.Default.LocationOn,
                    null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    report.location,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(5.dp))

            Text(
                "Report #${report.id}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                report.date,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(5.dp))

            Text(
                "View Details  ›",
                color = EcoGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailsScreen(
    report: EnvironmentalReport,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Report Details")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                report.imageUri?.let { uriString ->
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                scaleType =
                                    ImageView.ScaleType.CENTER_CROP
                            }
                        },
                        update = {
                            it.setImageURI(
                                Uri.parse(uriString)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                    )

                    Spacer(Modifier.height(10.dp))
                }

                Text(
                    report.issueType,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                DetailRow(
                    "Report Type",
                    report.issueType
                )

                DetailRow(
                    "Description",
                    report.description
                )

                DetailRow(
                    "Location",
                    report.location
                )

                DetailRow(
                    "Date",
                    report.date
                )

                DetailRow(
                    "Status",
                    report.status
                )
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                label,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(3.dp))

            Text(
                value,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    reports: List<EnvironmentalReport>,
    onBack: () -> Unit
) {
    val total = reports.size
    val pending = reports.count {
        it.status == "Pending"
    }
    val underReview = reports.count {
        it.status == "Under Review"
    }
    val resolved = reports.count {
        it.status == "Resolved"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Environmental Dashboard")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "Monitoring Summary",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardSmallCard(
                        "Total Reports",
                        total.toString(),
                        Modifier.weight(1f),
                        EcoLightGreen
                    )

                    DashboardSmallCard(
                        "Pending",
                        pending.toString(),
                        Modifier.weight(1f),
                        EcoYellow
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardSmallCard(
                        "Under Review",
                        underReview.toString(),
                        Modifier.weight(1f),
                        EcoBlue
                    )

                    DashboardSmallCard(
                        "Resolved",
                        resolved.toString(),
                        Modifier.weight(1f),
                        EcoLightGreen
                    )
                }

                Spacer(Modifier.height(15.dp))

                Text(
                    "Common Environmental Issues",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                IssueCountRow(
                    "Improper Waste Disposal",
                    reports.count {
                        it.issueType ==
                                "Improper Waste Disposal"
                    }
                )

                IssueCountRow(
                    "Littering",
                    reports.count {
                        it.issueType == "Littering"
                    }
                )

                IssueCountRow(
                    "Dirty Area",
                    reports.count {
                        it.issueType == "Dirty Area"
                    }
                )

                IssueCountRow(
                    "Drainage Problem",
                    reports.count {
                        it.issueType == "Drainage Problem"
                    }
                )

                IssueCountRow(
                    "Plastic Waste",
                    reports.count {
                        it.issueType == "Plastic Waste"
                    }
                )
            }
        }
    }
}

@Composable
fun DashboardSmallCard(
    title: String,
    number: String,
    modifier: Modifier,
    background: Color
) {
    Card(
        modifier = modifier.padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                title,
                fontSize = 12.sp,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(8.dp))

            Text(
                number,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun IssueCountRow(
    title: String,
    count: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Delete,
                null,
                tint = EcoGreen
            )

            Spacer(Modifier.width(12.dp))

            Text(
                title,
                modifier = Modifier.weight(1f)
            )

            Text(
                count.toString(),
                fontWeight = FontWeight.Bold,
                color = EcoGreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvironmentalInformationScreen(
    onBack: () -> Unit
) {
    val tips = listOf(
        "♻️ Proper Waste Segregation" to
                "Separate biodegradable, recyclable, residual, and other waste properly.",
        "🗑 Proper Waste Disposal" to
                "Always dispose of waste in the correct designated bin.",
        "🌱 Protect the Environment" to
                "Avoid unnecessary plastic use and help maintain clean surroundings.",
        "💧 Save Water" to
                "Avoid wasting water and report leaks when you notice them.",
        "🌳 Keep the Campus Clean" to
                "Everyone can help maintain a clean and sustainable PLMUN campus."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Environmental Information")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tips) { tip ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EcoLightGreen
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            tip.first,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            tip.second,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit
) {
    val notifications = listOf(
        "🔔 EcoPLMUN" to
                "Welcome to the EcoPLMUN Environmental Monitoring System.",
        "🌱 Environmental Reminder" to
                "Please help maintain cleanliness around the PLMUN campus.",
        "📢 Eco Activity" to
                "Check the latest environmental activities and announcements."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Notifications")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(notifications) { notification ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            notification.first,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(notification.second)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    onBack: () -> Unit
) {
    var feedback by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("User Feedback")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "We value your feedback.",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Your thoughts help us improve the EcoPLMUN app.",
                color = Color.Gray
            )

            Spacer(Modifier.height(15.dp))

            OutlinedTextField(
                value = feedback,
                onValueChange = {
                    feedback = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                placeholder = {
                    Text(
                        "Write your comments or suggestions..."
                    )
                }
            )

            Spacer(Modifier.height(15.dp))

            Button(
                onClick = {
                    if (feedback.isNotBlank()) {
                        submitted = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreen
                )
            ) {
                Icon(Icons.Default.Send, null)

                Spacer(Modifier.width(8.dp))

                Text("SUBMIT FEEDBACK")
            }

            if (submitted) {
                Spacer(Modifier.height(15.dp))

                Text(
                    "Thank you for your feedback! ✓",
                    color = EcoGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    email: String,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "EcoPLMUN Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "EcoPLMUN Student",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                email,
                color = Color.Gray
            )

            Spacer(Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        "Account Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Text("Role: Student")
                    Text("Organization: PLMUN")
                    Text("System: EcoPLMUN")
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "LOG OUT",
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selected: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == "home",
            onClick = {
                onNavigate("home")
            },
            icon = {
                Icon(
                    Icons.Default.Home,
                    "Home"
                )
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = selected == "reports",
            onClick = {
                onNavigate("reports")
            },
            icon = {
                Icon(
                    Icons.Default.Assignment,
                    "Reports"
                )
            },
            label = {
                Text("Reports")
            }
        )

        NavigationBarItem(
            selected = selected == "dashboard",
            onClick = {
                onNavigate("dashboard")
            },
            icon = {
                Icon(
                    Icons.Default.Dashboard,
                    "Monitor"
                )
            },
            label = {
                Text("Monitor")
            }
        )
    }
}
