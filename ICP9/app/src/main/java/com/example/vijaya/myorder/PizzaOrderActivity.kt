package com.example.vijaya.myorder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import java.util.*
import kotlin.random.Random


class PizzaOrderActivity : AppCompatActivity() {

    private var nameInput: EditText? = null
    private var numberInput: EditText? = null
    private var jalapenoChecked: ImageView? = null
    private var olivesChecked: ImageView? = null
    private var pineappleChecked: ImageView? = null
    private var tomatoChecked: ImageView? = null
    private var pepperChecked: ImageView? = null
    private var cheeseChecked: CheckBox? = null
    private var chiliChecked: CheckBox? = null
    private var oreganoChecked: CheckBox? = null
    private var quantity: Int = 1
    private var orderNow: Button? = null
    private var summary: Button? = null
    private var spinner: Spinner? = null

    private var isJalapenoClicked: Boolean = false
    private var isOliveClicked: Boolean = false
    private var isPineappleClicked: Boolean = false
    private var isTomatoClicked: Boolean = false
    private var isPepperClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(R.string.app_name)

        nameInput = findViewById(R.id.name_input)
        numberInput = findViewById(R.id.number_input)
        jalapenoChecked = findViewById(R.id.jalapeno)
        olivesChecked = findViewById(R.id.olives)
        pineappleChecked = findViewById(R.id.pineapple)
        tomatoChecked = findViewById(R.id.tomato)
        pepperChecked = findViewById(R.id.pepper)
        cheeseChecked = findViewById(R.id.cheese)
        chiliChecked = findViewById(R.id.chilli)
        oreganoChecked = findViewById(R.id.oregano)
        orderNow = findViewById(R.id.order_now)
        summary = findViewById(R.id.summary)
        spinner = findViewById(R.id.picker)

