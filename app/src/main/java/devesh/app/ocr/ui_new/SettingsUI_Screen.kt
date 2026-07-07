package devesh.app.ocr.ui_new

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import devesh.app.ocr.BuildConfig
import devesh.app.ocr.R
import devesh.app.ocr.billing.BillingActivity
import devesh.app.ocr.utils.CachePref
import devesh.app.ocr.utils.InstallSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsUI_Screen(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val cachePref = remember { CachePref(context) }

    val prefIsMultiline = stringResource(R.string.Pref_isMultiline)
    var isSingleLine by remember(prefIsMultiline) {
        mutableStateOf(cachePref.getBoolean(prefIsMultiline))
    }

    val prefIsSubscribed = stringResource(R.string.Pref_isSubscribed)
    val isSubscribed by remember(prefIsSubscribed) {
        mutableStateOf(cachePref.getBoolean(prefIsSubscribed))
    }

    val languageOptions = listOf(
        "Default (English)",
        "Devanagari देवनागरी",
        "Japanese 日本",
        "Korean 한국인",
        "Chinese 中國人"
    )
    val selectedLanguageIndex by remember {
        val lang = cachePref.getString("ocrlang")
        mutableIntStateOf(lang?.toIntOrNull() ?: 0)
    }

    val playStoreUrl = stringResource(R.string.PLAY_STORE_URL)
    val galaxyStoreUrl = stringResource(R.string.GALAXY_STORE_URL)
    val appName = stringResource(R.string.app_name)
    MaterialTheme(
        //colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
        colorScheme = darkColorScheme()
    ) {
        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // App Info Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Made with ❤️ in India",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider()

                SettingsCategoryHeader("Settings")

                // Premium Plan
                SettingsItem(
                    title = "Premium Plan",
                    subtitle = if (isSubscribed) "Thank You for Subscribing" else "Get Ad-Free Experience",
                    icon = if (isSubscribed) Icons.Default.Favorite else Icons.Default.Diamond,
                    iconColor = if (isSubscribed) Color.Red else Color(0xFFFFD700),
                    onClick = {
                        context.startActivity(Intent(context, BillingActivity::class.java))
                    }
                )

                // Single Line Text Switch
                SettingsSwitchItem(
                    title = "Single Line Text",
                    subtitle = "After scan, convert multiline text paragraph into single line",
                    checked = isSingleLine,
                    onCheckedChange = {
                        isSingleLine = it
                        cachePref.setBoolean(prefIsMultiline, it)
                    }
                )

                // OCR Language
                SettingsItem(
                    title = "OCR Language",
                    subtitle = languageOptions.getOrElse(selectedLanguageIndex) { languageOptions[0] },
                    onClick = {
                        // Logic for changing language could be added here
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                SettingsCategoryHeader("About")

                SettingsItem(
                    title = "Rate App 5 Stars",
                    onClick = {
                        val url = if (InstallSource.isGalaxyStore(context)) {
                            galaxyStoreUrl
                        } else {
                            playStoreUrl
                        }
                        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                    }
                )

                SettingsItem(
                    title = "Share with Friends",
                    onClick = {
                        val url = if (InstallSource.isGalaxyStore(context)) {
                            galaxyStoreUrl
                        } else {
                            playStoreUrl
                        }
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "$appName $url")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, null))
                    }
                )

                SettingsItem(
                    title = "Version",
                    subtitle = BuildConfig.VERSION_NAME
                )

                SettingsItem(
                    title = "Build Number",
                    subtitle = BuildConfig.VERSION_CODE.toString()
                )

                SettingsItem(
                    title = "Check for Update",
                    onClick = {
                        val url = if (InstallSource.isGalaxyStore(context)) {
                            galaxyStoreUrl
                        } else {
                            playStoreUrl
                        }
                        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                    }
                )
                SettingsItem(
                    title = "Official Website",
                    subtitle = "https://DeveshRx.com",
                    onClick = {
                        val url = "https://DeveshRx.com"
                        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                SettingsCategoryHeader("Devesh Rx Apps & Games")
                MoreAppsScreen()

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

}

@Composable
fun SettingsCategoryHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsUI_ScreenPreview() {
    MaterialTheme {
        SettingsUI_Screen()
    }
}
