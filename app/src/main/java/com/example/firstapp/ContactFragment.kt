package com.example.firstapp

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firstapp.database.ContactDatabase
import com.example.firstapp.databinding.FragmentContactBinding

class ContactFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        var binding = DataBindingUtil.inflate<FragmentContactBinding>(
            inflater,
            R.layout.fragment_contact,
            container,
            false

        )
        setHasOptionsMenu(true)
        val application = requireNotNull(this.activity).application
        val dataSource = ContactDatabase.getInstance(application).contactDAo
        val viewModelFactory = ContactViewModelFactory(dataSource, binding, application)
        val contactViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(ContactViewModel::class.java)
        binding.contactViewModel = contactViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

}