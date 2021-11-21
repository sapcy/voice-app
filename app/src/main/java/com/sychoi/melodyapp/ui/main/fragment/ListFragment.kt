package com.sychoi.melodyapp.ui.main.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sychoi.melodyapp.R
import com.sychoi.melodyapp.data.api.ApiHelperImpl
import com.sychoi.melodyapp.data.api.RetrofitBuilder
import com.sychoi.melodyapp.data.model.Voice
import com.sychoi.melodyapp.databinding.FragmentListBinding
import com.sychoi.melodyapp.ui.main.adapter.MainAdapter
import com.sychoi.melodyapp.ui.main.intent.MainIntent
import com.sychoi.melodyapp.ui.main.viewmodel.VoiceViewModel
import com.sychoi.melodyapp.ui.main.viewstate.VoiceState
import com.sychoi.melodyapp.util.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var voiceViewModel: VoiceViewModel
    private lateinit var binding: FragmentListBinding
    private var adapter = MainAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        setHasOptionsMenu(true)

        setupUI()
        setupViewModel()
        observeViewModel()
        getList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addfile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_addfile) {
            AddFileFragment().show(
                childFragmentManager, "AddFileDialog"
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerView.context,
                    (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        voiceViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(
                    RetrofitBuilder.apiService
                )
            )
        ).get(VoiceViewModel::class.java)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            voiceViewModel.state.collect {
                when (it) {
                    is VoiceState.Idle -> {

                    }
                    is VoiceState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is VoiceState.Voices -> {
                        binding.progressBar.visibility = View.GONE
                        renderList(it.voices)
                    }
                    is VoiceState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getList() {
        lifecycleScope.launch {
            voiceViewModel.musicIntent.send(MainIntent.FetchVoice)
        }
    }

    private fun renderList(voices: List<Voice>) {
        binding.recyclerView.visibility = View.VISIBLE
        voices.let { listOfMusics -> listOfMusics.let { adapter.addData(it) } }
        adapter.notifyDataSetChanged()
    }
}