package com.example.hackathonproject.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hackathonproject.viewmodel.AuthState
import com.example.hackathonproject.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

data class Subject(
    val id: String,
    val name: String,
    val code: String,
    val color: Color,
    val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDarkMode by remember { mutableStateOf(false) }

    val subjects = listOf(
        Subject("1", "Engineering Chemistry", "DI01000071", Color(0xFF009688), Icons.Default.Warning),
        Subject("2", "Applied Mathematics", "DI02000011", Color(0xFF2962FF), Icons.Default.Add),
        Subject("3", "Environmental Sustainability", "DI02000051", Color(0xFF00897B), Icons.Default.Home),
        Subject("4", "Indian Constitution", "DI02000061", Color(0xFFEF6C00), Icons.Default.DateRange),
        Subject("5", "Electronics Workshop and Practice", "DI02000091", Color(0xFF6200EA), Icons.Default.Build),
        Subject("6", "Scripting Language", "DI02000101", Color(0xFF304FFE), Icons.Default.PlayArrow),
        Subject("7", "Essence of Indian Knowledge and Tradition", "DI02000131", Color(0xFFD81B60), Icons.Default.Info)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White
            ) {
                HomeDrawerContent(
                    onCloseClick = { scope.launch { drawerState.close() } },
                    isDarkMode = isDarkMode,
                    onThemeToggle = { isDarkMode = it },
                    onLogoutClick = { authViewModel.signout() }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "StudyNest",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            containerColor = Color(0xFFF0F4FC)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome to StudyNest",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(subjects) { subject ->
                        SubjectItem(subject) {
                            navController.navigate(
                                "subject_home/${subject.id}/${subject.name}/${subject.code}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeDrawerContent(
    onCloseClick: () -> Unit,
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Menu", fontSize = 18.sp, color = Color.Gray)

            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF0F4FC),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF2B5CFA),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text("Student Name", fontWeight = FontWeight.SemiBold)
                    Text("bagiamaryam8@gmail.com", fontSize = 12.sp, color = Color.Gray)
                    Text("EN: 246150307008", fontSize = 12.sp, color = Color.Gray)
                    Text("Sem: 3", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(if (isDarkMode) "Dark Mode" else "Light Mode")
            }

            Switch(
                checked = isDarkMode,
                onCheckedChange = onThemeToggle,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF2B5CFA)
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLogoutClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ExitToApp, null, tint = Color.Red)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Logout", color = Color.Red, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SubjectItem(
    subject: Subject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = subject.color,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = subject.icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = subject.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subject.code,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
