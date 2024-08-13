package com.example.gallery_mvvm

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gallery_mvvm.ui.theme.Gallery_MVVMTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModels<MainViewModel> {
        MainViewModel.Factory
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            Gallery_MVVMTheme {
                RequestPermissions(viewModel)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GridImages(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GridImages(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    val media = viewModel.media.collectAsState().value

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 100.dp), modifier) {

        items(media) { imageItem ->

            PhotoItem(imageItem, LocalContext.current)

        }
    }

}

@Composable
fun PhotoItem(images: Media, context: Context) {

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ){

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(images.uri)
                .placeholder(R.drawable.ic_launcher_foreground)
                .build(),
            contentDescription = "",

            modifier = Modifier
                .padding(4.dp)
                .size(100.dp, 250.dp)
                .clickable {

                    Toast.makeText(context, "Latitude : ${images.latitude} Longitude: ${images.longitude} ", Toast.LENGTH_SHORT).show()

                }
                .clip(CircleShape),
            contentScale = ContentScale.Crop,

        )
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(viewModel: MainViewModel) {

    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted
            Log.d("Permission", "RequestPermissions: Granted")
            viewModel.getImagesFromRepo()
        } else {
            // Handle permission denial
            Log.d("Permission", "RequestPermissions: Denied")

        }
    }

    LaunchedEffect(permissionState) {
        if (!permissionState.status.isGranted && permissionState.status.shouldShowRationale) {
            // Show rationale if needed
        } else {
            Log.d("Permission", "RequestPermissions: requesting")
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

        }
    }



}
