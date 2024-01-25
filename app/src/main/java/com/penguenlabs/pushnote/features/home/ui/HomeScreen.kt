package com.penguenlabs.pushnote.features.home.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mertceyhan.compose.inappreviews.rememberInAppReviewManager
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.navigation.Destination
import com.penguenlabs.pushnote.util.Screen
import com.penguenlabs.pushnote.util.findActivity
import kotlinx.coroutines.delay

private const val FOCUS_REQUEST_DELAY: Long = 300

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    pushNotificationText: String = "",
    onDialogDismissRequest: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onNotificationPermissionNeed: (pushNotificationText: String) -> Unit
) {
    Screen(destination = Destination.Home, backgroundColor = Color.Transparent) {
        Dialog(
            onDismissRequest = onDialogDismissRequest
        ) {
            val textFieldFocusRequester = remember { FocusRequester() }
            val homeScreeState = homeViewModel.homeScreeState
            val notificationPermissionState: PermissionState? =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    rememberPermissionState(
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                } else {
                    null
                }
            val inAppReviewManager = rememberInAppReviewManager()
            val context = LocalContext.current

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.push_note),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(textFieldFocusRequester),
                    value = homeScreeState.textFieldValue,
                    onValueChange = homeViewModel::onTextFieldValueChange,
                    placeholder = {
                        Text(text = stringResource(id = R.string.your_note_goes_here))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        homeViewModel.sendNotification(
                            pushNotificationText = homeScreeState.textFieldValue,
                            isPinnedNote = homeScreeState.isPinnedNote
                        )
                    }),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.onBackground),
                    isError = homeScreeState.isError
                )
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        val hapticFeedback = LocalHapticFeedback.current

                        Checkbox(checked = homeScreeState.isPinnedNote, onCheckedChange = {
                            homeViewModel.onCheckedChange(it)
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        })
                    }
                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.pinned_note),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(
                    modifier = Modifier.height(32.dp)
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp), onClick = {
                        if (notificationPermissionState?.status?.isGranted?.not() == true) {
                            onNotificationPermissionNeed(homeScreeState.textFieldValue)
                        } else {
                            homeViewModel.sendNotification(
                                pushNotificationText = homeScreeState.textFieldValue,
                                isPinnedNote = homeScreeState.isPinnedNote
                            )
                        }
                    }, shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.push),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    onClick = onSettingsButtonClick,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.settings),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            LaunchedEffect(Unit) {
                homeViewModel.updateScreenState()
                delay(timeMillis = FOCUS_REQUEST_DELAY)
                textFieldFocusRequester.requestFocus()

                if (pushNotificationText.isNotEmpty() or pushNotificationText.isNotBlank()) {
                    homeViewModel.sendNotification(
                        pushNotificationText, homeScreeState.isPinnedNote
                    )
                }
            }

            LaunchedEffect(Unit) {
                homeViewModel.inAppReviewLauncher.collect {
                    inAppReviewManager.launchReviewFlow(context.findActivity())
                }
            }
        }
    }
}