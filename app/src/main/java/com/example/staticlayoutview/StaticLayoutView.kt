package com.example.staticlayoutview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.AutoText
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.M)
class StaticLayoutView(context: Context?, attrs: AttributeSet?) : View(context, attrs),
    View.OnClickListener {
    private var mExpand = false
    private var mMessage: String
    private val mPaint: TextPaint = TextPaint()
    private var mTextWidth: Int = 0
    private var mTextHeight: Int = 0

    private lateinit var mStaticlayout: StaticLayout
    private lateinit var mLayoutBuilder: StaticLayout.Builder

    private var mDown: Bitmap
    private var mUp: Bitmap
    private val BITMAP_MARGIN_END = dp2px(4f)

    init {
        Log.e("init", "=======")
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.FILL
        mPaint.textSize = dp2px(14f).toFloat()
        mDown = BitmapFactory.decodeResource(resources, R.mipmap.main_icon_newclasses_down)
        mUp = BitmapFactory.decodeResource(resources, R.mipmap.main_icon_newclasses_up)

        mMessage =
            "uuuuuuuuuuuuuuuuuuuuu指用颜色画,如油画颜料、水彩或者水墨画,而draw 通常指用铅笔、钢笔或者粉笔画,后者一般并不涂上颜料。两动词的相应名词分别为p"

        mTextWidth = measureText(mMessage)

        setOnClickListener(this)
    }

    private fun initStaticlayout(width: Int) {
        mTextWidth = width
        mLayoutBuilder = StaticLayout.Builder.obtain(mMessage, 0, mMessage.length, mPaint, width)
        setExpand()
        mTextHeight = mStaticlayout.height

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)

        initStaticlayout(width)

        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)

        Log.e("onMeasure", "$width=======$height")

    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        var result = 0
        val size = MeasureSpec.getSize(widthMeasureSpec)
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        when (mode) {
            MeasureSpec.EXACTLY -> result = size
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> {
                result = mTextWidth
                result += paddingLeft + paddingRight
            }
        }
        result = if (mode == MeasureSpec.AT_MOST) result.coerceAtMost(size) else result
        return result
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        var result = 0
        val size = MeasureSpec.getSize(heightMeasureSpec)
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        when (mode) {
            MeasureSpec.EXACTLY -> result = size
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> {
                result = mTextHeight
                result += paddingTop + paddingBottom
            }
        }
        result = if (mode == MeasureSpec.AT_MOST) result.coerceAtMost(size) else result
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        Log.e("onDraw", "$width=======$height")

        super.onDraw(canvas)
        mStaticlayout.draw(canvas)

        val x = width - mUp.width - BITMAP_MARGIN_END
        val y =
            height * (mStaticlayout.lineCount - 0.5f) / mStaticlayout.lineCount - mUp.height / 2f

        if (mExpand) {
            canvas?.drawBitmap(mUp, x.toFloat(), y, mPaint)
        } else {
            canvas?.drawBitmap(mDown, x.toFloat(), y, mPaint)
        }

    }

    private fun setExpand() {
        if (mExpand) {
            mLayoutBuilder =
                StaticLayout.Builder.obtain(mMessage, 0, mMessage.length, mPaint, mTextWidth)
            mLayoutBuilder.setEllipsize(null)
            mLayoutBuilder.setMaxLines(Int.MAX_VALUE)
            mLayoutBuilder.setEllipsizedWidth(mTextWidth)
        } else {
            mLayoutBuilder =
                StaticLayout.Builder.obtain(mMessage, 0, mMessage.length, mPaint, mTextWidth)
            mLayoutBuilder.setEllipsize(TextUtils.TruncateAt.END)
            mLayoutBuilder.setMaxLines(2)
            mLayoutBuilder.setEllipsizedWidth(mTextWidth - mUp.width - BITMAP_MARGIN_END)
        }
        mStaticlayout = mLayoutBuilder.build()
    }

    override fun onClick(v: View?) {
        mExpand = !mExpand
        setExpand()
        requestLayout()
    }

    private fun measureText(text: String): Int {
        if (TextUtils.isEmpty(text)) {
            return 0
        }
        return mPaint.measureText(text).toInt()
    }

    private fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, resources.displayMetrics
        ).toInt()
    }

    private fun sp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpVal, resources.displayMetrics
        ).toInt()
    }
}