package com.example.adventureescapesa.ui

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adventureescapesa.R
import com.example.adventureescapesa.databinding.FragmentContactUsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Screen 6: The Contact Us Page
 *
 * Reached from the BottomNavigationView or from the quotation summary.
 * Features:
 *   - Booking request form with Name, Email and Message validation
 *   - "Get in Touch" contact details (Phone/WhatsApp, Email, Cape Town base)
 *   - Operating area map placeholder
 */
class ContactUsFragment : Fragment() {

    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupFormSubmission()
    }

    private fun setupToolbar() {
        if (parentFragmentManager.backStackEntryCount > 0) {
            // Opened on top of another screen: the back arrow returns to it
            binding.toolbarContact.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
        } else {
            // Opened as a root tab: no back arrow
            binding.toolbarContact.navigationIcon = null
        }
    }

    /**
     * Validates Full Name, Email Address and Message before "sending" the request.
     */
    private fun setupFormSubmission() {
        binding.btnSendMessage.setOnClickListener {
            val name = binding.etName.text?.toString().orEmpty().trim()
            val email = binding.etEmail.text?.toString().orEmpty().trim()
            val message = binding.etMessage.text?.toString().orEmpty().trim()

            binding.tilName.error =
                if (name.isEmpty()) getString(R.string.contact_error_name) else null

            binding.tilEmail.error = when {
                email.isEmpty() -> getString(R.string.contact_error_email_empty)
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> getString(R.string.contact_error_email_invalid)
                else -> null
            }

            binding.tilMessage.error =
                if (message.isEmpty()) getString(R.string.contact_error_message) else null

            val isValid = binding.tilName.error == null &&
                binding.tilEmail.error == null &&
                binding.tilMessage.error == null

            if (isValid) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.contact_sent_title)
                    .setMessage(getString(R.string.contact_sent_message, name, email))
                    .setPositiveButton(R.string.contact_sent_done) { _, _ ->
                        binding.etName.text?.clear()
                        binding.etEmail.text?.clear()
                        binding.etMessage.text?.clear()
                    }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
