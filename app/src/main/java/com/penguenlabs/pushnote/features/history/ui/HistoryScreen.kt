package com.penguenlabs.pushnote.features.history.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import com.penguenlabs.pushnote.R
import com.penguenlabs.pushnote.data.local.entity.HistoryEntity
import com.penguenlabs.pushnote.theme.PushNoteTheme
import com.penguenlabs.pushnote.util.OverlayBottomSheetScaffold
import com.penguenlabs.pushnote.util.TimeFormat
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel = hiltViewModel(),
    onBackPressClick: () -> Unit = {}
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val historyScreenState = historyViewModel.historyScreenState
    val context = LocalContext.current

    OverlayBottomSheetScaffold(scaffoldState = scaffoldState, sheetContent = {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .height(5.dp)
                        .width(35.dp)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(Color.Gray)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        historyScreenState.selectedHistoryEntity?.let {
                            historyViewModel.sendNotification(
                                pushNotificationText = it.note,
                                isPinnedNote = it.isPinnedNote
                            )
                        }

                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                    .padding(16.dp),
                text = stringResource(id = R.string.push),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        historyScreenState.selectedHistoryEntity?.let {
                            historyViewModel.onCopyClick()

                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("", it.note)
                            clipboard.setPrimaryClip(clip)

                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.copied),
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }

                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                    .padding(16.dp),
                text = stringResource(id = R.string.copy),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        historyScreenState.selectedHistoryEntity?.let {
                            historyViewModel.onShareClick()

                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, it.note)
                                type = "text/plain"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }

                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                    .padding(16.dp),
                text = stringResource(id = R.string.share),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        historyScreenState.selectedHistoryEntity?.let {
                            historyViewModel.onDeleteClick(it)
                        }

                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                    .padding(16.dp),
                text = stringResource(id = R.string.delete),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp
            )
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .statusBarsPadding(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.history),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onBackground,
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
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }

            LazyColumn {
                items(
                    items = historyScreenState.historyItems,
                    key = { it.id }) { historyEntity ->
                    HistoryItem(
                        historyEntity = historyEntity,
                        onSendClick = historyViewModel::sendNotification,
                        onLongClick = {
                            historyViewModel.onHistoryEntitySelect(it)
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(visible = historyScreenState.hasHistory().not()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_history),
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h6,
                    fontSize = 16.sp,
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        historyViewModel.getAllHistory()
    }
}

@ExperimentalFoundationApi
@Composable
private fun HistoryItem(
    modifier: Modifier = Modifier,
    historyEntity: HistoryEntity,
    onSendClick: (note: String, isPinnedNote: Boolean) -> Unit = { _, _ -> },
    onLongClick: (historyEntity: HistoryEntity) -> Unit = { }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = { }, onLongClick = { onLongClick(historyEntity) })
            .padding(16.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = historyEntity.note,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = TimeFormat.format(historyEntity.time),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body2
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
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HistoryItemPreview() {
    PushNoteTheme {
        HistoryItem(historyEntity = HistoryEntity(note = "Note", time = 0, isPinnedNote = false))
    }
}