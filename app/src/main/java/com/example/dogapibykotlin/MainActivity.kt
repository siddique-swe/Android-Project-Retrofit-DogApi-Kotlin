package com.example.dogapibykotlin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.example.dogapibykotlin.Network.DogResponse
import com.example.dogapibykotlin.Network.RetrofitInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogApp()
        }
    }
}

@Composable
fun DogApp() {
    var imageUrl by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Fetching a dog image...") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUrl.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = "Dog Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.doggo),
                contentDescription = "Dog Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = message)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val response: Response<DogResponse> = RetrofitInstance.getApiService().getRandomImage()

                    // Ensure UI updates on the main thread
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val dogImageUrl = response.body()?.message
                            if (!dogImageUrl.isNullOrEmpty()) {
                                imageUrl = dogImageUrl
                                message = "Here is a random dog!"
                            } else {
                                message = "No image found!"
                            }
                        } else {
                            message = "Failed to load image! Error: ${response.code()}"
                        }
                    }
                } catch (e: Exception) {
                    // Handle network failures
                    withContext(Dispatchers.Main) {
                        message = "Network error: ${e.localizedMessage}"
                        //Toast.makeText(LocalContext.current, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }) {
            Text(text = "Fetch another image")
        }
    }
}
