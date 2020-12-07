//package com.wyt.videolibrary.activity
//
//import android.content.res.Configuration
//import android.graphics.Color
//import android.os.Bundle
//import android.text.TextUtils
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import cn.jzvd.JZDataSource
//import cn.jzvd.Jzvd
//import com.wyt.baselibrary.netWork.RetrofitManager
//import com.wyt.baselibrary.rx.network.IRequestResultListener
//import com.wyt.baselibrary.com.yoy.videoPlayer.utils.LogTool
//import com.wyt.baselibrary.com.yoy.videoPlayer.utils.ToastUtil
//import com.wyt.picturebook.R
//import com.wyt.picturebook.adapter.VideoListAdapter
//import com.wyt.picturebook.base.BaseActivity
//import com.wyt.picturebook.bean.VideoInfoBean
//import com.wyt.picturebook.bean.VideoListBean
//import com.wyt.picturebook.netWork.RequestFlowableGLY
//import com.wyt.picturebook.view.RItemDecoration
//import com.wyt.picturebook.view.VideoContainListView
//import com.wyt.videolibrary.JZMediaExo
//import com.wyt.videolibrary.bean.VideoUrlBean
//import kotlinx.android.synthetic.finger_reading_default.activity_fr_video.*
//import okhttp3.ResponseBody
//import java.util.*
//import kotlin.collections.ArrayList
//
//class VideoContainListViewTest : BaseActivity(), VideoContainListView.OnVideoListItemClickListener, VideoListAdapter.OnItemClickListener {
//
////    private lateinit var videoListAdapter: VideoListAdapter
//    private  val videoListAdapter by lazy { VideoListAdapter() }
//    private lateinit var video_data:ArrayList<VideoListBean.Result>
//    private var currentPlayPosition=0
//
//    override fun initEvent(savedInstanceState: Bundle?) {
//        setContentView(R.layout.activity_fr_video)
//
//        val orientation = resources.configuration.orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            videoView.setOrientation()
////            videoView.gotoScreenFullscreen()
//        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
////            videoView.gotoScreenNormal()
//        }
//        videoView.backButton.setOnClickListener({ v -> finish() })
//        video_data = intent.getParcelableArrayListExtra<VideoListBean.Result>("video_data")
//        if (video_data.isNullOrEmpty()){
//            video_data= savedInstanceState?.getParcelableArrayList<VideoListBean.Result>("video_data") as ArrayList<VideoListBean.Result>
//        }
//        videoView.setVideoDataList(video_data, videoListAdapter)
//        videoListAdapter.setNewData(video_data)
//        rv_data.layoutManager = LinearLayoutManager(this)
//        rv_data.addItemDecoration(
//                RItemDecoration.newInstance(
//                        resources.getDimension(R.dimen.x1).toInt(),
//                        resources.getDimension(R.dimen.x20).toInt(), 1, Color.TRANSPARENT
//                )
//        )
//        rv_data.adapter = videoListAdapter
//        videoListAdapter.onItemClickListener = this
//        currentPlayPosition= savedInstanceState?.getInt("currentPlayPosition",0) ?:0
//        if (!video_data.isNullOrEmpty()) {
//            getVideoUrl(video_data[currentPlayPosition].name, video_data[currentPlayPosition].vkname)
//            videoListAdapter.setSelected(currentPlayPosition)
//            rv_data.smoothScrollToPosition(currentPlayPosition)
//        }
//
//        runOnUiThread {
//            videoView.iv_logo_left.setVisibility(View.VISIBLE)
//            videoView.iv_logo_right.setVisibility(View.VISIBLE)
//            videoView.iv_logo_left.setImageResource(com.wyt.videolibrary.R.drawable.mingshi_left)
//            videoView.iv_logo_right.setImageResource(com.wyt.videolibrary.R.drawable.mingshi_right)
//        }
//
//        LogTool.e(this.localClassName, "创建")
//    }
//
//        override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//            outState.putInt("currentPlayPosition",currentPlayPosition)
//            outState.putParcelableArrayList("video_data",video_data)
//        LogTool.e(this.localClassName,"onSaveInstanceState")
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        super.onRestoreInstanceState(savedInstanceState)
//        LogTool.e(this.localClassName,"onRestoreInstanceState");
//    }
//
//    override fun onRetainCustomNonConfigurationInstance(): Any {
//        LogTool.e(this.localClassName,"onRetainCustomNonConfigurationInstance")
//        return intent
//    }
//
//    fun init(){
//        val orientation = resources.configuration.orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            videoView.setOrientation()
////            videoView.gotoScreenFullscreen()
//        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
////            videoView.gotoScreenNormal()
//        }
//        videoView.backButton.setOnClickListener({ v -> finish() })
//        val data = intent.getParcelableArrayListExtra<VideoListBean.Result>("video_data")
////        videoListAdapter = VideoListAdapter()
//        videoView.setVideoDataList(data, videoListAdapter)
//        videoListAdapter.setNewData(data)
//        videoListAdapter.addData(data)
//        rv_data.layoutManager = LinearLayoutManager(this)
//        rv_data.addItemDecoration(
//                RItemDecoration.newInstance(
//                        resources.getDimension(R.dimen.x1).toInt(),
//                        resources.getDimension(R.dimen.x20).toInt(), 1, Color.TRANSPARENT
//                )
//        )
//        rv_data.adapter = videoListAdapter
//        videoListAdapter.onItemClickListener = this
//        if (!data.isNullOrEmpty()) {
//            getVideoUrl(data[currentPlayPosition].name, data[currentPlayPosition].vkname)
//        }
//    }
//
//    override fun videoListItemClick(position: Int, data: VideoListBean.Result) {
//        getVideoUrl(data.name, data.vkname)
//    }
//
//    override fun itemClick(position: Int, data: VideoListBean.Result) {
//        currentPlayPosition=position
//        getVideoUrl(data.name, data.vkname)
//    }
//
//    fun getVideoUrl(videoName: String, vkname: String) {
//        val map = TreeMap<String, String>()
//        map["vkname"] = vkname
//        RetrofitManager.sendRequest(RequestFlowableGLY.getVideoUrl(map), object : IRequestResultListener<ResponseBody> {
//            override fun onSuccess(responseBody: ResponseBody) {
//                try {
//                    val result = responseBody.string()
//                    LogTool.e("获取视频url数据\$result")
//                    val videoInfoBean = com.wyt.baselibrary.com.yoy.videoPlayer.utils.CommonUtils.gsonToBean(result, VideoInfoBean::class.java)
//                    if (videoInfoBean.code == 9 && videoInfoBean.result != null) {
//                        val videoUrlBeans = ArrayList<VideoUrlBean>()
//                        if (!TextUtils.isEmpty(videoInfoBean.result.biaozhun)) {
//                            videoUrlBeans.add(VideoUrlBean("标准", videoInfoBean.result.biaozhun))
//                        }
//                        if (!TextUtils.isEmpty(videoInfoBean.result.gaoqing)) {
//                            videoUrlBeans.add(VideoUrlBean("高清", videoInfoBean.result.gaoqing))
//                        }
//                        if (!TextUtils.isEmpty(videoInfoBean.result.chaoqing)) {
//                            videoUrlBeans.add(VideoUrlBean("超清", videoInfoBean.result.chaoqing))
//                        }
//                        val map = linkedMapOf<String, String>()
//                        for (videoBean in videoUrlBeans) {
//                            map[videoBean.name] = videoBean.url
//                        }
//                        val orientation = resources.configuration.orientation;
//                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                            videoView.setUp(JZDataSource(map, videoInfoBean.result.name), Jzvd.SCREEN_FULLSCREEN, JZMediaExo::class.java)
////                            videoView.gotoScreenFullscreen()
//                        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//                            videoView.setUp(JZDataSource(map, videoInfoBean.result.name), Jzvd.SCREEN_NORMAL, JZMediaExo::class.java)
//                        }
//                        videoView.startButton.performClick()
//
//                    } else {
//                        ToastUtil.showToast("暂无视频信息")
//                    }
//                } catch (e: Exception) {
//                }
//
//            }
//
//            override fun onError(throwable: Throwable) {
//                LogTool.e("获取音频下载地址" + throwable.message)
//            }
//        })
//    }
//
//    //Home键退出界面暂停播放，返回界面继续播放
//    override fun onResume() {
//        super.onResume()
//        //home back
//        Jzvd.goOnPlayOnResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        //home back
//        Jzvd.goOnPlayOnPause()
//    }
//
//    override fun onBackPressed() {
//        if (Jzvd.backPress()) {
//            return
//        } else {
//            finish()
//        }
//        super.onBackPressed()
//    }
//
//
////    override fun onConfigurationChanged(newConfig: Configuration) {
////        super.onConfigurationChanged(newConfig)
////
////        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
////            videoView.setOrientation()
////            videoView.gotoScreenFullscreen()
////            videoListAdapter = VideoListAdapter()
////        } else {
//////            videoView.gotoScreenNormal()
////            val data = intent.getParcelableArrayListExtra<VideoListBean.Result>("video_data")
////            videoListAdapter = VideoListAdapter()
////            videoView.setVideoDataList(data, videoListAdapter)
////        }
////        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT)
////        com.wyt.baselibrary.com.yoy.videoPlayer.utils.CommonUtils.setFullScreen(this)
////        videoListAdapter.notifyDataSetChanged()
////        videoListAdapter.getSelectPosition()?.let {
////            rv_data.smoothScrollToPosition(it)
////        }
////
////    }
//}