package com.example.samojlov_av_homework_module_13_number_6

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samojlov_av_homework_module_13_number_6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbarMain: androidx.appcompat.widget.Toolbar
    private lateinit var nameET: EditText
    private lateinit var priceET: EditText
    private lateinit var weightET: EditText
    private lateinit var saveBT: Button
    private lateinit var listViewLV: ListView

    private var productList = mutableListOf<Product>()
    private var listAdapter: ArrayAdapter<Product>? = null
    private var id = 0

    private val db = DBHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        dbEnter()
    }

    override fun onResume() {
        super.onResume()
        listViewLV.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                updateRecord(productList[position])
            }
    }

    private fun updateRecord(product: Product) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogValues = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogValues)
        val editName = dialogValues.findViewById<EditText>(R.id.updateNameET)
        val editPrice = dialogValues.findViewById<EditText>(R.id.updatePriceET)
        val editWeight = dialogValues.findViewById<EditText>(R.id.updateWeightET)

        editName.setText(product.name)
        editPrice.setText(product.price)
        editWeight.setText(product.weight)

        dialogBuilder.setTitle(getString(R.string.dialog_Title))
        dialogBuilder.setPositiveButton(getString(R.string.dialog_PositiveButton)) { _, _ ->
            val updateName = editName.text.toString().trim()
            val updatePrice = editPrice.text.toString().trim()
            val updateWeight = editWeight.text.toString().trim()

            val updateProduct = Product(product.id, updateName, updatePrice, updateWeight)
            db.updateProduct(updateProduct)
            listAdapterInit()
            Toast.makeText(this, getString(R.string.dialog_PositiveButton_Toast), Toast.LENGTH_LONG)
                .show()
        }
        dialogBuilder.setNeutralButton(getString(R.string.dialog_NeutralButton)) { _, _ ->
            db.deleteProduct(product)
            listAdapterInit()
            Toast.makeText(this, getString(R.string.dialog_NeutralButton_Toast), Toast.LENGTH_LONG).show()
        }
        dialogBuilder.setNegativeButton(getString(R.string.dialog_NegativeButton), null)
        dialogBuilder.create().show()
    }

    private fun dbEnter() {
        saveBT.setOnClickListener {
            if (nameET.text.isEmpty() || priceET.text.isEmpty() || weightET.text.isEmpty()) return@setOnClickListener

            val name = nameET.text.toString().trim()
            val price = priceET.text.toString().trim()
            val weight = weightET.text.toString().trim()

            val product = Product(id++, name, price, weight)
            db.addProduct(product)
            Toast.makeText(this, getString(R.string.addProduct_Toast, name), Toast.LENGTH_LONG)
                .show()
            listAdapterInit()

            nameET.text.clear()
            priceET.text.clear()
            weightET.text.clear()
        }
    }

    private fun listAdapterInit() {
        productList = db.readProduct()
        listAdapter = ListAdapter(this, productList)
        listViewLV.adapter = listAdapter
        listAdapter!!.notifyDataSetChanged()
    }

    private fun init() {

        toolbarMain = binding.toolbarMain
        setSupportActionBar(toolbarMain)
        title = getString(R.string.toolbar_title)
        toolbarMain.subtitle = getString(R.string.toolbar_subtitle)

        nameET = binding.tablePanel.nameET
        priceET = binding.tablePanel.priceET
        weightET = binding.tablePanel.weightET
        saveBT = binding.saveBT
        listViewLV = binding.listViewLV

        listAdapterInit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    @SuppressLint("SetTextI18n", "ShowToast")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenu -> {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_exit),
                    Toast.LENGTH_LONG
                ).show()
                finishAffinity()
            }

            R.id.clearMenu -> {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.clearMenu_Toast), Toast.LENGTH_LONG
                ).show()
                db.removeAll()
                listAdapterInit()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}