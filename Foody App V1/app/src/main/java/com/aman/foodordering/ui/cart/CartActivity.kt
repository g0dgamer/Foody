package com.aman.foodordering.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.foodordering.Order
import com.aman.foodordering.R
import com.aman.foodordering.databinding.ActivityCartBinding
import com.aman.foodordering.extension.createFactory
import com.aman.foodordering.repo.OrderRepoI
import com.aman.foodordering.room.entity.Food
import com.aman.foodordering.telp
import com.aman.foodordering.ui.MainViewModel
import com.aman.foodordering.ui.adapter.FoodAdapter
import com.aman.foodordering.ui.adapter.OnClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_cart.*
import javax.inject.Inject

class CartActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: FoodAdapter

    private lateinit var binding: ActivityCartBinding

    private var cartList: List<Food> = listOf()

    private var itemPosition: Int = 0

    @Inject
    lateinit var orderRepoI: OrderRepoI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)

        init()
        getCartList()
        setObserver()
        setRecyclerView()
        onClicks()

    }

    private fun init() {
        Log.d(TAG, " >>> Initializing viewModel")

        val factory = MainViewModel(orderRepoI).createFactory()
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private fun getCartList() {
        viewModel.getCartList()
    }

    private fun setObserver() {
        viewModel.observableState.observe(this, Observer {
            when {
                it.success -> {
                    Handler().postDelayed({
                        binding.state = it
                    }, 700)

                    cartList = it.list!!
                    if (cartList.size > 2) {
                        adapter.result = listOf(cartList[0], cartList[1])
                        binding.shouldShowMore = true
                    } else {
                        adapter.result = cartList
                        binding.shouldShowMore = false
                    }
                    adapter.notifyItemChanged(itemPosition)
                    calculateTotalPrice(cartList)
                }
                else -> binding.state = it
            }
        })
    }

    private fun calculateTotalPrice(cartList: List<Food>?) {
        var totalPrice = 0
        cartList?.let { list ->
            list.forEach {
                if (it.quantity!! != 0) {
                    totalPrice += it.rate.toInt() * it.quantity!!
                }
            }
        }
        binding.totalPrice = String.format(getString(R.string.total_price), totalPrice.toString())
    }

    private fun setRecyclerView() {
        rv_cart.layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(cartList, object : OnClickListener {
            override fun update(food: Food, position: Int) {
                itemPosition = position
                viewModel.updateItem(food)
            }
        })
        rv_cart.adapter = adapter
        rv_cart.itemAnimator = null
    }

    private fun onClicks() {
        view_place_order_bottom.setOnClickListener() {
//            Toast.makeText(this@CartActivity, "u clik me ",Toast.LENGTH_SHORT).show();
            val detailIntent = Intent(this,telp::class.java);
            startActivity(detailIntent);
        }
        tv_show_more.setOnClickListener {
            binding.shouldShowMore = false
            adapter.result = cartList
            adapter.notifyDataSetChanged()
        }
        btn_back.setOnClickListener { onBackPressed() }
    }

    companion object {
        const val TAG = "CartActivity"

        fun start(context: Context) {
            context.startActivity(Intent(context, CartActivity::class.java))
        }
    }
}
