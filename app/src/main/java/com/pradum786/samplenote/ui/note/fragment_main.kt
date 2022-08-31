package com.pradum786.samplenote.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.pradum786.samplenote.R
import com.pradum786.samplenote.databinding.FragmentMainBinding
import com.pradum786.samplenote.models.NoteResponse
import com.pradum786.samplenote.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class fragment_main : Fragment() {

    private var _bidinig : FragmentMainBinding? = null;
    private val bidinig  get() = _bidinig!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var noteAdapter : NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bidinig= FragmentMainBinding.inflate(inflater,container,false)
        noteAdapter = NoteAdapter(::onNoteClick);
        return bidinig.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.getnotes()
        bidinig.noteList.layoutManager =
            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        bidinig.noteList.adapter = noteAdapter
        bidinig.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_main_to_fragment_note)
        }
        bindObservers()

    }

    private fun bindObservers() {
        noteViewModel.noteLiveData.observe(viewLifecycleOwner, Observer {
            bidinig.progressBar.visibility = View.GONE
            when(it){
                is NetworkResult.Success ->{
                    noteAdapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.massage.toString(), Toast.LENGTH_LONG)
                        .show()
                }
                is NetworkResult.Loading ->{
                    bidinig.progressBar.visibility = View.VISIBLE
                }
            }


        })
    }


    private fun onNoteClick(noteResponse: NoteResponse){
        val bundle = Bundle()
        bundle.putString("note",Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_fragment_main_to_fragment_note,bundle)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bidinig = null
    }


}