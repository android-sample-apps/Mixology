package com.zemingo.drinksmenu.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.extensions.fromLink
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.view_model.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.list_item_suggestion.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


/*
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by viewModel()

    private val previousSearchesAdapter = DrinkPreviewAdapter().apply {
        onClick = { onDrinkClicked(it) }
    }

    private val suggestionAdapter: SuggestionAdapter by lazy {
        SuggestionAdapter(
            requireContext(),
            R.layout.list_item_suggestion
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_result_rv.adapter = previousSearchesAdapter
        initAutoComplete()
        searchViewModel.suggestions.observe(viewLifecycleOwner, Observer {
            Timber.d("received ${it.size} suggestions")
            suggestionAdapter.update(it)
        })

        searchViewModel.previousSearches.observe(viewLifecycleOwner, Observer {
            Timber.d("received ${it.size} previous searches")
            previousSearchesAdapter.update(it)
        })
    }

    private fun initAutoComplete() {
        drink_actv.apply {
            threshold = 1
            setAdapter(suggestionAdapter)
            onItemClickListener =
                OnItemClickListener { adapterView, _, i, _ ->
                    val drink = adapterView.getItemAtPosition(i) as DrinkPreviewUiModel
                    searchViewModel.markAsSearched(drink)
                    onDrinkClicked(drink)
                    setText(drink.name)
                }
        }
    }

    private fun onDrinkClicked(model: DrinkPreviewUiModel) {
        findNavController()
            .navigate(SearchFragmentDirections.actionSearchFragmentToDrinkFragment(model.id))
    }
}

class SuggestionAdapter(
    context: Context,
    resource: Int
) : ArrayAdapter<DrinkPreviewUiModel>(context, resource) {

    private val displayData = mutableListOf<DrinkPreviewUiModel>()
    private val searchData = mutableListOf<DrinkPreviewUiModel>()
    private val filter = DrinksFilter()

    fun update(suggestions: List<DrinkPreviewUiModel>) {
        updateData(suggestions)
        notifyDataSetChanged()
    }

    private fun updateData(suggestions: List<DrinkPreviewUiModel>) {
        searchData.apply {
            clear()
            addAll(suggestions)
        }
        displayData.apply {
            clear()
            addAll(suggestions)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_suggestion, parent, false)
        return view.apply {
            val suggestion = getItem(position)
            name_tv.text = suggestion.name
            thumbnail_iv.fromLink(suggestion.thumbnail)
        }
    }

    override fun getItem(position: Int): DrinkPreviewUiModel {
        return displayData[position]
    }

    override fun getCount(): Int {
        return displayData.size
    }

    override fun getFilter(): Filter {
        return filter
    }

    private inner class DrinksFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if (constraint == null) {
                return FilterResults()
            }

            val suggestions = mutableListOf<DrinkPreviewUiModel>()
            searchData.forEach {
                if (matchesSearch(it, constraint)) {
                    suggestions.add(it)
                }
            }
            return FilterResults().apply {
                values = suggestions
                count = suggestions.size
            }
        }

        */
/* Suppressing the cast warning because it has to be of type List<DrinkPreviewUiModel> *//*

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            displayData.clear()
            results?.values?.run { displayData.addAll(this as List<DrinkPreviewUiModel>) }
            notifyDataSetChanged()
        }

        private fun matchesSearch(
            drinkPreviewUiModel: DrinkPreviewUiModel,
            constraint: CharSequence
        ): Boolean {
            return drinkPreviewUiModel.name.startsWith(
                prefix = constraint.toString(),
                ignoreCase = true
            ) || drinkPreviewUiModel.name.contains(
                other = constraint, ignoreCase = true
            )
        }

    }
}*/
