package com.penguenlabs.pushnote.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.navigation.Destination
import com.penguenlabs.pushnote.util.Screen
import kotlinx.coroutines.delay

private const val FOCUS_REQUEST_DELAY: Long = 300

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onDialogDismissRequest: () -> Unit,
    onSettingsButtonClick: () -> Unit
) {
    Screen(destination = Destination.Home) {
        Dialog(
            onDismissRequest = onDialogDismissRequest
        ) {
            val textFieldFocusRequester = remember { FocusRequester() }

            val homeScreeState = homeViewModel.homeScreeState

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
                    colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colorScheme.onBackground),
                    isError = homeScreeState.isError
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
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

                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp), onClick = {
                    homeViewModel.sendNotification(
                        pushNotificationText = homeScreeState.textFieldValue,
                        isPinnedNote = homeScreeState.isPinnedNote
                    )
                }) {
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
                    onClick = onSettingsButtonClick
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
            }
        }
    }
}