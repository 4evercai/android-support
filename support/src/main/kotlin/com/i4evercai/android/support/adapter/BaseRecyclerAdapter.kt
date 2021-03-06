package com.i4evercai.android.support.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.OnCompositionLoadedListener
import com.i4evercai.android.support.R
import com.i4evercai.android.support.widget.RecyclerView.BaseViewHolder
import kotlinx.android.synthetic.main.support_item_empty.view.*

/**
 *
 * @Description: BaseRecyclerAdapter
 * @author Fitz
 * @email FitzPro@qq.com
 * @date 2017/9/4 14:39
 * @version V1.0
 */
abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    companion object {
        private val S_TYPE_EMPTY = -25555411

        const val EMPTY_NOTICE_STATUS_LOADING = 1
        const val EMPTY_NOTICE_STATUS_NO_DATA = 2

    }

    protected val mContext: Context
    protected var mIsShowEmptyView = true
    protected var mLoadingLottieAnimName: String = "support_empty_loading_lottie.json"
    protected var mLoadingMsg: String = ""
    protected var mLoadingImageViewResId: Int = 0
    protected var mEmptyLottieAnimName: String = "support_empty_no_data_lottie.json"
    protected var mEmptyMsg: String = ""
    protected var mEmptyImageViewResId: Int = 0
    protected var mEmptyStatus: Int = EMPTY_NOTICE_STATUS_NO_DATA

    constructor(context: Context) : this(context, true)

    constructor(context: Context, showEmptyView: Boolean) : super() {
        this.mContext = context
        this.mIsShowEmptyView = showEmptyView
        this.mLoadingMsg = context.resources.getString(R.string.support_recycler_empty_loading_msg)
        this.mEmptyMsg = context.resources.getString(R.string.support_recycler_empty_no_data_msg)
    }

    constructor(context: Context, emptyLottieAnimName: String, emptyMsg: String) : super() {
        this.mContext = context
        this.mEmptyLottieAnimName = emptyLottieAnimName
        this.mEmptyMsg = emptyMsg
    }

    constructor(context: Context, emptyLottieAnimName: String, @StringRes emptyMsgRes: Int) : super() {
        this.mContext = context
        this.mEmptyLottieAnimName = emptyLottieAnimName
        this.mEmptyMsg = context.getString(emptyMsgRes)
    }

    constructor(context: Context, @DrawableRes mEmptyImageViewResId: Int, @StringRes emptyMsgRes: Int) : super() {
        this.mContext = context
        this.mEmptyImageViewResId = mEmptyImageViewResId
        this.mEmptyMsg = context.getString(emptyMsgRes)
    }

    constructor(context: Context, @StringRes emptyMsgRes: Int) : super() {
        this.mContext = context
        this.mEmptyMsg = context.getString(emptyMsgRes)
    }

    constructor(context: Context, emptyMsg: String) : super() {
        this.mContext = context
        this.mEmptyMsg = emptyMsg
    }

    open fun updateEmptyNoticeStatus(noticeStatus: Int) {
        mEmptyStatus = noticeStatus
        if (getAdapterItemCount() == 0 && mIsShowEmptyView) {
            notifyDataSetChanged()
        }
    }

    open fun updateEmptyViewData(emptyLottieAnimName: String,@DrawableRes emptyImageViewResId: Int, @StringRes emptyMsgRes: Int) {
        updateEmptyViewData(emptyLottieAnimName,emptyImageViewResId, mContext.getString(emptyMsgRes))
    }

    open fun updateEmptyViewData(emptyLottieAnimName: String,@DrawableRes emptyImageViewResId: Int, emptyMsg: String) {
        this.mEmptyLottieAnimName = emptyLottieAnimName
        this.mEmptyMsg = emptyMsg
        this.mEmptyImageViewResId = emptyImageViewResId
        if (getAdapterItemCount() == 0 && mIsShowEmptyView) {
            notifyDataSetChanged()
        }
    }


    final override fun getItemCount(): Int {
        if (getAdapterItemCount() == 0 && mIsShowEmptyView) {
            return 1
        } else {
            return getAdapterItemCount()
        }
    }

    final override fun getItemViewType(position: Int): Int {
        if (getAdapterItemCount() == 0 && mIsShowEmptyView) {
            return S_TYPE_EMPTY
        } else {
            return getAdapterItemViewType(position)
        }
    }


    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isEmptyViewHolder(holder)) {
            onBindEmptyViewHolder(holder)
        } else {
            onBindAdapterViewHolder(holder as VH, position)
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (S_TYPE_EMPTY == viewType) {
            return getEmptyViewHolder(parent)
        } else {
            return onCreateAdapterViewHolder(parent, viewType)
        }
    }


    fun getEmptyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {

        return EmptyViewHolder(mContext, parent)
    }

    fun onBindEmptyViewHolder(holder: RecyclerView.ViewHolder) {
        if (holder is EmptyViewHolder) {
            val emptyViewHolder: EmptyViewHolder = holder
            if (mEmptyStatus == EMPTY_NOTICE_STATUS_LOADING){
                emptyViewHolder.setData(mLoadingLottieAnimName, mLoadingImageViewResId,mLoadingMsg)
            }else{
                emptyViewHolder.setData(mEmptyLottieAnimName, mEmptyImageViewResId,mEmptyMsg)
            }

        }

    }

    /**
     * 判断是否是EmptyViewHolder
     */
    fun isEmptyViewHolder(holder: RecyclerView.ViewHolder): Boolean {
        return holder is EmptyViewHolder
    }

    open class EmptyViewHolder : BaseViewHolder, OnCompositionLoadedListener {
        private val context: Context

        constructor(context: Context, parent: ViewGroup?) : super(context, parent, R.layout.support_item_empty) {
            this.context = context
        }

        fun setData(emptyLottieAnimName: String, emptyDrawableRes: Int, emptyMsg: String) = with(itemView) {

            if (emptyDrawableRes > 0) {
                supportLottieView.cancelAnimation()
                supportLottieView.setImageResource(emptyDrawableRes)
            } else {
                LottieComposition.Factory.fromAssetFileName(context, emptyLottieAnimName, this@EmptyViewHolder)
            }


            //  supportIvEmptyNotice.setImageResource(emptyIconRes)
            supportTvEmptyMsg.text = emptyMsg
        }

        override fun onCompositionLoaded(composition: LottieComposition?) {
            if (composition != null) {
                with(itemView) {
                    supportLottieView.setComposition(composition)
                    supportLottieView.playAnimation()
                }
            }
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    val spanCount = manager.spanCount
                    if (type == S_TYPE_EMPTY) {
                        return spanCount
                    } else {
                        return getGridLayoutManagerSpanSize(position)
                    }
                }
            }
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder is EmptyViewHolder) {
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                if (holder is EmptyViewHolder) {
                    lp.isFullSpan = true
                }
            }
        }
        super.onViewAttachedToWindow(holder)
    }

    open fun getGridLayoutManagerSpanSize(position: Int): Int = 1

    open fun getAdapterItemViewType(position: Int): Int = 0

    abstract fun getAdapterItemCount(): Int

    abstract fun onCreateAdapterViewHolder(parent: ViewGroup?, viewType: Int): VH
    abstract fun onBindAdapterViewHolder(holder: VH, position: Int)


}