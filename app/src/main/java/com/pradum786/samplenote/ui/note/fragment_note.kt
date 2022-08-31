package com.pradum786.samplenote.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.pradum786.samplenote.R
import com.pradum786.samplenote.databinding.FragmentNoteBinding
import com.pradum786.samplenote.models.NoteRequest
import com.pradum786.samplenote.models.NoteResponse
import com.pradum786.samplenote.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class fragment_note : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private  var note : NoteResponse? = null;
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentNoteBinding.inflate(inflater, container, false)

        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        })
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNote(it!!._id) }
        }
        binding.apply {
            btnSubmit.setOnClickListener {
                val title = txtTitle.text.toString()
                val description = txtDescription.text.toString()
                val noteRequest = NoteRequest(title, description)
                if (note == null) {
                    noteViewModel.createNote(noteRequest)
                } else {
                    noteViewModel.updateNote(note!!._id, noteRequest)
                }
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null){
            note=  Gson().fromJson<NoteResponse>(jsonNote,NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(note!!.title)
                binding.txtDescription.setText(note!!.description)
            }
        }else{
            binding.addEditText.text = resources.getString(R.string.add_note)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null;
    }


}