package com.bignerdranch.android.praktikabotomnavigation.ui.Movie

import Interface.IMovie
import Models.Movie
import adapters.MovieAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.praktikabotomnavigation.R
import com.bignerdranch.android.praktikabotomnavigation.databinding.FragmentMovieBinding
import retrofit2.Retrofit
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class   MovieFragment : Fragment() {
    private lateinit var binding: FragmentMovieBinding
    private lateinit var context: Context
    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: NotificationsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        context = requireContext()

        viewModel = ViewModelProvider(requireActivity())[NotificationsViewModel::class.java]
        adapter = MovieAdapter(viewModel)

        binding.rvMovie.layoutManager = LinearLayoutManager(context)
        binding.rvMovie.adapter = adapter
        binding.rvMovie.setPadding(0, 0, 0, 0)

        viewModel.lastSearchQuery?.let {
            adapter.filter(it)
            binding.searchBadge.setQuery(it, false)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyapi.online/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val movieApi = retrofit.create(IMovie::class.java)
        val call = movieApi.getMovies()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>>,
                response: Response<List<Movie>>
            ) {
                if (response.isSuccessful) {
                    Log.d("d", "dsa")
                    val movies = response.body()
                    if (movies != null) {
                        adapter.refreshMoviess(movies)
                    }
                } else {
                    Toast.makeText(context, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(context, "Ошибка сети", Toast.LENGTH_SHORT).show()
            }
        })
        adapter.setOnClickListener(object : MovieAdapter.OnClickListener{
            override fun onClick(position: Int, model: Movie) {
                viewModel.selectedMovie = model
                findNavController().navigate(R.id.action_navigation_notifications_to_navigation_movieweb)
            }
        })
        binding.searchBadge.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    adapter.filter(it)
                    viewModel.lastSearchQuery = it
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    adapter.filter(it)
                    viewModel.lastSearchQuery = it
                }
                return false
            }
        })
        return binding.root
    }
}