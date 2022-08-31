package com.pradum786.samplenote.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pradum786.samplenote.databinding.NoteItemBinding
import com.pradum786.samplenote.models.NoteResponse

class NoteAdapter(private val onNoteClick: (NoteResponse) -> Unit) : ListAdapter<NoteResponse, NoteAdapter.NoteViewHolder>(ComparatorDiffUtil()) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val biding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return NoteViewHolder(biding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(note)
        }

    }

    inner class NoteViewHolder(private val biding: NoteItemBinding) :RecyclerView.ViewHolder(biding.root) {
        fun  bind(note: NoteResponse){
            biding.title.text=note.title
            biding.desc.text= note.description
            biding.root.setOnClickListener {
                onNoteClick(note)
            }

        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }

    }


}