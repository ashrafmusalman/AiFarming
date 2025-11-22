import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.R
import com.shahbaz.farming.datamodel.chatbot.ChatResponseHistory

class ChatHistoryAdapter(
    private val onItemClick: (ChatResponseHistory) -> Unit
) : RecyclerView.Adapter<ChatHistoryAdapter.HistoryViewHolder>() {

    private val list = mutableListOf<ChatResponseHistory>()

    fun submitList(newList: List<ChatResponseHistory>) {
        list.clear()
        list.addAll(newList.filter { !it.isBot }) // only USER messages
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ChatResponseHistory) {
            view.findViewById<TextView>(R.id.historyText).text = item.message
            view.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
