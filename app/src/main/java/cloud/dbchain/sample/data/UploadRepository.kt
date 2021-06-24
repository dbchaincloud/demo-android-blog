package cloud.dbchain.sample.data

import cloud.dbchain.sample.api.FileApi
import com.gcigb.dbchain.DBChain
import com.gcigb.dbchain.createAccessToken
import com.gcigb.network.RetrofitClient
import com.gcigb.network.okHttpClientUpload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
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
            return@withContext RetrofitClient.sendRequestForReturn {
                val body = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val part = MultipartBody.Part.createFormData("file", file.name, body)
                return@sendRequestForReturn RetrofitClient.createService(
                    cls = FileApi::class.java,
                    client = okHttpClientUpload
                ).upload(createAccessToken(), DBChain.appCode, part).await().result
            }
        }
    }
}