package com.codek.pensamentos.presentation.ui.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.dieyteixeira.pensamentos.R
import com.codek.pensamentos.presentation.ui.layouts.toColor
import com.codek.pensamentos.data.model.Pensamento
import com.codek.pensamentos.presentation.viewmodel.PensamentoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PensamentoDialog(
    pensamento: Pensamento?,
    onDismiss: () -> Unit,
    onSave: (Pensamento) -> Unit,
    onRefresh: () -> Unit
) {
    var conteudo by remember { mutableStateOf(pensamento?.conteudo ?: "") }
    var autoria by remember { mutableStateOf(pensamento?.autoria ?: "") }
    var selectedPrimaryColor by remember { mutableStateOf(pensamento?.cor_pri ?: "") }
    var selectedSecondaryColor by remember { mutableStateOf(pensamento?.cor_sec ?: "") }

    val colorPrimary = if (selectedPrimaryColor.isNotBlank()) selectedPrimaryColor.toColor() else Color.Gray
    val colorSecondary = if (selectedSecondaryColor.isNotBlank()) selectedSecondaryColor.toColor() else Color.LightGray

    val colorOptions = listOf(
        // aspas to card
        "#727171" to "#E7E6E6",
        "#36AFCE" to "#D6EFF5",
        "#8BC145" to "#E7F2D9",
        "#FFC000" to "#FFF2CC",
        "#B74919" to "#F7D7C9"
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = if (pensamento == null) "Novo Pensamento" else "Editar Pensamento",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.size(8.dp))
                CardEdit(
                    conteudo = conteudo,
                    onConteudoChange = { conteudo = it },
                    autoria = autoria,
                    onAutoriaChange = { autoria = it },
                    selectedPrimaryColor = colorPrimary,
                    selectedSecondaryColor = colorSecondary
                )
                Spacer(Modifier.size(16.dp))
                // Opções de cores
                Text("Escolha um modelo de cor:")
                Spacer(Modifier.size(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colorOptions.forEach { (primary, secondary) ->
                        ColorOption(
                            primaryColor = primary.toColor(),
                            secondaryColor = secondary.toColor(),
                            isSelected = primary == selectedPrimaryColor && secondary == selectedSecondaryColor,
                            onClick = {
                                selectedPrimaryColor = primary
                                selectedSecondaryColor = secondary
                            }
                        )
                    }
                }
                Spacer(Modifier.size(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(
                            text = "Cancelar",
                            color = Color.Gray
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .height(35.dp)
                            .background(Color.Gray, RoundedCornerShape(30.dp))
                    ) {
                        TextButton(onClick = {
                            val newPensamento = Pensamento(
                                id = pensamento?.id,
                                conteudo = conteudo,
                                autoria = autoria,
                                cor_pri = selectedPrimaryColor,
                                cor_sec = selectedSecondaryColor
                            )
                            onSave(newPensamento)
                            onRefresh()
                        }) {
                            Text(
                                text = "Salvar",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEdit(
    conteudo: String,
    onConteudoChange: (String) -> Unit,
    autoria: String,
    onAutoriaChange: (String) -> Unit,
    selectedPrimaryColor: Color,
    selectedSecondaryColor: Color,
) {

    // card principal
    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp, max = 300.dp)
            .clip(CutCornerShape(0.dp, 0.dp, 30.dp, 0.dp))
            .background(selectedSecondaryColor)
    ) {
        Row(
            Modifier
                .padding(2.dp)
        ) {
            Row(
                Modifier
                    .weight(1f)
                    .background(selectedSecondaryColor)
            ) {
                // primeira coluna
                Column(
                    Modifier
                        .padding(end = 0.dp)
                ) {
                    // aspas
                    Box(
                        Modifier
                            .size(50.dp)
                            .padding(end = 0.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pensamentos),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp),
                            colorFilter = ColorFilter.tint(selectedPrimaryColor)
                        )
                    }
                }

                // segunda coluna
                Column(
                    Modifier
                        .fillMaxHeight()
                ) {
                    TextField(
                        value = conteudo,
                        onValueChange = onConteudoChange,
                        placeholder = {
                            Text(
                                text = "Pensamento",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic
                            )
                        },
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = Color.Transparent,
                            unfocusedLabelColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic
                        )
                    )
                    Column(
                        Modifier
                            .padding(start = 10.dp, end = 13.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Box(
                            Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(selectedPrimaryColor),
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        TextField(
                            value = autoria,
                            onValueChange = onAutoriaChange,
                            placeholder = {
                                Text(
                                    text = "Autor",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            maxLines = 1,
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent
                            ),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                        )
                    }
                }
            }
        }
        Box(
            Modifier
                .size(30.dp)
                .clip(CutCornerShape(0.dp, 0.dp, 30.dp, 0.dp))
                .background(selectedPrimaryColor)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun ColorOption(
    primaryColor: Color,
    secondaryColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = CircleShape
            )
            .background(Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Transparent)
        ) {
            // Metade primária
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .background(primaryColor)
            )
            // Metade secundária
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(secondaryColor)
            )
        }
    }
}