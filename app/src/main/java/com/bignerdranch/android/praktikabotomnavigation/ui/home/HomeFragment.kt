package com.bignerdranch.android.praktikabotomnavigation.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ContentInfo
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.bignerdranch.android.praktikabotomnavigation.databinding.FragmentHomeBinding
import database.MyData
import database.MyDataBase
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var ivMyImage: ImageView
    private lateinit var imageUrl: Uri
    private val binding get() = _binding!!
    private lateinit var myDataBase: MyDataBase
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        myDataBase = MyDataBase.getInstance(requireContext())
        ivMyImage = binding.ivMyImage
        imageUrl = createImageUri()

        lifecycleScope.launch {
            myDataBase.getDbDao().getUserById(1)?.let { user ->
                with(binding) {
                    editTextName.setText(user.name)
                    editTextSurname.setText(user.surname)
                    editTextGroup.setText(user.group)
                }
                val userImage = convertBytesToImage(user.image)
                if (userImage != null) {
                    ivMyImage.setImageBitmap(userImage)
                } else {

                }
            }
        }

        binding.buttonSave.setOnClickListener {
            with(binding) {
                val name = editTextName.text.toString()
                val surname = editTextSurname.text.toString()
                val group = editTextGroup.text.toString()

                if (name.isNotEmpty() && surname.isNotEmpty() && group.isNotEmpty()) {
                    lifecycleScope.launch {
                        val existingUser = myDataBase.getDbDao().getUserById(1)
                        val imageBytes = if (ivMyImage.drawable is BitmapDrawable) {
                            convertImageToBytes((ivMyImage.drawable as BitmapDrawable).bitmap)
                        } else {
                            ByteArray(0)
                        }
                        val user = existingUser?.copy(name = name, surname = surname, group = group, image = imageBytes)
                            ?: MyData(id = 1, name = name, surname = surname, group = group, image = imageBytes)
                        if (existingUser != null) {
                            myDataBase.getDbDao().update(user)
                        } else {
                            myDataBase.getDbDao().insert(user)
                        }
                        Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val takePictureContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                ivMyImage.setImageURI(imageUrl)
            }
        }

        ivMyImage.setOnClickListener {
            takePictureContract.launch(imageUrl)
        }

        return binding.root
    }

    fun convertImageToBytes(image: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun convertBytesToImage(bytes: ByteArray?): Bitmap? {
        return if (bytes == null || bytes.isEmpty()) {
            null
        } else {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    private fun createImageUri(): Uri{
        val image = File(requireActivity().filesDir, "myPhoto.png")
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.mypracticeapp.FileProvider",
            image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun insertDataIntoDatabase(myData: MyData) {
        val myDataBase = MyDataBase.getInstance(requireContext())

        lifecycleScope.launch {
            myDataBase.getDbDao().insert(myData)
            Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show()
        }
    }
}