package com.team.bpm.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.team.bpm.presentation.R

abstract class BaseActivity<T : ViewDataBinding>(private val inflater: (LayoutInflater) -> T) :
    AppCompatActivity() {

    lateinit var binding: T

    protected abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflater(layoutInflater)
        setContentView(binding.root)

        initLayout()
        setupCollect()

        findViewById<ImageView>(R.id.back)?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    protected abstract fun initLayout()

    protected abstract fun setupCollect()

    protected fun bind(action: T.() -> Unit) {
        binding.run(action)
    }
}