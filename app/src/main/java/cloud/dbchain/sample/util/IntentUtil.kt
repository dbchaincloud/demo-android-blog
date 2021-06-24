package cloud.dbchain.sample.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import java.io.File

/**
 * @author: Xiao Bo
 * @date: 17/6/2021
 */
object IntentUtil {

    fun getFilePathFromUri(context: Context, intent: Intent): MutableList<String>? {
        intent.data?.let {
            val list = mutableListOf<String>()
            val path = getPathFromUri(context, it)
            if (!TextUtils.isEmpty(path)) {
                if (!path!!.contains(BOX_FILE_PATH_SPLIT)) {
                    list.add(path)
                } else {
                    path.split(BOX_FILE_PATH_SPLIT).forEach { pathIt ->
                        list.add(pathIt)
                    }
                }
            }
            return list
        }
        intent.clipData?.let {
            val list = mutableListOf<String>()
            for (i in 0 until it.itemCount) {
                val uri = it.getItemAt(i).uri
                val path = getPathFromUri(context, uri)
                if (!TextUtils.isEmpty(path)) {
                    if (!path!!.contains(BOX_FILE_PATH_SPLIT)) {
                        list.add(path)
                    } else {
                        path.split(BOX_FILE_PATH_SPLIT).forEach { pathIt ->
                            list.add(pathIt)
                        }
                    }
                }
            }
            return list
        }
        return null
    }

    private const val BOX_FILE_PATH_SPLIT = "file_path_split"

    private fun getPathFromUri(context: Context, contentUri: Uri?): String? {
        return contentUri?.let { getPath(context, it) } ?: "uri is empty"
    }

    private fun findPath(dir: File, name: String, buffer: StringBuffer): StringBuffer {
        // 如果不是文件夹
        if (!dir.isDirectory) {
            // 如果是选中的文件
            if (TextUtils.equals(dir.name, name)) {
                if (buffer.toString().isNotEmpty()) {
                    buffer.append(BOX_FILE_PATH_SPLIT)
                }
                buffer.append(dir.absolutePath)
                return buffer
            }
            // 不是选中的文件，直接返回
            return buffer
        }
        // 如果是文件夹
        if (dir.isDirectory) {
            // 递归遍历底下的文件及文件夹
            for (file in dir.listFiles()) {
                findPath(file, name, buffer)
            }
        }
        return buffer
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    private fun getPath(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val fileName = getFilePath(context, uri)
                if (fileName != null) {
                    val parentPath =
                        Environment.getExternalStorageDirectory()
                            .toString() + "/Download"
                    var file = File(parentPath, fileName)
                    if (file.exists()) return file.absolutePath
                    file = File("$parentPath/Browser", fileName)
                    return if (file.exists()) file.absolutePath else findPath(
                        File(parentPath),
                        fileName,
                        StringBuffer()
                    ).toString()
                }
                var id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    id = id.replaceFirst("raw:".toRegex(), "")
                    val file = File(id)
                    if (file.exists()) return id
                }
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs =
                    arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getFilePath(
        context: Context,
        uri: Uri
    ): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            cursor = context.contentResolver.query(
                uri, projection, null, null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }
}