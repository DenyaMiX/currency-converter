package com.federicocotogno.currencyconversionapi.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.federicocotogno.currencyconversionapi.R
import kotlinx.android.synthetic.main.fragment_convertor.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ConvertorFragment : Fragment() {

    var baseCurrency = "EUR"
    var convertedToCurrency = "USD"
    var conversionRate = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_convertor, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerSetup()
        textChangedStuff()
    }

    private fun textChangedStuff() {
        view?.et_firstConversion?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val value = s.toString().toDouble()
                    getApiResult(value)
                } catch (e: Exception) {
                    Log.d("test1", e.toString());
                    Toast.makeText(view?.context, "Type a value", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "OnTextChanged")
            }

        })

    }

    private fun getApiResult(value: Double) {
        if (view?.et_firstConversion != null && view?.et_firstConversion?.text?.isNotEmpty() == true && view?.et_firstConversion?.text?.isNotBlank() == true) {

            var API =
                "https://api.apilayer.com/exchangerates_data/convert?to=$convertedToCurrency&from=$baseCurrency&amount=1"

            if (baseCurrency == convertedToCurrency) {
                Toast.makeText(
                    view?.context,
                    "Please pick a currency to convert",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                GlobalScope.launch(Dispatchers.IO) {

                    try {

                        val client = OkHttpClient().newBuilder().build();
                        val request = Request.Builder()
                            .url(API)
                            .addHeader("apikey", "KBdps7ySiew9Od1RzQwcWHtpPhv7gN6p")
                            .method("GET", null)
                            .build();
                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.d("http", e.toString());
                            }
                            override fun onResponse(call: Call, response: Response) {
                                val apiResult = response.body?.string()

                                Log.d("ceva", apiResult.toString())
                                val jsonObject = JSONObject(apiResult)
                                conversionRate =
                                    jsonObject.getDouble("result")

                                Log.d("Main", "$conversionRate")
                                Log.d("Main", apiResult.toString())

                                val text = value * conversionRate;

                                activity?.runOnUiThread(Runnable {
                                    view?.et_secondConversion?.setText(text.toString())
                                })
                            }
                        });
                    } catch (e: Exception) {
                        Log.e("Main", "$e")
                    }
                }
            }
        }
    }

    private fun spinnerSetup() {

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            view?.spinner_firstConversion?.adapter = adapter

        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies2,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            view?.spinner_secondConversion?.adapter = adapter

        }

        view?.spinner_firstConversion?.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                baseCurrency = parent?.getItemAtPosition(position).toString()
                var value = 0.0;
                try {
                    value = view?.et_firstConversion?.text.toString().toDouble()
                }
                catch (e: Exception) {

                }
                getApiResult(value)
            }

        })

        view?.spinner_secondConversion?.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                convertedToCurrency = parent?.getItemAtPosition(position).toString()
                var value = 0.0;
                try {
                    value = view?.et_firstConversion?.text.toString().toDouble()
                }
                catch (e: Exception) {

                }
                getApiResult(value)
            }

        })
    }

}