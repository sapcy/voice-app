package com.sychoi.melodyapp.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sychoi.melodyapp.R

//@ExperimentalCoroutinesApi
class SettingFragment : Fragment() {
//    private lateinit var binding: ListFragmentBinding
//    private lateinit var mainViewModel: MainViewModel
//    private var adapter = MainAdapter(arrayListOf())

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ListFragmentBinding.inflate(layoutInflater)
//
//        setupUI()
//        setupViewModel()
//        observeViewModel()
//        getList()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = ListFragmentBinding.inflate(layoutInflater)

//        setupUI()
//        setupViewModel()
//        observeViewModel()
//        getList()
//    }

//    private fun setupUI() {
//        binding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
//        binding.recyclerView.run {
//            addItemDecoration(
//                DividerItemDecoration(
//                    binding.recyclerView.context,
//                    (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
//                )
//            )
//        }
//        binding.recyclerView.adapter = adapter
//    }
//
//    private fun setupViewModel() {
//        mainViewModel = ViewModelProviders.of(
//            this,
//            ViewModelFactory(
//                ApiHelperImpl(
//                    RetrofitBuilder.apiService
//                )
//            )
//        ).get(MainViewModel::class.java)
//    }
//
//    private fun observeViewModel() {
//        lifecycleScope.launch {
//            mainViewModel.state.collect {
//                when (it) {
//                    is MainState.Idle -> {
//
//                    }
//                    is MainState.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                    }
//
//                    is MainState.Musics -> {
//                        binding.progressBar.visibility = View.GONE
//                        renderList(it.music)
//                    }
//                    is MainState.Error -> {
//                        binding.progressBar.visibility = View.GONE
//                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun getList() {
//        lifecycleScope.launch {
//            mainViewModel.musicIntent.send(MainIntent.FetchMusic)
//        }
//    }
//
//
//    private fun renderList(musics: List<Music>) {
//        binding.recyclerView.visibility = View.VISIBLE
//        musics.let { listOfMusics -> listOfMusics.let { adapter.addData(it) } }
//        adapter.notifyDataSetChanged()
//    }
}