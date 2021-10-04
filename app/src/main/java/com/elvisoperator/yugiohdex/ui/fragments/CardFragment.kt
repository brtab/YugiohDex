package com.elvisoperator.yugiohdex.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elvisoperator.yugiohdex.Data
import com.elvisoperator.yugiohdex.R
import com.elvisoperator.yugiohdex.models.CardAdapter
import com.elvisoperator.yugiohdex.network.YugiohAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.elvisoperator.yugiohdex.*

import com.elvisoperator.yugiohdex.databinding.CardFragmentBinding

class CardFragment : Fragment() {

    companion object {
        fun newInstance() = CardFragment()
    }

    lateinit var adapter : CardAdapter
    private val listCards = mutableListOf<Data>()

    private lateinit var viewModel: CardViewModel
    lateinit var binding: CardFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         binding = CardFragmentBinding.bind(view)

        initReciclerView()
        testReciclerView()

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        // TODO: Use the ViewModel


    }

    private fun testReciclerView() {
        viewLifecycleOwner.lifecycleScope.launch  {
            val call =getListCharacter().create(YugiohAPI::class.java).getCharacters("?format=Speed%20Duel")
            val cards = call.body()
            activity?.runOnUiThread {
                if(call.isSuccessful){
                    val actualCards = cards?.list ?: emptyList()
                    listCards.clear()
                    listCards.addAll(actualCards)
                    adapter.notifyDataSetChanged()

                }else{
                    showError()
                }
            }

        }
    }

    private fun showError() {
        Toast.makeText(activity,"Error", Toast.LENGTH_LONG).show()
    }

    private fun initReciclerView() {
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(activity)
        adapter = CardAdapter(listCards)
        binding.recyclerViewCard.adapter = adapter

    }

    private fun  getListCharacter(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://db.ygoprodeck.com/api/v7/cardinfo.php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



}