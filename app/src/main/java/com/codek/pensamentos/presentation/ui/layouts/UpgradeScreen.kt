package com.codek.pensamentos.presentation.ui.layouts

import android.annotation.SuppressLint
    import android.app.Activity
    import android.app.DownloadManager
    import android.content.BroadcastReceiver
    import android.content.Context
    import android.content.Intent
    import android.content.IntentFilter
    import android.net.Uri
    import android.os.Environment
    import android.util.Log
    import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Button
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.Icon
    import androidx.compose.material3.LinearProgressIndicator
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.DisposableEffect
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathNode
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.TextStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.core.content.FileProvider
    import com.codek.pensamentos.R
    import com.codek.pensamentos.data.version.currentVersionName
    import com.codek.pensamentos.presentation.viewmodel.VersionadorViewModel
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import java.io.File
    import kotlin.random.Random

private const val TAG = "UpdateScreen"

@SuppressLint("UnspecifiedRegisterReceiverFlag", "Range")
@Composable
fun UpdateScreen(
    versionadorViewModel: VersionadorViewModel
) {
    val context = LocalContext.current
    var isButtonEnabled by remember { mutableStateOf(true) }
    val currentVersionName = currentVersionName
    val lastVersionName by versionadorViewModel.lastVersionName.collectAsState()
    var isLoading by remember { mutableStateOf(lastVersionName == null) }
    var downloadProgress by remember { mutableStateOf(0f) }
    var downloadId by remember { mutableStateOf<Long?>(null) }
    var pleaseWait by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isButtonEnabled = true
    }

    LaunchedEffect(lastVersionName) {
        if (lastVersionName != null) {
            isLoading = false
        }
    }

    LaunchedEffect(downloadId) {
        if (downloadId != null) {
            coroutineScope.launch {
                trackDownloadProgress(context, downloadId!!) { progress ->
                    downloadProgress = progress
                }
            }
        }
    }

    DisposableEffect(downloadId) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                Log.d(TAG, "Download completed with ID: $id")
                if (id == downloadId) {
                    handleDownloadComplete(context, id) { progress ->
                        Log.d(TAG, "Download progress updated: $progress")
                        downloadProgress = progress
                    }
                }
            }
        }

        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.LightGray, Color.White),
                    startY = 0f,
                    endY = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Atualização disponível",
                style = TextStyle.Default.copy(
                    fontSize = 25.sp,
                    color = Color.DarkGray
                )
            )
            Text(
                text = "Clique no botão para atualizar",
                style = TextStyle.Default.copy(
                    fontSize = 25.sp,
                    color = Color.DarkGray
                )
            )
            Spacer(modifier = Modifier.height(50.dp))
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Gray
                    )
                }
            } else {
                Text(
                    text = "VERSÃO INSTALADA:",
                    style = TextStyle.Default.copy(
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = currentVersionName,
                    style = TextStyle.Default.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "ATUALIZAÇÃO DISPONÍVEL:",
                    style = TextStyle.Default.copy(
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "$lastVersionName",
                    style = TextStyle.Default.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                )
            }
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(40.dp)
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clickable {
                        Log.d(TAG, "Update button clicked")
                        pleaseWait = true
                        isButtonEnabled = false
                        downloadId = downloadApk(context)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pleaseWait) {
                        val infiniteTransition = rememberInfiniteTransition()
                        val rotationDegrees by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 180f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000),
                                repeatMode = RepeatMode.Restart
                            )
                        )

                        Icon(
                            imageVector = Icons.Default.HourglassEmpty,
                            contentDescription = "Aguardando",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer {
                                    rotationZ = rotationDegrees // Aplica a rotação ao ícone
                                }
                        )
                    } else {
                        Icon(
                            imageVector =
                            if (downloadProgress > 0f && downloadProgress < 1f) Icons.Default.Downloading
                            else if (downloadProgress == 1f) Icons.Default.DownloadDone
                            else Icons.Default.Download,
                            contentDescription = "Download",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text =
                        if (pleaseWait) "Aguarde..."
                        else if (downloadProgress > 0f && downloadProgress < 1f) "Baixando APK"
                        else if (downloadProgress == 1f) "Download concluído"
                        else "Baixar APK",
                        color = Color.White,
                        modifier = Modifier.padding(8.dp),
                        style = TextStyle.Default.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .height(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (pleaseWait && downloadProgress == 0f) {
                    Text(
                        text = "Seu download começará em breve",
                        style = TextStyle.Default.copy(
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                // Barra de progresso do download
                if (downloadProgress > 0f) {  // Exibe apenas quando o progresso for maior que 0
                    pleaseWait = false
                    LinearProgressIndicator(
                        progress = downloadProgress,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(8.dp)
                    )
                }
                Log.d(TAG, "Progress bar updated with progress: $downloadProgress")

                Spacer(modifier = Modifier.height(30.dp))
                if (downloadProgress == 1f) {
                    Text(
                        text = "Caso não inicie a instalação,\nfaça o processo manualmente.",
                        style = TextStyle.Default.copy(
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

fun downloadApk(context: Context): Long {
    val apkUrl = "https://www.dropbox.com/scl/fi/zinpphfbbloe43rpcrx8w/Memoteca-Codek.apk?rlkey=ryiun7oxh87lowubfzuns8e6g&st=4ihe2u90&dl=1"
    val request = DownloadManager.Request(Uri.parse(apkUrl)).apply {
        setTitle("Baixando APK")
        setDescription("Fazendo o download do aplicativo.")
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "memoteca_codek.apk")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setAllowedOverMetered(true)
        setAllowedOverRoaming(true)
    }

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val id = downloadManager.enqueue(request)
    Log.d(TAG, "Download enqueued with ID: $id")
    return id
}

// Monitora o progresso real do download
fun trackDownloadProgress(context: Context, downloadId: Long, onProgressUpdate: (Float) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        var isDownloading = true
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        while (isDownloading) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = downloadManager.query(query)

            if (cursor != null && cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                val progress = if (bytesTotal > 0) {
                    bytesDownloaded.toFloat() / bytesTotal
                } else {
                    0f
                }

                withContext(Dispatchers.Main) {
                    onProgressUpdate(progress)
                }

                if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                    isDownloading = false
                }
            }
            cursor?.close()
            delay(1000) // Espera um segundo antes de verificar o progresso novamente
        }
    }
}

@SuppressLint("Range")
fun handleDownloadComplete(context: Context, downloadId: Long, onProgressUpdate: (Float) -> Unit) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query().setFilterById(downloadId)
    val cursor = downloadManager.query(query)
    if (cursor != null && cursor.moveToFirst()) {
        try {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            val progress = if (bytesTotal > 0) {
                bytesDownloaded.toFloat() / bytesTotal
            } else {
                0f
            }
            Log.d(TAG, "Download status: $status, bytesDownloaded: $bytesDownloaded, bytesTotal: $bytesTotal, progress: $progress")
            onProgressUpdate(progress)

            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    Log.d(TAG, "Download successful, starting installation")
                    installApk(context)
                }
                DownloadManager.STATUS_FAILED -> {
                    Log.d(TAG, "Download failed")
                    Toast.makeText(context, "Download falhou", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing download", e)
            Toast.makeText(context, "Erro ao processar o download: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            cursor.close()
        }
    } else {
        Log.e(TAG, "Error querying download")
        Toast.makeText(context, "Erro ao consultar o download", Toast.LENGTH_SHORT).show()
    }
}

fun installApk(context: Context) {
    val apkFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "memoteca_codek.apk")
    if (apkFile.exists()) {
        val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apkFile)
        val installIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        Log.d(TAG, "Starting APK installation")
        context.startActivity(installIntent)
    } else {
        Log.e(TAG, "APK file not found")
        Toast.makeText(context, "Falha ao encontrar o APK", Toast.LENGTH_SHORT).show()
    }
}