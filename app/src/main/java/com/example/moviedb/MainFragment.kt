package com.example.moviedb

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.moviedb.Room.MovieDao
import com.example.moviedb.Room.MovieDatabase
import com.example.moviedb.Room.MovieEntity
import com.example.moviedb.Room.ViewModelFactory
import com.example.moviedb.adapter.MovieAdapter
import com.example.moviedb.databinding.FragmentMainBinding
import com.example.moviedb.model.MainViewModel

class MainFragment : Fragment(), MovieAdapter.MovieClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application, provideMovieDao(requireContext()))).get(MainViewModel::class.java)

        // Initialize RecyclerView and Adapter
        movieAdapter = MovieAdapter(this)
        binding.recyclerView.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        // Observe LiveData for movie list updates
        viewModel.movies.observe(viewLifecycleOwner) { movieEntities ->
            movieAdapter.submitList(movieEntities)
        }

        // Fetch movies
        viewModel.fetchMoviesIfNeeded()
    }

    private fun provideMovieDao(context: Context): MovieDao {
        val database = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_database"
        ).build()

        return database.movieDao()
    }

    override fun onMovieClick(movie: MovieEntity) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(movie.id)
        findNavController().navigate(action)
    }
}