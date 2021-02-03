package com.yoy.videoPlayer.ui.video.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.ui.video.FragmentCallback
import com.yoy.videoPlayer.utils.PlayHistoryManager

/**
 * Created by Void on 2020/12/8 13:54
 * 播放历史记录弹窗
 */
class PlayHistoryPopupWindow(private val context: Context) :
        BaseAdapter(),
        ViewTreeObserver.OnGlobalLayoutListener {
    private var contentView = LayoutInflater.from(context).inflate(
            R.layout.layout_play_history_popup_window,
            FrameLayout(context)
    )
    private var mPopupWindow = PopupWindow(contentView,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
    )
    private var callback: FragmentCallback? = null
    private var mHandler = Handler(Looper.getMainLooper())
    private var items = ArrayList<VideoFileInfo>()

    init {
        mPopupWindow.isOutsideTouchable = true
        mPopupWindow.elevation = 20f

        mPopupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.popupWindowBg)))
        contentView.findViewById<ListView>(R.id.playHistoryList).apply {
            adapter = this@PlayHistoryPopupWindow
            setOnItemClickListener { _, _, position, _ ->
                callback?.onItemClick(getItem(position))
                mPopupWindow.dismiss()
            }
        }
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): VideoFileInfo = items[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
                R.layout.layout_popup_window_item,
                null
        )
        view.findViewById<TextView>(R.id.nameTv).text = context.getString(R.string.fileNameStr,
                getItem(position).vName)
        view.findViewById<TextView>(R.id.pathTv).text = context.getString(R.string.filePathStr,
                getItem(position).vPath)
        return view
    }

    /**
     * 计算view高度
     * 注：结束计算后记得移除接口监听，否则将占用大量资源
     */
    override fun onGlobalLayout() {
        var maxHeight = measureHeight(this)
        if (maxHeight > 800) maxHeight = 800
        if (mPopupWindow.height != maxHeight) {
            mPopupWindow.height = maxHeight
        }
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    /**
     * 用于测量所有item高度之和
     */
    private fun measureHeight(adapter: ListAdapter): Int {
        val mMeasureParent = FrameLayout(context)
        var itemView: View? = null
        var heightCount = 0
        var itemType = 0
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        for (i in 0 until adapter.count) {
            val positionType = adapter.getItemViewType(i)
            if (positionType != itemType) {
                itemType = positionType
                itemView = null
            }
            itemView = adapter.getView(i, itemView, mMeasureParent)
            itemView.measure(widthMeasureSpec, heightMeasureSpec)
            heightCount += itemView.measuredHeight
        }
        //item高度之和加上标题的高度和间隔留空等
        heightCount += 100
        return heightCount
    }

    fun setCallback(callback: FragmentCallback) {
        this.callback = callback
    }

    fun show(view: View) {
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        Thread(runable).start()
    }

    fun close() {
        if (!mPopupWindow.isShowing) return
        mPopupWindow.dismiss()
    }

    val runable = Runnable {
        val data = PlayHistoryManager.queryData()
        mHandler.post {
            if (data.isEmpty()) {
                ToastUtils.showShort(context, "记录为空……")
                mPopupWindow.dismiss()
            } else {
                contentView.viewTreeObserver.addOnGlobalLayoutListener(this)
                items.clear()
                items.addAll(data)
                notifyDataSetChanged()
            }
        }
    }
}