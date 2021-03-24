package com.example.vijaya.myorder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class SummaryActivity : AppCompatActivity() {

    private var name: TextView? = null
    private var topping: TextView? = null
    private var quantity: TextView? = null
    private var price: TextView? = null
    private var goToOrders: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_summary)
        title = getString(R.string.pizza_sumary)

        name = findViewById(R.id.dear)
        topping = findViewById(R.id.topping)
        quantity = findViewById(R.id.quantity)
        price = findViewById(R.id.totalamount)
        goToOrders = findViewById(R.id.gotoorders)

        val bundle = intent.extras

        val printName = "${getString(R.string.dear)} ${bundle?.getString("NAME")},"
        name?.text = printName

        val toppingName = "${bundle?.getString("INGREDIENTS")}"
        topping?.text = toppingName

        val quantityName = "${getString(R.string.summary_quantity)} ${bundle?.getString("QUANTITY")}"
        quantity?.text = quantityName

        val priceName = "${bundle?.getString("TOTALPRICE")}"
        price?.text = priceName


    }

    override fun onResume() {
        super.onResume()
        goToOrders?.setOnClickListener {
            finish()
        }
    }
}