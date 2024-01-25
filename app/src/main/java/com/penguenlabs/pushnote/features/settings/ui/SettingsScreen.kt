package com.penguenlabs.pushnote.features.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.penguenlabs.pushnote.BuildConfig
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.navigation.Destination
import com.penguenlabs.pushnote.util.APP_URL
import com.penguenlabs.pushnote.util.PENGUENLABS_MAIL
import com.penguenlabs.pushnote.util.Screen

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onDarkModeChange: (Boolean) -> Unit,
    onHistoryClick: () -> Unit,
    onBackPressClick: () -> Unit,
) {
    val context = LocalContext.current

    Screen(context = context, destination = Destination.Settings) {
        Column {
            val scrollableState = rememberScrollState()
            val settingsScreenState = settingsViewModel.settingsScreenState

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.settings),
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
                    .fillMaxWidth()
                    .verticalScroll(scrollableState)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = stringResource(id = R.string.general_settings),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )
                SettingItem(
                    settingTitle = stringResource(id = R.string.history),
                    settingDescription = stringResource(id = R.string.your_previous_notes),
                    settingIcon = painterResource(
                        id = R.drawable.ic_history
                    ),
                    onItemClick = onHistoryClick
                )
                SettingSwitchableItem(settingTitle = stringResource(id = R.string.dark_mode),
                    settingDescription = stringResource(id = R.string.enable_dark_mode),
                    settingIcon = painterResource(id = R.drawable.ic_night_mode),
                    isChecked = settingsScreenState.darkModeEnabled,
                    onCheckedChange = {
                        settingsViewModel.setDarkModeUserDefault(it)
                        onDarkModeChange(it)
                    })
                SettingSwitchableItem(
                    settingTitle = stringResource(id = R.string.default_pinned_note),
                    settingDescription = stringResource(id = R.string.select_pinned_note_by_default),
                    settingIcon = painterResource(id = R.drawable.ic_checkbox),
                    isChecked = settingsScreenState.defaultPinnedNoteEnabled,
                    onCheckedChange = settingsViewModel::setPinnedNoteUserDefault
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = stringResource(id = R.string.support_settings),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )
                SettingItem(
                    settingTitle = stringResource(id = R.string.share_this_application),
                    settingDescription = stringResource(id = R.string.invite_your_friend_to_push_code),
                    settingIcon = painterResource(id = R.drawable.ic_share)
                ) {
                    context.startActivity(
                        Intent.createChooser(
                            Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Heyy! Check out push note on Google Play; $APP_URL"
                                )
                                type = "text/plain"
                            }, null
                        )
                    )

                    settingsViewModel.onShareApplicationClick()
                }
                SettingItem(
                    settingTitle = stringResource(id = R.string.report_a_problem),
                    settingDescription = stringResource(id = R.string.help_us_make_push_note_better),
                    settingIcon = painterResource(id = R.drawable.ic_report_problem)
                ) {
                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$PENGUENLABS_MAIL")
                            }, null
                        )
                    )

                    settingsViewModel.onReportProblemClick()
                }
                SettingItem(
                    settingTitle = stringResource(id = R.string.rate_us),
                    settingDescription = stringResource(id = R.string.share_your_feedback_with_us),
                    settingIcon = painterResource(id = R.drawable.ic_star)
                ) {
                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_VIEW, Uri.parse(APP_URL)), null
                        )
                    )

                    settingsViewModel.onRateUsClick()
                }
                SettingItem(
                    settingTitle = stringResource(id = R.string.version, BuildConfig.VERSION_NAME),
                    settingDescription = stringResource(id = R.string.current_application_version),
                    settingIcon = painterResource(id = R.drawable.ic_code),
                    isClickable = false
                )
            }
        }
    }
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    settingTitle: String,
    settingDescription: String,
    settingIcon: Painter,
    isClickable: Boolean = true,
    onItemClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick, enabled = isClickable)
            .padding(16.dp)
            .height(50.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = settingIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = settingTitle,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = settingDescription,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SettingSwitchableItem(
    modifier: Modifier = Modifier,
    settingTitle: String,
    settingDescription: String,
    settingIcon: Painter,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = settingIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = settingTitle,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = settingDescription,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Switch(checked = isChecked, onCheckedChange = {
            onCheckedChange(it)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        })
    }
}