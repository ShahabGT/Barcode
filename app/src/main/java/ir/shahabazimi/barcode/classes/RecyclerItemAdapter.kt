package ir.shahabazimi.barcode.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.shahabazimi.barcode.R
import ir.shahabazimi.barcode.databinding.ItemRecyclerBinding

class RecyclerItemAdapter(
    private val data: MutableList<RecyclerItemModel>,
    private val onSelect: (RecyclerItemModel?) -> Unit
) :
    RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder>() {

    private lateinit var binding: ItemRecyclerBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerItemAdapter.ViewHolder {
        binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: RecyclerItemAdapter.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.setData(data[position])
    }

    override fun getItemCount() = data.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun setData(item: RecyclerItemModel) = with(binding) {
            weight.text = root.context.getString(R.string.row_title, item.weight)
            row.text = item.id.toString()
            delete.setOnClickListener { onSelect(item) }
        }
    }

    fun add(newData: MutableList<RecyclerItemModel>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

}