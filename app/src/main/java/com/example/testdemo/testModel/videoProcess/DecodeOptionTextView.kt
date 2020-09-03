package com.example.testdemo.testModel.videoProcess

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.testdemo.R
import com.example.testdemo.utlis.KLog
import kotlinx.android.synthetic.main.activity_start_phone_app.view.*
import java.util.*

/**
 * Created by Void on 2020/9/2 15:34
 */
class DecodeOptionTextView : AppCompatTextView, View.OnClickListener,
        AdapterView.OnItemClickListener,
        ViewTreeObserver.OnGlobalLayoutListener {
    private lateinit var mPopupWindow: PopupWindow
    private var mContext: Context
    private val mAdapter = MyAdapter()
    private var currentDecodeType = DecodeType.HARD
    private var isInitEnd = false

    constructor(mContext: Context) : this(mContext, null)
    constructor(mContext: Context, attrs: AttributeSet?) : super(mContext, attrs) {
        this.mContext = mContext
        mAdapter.addItem("硬解码")
        mAdapter.addItem("FFMPEG")
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
        currentDecodeType = when (mAdapter.getItem(position)) {
            "硬解码" -> DecodeType.HARD
            "FFMPEG" -> DecodeType.FFMPEG
            else -> DecodeType.OTHER
        }
        text = mAdapter.getItem(position)
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

    /**
     * 返回当前选择的解码类型
     * @see DecodeType
     */
    fun getDecodeType(): DecodeType = currentDecodeType

    inner class MyAdapter : BaseAdapter() {
        private val items = LinkedList<String>()

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

        override fun getItem(position: Int): String = items[position]

        override fun getItemId(position: Int): Long = 0

        override fun getCount(): Int = items.size

        fun addItem(s: String) {
            items.add(s)
        }

//        fun addItems(s: List<String>) {
//            items.clear()
//            items.addAll(s)
//        }
    }
}