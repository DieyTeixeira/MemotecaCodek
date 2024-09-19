package com.codek.pensamentos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object InstallUpdateApk {

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun downloadAndInstallApk(context: Context, apkUrl: String) {
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, "Conexão à internet indisponível", Toast.LENGTH_SHORT).show()
            return
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(apkUrl)
        val request = DownloadManager.Request(uri)
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "update.apk")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setMimeType("application/vnd.android.package-archive") // Definir o MIME type corretamente

        val downloadId = downloadManager.enqueue(request)

        // Registrar o BroadcastReceiver para ouvir o término do download
        val receiver = DownloadCompleteReceiver(context, downloadId)
        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private class DownloadCompleteReceiver(val context: Context, val downloadId: Long) : BroadcastReceiver() {
        @SuppressLint("ObsoleteSdkInt")
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "update.apk")
                if (file.exists()) {
                    val apkUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                    } else {
                        Uri.fromFile(file)
                    }

                    val installIntent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(apkUri, "application/vnd.android.package-archive")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }

                    context.startActivity(installIntent)

                    context.unregisterReceiver(this)
                } else {
                    Toast.makeText(context, "Falha no download", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}