package com.example.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.adapter.ImageAdapter
import com.example.moviedb.databinding.FragmentDetailBinding
import com.example.moviedb.model.DetailViewModel
import com.example.moviedb.model.ImagesViewModel
import com.example.moviedb.model.Movie

class DetailFragment : Fragment() {
    // Other declarations
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var imagesViewModel: ImagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

    private fun againCall(movieId: Int) {
        val movieId = arguments?.getInt("movieId") ?: -1
        if (movieId != -1) {
            viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
            imagesViewModel = ViewModelProvider(this).get(ImagesViewModel::class.java)

            observeMovieDetails(movieId)
            observeMovieImages(movieId)
        } else {

        }

    }

    private fun observeMovieDetails(movieId: Int) {
        viewModel.fetchMovieDetails(movieId)
        viewModel.movieDetails.observe(viewLifecycleOwner, { movieDetails ->
            updateUI(movieDetails)
        })
    }

    private fun observeMovieImages(movieId: Int) {
        imagesViewModel.fetchMovieImages(movieId)
        imagesViewModel.imagesList.observe(viewLifecycleOwner, { movieImages ->
            updateImages(movieImages)
        })
    }

    private fun updateUI(movieDetails: Movie) {
        binding.title.text = movieDetails.title




        movieDetails.posterPath?.let { posterPath ->
            // Use the view property of the fragment to access ImageView
            view?.findViewById<ImageView>(R.id.toolbar_image)?.let { imageView ->
                Glide.with(imageView)
                    .load("https://image.tmdb.org/t/p/w500$posterPath")
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(imageView)
            }

            view?.findViewById<TextView>(R.id.releaseDateTextView)?.text =
                " ${movieDetails.releaseDate}"
            view?.findViewById<TextView>(R.id.overviewTextView)?.text =
                " ${movieDetails.overview ?: "No overview available"}"
            view?.findViewById<TextView>(R.id.budgetTextView)?.text = " ${movieDetails.budget}"
            view?.findViewById<TextView>(R.id.revenueTextView)?.text = " ${movieDetails.revenue}"

            view?.findViewById<TextView>(R.id.trailer)?.setOnClickListener {
                fetchTrailerUrl(movieDetails.id)
            }


            val writers = movieDetails.writers.joinToString(", ")
            view?.findViewById<TextView>(R.id.writersTextView)?.text = "Writers: $writers"

            view?.findViewById<TextView>(R.id.directorTextView)?.text =
                "Director: ${movieDetails.director}"
        }


    }

    private fun updateImages(movieImages: Movie) {
        // Extract the file paths of backdrops, logos, and posters
        val imageUrlsPosters = mutableListOf<String>()
        val imageUrlsLogos = mutableListOf<String>()
        val imageUrlsbackdrops = mutableListOf<String>()
        movieImages.backdrops?.forEach { backdrop ->
            backdrop.filePath?.let { filePath ->
                imageUrlsbackdrops.add(filePath)
            }
        }
        movieImages.logos?.forEach { logo ->
            logo.filePath?.let { filePath ->
                imageUrlsLogos.add(filePath)
            }
        }
        movieImages.posters?.forEach { poster ->
            poster.filePath?.let { filePath ->
                imageUrlsPosters.add(filePath)
            }
        }

        // Create RecyclerView adapter and set it to the RecyclerView
        val recyclerViewPoster: RecyclerView =
            requireView().findViewById(R.id.imagesListRecyclerposter)
        recyclerViewPoster.adapter = ImageAdapter(imageUrlsPosters)
        recyclerViewPoster.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val seeallposter: TextView = requireView().findViewById(R.id.seeallposter)
        seeallposter.setOnClickListener {
            // This code will be executed when the button is clicked
            val action = DetailFragmentDirections.actionDetailFragmentToSeeAllFragment(imageUrlsPosters.toTypedArray())
            findNavController().navigate(action)
        }



        val recyclerViewLogos: RecyclerView =
            requireView().findViewById(R.id.imagesListRecyclerLogos)
        recyclerViewLogos.adapter = ImageAdapter(imageUrlsLogos)
        recyclerViewLogos.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val seeallLogos: TextView = requireView().findViewById(R.id.seealllogos)
        seeallLogos.setOnClickListener {
            // This code will be executed when the button is clicked
            val action = DetailFragmentDirections.actionDetailFragmentToSeeAllFragment(imageUrlsLogos.toTypedArray())
            findNavController().navigate(action)
        }


        val recyclerViewbackdrops: RecyclerView =
            requireView().findViewById(R.id.imagesListRecyclerbackdrops)
        recyclerViewbackdrops.adapter = ImageAdapter(imageUrlsbackdrops)
        recyclerViewbackdrops.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val seeallbackdrops: TextView = requireView().findViewById(R.id.seeallbackdrops)

        seeallbackdrops.setOnClickListener {
            // This code will be executed when the button is clicked
            val action = DetailFragmentDirections.actionDetailFragmentToSeeAllFragment(imageUrlsbackdrops.toTypedArray())
            findNavController().navigate(action)
        }


    }

    private fun fetchTrailerUrl(movieId: Int) {

        showLoading()

        viewModel.fetchMovieTrailer(movieId)
        viewModel.trailerUrl.observe(viewLifecycleOwner) { trailerUrl ->
            // Dismiss loading indicator
            hideLoading()

            trailerUrl?.let { url ->
                openFullScreenPlayer(url)
            } ?: run {
                Toast.makeText(requireContext(), "No trailer available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {

        view?.findViewById<ProgressBar>(R.id.loadingProgressBar)?.visibility = View.VISIBLE
    }

    private fun hideLoading() {

        view?.findViewById<ProgressBar>(R.id.loadingProgressBar)?.visibility = View.GONE
    }

    private fun openFullScreenPlayer(trailerUrl: String) {
        val action =
            DetailFragmentDirections.actionDetailFragmentToFullScreenPlayerFragment(trailerUrl)
        findNavController().navigate(action)

    }

    override fun onResume() {
        super.onResume()
        val movieId = arguments?.getInt("movieId") ?: -1
        againCall(movieId)

    }
}