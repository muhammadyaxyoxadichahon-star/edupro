package com.edupro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF14213D)
private val Blue = Color(0xFF3267E3)
private val Page = Color(0xFFF4F6FB)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EduProApp() }
    }
}

@Composable
fun EduProApp() {
    var grade by remember { mutableIntStateOf(7) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var completed by remember { mutableIntStateOf(0) }
    val subjects = listOf("Matematika", "Ona tili", "Informatika", "Tarix", "Ingliz tili", "Fizika", "Biologiya")
    MaterialTheme(colorScheme = lightColorScheme(primary = Blue, background = Page)) {
        Scaffold(
            containerColor = Page,
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(selectedTab == 0, { selectedTab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Bosh sahifa") })
                    NavigationBarItem(selectedTab == 1, { selectedTab = 1 }, icon = { Icon(Icons.Default.MenuBook, null) }, label = { Text("Fanlar") })
                    NavigationBarItem(selectedTab == 2, { selectedTab = 2 }, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profil") })
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> HomeScreen(grade, { grade = it }, completed, padding)
                1 -> SubjectsScreen(subjects, grade, { completed++ }, padding)
                else -> ProfileScreen(grade, completed, padding)
            }
        }
    }
}

@Composable
fun HomeScreen(grade: Int, onGrade: (Int) -> Unit, completed: Int, padding: PaddingValues) {
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("EDUPRO", color = Blue, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            Text("Bilim sari ishonchli qadam!", color = Navy, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text("Bugungi o'qish rejangizni boshlang.", color = Color.Gray)
        }
        item {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Navy)) {
                Column(Modifier.padding(18.dp)) {
                    Text("Sinfingizni tanlang", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        (5..11).forEach { n ->
                            FilterChip(selected = grade == n, onClick = { onGrade(n) }, label = { Text("$n") },
                                colors = FilterChipDefaults.filterChipColors(labelColor = Color.White, selectedLabelColor = Navy))
                        }
                    }
                    Text("$grade-sinf uchun darslar", color = Color.White.copy(alpha = .8f))
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Tugatilgan mavzu", "$completed", Modifier.weight(1f))
                StatCard("Natija", if (completed == 0) "0%" else "Boshladik", Modifier.weight(1f))
            }
        }
        item { Text("O'rganishni davom ettiring", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Navy) }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, null, tint = Blue, modifier = Modifier.size(38.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Fanlar bo'limi", fontWeight = FontWeight.Bold)
                        Text("Mavzular va darslarni ochish uchun Fanlar bo'limiga o'ting.", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp)) {
            Text(value, color = Blue, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(title, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun SubjectsScreen(subjects: List<String>, grade: Int, onComplete: () -> Unit, padding: PaddingValues) {
    var opened by remember { mutableStateOf<String?>(null) }
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("$grade-sinf fanlari", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy) }
        items(subjects) { subject ->
            Card(Modifier.fillMaxWidth().clickable { opened = if (opened == subject) null else subject },
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MenuBook, null, tint = Blue)
                        Spacer(Modifier.width(12.dp))
                        Text(subject, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        Icon(if (opened == subject) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null)
                    }
                    if (opened == subject) {
                        Spacer(Modifier.height(10.dp))
                        Text("1-mavzu: Kirish va asosiy tushunchalar", color = Color.Gray)
                        Text("Video dars havolasi keyingi bosqichda mavzuga mos YouTube manbasi bilan qo'shiladi.", fontSize = 12.sp, color = Color.Gray)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = onComplete) { Text("Mavzuni tugatdim") }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(grade: Int, completed: Int, padding: PaddingValues) {
    Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Profil", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Navy)
        Icon(Icons.Default.AccountCircle, null, tint = Blue, modifier = Modifier.size(64.dp))
        Text("EduPro o'quvchisi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Sinf: $grade")
        Text("Tugatilgan mavzular: $completed")
        Text("Bu boshlang'ich versiya. Profilni ro'yxatdan o'tish va bulutli saqlash bilan keyin bog'laymiz.", color = Color.Gray)
    }
}
