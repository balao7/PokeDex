package com.adammcneilly.pokedex.pokemonlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adammcneilly.pokedex.PokeApp
import com.adammcneilly.pokedex.R
import com.adammcneilly.pokedex.data.PokemonService
import com.adammcneilly.pokedex.databinding.FragmentPokemonListBinding
import com.adammcneilly.pokedex.models.Pokemon
import com.adammcneilly.pokedex.network.DefaultPokemonAPI
import com.adammcneilly.pokedex.pokemonlist.PokemonListFragmentDirections.Companion.toPokemonDetail
import com.adammcneilly.pokedex.views.PokemonAdapter
import com.adammcneilly.pokedex.views.PokemonPagingAdapter
import java.util.concurrent.Executors

class PokemonListFragment : Fragment() {
    private val pokemonAdapter = PokemonAdapter(this::onPokemonClicked)
    private val pagingPokemonAdapter = PokemonPagingAdapter(this::onPokemonClicked)

    private lateinit var viewModel: PokemonListViewModel
    private lateinit var binding: FragmentPokemonListBinding

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val pokemonAPI = DefaultPokemonAPI((activity?.application as? PokeApp)?.baseUrl.orEmpty())
            val executor = Executors.newFixedThreadPool(5)
            val repository = PokemonService(
                database = null,
                api = pokemonAPI,
                networkExecutor = executor
            )

            @Suppress("UNCHECKED_CAST")
            return PokemonListViewModel(repository) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PokemonListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupRecyclerView()
        setupViewModel()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.pokemonList

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = pagingPokemonAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel

        // viewModel.pokemon.observe(
        //     this,
        //     Observer {
        //         it?.let(pokemonAdapter::items::set)
        //     }
        // )
        viewModel.pokemonList.observe(
            viewLifecycleOwner,
            Observer {
                it?.let(pagingPokemonAdapter::submitList)
            }
        )
    }

    private fun onPokemonClicked(pokemon: Pokemon) {
        findNavController().navigate(
            toPokemonDetail(pokemonName = pokemon.name.orEmpty())
        )
    }

    companion object {
        fun newInstance(): PokemonListFragment {
            return PokemonListFragment()
        }
    }
}