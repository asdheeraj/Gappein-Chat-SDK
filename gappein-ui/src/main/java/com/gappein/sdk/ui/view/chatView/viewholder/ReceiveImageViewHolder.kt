package com.gappein.sdk.ui.view.chatView.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.ui.view.util.hide
import com.gappein.sdk.ui.view.util.onDoubleTapListener
import com.gappein.sdk.ui.view.util.show
import kotlinx.android.synthetic.main.item_image_received_message.view.*

class ReceiveImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        private const val GIPHY = "giphy"
        private const val SPACE_SPLITTER = " "
    }

    fun bind(
        message: Message,
        onImageClick: (String) -> Unit,
        onMessageLike: (String) -> Unit
    ) {

        if (message.message.contains(GIPHY)) {
            if (ChatClient.getInstance().getApiKey().isNotEmpty()) {
                val _message = message.message
                val listOfMessages = _message.split(SPACE_SPLITTER)

                view.gifView.setMediaWithId(listOfMessages.last())
                view.gifView.show()
                view.receivedImageMessage.hide()
            } else {
                view.run {
                    gifView.hide()
                    receivedImageMessage.hide()
                }
            }
        } else {
            Glide.with(view)
                .load(message.message)
                .transform(CenterCrop(), RoundedCorners(48))
                .into(view.receivedImageMessage)
            view.receivedImageMessage.show()
            view.gifView.hide()
        }

        view.likeImageView.visibility = if (message.liked) View.VISIBLE else View.INVISIBLE

        view.receivedImageMessage.setOnClickListener{ onImageClick.invoke(message.message) }

        if (!message.deleted) {
            view.onDoubleTapListener {
                onMessageLike(message._id)
            }
        }
    }
}