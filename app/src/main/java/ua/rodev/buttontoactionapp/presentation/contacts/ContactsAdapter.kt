package ua.rodev.buttontoactionapp.presentation.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.Mapper

class ContactsAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<NumberViewHolder>(), Mapper.Unit<List<ContactUi>> {

    private val list = mutableListOf<ContactUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NumberViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.contact_layout, parent, false),
        clickListener
    )

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount() = list.size

    override fun map(source: List<ContactUi>) {
        val diff = DiffUtilCallback(list, source)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(source)
        result.dispatchUpdatesTo(this)
    }
}

class NumberViewHolder(view: View, private val clickListener: ClickListener) :
    RecyclerView.ViewHolder(view) {

    private val title = itemView.findViewById<TextView>(R.id.tvName)
    private val subTitle = itemView.findViewById<ImageView>(R.id.ivAvatar)
    private val mapper = ContactUiMapper(title, subTitle)

    fun bind(model: ContactUi) {
        model.map(mapper)
        itemView.setOnClickListener { clickListener.click(model) }
    }
}

interface ClickListener {
    fun click(item: ContactUi)
}

class DiffUtilCallback(
    private val oldList: List<ContactUi>,
    private val newList: List<ContactUi>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].map(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].equals(newList[newItemPosition])
}