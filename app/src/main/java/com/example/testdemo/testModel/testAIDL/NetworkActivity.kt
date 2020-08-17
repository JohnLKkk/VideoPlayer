package com.example.testdemo.testModel.testAIDL

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.testdemo.R
import com.example.testdemo.base.BaseDefaultActivity
import java.io.*
import java.net.*
import kotlin.concurrent.thread

class NetworkActivity : BaseDefaultActivity() {
    private val TAG = this::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar("网络处理",true)
    }

    override fun getLayoutID(): Int = R.layout.activity_network

    override fun isFullScreenWindow(): Boolean = true

    fun createServer(view: View) {
//        createTCPServer()
        createUDPServer()
    }

    fun createClient(view: View) {
//        createTCPClient()
        createUDPClient()
    }

    @Throws(Exception::class)
    fun createTCPServer() {
        thread {
            try {
                //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
                val serverSocket = ServerSocket(8888)
                var socket: Socket?
                //记录客户端的数量
                var count = 0
                println("***服务器即将启动，等待客户端的连接***")
                //循环监听等待客户端的连接
                while (true) {
                    //调用accept()方法开始监听，等待客户端的连接
                    socket = serverSocket.accept()
                    //创建一个新的线程
                    val serverThread = ServerThread(socket)
                    //启动线程
                    serverThread.start()
                    count++ //统计客户端的数量
                    Log.d(TAG, "客户端的数量：$count")
                    val address: InetAddress = socket.inetAddress
                    Log.d(TAG, "当前客户端的IP：" + address.hostAddress)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(Exception::class)
    fun createTCPClient() {
        thread {
            try {
                //1.创建客户端Socket，指定服务器地址和端口
                val socket = Socket("localhost", 8888)
                //2.获取输出流，向服务器端发送信息
                val os: OutputStream = socket.getOutputStream() //字节输出流
                val pw = PrintWriter(os) //将输出流包装为打印流
                pw.write("用户名：whf;密码：789")
                pw.flush()
                socket.shutdownOutput() //关闭输出流
                //3.获取输入流，并读取服务器端的响应信息
                val inputStream: InputStream = socket.getInputStream()
                val br = BufferedReader(InputStreamReader(inputStream))
                var info: String?
                while (br.readLine().also { info = it } != null) {
                    Log.d(TAG, "我是客户端，服务器说：$info")
                }
                //4.关闭资源
                br.close()
                inputStream.close()
                pw.close()
                os.close()
                socket.close()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(Exception::class)
    fun createUDPServer() {
        thread {
            // 创建接收端
            val ds = DatagramSocket(12345)

            /**
             * 接收数据包
             * receive(DatagramPacket p)
             * 从这个套接字接收数据报包。
             */
            val data = ByteArray(1024)
            val dp = DatagramPacket(data, data.size)
            ds.receive(dp)
            // 获取接受到的信息
            val str = String(data, 0, dp.length)
            println(str)
            // 关闭接收端
            ds.close()
        }
    }

    @Throws(Exception::class)
    fun createUDPClient() {
        thread {
            /**
             * 准备发送端
             * DatagramSocket()
             * 构造一个数据报套接字绑定到本地主机机器上的任何可用的端口。
             */
            val ds = DatagramSocket()

            /**
             * 准备数据包
             * 1、 DatagramPacket(byte[] buf, int length)
             * 构造一个 DatagramPacket length接收数据包的长度
             * 2、 String的getBytes()
             * 方法是得到一个操作系统默认的编码格式的字节数组
             * 3、 setSocketAddress()
             * 设置SocketAddress(通常是IP地址+端口号)都的远程主机发送数据报。
             * 4、InetSocketAddress(InetAddress addr, int port)
             * 创建一个套接字地址的IP地址和端口号。
             */
            val str = "你好，UDP"
            val ch = str.toByteArray()

            val dp = DatagramPacket(ch, ch.size)
            dp.socketAddress = InetSocketAddress("127.0.0.1", 12345)
            // 发送数据
            ds.send(dp)
            // 关闭套接字
            ds.close()
        }
    }

    /*
 * 服务器线程处理类
 */
    inner class ServerThread(socket: Socket?) : Thread() {
        // 和本线程相关的Socket
        private var socket: Socket? = null

        //线程执行的操作，响应客户端的请求
        override fun run() {
            var inputStream: InputStream? = null
            var isr: InputStreamReader? = null
            var br: BufferedReader? = null
            var os: OutputStream? = null
            var pw: PrintWriter? = null
            try {
                //获取输入流，并读取客户端信息
                inputStream = socket!!.getInputStream()
                isr = InputStreamReader(inputStream!!)
                br = BufferedReader(isr)
                var info: String?
                while (br.readLine().also { info = it } != null) { //循环读取客户端的信息
                    Log.d(TAG, "我是服务器，客户端说：$info")
                }
                socket!!.shutdownInput() //关闭输入流
                //获取输出流，响应客户端的请求
                os = socket!!.getOutputStream()
                pw = PrintWriter(os)
                pw.write("欢迎您！")
                pw.flush() //调用flush()方法将缓冲输出
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                //关闭资源
                try {
                    pw?.close()
                    os?.close()
                    br?.close()
                    isr?.close()
                    inputStream?.close()
                    if (socket != null) socket!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        init {
            this.socket = socket
        }
    }
}
