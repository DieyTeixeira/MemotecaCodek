package com.codek.pensamentos.presentation.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codek.pensamentos.R
import com.codek.pensamentos.data.model.Pensamento
import com.codek.pensamentos.presentation.ui.layouts.toColor

@Composable
fun PensamentoCard(
    pensamento: Pensamento,
    isExpandedCard: Boolean,
    isExpandedOptions: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val primaryColor = pensamento.cor_pri?.toColor() ?: Color(0xFF727171)
    val secondaryColor = pensamento.cor_sec?.toColor() ?: Color(0xFFE7E6E6)

    var isTextOverflowing by remember { mutableStateOf(false) }
    var isExpandCard by remember { mutableStateOf(isExpandedCard) }

    // card principal
    Box(
        Modifier
            .fillMaxWidth()
            .clip(CutCornerShape(0.dp, 0.dp, 30.dp, 0.dp))
            .background(secondaryColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            }
    ) {
        Row(
            Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                Modifier
                    .weight(1f)
                    .background(secondaryColor)
            ) {
                // primeira coluna
                Column {
                    // aspas
                    Box(
                        Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pensamentos),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 10.dp),
                            colorFilter = ColorFilter.tint(primaryColor)
                        )
                    }
                }

                // segunda coluna
                Column(
                    Modifier
                        .fillMaxHeight()
                        .padding(end = 5.dp)
                ) {
                    Text(
                        text = pensamento.conteudo,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 22.sp,
                        maxLines = if (isExpandedCard) Int.MAX_VALUE else 5,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { textLayoutResult ->
                            isTextOverflowing = textLayoutResult.hasVisualOverflow
                        }
                    )
                    if (isTextOverflowing) {
                        Row (
                            verticalAlignment = Alignment.Bottom
                        ){
                            Text(
                                text = "Ver mais",
                                color = primaryColor,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clickable {
                                        isExpandCard = !isExpandedCard
                                        onClick()
                                    },
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier
                                    .clickable {
                                        isExpandCard = !isExpandedCard
                                        onClick()
                                    }
                            )
                        }
                    }
                    Spacer(Modifier.size(10.dp))
                    Column(
                        Modifier
                            .padding(end = if (isExpandedOptions) 0.dp else 38.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Box(
                            Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(primaryColor),
                        )
                        Text(
                            text = pensamento.autoria,
                            maxLines = 1,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (isExpandedOptions) {
                Column(
                    Modifier
                        .weight(0.08f)
                        .height(85.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Box(
                        Modifier
                            .size(23.dp)
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(15.dp),
                                clip = true
                            )
                            .background(Color.White, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar",
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .size(15.dp)
                                .clickable {
                                    onDelete()
                                }
                        )
                    }
                    Spacer(Modifier.size(5.dp))
                    Box(
                        Modifier
                            .size(23.dp)
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(15.dp),
                                clip = true
                            )
                            .background(Color.White, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .size(15.dp)
                                .clickable {
                                    onEdit()
                                }
                        )
                    }
                }
            }
        }
        Box(
            Modifier
                .size(30.dp)
                .clip(CutCornerShape(0.dp, 0.dp, 30.dp, 0.dp))
                .background(primaryColor)
                .align(Alignment.BottomEnd)
        )
    }
}