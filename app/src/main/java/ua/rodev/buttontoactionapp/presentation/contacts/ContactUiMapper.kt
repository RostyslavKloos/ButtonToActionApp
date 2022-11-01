package ua.rodev.buttontoactionapp.presentation.contacts

import android.widget.ImageView
import android.widget.TextView

class ContactUiMapper(
    private val head: TextView,
    private val subTitle: ImageView,
) : ContactUi.Mapper<Unit> {

    override fun map(id: String, avatarUri: String?) {
        head.text = id
        // TODO: add image
    }
}