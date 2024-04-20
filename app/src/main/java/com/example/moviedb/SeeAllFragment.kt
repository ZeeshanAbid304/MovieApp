package com.example.moviedb
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.adapter.ImageAdapter
import com.example.moviedb.databinding.FragmentSeeAllBinding

class SeeAllFragment : Fragment() {

    private lateinit var imageUrlsPosters: List<String>
    private lateinit var binding: FragmentSeeAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the passed data from arguments
        imageUrlsPosters = arguments?.getStringArray("imageUrlsPosters")?.toList() ?: emptyList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeeAllBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        val recyclerViewPoster: RecyclerView = binding.recyclerViewseeallPoster
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        recyclerViewPoster.layoutManager = layoutManager

        // Set up the adapter for RecyclerView
        recyclerViewPoster.adapter = ImageAdapter(imageUrlsPosters)

        // Set back button click listener
        binding.backButtonSeeall.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}