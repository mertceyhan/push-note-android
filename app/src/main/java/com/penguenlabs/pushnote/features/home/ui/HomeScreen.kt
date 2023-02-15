package com.penguenlabs.pushnote.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
                    .background(color = colors.background)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.push_note),
                    style = typography.h6,
                    color = colors.onBackground
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
                    colors = TextFieldDefaults.textFieldColors(textColor = colors.onBackground),
                    isError = homeScreeState.isError
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = homeScreeState.isPinnedNote,
                        onCheckedChange = homeViewModel::onCheckedChange
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.pinned_note),
                        color = colors.onBackground,
                        style = typography.body2
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
                    Text(text = stringResource(id = R.string.push), color = colors.onPrimary)
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
                    Text(text = stringResource(id = R.string.settings), color = colors.onBackground)
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