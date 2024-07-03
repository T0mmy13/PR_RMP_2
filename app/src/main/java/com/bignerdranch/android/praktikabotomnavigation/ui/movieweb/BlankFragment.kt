package com.bignerdranch.android.praktikabotomnavigation.ui.movieweb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.praktikabotomnavigation.databinding.FragmentWebmovieBinding
import com.bignerdranch.android.praktikabotomnavigation.ui.Movie.NotificationsViewModel

class BlankFragment : Fragment() {

    private lateinit var binding: FragmentWebmovieBinding
    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(requireActivity())[NotificationsViewModel::class.java]

        viewModel.selectedMovie.imdb_url

        binding = FragmentWebmovieBinding.inflate(inflater, container, false)
        binding.webmovie.settings.javaScriptEnabled = true
        binding.webmovie.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        binding.webmovie.loadUrl(viewModel.selectedMovie.imdb_url)
        return binding.root
    }
}
