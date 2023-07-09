package com.penguenlabs.pushnote.features.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.permissions.*
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.navigation.Destination
import com.penguenlabs.pushnote.util.Screen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionScreen(
    pushNotificationText: String = "",
    onBackPressClick: () -> Unit = {},
    onPermissionGranted: (pushNotificationText: String) -> Unit = {}
) {
    Screen(destination = Destination.NotificationPermission) {
        val lifecycleOwner = LocalLifecycleOwner.current
        var isPermissionDenied = rememberSaveable { false }
        val notificationPermissionState: PermissionState? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS,
                    onPermissionResult = { isGranted ->
                        if (isGranted) {
                            onPermissionGranted(pushNotificationText)
                        }
                        isPermissionDenied = !isGranted
                    })
            } else {
                null
            }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.permission),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onBackPressClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(id = R.string.back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val context = LocalContext.current
                Text(
                    text = stringResource(id = R.string.please_turn_on_notifications),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.notification_permission_description),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    fontSize = 16.sp
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp), onClick = {
                        when {
                            notificationPermissionState?.status?.isGranted == true -> {
                                onPermissionGranted(pushNotificationText)
                            }

                            (notificationPermissionState?.status?.shouldShowRationale == true) or isPermissionDenied -> {
                                openAppNotificationDeviceSettings(context)
                            }

                            else -> {
                                notificationPermissionState?.launchPermissionRequest()
                            }
                        }
                    }, shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = stringResource(id = R.string.allow))
                }
            }
        }
        DisposableEffect(key1 = lifecycleOwner, effect = {
            val lifecycleObserver = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME && notificationPermissionState?.status?.isGranted == true) {
                    onPermissionGranted(pushNotificationText)
                }
            }
            lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            }
        })
    }
}

private fun openAppNotificationDeviceSettings(context: Context) {
    val intent = Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }

            else -> {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + context.packageName)
            }
        }
    }
    context.startActivity(intent)
}