package devesh.app.ocr.ui_new

import android.content.Intent
import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

data class AppItem(
    val slug: String,
    val title: String,
    val image: String,
    val icon: String,
    val date: String,
    val description: String,
    val type: String,
    val apklink: String? = null,
    val playstore: String? = null,
    val website: String? = null,
    val github: String? = null
)

const val All_APPS_API_URL = "https://deveshrx.com/apps/all.json"

@Composable
fun MoreAppsScreen() {
    val context = LocalContext.current
    var apps by remember { mutableStateOf<List<AppItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val json = URL(All_APPS_API_URL).readText()
                val listType = object : TypeToken<List<AppItem>>() {}.type
                val fetchedApps: List<AppItem> = Gson().fromJson(json, listType)
                withContext(Dispatchers.Main) {
                    apps = fetchedApps
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }


        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                apps.forEach { app ->
                    AppItemRow(app = app) {
                        val targetUrl = app.playstore ?: app.website ?: app.github ?: "https://deveshrx.com/apps"
                        if (targetUrl != null) {
                            val intent = Intent(Intent.ACTION_VIEW, targetUrl.toUri())
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }

}

@Composable
fun AppItemRow(app: AppItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            update = { imageView ->
                Glide.with(imageView.context)
                    .load(app.icon)
                    .into(imageView)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = app.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = app.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun PreviewMoreAppsScreen() {
    MaterialTheme {
        MoreAppsScreen()
    }
}
