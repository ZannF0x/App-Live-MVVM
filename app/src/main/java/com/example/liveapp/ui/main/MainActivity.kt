package com.example.liveapp.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liveapp.R
import com.example.liveapp.ui.adapter.LiveAdapter
import com.example.liveapp.databinding.ActivityMainBinding
import com.example.liveapp.model.Live
import com.example.liveapp.repository.MainRepository
import com.example.liveapp.service.RetrofitService
import com.example.liveapp.ui.main.viewmodel.MainViewModel
import com.example.liveapp.ui.main.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), LiveAdapter.OnClickListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var liveAdapter: LiveAdapter
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(MainRepository(retrofitService)))[MainViewModel::class.java]
        supportActionBar?.apply {
            title = getString(R.string.label_main)
            subtitle = getString(R.string.label_main_author)
        }
    }

    override fun onStart() {
        super.onStart()
        with(mainViewModel) {
            this.requestAllLives()
            live.observe(this@MainActivity) { lives ->
                lives?.let {
                    if (lives.isNotEmpty()) {
                        initRecyclerView(it)
                        binding.progressBarView.visibility = View.GONE
                        binding.recyclerViewLive.visibility = View.VISIBLE
                    }
                }
            }
            error.observe(this@MainActivity) { error ->
                if (error == MainViewModel.NO_NETWORK) {
                    showToast(getString(R.string.main_error_network))
                }
                if (error == MainViewModel.GENERIC_ERROR) {
                    showToast(getString(R.string.main_error_generic))
                }
            }
        }
    }

    private fun initRecyclerView(setList: List<Live>) {
        this.liveAdapter = LiveAdapter(this)
        this.liveAdapter.setList(setList)
        binding.recyclerViewLive.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerViewLive.adapter = this.liveAdapter
    }

    override fun onLiveClick(itemClicked: Live) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(itemClicked.link))
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
}