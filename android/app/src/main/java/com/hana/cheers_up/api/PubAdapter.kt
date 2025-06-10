package com.hana.cheers_up.api

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hana.cheers_up.api.response.PubResponse
import com.hana.cheers_up.databinding.ItemPubBinding

class PubAdapter(
    private val pubList: List<PubResponse>,
    private val onItemClick: (PubResponse) -> Unit
) : RecyclerView.Adapter<PubAdapter.PubViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PubViewHolder {
        val binding = ItemPubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PubViewHolder, position: Int) {
        holder.bind(pubList[position])
    }

    override fun getItemCount(): Int = pubList.size


    inner class PubViewHolder(private val binding: ItemPubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pub: PubResponse) {
            binding.apply {
                tvPubName.text = pub.pubName
                tvPubAddress.text = pub.pubAddress
                tvDistance.text = pub.distance
                tvCategory.text = pub.categoryName

                // 길안내 버튼
                btnDirection.setOnClickListener {
                    openUrl(pub.directionUrl)
                }

                // 로드뷰 버튼
                btnRoadView.setOnClickListener {
                    openUrl(pub.roadViewUrl)
                }

                root.setOnClickListener {
                    onItemClick(pub)
                }
            }
        }

        private fun openUrl(url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            } catch (e : Exception) {

            }
        }
    }
}