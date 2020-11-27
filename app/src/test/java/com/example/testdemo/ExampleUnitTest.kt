package com.example.testdemo

import com.example.testdemo.gg.GsonUtils.serializedToJson
import com.example.testdemo.gg.RSACryption
import com.example.testdemo.gg.RSAUtils
import com.example.testdemo.testModel.network.NetWorkManager
import okhttp3.Response
import org.junit.Test
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.TreeMap

/**
 * Example local unit testBG, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : NetWorkManager.RequestCallback {
    @Test
    fun addition_isCorrect() {
//        val map = TreeMap<String, Any>()
//        map["ad_space_id"] = "12"
//        val items = client(map)
//        server(items)
//        NetWorkManager.getInstant().executeRequest(NetRequestObj(NetCode.getAdvertisingInfo, this).apply {
//            addBody("params", items[1])
//        })
//        println("java.util.Base64:"+java.util.Base64.getEncoder().encodeToString(b))
//        println(RSAUtils.parseString2PublicKey(RSACryption.serverPublicKey).toString())

    }

    override fun onSuccess(response: Response) {
        println("-------onSuccess")
        println("-------" + response.message())
    }

    override fun onError(e: IOException) {
        e.printStackTrace()
    }

    private val b = byteArrayOf(8, 16, 47, -82, -119, -125, 10, 61, 36, -116, -110, 20, 114, 92, 26, -65, 73, -56, 35, 50, 118, 50, -59, -99, 89, -60, -5, -16, -35, -25, 100, -16, 124, -71, 22, 76, 59, -127, 17, 103, -25, -15, -34, 110, 68, -24, 60, -102, 3, -34, -63, 104, 89, 104, -25, 105, -19, -43, 47, -99, -96, 69, -56, 99, 61, -5, -50, 124, -56, 82, 118, -109, -39, 104, 19, -116, -127, 24, -119, 45, -79, 88, 7, 42, -28, -12, 18, -83, -75, 85, 84, -19, 108, -61, 111, -77, -8, 124, 33, 6, -11, -31, -117, 96, -4, 114, -54, 105, 84, 99, -85, -103, -49, -125, -112, -61, 51, -108, -55, -119, -94, 72, 68, -49, 78, -35, 116, -57)
    private val b2 = byteArrayOf(-106, -43, 117, -60, -39, 125, 98, 115, -45, -128, -57, -68, -70, 65, -67, 83, -38, 66, -40, 2, 43, -101, 107, -126, -52, 64, 42, 98, 53, 47, 64, -61, 83, 95, 34, -37, 68, 91, -55, -49, -116, 124, 108, -126, 54, 113, 86, -106, 32, -77, 23, 108, 93, 19, -117, -94, 100, 100, -20, 2, 32, -126, 16, 94, 18, -88, 86, -7, -33, -105, 30, 49, -55, 87, -17, -33, -59, 23, -72, 97, -117, -57, -121, -99, -98, 103, 109, -4, 106, 105, 65, -31, -16, 88, 122, 72, 27, -74, 54, -45, 115, 82, -4, -122, -113, -83, -99, 65, 106, -26, -111, 100, -64, -67, -71, 53, -74, -102, -59, -101, 56, 88, -42, -14, 79, -40, 40, -111)
    //    CBAvromDCj0kjJIUclwav0nIIzJ2MsWdWcT78N3nZPB8uRZMO4ERZ+fx3m5E6DyaA97BaFlo52nt1S+doEXIYz37znzIUnaT2WgTjIEYiS2xWAcq5PQSrbVVVO1sw2+z+HwhBvXhi2D8csppVGOrmc+DkMMzlMmJokhEz07ddMc=
    fun client(params: Map<String, Any>): Array<String?> {
        val item = arrayOfNulls<String>(3)
        item[0] = serializedToJson(params)
        item[0] = java.util.Base64.getEncoder().encodeToString(item[0]!!.toByteArray(StandardCharsets.UTF_8))
//        item[0] = Base64.encodeToString(item[0].getBytes(StandardCharsets.UTF_8), Base64.NO_PADDING);
        //        item[0] = Base64.encodeToString(item[0].getBytes(StandardCharsets.UTF_8), Base64.NO_PADDING);
        println("原始请求数据: " + item[0])
        println("java.util.Base64:"+java.util.Base64.getEncoder().encodeToString(b2))
        item[1] = java.util.Base64.getEncoder().encodeToString(RSAUtils.encryptByPublicKey(item[0]!!.toByteArray(), RSACryption.serverPublicKey))
        item[2] = java.util.Base64.getEncoder().encodeToString(RSAUtils.createSign(item[0], RSACryption.clientPrivateKey))
//            item[1] = Base64.encodeToString(RSAUtils.encryptByPublicKey(item[0].getBytes(), serverPublicKey), Base64.NO_PADDING);
//            item[2] = Base64.encodeToString(RSAUtils.createSign(item[0], clientPrivateKey), Base64.NO_PADDING);

        println(RSACryption.arrayToString(item))
        return item
    }

    fun server(item: Array<String?>) {
//        boolean passSign = RSAUtils.checkSign(item[0], Base64.decode(item[2], Base64.DEFAULT), serverPublicKey);
        val passSign = RSAUtils.checkSign(item[0], java.util.Base64.getDecoder().decode(item[2]), RSACryption.serverPublicKey)
        if (passSign) {
//            String data = new String(RSAUtils.decryptByPrivateKey(Base64.decode(item[1], Base64.DEFAULT), clientPrivateKey));
            val data = String(RSAUtils.decryptByPrivateKey(java.util.Base64.getDecoder().decode(item[1]), RSACryption.clientPrivateKey))
            println("解密后的明文：$data")
        } else {
            println("验签失败")
        }
    }
}
