package com.gappein.sdk.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.base.ChatBaseView
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.show
import kotlinx.android.synthetic.main.header_view.view.*

class GappeinHeader : LinearLayout, ChatBaseView {

    private lateinit var view: View
    private lateinit var toolbar: Toolbar

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews()
    }

    private fun initViews() {
        view = inflate(context, R.layout.header_view, this)
    }

    fun init(channelId: String) {
        getClient().getChannelRecipientUser(channelId) {
            getClient().getTypingStatus(channelId, it.token) { typingStatus ->
                if (typingStatus != "-") {
                    view.typingStatus.show()
                    view.typingStatus.text = typingStatus
                } else {
                    view.typingStatus.hide()
                }
            }
            view.titleToolbar.text = it.name

            Glide.with(view)
                .load(it.profileImageUrl)
                .placeholder(R.drawable.ic_user_placeholder)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(view.avatarImageView)
        }
    }

    fun init(channelId: () -> String) {
        init(channelId.invoke())
    }

    fun setMenu(@MenuRes menu: Int) {
        view.toolbar.inflateMenu(menu)
    }

    fun setOnBackPressed(onBackPress: () -> Unit) {
        view.imageViewBack.setOnClickListener { onBackPress.invoke() }
    }

    override fun getClient() = ChatClient.getInstance()

}