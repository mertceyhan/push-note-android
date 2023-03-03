package com.penguenlabs.pushnote.features.history.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.navigation.Destination
import com.penguenlabs.pushnote.theme.PushNoteTheme
import com.penguenlabs.pushnote.util.Screen
import com.penguenlabs.pushnote.util.TimeFormat

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel = hiltViewModel(), onBackPressClick: () -> Unit = {}
) {
    val historyScreenState = historyViewModel.historyScreenState
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    Screen(context = context, destination = Destination.History) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .systemBarsPadding(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (historyScreenState.isSelectable().not()) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = historyScreenState.isSelectable().not()
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.history),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                        AnimatedVisibility(
                            visible = historyScreenState.isSelectable(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 6.dp),
                                text = historyScreenState.selectedHistoryEntityCountString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        AnimatedVisibility(
                            visible = historyScreenState.isSelectable(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        historyViewModel.onDeleteAllClick(
                                            historyScreenState.selectedHistoryEntities
                                        )
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = R.drawable.ic_delete_forever),
                                    contentDescription = stringResource(id = R.string.back),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = historyScreenState.isSelectable(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { historyViewModel.onSelectAllClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = R.drawable.ic_select_all),
                                    contentDescription = stringResource(id = R.string.select_all),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = historyScreenState.isSelectable(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { historyViewModel.onCloseClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = stringResource(id = R.string.close),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
                LazyColumn {
                    items(
                        items = historyScreenState.historyItems,
                        key = { it.id }) { historyEntity ->
                        HistoryItem(modifier = Modifier.animateItemPlacement(),
                            historyEntity = historyEntity,
                            isSelectable = historyScreenState.isSelectable(),
                            isSelected = historyScreenState.isSelected(historyEntity),
                            onSendClick = historyViewModel::sendNotification,
                            onLongClick = {
                                historyViewModel.onHistoryEntitySelect(isSelected = true, it)
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onCheckedChange = { isSelected, it ->
                                historyViewModel.onHistoryEntitySelect(isSelected, it)
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            })
                    }
                }
            }
            AnimatedVisibility(
                visible = historyScreenState.hasHistory().not(), enter = fadeIn(), exit = fadeOut()
            ) {
                Text(
                    text = stringResource(id = R.string.no_history),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp,
                )
            }
        }
    }
        LaunchedEffect(Unit) {
            historyViewModel.getAllHistory()
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
private fun HistoryItem(
    modifier: Modifier = Modifier,
    isSelectable: Boolean = false,
    isSelected: Boolean = false,
    historyEntity: HistoryEntity,
    onSendClick: (note: String, isPinnedNote: Boolean) -> Unit = { _, _ -> },
    onLongClick: (historyEntity: HistoryEntity) -> Unit = { },
    onCheckedChange: (isSelected: Boolean, historyEntity: HistoryEntity) -> Unit = { _, _ -> },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {
                if (isSelectable) {
                    onCheckedChange(isSelected.not(), historyEntity)
                }
            }, onLongClick = {
                if (isSelectable.not()) {
                    onLongClick(historyEntity)
                }
            })
            .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(
            visible = isSelectable, enter = fadeIn() + expandIn(), exit = fadeOut() + shrinkOut()
        ) {
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                val hapticFeedback = LocalHapticFeedback.current

                Checkbox(modifier = Modifier.padding(end = 16.dp),
                    checked = isSelected,
                    onCheckedChange = {
                        onCheckedChange(it, historyEntity)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    })
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = historyEntity.note,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = TimeFormat.format(historyEntity.time),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Box(
            modifier = modifier
                .size(48.dp)
                .clickable { onSendClick(historyEntity.note, historyEntity.isPinnedNote) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = stringResource(id = R.string.push),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
