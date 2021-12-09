package cloud.dbchain.sample.data

import cloud.dbchain.sample.api.FileApi
import com.gcigb.dbchain.appCode
import com.gcigb.dbchain.createAccessToken
import com.gcigb.dbchain.dbChainKey
import com.gcigb.network.createService
import com.gcigb.network.okHttpClientUpload
import com.gcigb.network.sendRequestForReturn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author: Xiao Bo
 * @date: 17/6/2021
 */
object UploadRepository {

    suspend fun upload(file: File): String? {
        return withContext(Dispatchers.IO) {
            return@withContext sendRequestForReturn {
                val body = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val part = MultipartBody.Part.createFormData("file", file.name, body)
                val accessToken = createAccessToken(
                    privateKeyBytes = dbChainKey.privateKeyBytes,
                    publicKeyBytes33 = dbChainKey.publicKeyBytes33
                )
                return@sendRequestForReturn createService(
                    cls = FileApi::class.java,
                    client = okHttpClientUpload
                ).upload(accessToken, appCode, part).await().result
            }
        }
    }
}