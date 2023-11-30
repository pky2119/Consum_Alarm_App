package com.example.consum_alarm_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val productList: MutableList<MainPage.Product> = mutableListOf()

    fun setData(products: List<MainPage.Product>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.rectify_name)
        private val purchaseDateTextView: TextView = itemView.findViewById(R.id.rectify_date)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.rectify_price)
        private val expirationDateTextView: TextView = itemView.findViewById(R.id.rectify_expiration_date)
        private val remainingExpirationTextView: TextView = itemView.findViewById(R.id.rectify_Remaining_expiration_date)

        fun bind(product: MainPage.Product) {
            productNameTextView.text = "제품명: ${product.productName}"
            purchaseDateTextView.text = "제품 구매일: ${product.purchaseDate}"
            productPriceTextView.text = "제품 가격: ${product.price}"
            expirationDateTextView.text = "소비기한: ${product.expirationDate}"
            remainingExpirationTextView.text = "남은 소비기한: ${product.remainingExpiration}"
        }
    }
}
