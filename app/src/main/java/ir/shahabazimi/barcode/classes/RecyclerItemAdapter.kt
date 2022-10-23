package ir.shahabazimi.barcode.classes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.shahabazimi.barcode.databinding.ItemRecyclerBinding

class RecyclerItemAdapter : RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder>(){
    private lateinit var binding: ItemRecyclerBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemAdapter.ViewHolder {
        binding= ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: RecyclerItemAdapter.ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount()=differ.currentList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root){
        fun setData(item : RecyclerItemModel){
            binding.apply {
                weight.text = item.weight
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<RecyclerItemModel>(){
        override fun areItemsTheSame(oldItem: RecyclerItemModel, newItem: RecyclerItemModel): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: RecyclerItemModel, newItem: RecyclerItemModel): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)

}