        val arrayList = ArrayList<String>()
        arrayList.add("+1")
        arrayList.add("+91")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = arrayAdapter
    }

    override fun onResume() {
        super.onResume()
        jalapenoChecked?.setOnClickListener {
            isJalapenoClicked = if (!isJalapenoClicked) {
                jalapenoChecked?.setImageResource(R.drawable.jalapenoselected)
                true
            } else {
                jalapenoChecked?.setImageResource(R.drawable.jalapeno)
                false
            }
        }
        olivesChecked?.setOnClickListener {
            isOliveClicked = if (!isOliveClicked) {
                olivesChecked?.setImageResource(R.drawable.olivesselected)
                true
            } else {
                olivesChecked?.setImageResource(R.drawable.olives)
                false
            }

        }
        pineappleChecked?.setOnClickListener {
            isPineappleClicked = if (!isPineappleClicked) {
                pineappleChecked?.setImageResource(R.drawable.pineappleselected)
                true
            } else {
                pineappleChecked?.setImageResource(R.drawable.pineapple)
                false
            }
        }
        tomatoChecked?.setOnClickListener {
            isTomatoClicked = if (!isTomatoClicked) {
                tomatoChecked?.setImageResource(R.drawable.tomatoselected)
                true
            } else {
                tomatoChecked?.setImageResource(R.drawable.tomato)
                false
            }

        }
        pepperChecked?.setOnClickListener {
            isPepperClicked = if (!isPepperClicked) {
                pepperChecked?.setImageResource(R.drawable.bellpepperselected)
                true
            } else {
                pepperChecked?.setImageResource(R.drawable.bellpepper)
                false
            }

        }
        orderNow?.setOnClickListener {

            val isInputAllCorrect = isValidInput()
            if (!isInputAllCorrect) {
                val errorMessage = getErrorMessage()
                showUserDialog(errorMessage)
            } else {
                val body: String = frameEmailBody()
                val subject: String = frameEmailSubject()

                val send = Intent(Intent.ACTION_SENDTO)
                val uriText = "mailto:" + Uri.encode("order@pizzeria.com") +
                        "?subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body)
                val uri = Uri.parse(uriText)

                send.data = uri
                startActivity(Intent.createChooser(send, "Send mail..."))
            }
        }

        summary?.setOnClickListener {

            val isInputAllCorrect = isValidInput()
            if (!isInputAllCorrect) {
                val errorMessage = getErrorMessage()
                showUserDialog(errorMessage)
            } else {
                val name: String = nameInput?.text.toString()
                val ingredientText: String = getIngredientsText()
                val totalPrice: String = priceCalculator()

                val routeSummary = Intent(this, SummaryActivity::class.java)
                val bundle = Bundle()
                bundle.putString("NAME", name)
                bundle.putString("INGREDIENTS", ingredientText)
                bundle.putString("TOTALPRICE", totalPrice)
                bundle.putString("QUANTITY", quantity.toString())
                routeSummary.putExtras(bundle)
                startActivity(routeSummary)
            }
        }
    }

    private fun showUserDialog(error: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                .setTitle("Error in placing order")
                .setMessage(error)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(R.drawable.alert)
        builder.show()
    }


    /**
     * Validate if all the inputs are correct
     */
    private fun isValidInput(): Boolean {
        val name: String = nameInput?.text.toString()
        val phone: String = numberInput?.text.toString()

        val phoneValid = if (phone.isNotEmpty()) {
            Patterns.PHONE.matcher(phone).matches()
        } else true

        return name.isNotEmpty() && phone.isNotEmpty() && phoneValid
    }

    /**
     * Frame error message t
     */
    private fun getErrorMessage(): String {
        val name: String = nameInput?.text.toString()
        val phone: String = numberInput?.text.toString()

        var errorMessage = "You need to fill all details to continue:\n"
        if (name.isEmpty()) {
            errorMessage += "• Name\n"
        }

        if (phone.isEmpty()) {
            errorMessage += "• Phone Number\n"
        }

        val phoneValid = if (phone.isNotEmpty()) {
            Patterns.PHONE.matcher(phone).matches()
        } else true

        if (!phoneValid) {
            errorMessage += "• Valid Phone Number\n"
        }

        return errorMessage
    }

    /**
     * Frame ingredients text.
     */
    private fun getIngredientsText(): String {
        var selectedIngredients = ""
        if (isJalapenoClicked) {
            selectedIngredients += " Jalapeno\n"
        }
        if (isOliveClicked) {
            selectedIngredients += " Olive\n"
        }
        if (isPineappleClicked) {
            selectedIngredients += " Pineapple\n"
        }
        if (isTomatoClicked) {
            selectedIngredients += " Tomato\n"
        }
        if (isPepperClicked) {
            selectedIngredients += " Bell peppers\n"
        }
        if (cheeseChecked?.isChecked!!) {
            selectedIngredients += " Cheese\n"
        }
        if (chiliChecked?.isChecked!!) {
            selectedIngredients += " Chilli flakes\n"
        }
        if (oreganoChecked?.isChecked!!) {
            selectedIngredients += " Oregano\n"
        }

        if (selectedIngredients.isEmpty()) {
            return "No ingredients selected."
        }

        return "Your ingredients: \n$selectedIngredients"
    }


    /**
     * Email Subject
     */
    private fun frameEmailSubject(): String {
        return "Order number is ${Random.nextInt(100, 1000)}"
    }

    /**
     * Email Body
     */
    private fun frameEmailBody(): String {
        val name: String = nameInput?.text.toString()
        val phone: String = numberInput?.text.toString()
        val countryCode: String = spinner?.selectedItem.toString()
        val selectedIngredients: String = getIngredientsText()

        return "Hey $name, \n " +
                "Your order will be  successfully placed. \n\n" +
                "Summary of your order is mentioned below. \n " +
                "Quantity: $quantity \n " +
                "\n $selectedIngredients \n\n" +
                "Your Cost: ${priceCalculator()} \n " +
                "We will send a text message on $countryCode-$phone \n\n" +
                "Thank you! Enjoy your Pizza hot!"
    }

    /**
     * Calculate price of pizza.
     *
     * Each Pizza is 10$ and 1$ for topping.
     */
    private fun priceCalculator(): String {
        val perPizza = 10
        var pricePerTopping = 0

        if (isJalapenoClicked) {
            pricePerTopping++
        }
        if (isOliveClicked) {
            pricePerTopping++
        }
        if (isPineappleClicked) {
            pricePerTopping++
        }
        if (isTomatoClicked) {
            pricePerTopping++
        }
        if (isPepperClicked) {
            pricePerTopping++
        }
        val eachPizzaPrice = perPizza + pricePerTopping
        val totalPrice: Int = quantity * eachPizzaPrice
        val tax = .095
        val gratuity = 0.18
        val taxAdded = totalPrice + (totalPrice * tax) + (totalPrice * gratuity)
        return "Pizza Price : $ $eachPizzaPrice \n" +
                "Quantity : $quantity \n " +
                "Tax : 9.5% \n" +
                "Gratuity : 18% \n" +
                "Total Price : $ ${String.format("%.2f",taxAdded)}"
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private fun display(number: Int) {
        val quantityTextView = findViewById<View>(R.id.quantity_text_view) as TextView
        quantityTextView.text = number.toString()
    }


    /**
     * This method decrements the quantity of coffee cups by one
     */
    fun decrement(view: View?) {
        if (quantity > 1) {
            quantity -= 1
            display(quantity)
        } else {
            Log.i("PizzaOrderActivity", "Please select at least one pizza")
            Toast.makeText(this, getString(R.string.no_pizza), Toast.LENGTH_SHORT).show()
            return
        }
    }

    /**
     * This method increments the quantity of coffee cups by one
     */
    fun increment(view: View?) {
        if (quantity < 5) {
            quantity += 1
            display(quantity)
        } else {
            Log.i("PizzaOrderActivity", "Please select less than five pizzas")
            Toast.makeText(this, getString(R.string.too_much_pizza), Toast.LENGTH_SHORT).show()
            return
        }
    }

}
