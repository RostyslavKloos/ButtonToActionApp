package ua.rodev.buttontoactionapp.presentation.contacts

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ContactUiMapper(
    private val name: TextView,
    private val avatar: ImageView,
) : ContactUi.Mapper<Unit> {

    override fun map(name: String, avatarUri: String) {
        this.name.text = name
        Glide
            .with(this.name)
            .load(avatarUri)
            .centerCrop()
            .into(avatar)
    }
}
