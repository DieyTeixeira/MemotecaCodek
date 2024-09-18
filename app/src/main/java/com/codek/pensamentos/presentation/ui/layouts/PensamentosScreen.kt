package com.codek.pensamentos.presentation.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.dieyteixeira.pensamentos.R
import com.codek.pensamentos.data.api.ApiClient
import com.codek.pensamentos.data.api.PensamentoApi
import com.codek.pensamentos.domain.repository.PensamentoRepositoryImpl
import com.codek.pensamentos.presentation.ui.composables.Baseboard
import com.codek.pensamentos.presentation.ui.composables.PensamentoCard
import com.codek.pensamentos.presentation.ui.composables.PensamentoDialog
import com.codek.pensamentos.presentation.ui.composables.SkeletonCard
import com.codek.pensamentos.presentation.viewmodel.PensamentoViewModel
import com.codek.pensamentos.theme.CodekTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PensamentosScreen(
    viewModel: PensamentoViewModel,
    backgroundColor: Color = Color(0xFFF7F8FA)
) {
    val pensamentos by viewModel.pensamentos.collectAsState()
    val currentPensamento by viewModel.currentPensamento.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val expandedPensamentoId by viewModel.expandedPensamentoId.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val scope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                viewModel.refreshPensamentos()
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadPensamentos()
    }

    if (showDialog) {
        PensamentoDialog(
            pensamento = currentPensamento,
            onDismiss = { viewModel.setShowDialog(false) },
            onSave = { newPensamento ->
                scope.launch {
                    if (currentPensamento != null) {
                        viewModel.updatePensamento(currentPensamento!!.id.toString(), newPensamento)
                    } else {
                        viewModel.createPensamento(newPensamento)
                    }
                    viewModel.setShowDialog(false)
                }
            },
            onRefresh = {
                scope.launch {
                    viewModel.refreshPensamentos()
                }
            }
        )
    }

    Column(
        Modifier
            .background(backgroundColor)
            .padding(top = 35.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                viewModel.setExpandedPensamentoId(null)
            }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .rotate(-90f),
                Alignment.CenterStart
            ) {
                Text(
                    text = "MEMOTECA\nCODEK",
                    fontSize = 20.sp,
                    color = Color(0xFF8F4A0E),
                    fontWeight = FontWeight.Bold
                )
            }
            Image(
                painter = painterResource(id = R.drawable.login_laranja),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 25.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Row(
                Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF8F4A0E))
                    .padding(10.dp, 5.dp)
                    .clickable {
                        viewModel.setCurrentPensamento(null)
                        viewModel.setShowDialog(true)
                        viewModel.setExpandedPensamentoId(null)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(1.dp)

                )
                Text(
                    text = "NOVO CARD",
                    color = Color.White,
                    modifier = Modifier
                        .padding(1.dp)
                )
            }
        }
        Spacer(Modifier.size(15.dp))

        Box(
            Modifier
                .fillMaxSize()
        ) {
            errorMessage?.let {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .padding(vertical = 100.dp, horizontal = 20.dp)
                        .pullRefresh(state = pullRefreshState),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .height(35.dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Red,
                                            Color.Transparent
                                        ),
                                    )
                                )
                        ) {
                            Text(
                                text = "ERRO NA CONEXÃO",
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.Center),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.size(15.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Não foi possível estabelecer\nconexão com o servidor.",
                                color = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(Modifier.size(15.dp))
                        Text(
                            text = it,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } ?: run {
                if (isLoading) {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.96f)
                            .pullRefresh(state = pullRefreshState),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(10) {
                            SkeletonCard()
                        }
                    }
                } else {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.96f)
                            .pullRefresh(state = pullRefreshState),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        // cards
                        items(pensamentos) { pensamento ->
                            PensamentoCard(
                                pensamento = pensamento,
                                isExpanded = expandedPensamentoId == pensamento.id,
                                onEdit = {
                                    viewModel.setCurrentPensamento(pensamento)
                                    viewModel.setShowDialog(true)
                                    viewModel.setExpandedPensamentoId(null)
                                },
                                onDelete = {
                                    viewModel.deletePensamento(pensamento.id.toString())
                                },
                                onClick = {
                                    viewModel.setExpandedPensamentoId(
                                        if (expandedPensamentoId == pensamento.id) null else pensamento.id
                                    )
                                }
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
    Baseboard(color = Color.LightGray)
}

fun String.toColor(): Color {
    return try {
        Color(Color(android.graphics.Color.parseColor(this)).toArgb())
    } catch (e: Exception) {
        Color.LightGray
    }
}

@Preview
@Composable
private fun ChatsListScreenPreview() {
    CodekTheme {
        PensamentosScreen(
            viewModel = PensamentoViewModel(PensamentoRepositoryImpl(ApiClient.createService(PensamentoApi::class.java)))
        )
    }
}