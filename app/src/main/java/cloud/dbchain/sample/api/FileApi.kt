package cloud.dbchain.sample.api

import com.gcigb.dbchain.bean.result.DBChainResult
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * @author: Xiao Bo
 * @date: 17/6/2021
 */
const val IPFS_URL = "https://www.dbchain.cloud/relay/ipfs/"

interface FileApi {

    @Multipart
    @POST("dbchain/upload/{token}/{appCode}")
    fun upload(
        @Path("token") token: String,
        @Path("appCode") appCode: String,
        @Part file: MultipartBody.Part
    ): Deferred<DBChainResult>
}