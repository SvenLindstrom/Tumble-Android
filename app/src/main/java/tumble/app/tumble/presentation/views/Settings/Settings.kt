package tumble.app.tumble.presentation.views.Settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.Types.appearanceTypeToStringResource
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.Buttons.SettingsExternalButton
import tumble.app.tumble.presentation.views.Settings.Buttons.SettingsNavigationButton
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.Settings.List.SettingsListGroup
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val appearance = viewModel.appearance.collectAsState()
    var showShareSheet by remember { mutableStateOf(false) }

    val currentLocale = Locale.getDefault().displayLanguage
    val appVersion = "1.0.0" // Replace with actual version retrieval logic
    val context = LocalContext.current

    val externalNav = {uri:String ->
        startActivity(
            context,
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            ),
            null)
    }


    Scaffold (
        topBar = {
            BackNav(
                onClick = {navController.popBackStack()},
                label = stringResource(R.string.account),
                title = stringResource(R.string.settings)
            )
        },
    ){padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(padding)
            ) {
                SettingsList {
                    SettingsListGroup {
                        SettingsNavigationButton(
                            title = stringResource(R.string.appearance),
                            current =  appearanceTypeToStringResource(appearance.value),
                            leadingIcon = Icons.Default.DarkMode,
                            leadingIconBackgroundColor = MaterialTheme.colors.primary,
                            destination = { navController.navigate(Routes.accountSettingsAppearance) }
                        )
                        Divider()
                SettingsExternalButton(
                    title = stringResource(R.string.language),
                    current = currentLocale,
                    leadingIcon = Icons.Default.Language,
                    leadingIconBackgroundColor = Color.Blue,
                    action = {
                        startActivity(context, Intent(Settings.ACTION_LOCALE_SETTINGS), null)
                    },
                )
                    }
                    SettingsListGroup {
                        SettingsNavigationButton(
                            title = stringResource(R.string.notification_offset),
                            leadingIcon = Icons.Default.AccessTime,
                            leadingIconBackgroundColor = Color.Red,
                            destination = {
                                navController.navigate(Routes.accountSettingsNotifications)
                            }
                        )
                        Divider()
                        SettingsNavigationButton(
                            title = stringResource(R.string.bookmark),
                            leadingIcon = Icons.Default.Bookmark,
                            leadingIconBackgroundColor = Color.Gray,
                            destination = { navController.navigate(Routes.accountSettingsBookmarks) }
                        )
                    }
                    SettingsListGroup {
                SettingsExternalButton(
                    title = stringResource(id = R.string.review_app),
                    leadingIcon = Icons.Default.Star,
                    leadingIconBackgroundColor = Color.Yellow,
                    action = { externalNav("market://details?id=com.tumble.kronoxtoapp") },// might work
                )
//              Divider()
//                SettingsExternalButton(
//                    title = stringResource(id = R.string.share_feedback),
//                    leadingIcon = Icons.Default.Email,
//                    leadingIconBackgroundColor = Color.Blue,
//                    action = { externalNav("http://www.google.com") }, //
//                )
                        Divider()
                        SettingsExternalButton(
                            title = stringResource(R.string.share_app),
                            leadingIcon = Icons.Default.Share,
                            leadingIconBackgroundColor = Color.Green,
                            action = { showShareSheet = true }
                        )
                    }
                    SettingsListGroup {
                SettingsExternalButton(
                    title = stringResource(id = R.string.github),
                    leadingIcon = Icons.Default.Code,
                    leadingIconBackgroundColor = Color.Black,
                    action = { externalNav("https://github.com/tumble-for-kronox/Tumble-Android") },
                )
                    }

                    if (appVersion != null) {
                        Text(
                            text = "Tumble, Android v.$appVersion",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.padding(35.dp)
                        )
                    }
                }
            }
        }

    if (showShareSheet) {
        ShareSheet(context = LocalContext.current) {
            showShareSheet = false
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackNav(
    onClick: () -> Unit,
    label: String = "",
    title: String = ""
){
    CenterAlignedTopAppBar(
        navigationIcon =
        {
            IconButton(onClick = { onClick() })
            {
                Row {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Text(text = label,
                        color = MaterialTheme.colors.primary)
                }
            }
        },
        title = { Text(
            text = title,
            fontSize = 20.sp
        )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colors.background)
    )
}