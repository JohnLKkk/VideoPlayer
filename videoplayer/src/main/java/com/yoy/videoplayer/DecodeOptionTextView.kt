package com.yoy.videoplayer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.testdemo.R
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.SPUtils
import java.util.*

/**
 * Created by Void on 2020/9/2 15:34
 */
class DecodeOptionTextView : AppCompatTextView,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        ViewTreeObserver.OnGlobalLayoutListener {
    private lateinit var mPopupWindow: PopupWindow
    private var callback: ClickItemCallback? = null
    private var mContext: Context
    private val mAdapter = MyAdapter()
    private var currentDecodeType = DecodeType.HARDDecoder
    private var isInitEnd = false

    constructor(mContext: Context) : this(mContext, null)
    constructor(mContext: Context, attrs: AttributeSet?) : super(mContext, attrs) {
        this.mContext = mContext
        mAdapter.addItem(DecodeType.HARDDecoder)
        mAdapter.addItem(DecodeType.HARDDecoder)
        setOnClickListener(this)
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onClick(v: View?) {
        if (mPopupWindow.isShowing) return
        val intArray = IntArray(2)
        getLocationOnScreen(intArray)
        KLog.e("----height:" + (intArray[1] - mPopupWindow.height + height))
        KLog.e("----height:" + (intArray[1] - mPopupWindow.height))
        mPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY,
                (intArray[0] + width / 2) - mPopupWindow.width / 2,
                intArray[1] - mPopupWindow.height - 10)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        KLog.e("onItemClick---------,item:" + mAdapter.getItem(position))
        currentDecodeType = mAdapter.getItemDT(position)
        setText(currentDecodeType)
        mPopupWindow.dismiss()
    }

    override fun onGlobalLayout() {
        initView()
    }

    private fun initView() {
        if (isInitEnd) return
        val listView = ListView(mContext)
        listView.adapter = mAdapter
        listView.setBackgroundColor(Color.BLACK)
        listView.onItemClickListener = this
        listView.divider = ContextCompat.getDrawable(mContext, R.color.defaultWhite)
        listView.dividerHeight = 1
        listView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }
        val tmp = measureContentWidth(mAdapter)
        mPopupWindow = PopupWindow(listView, tmp[0], tmp[1] * mAdapter.count)
        mPopupWindow.isOutsideTouchable = true
        mPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(
                context,
                android.R.drawable.screen_background_light_transparent
        ))
        isInitEnd = true
        //初始化当前解码类型
        //获取之前选择的解码类型
        currentDecodeType = when (SPUtils.getString(AppCode.currentDecodeType, "")) {
            "FFMPEGDecoder" -> DecodeType.FFMPEGDecoder
            "HARDDecoder" -> DecodeType.HARDDecoder
            else -> DecodeType.OTHER
        }
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    /**
     * 用于测量适配器item中最大的width。
     */
    private fun measureContentWidth(adapter: ListAdapter): IntArray {
        var mMeasureParent: ViewGroup? = null
        var itemView: View? = null
        var maxWidth = 0
        var maxHeight = 0
        var itemType = 0
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        for (i in 0 until adapter.count) {
            val positionType = adapter.getItemViewType(i)
            if (positionType != itemType) {
                itemType = positionType
                itemView = null
            }
            if (mMeasureParent == null) mMeasureParent = FrameLayout(context)
            itemView = adapter.getView(i, itemView, mMeasureParent)
            itemView.measure(widthMeasureSpec, heightMeasureSpec)
            val itemWidth = itemView.measuredWidth
            val itemHeight = itemView.measuredHeight
            if (itemWidth > maxWidth) maxWidth = itemWidth
            if (itemHeight > maxHeight) maxHeight = itemHeight
        }
        val tmp = IntArray(2)
        tmp[0] = maxWidth + 20
        tmp[1] = maxHeight
        return tmp
    }

    fun setClickCallback(clickItemCallback: ClickItemCallback) {
        this.callback = clickItemCallback
        setText(currentDecodeType)
    }

    private fun setText(decodeType: DecodeType) {
        text = decodeType.cn
        callback?.onClickCallback(currentDecodeType)
    }

    interface ClickItemCallback {
        fun onClickCallback(type: DecodeType)
    }

    inner class MyAdapter : BaseAdapter() {
        private val items = LinkedList<DecodeType>()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            return LinearLayout(mContext).apply {
                orientation = LinearLayout.VERTICAL
                addView(TextView(mContext).apply {
                    layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    gravity = Gravity.CENTER
                    isSingleLine = true
                    setPadding(10, 10, 10, 10)
                    setTextColor(Color.WHITE)
                    textSize = 19f
                    text = getItem(position)
                })
            }
        }

        override fun getItem(position: Int): String = items[position].cn

        override fun getItemId(position: Int): Long = 0

        override fun getCount(): Int = items.size

        fun getItemDT(position: Int): DecodeType = items[position]

        fun addItem(s: DecodeType) {
            items.add(s)
        }

//        fun addItems(s: List<String>) {
//            items.clear()
//            items.addAll(s)
//        }
    }
}