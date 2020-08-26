package com.example.firstapp

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.example.firstapp.database.Contact
import com.example.firstapp.database.ContactDAO
import com.example.firstapp.databinding.FragmentContactBinding
import kotlinx.coroutines.*
import java.lang.StringBuilder

class ContactViewModel(
    private val database: ContactDAO,
    private val binding: FragmentContactBinding,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val contacts = database.get()
    val contactString = Transformations.map(contacts) { contacts ->
        formatContact(contacts)
    }

    private fun formatContact(contact: List<Contact>): Spanned {
        val sb = StringBuilder()
        sb.apply {
            //append(resources.getString(R.string.title))
            contact.forEach {
                append(it.id)
                append(" : ")
                append(it.name)
                append(", ")
                append(it.lastname)
                append("<br>")
            }
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onContactAdd() {
        uiScope.launch {
            val newContact = Contact()
            newContact.name = binding.buttonInsert.text.toString()
            newContact.lastname = binding.textview.text.toString()
            insert(newContact)
        }
    }

    private suspend fun insert(contact: Contact) {
        withContext(Dispatchers.IO) {
            database.insert(contact)
        }
    }

}