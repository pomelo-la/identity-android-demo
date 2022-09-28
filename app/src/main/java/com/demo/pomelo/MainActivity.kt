package com.demo.pomelo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.demo.pomelo.databinding.ActivityMainBinding
import com.demo.pomelo.di.MainModule
import com.google.android.material.color.MaterialColors
import com.pomelo.identity.PomeloIdentity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import com.google.android.material.R.attr

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        startAppKoin()

        viewModel = viewModel<MainViewModel>().value

        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { state ->
                when (state) {
                    MainState.Finish -> {}
                    MainState.Init -> {
                        PomeloIdentity.register(application)
                        buildUI()
                    }
                    is MainState.InitIdentity -> PomeloIdentity.init(state .config)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    MainEffect.Loading.Hide -> binding.loading.isVisible = false
                    MainEffect.Loading.Show -> binding.loading.isVisible = true
                    is MainEffect.SessionError -> Toast.makeText(
                        this@MainActivity,
                        effect.error.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun buildUI() {
        binding.button.setOnClickListener {
            viewModel.handleEvents(
                MainEvent.InitIdentity(
                    binding.emailInput.editText?.text.toString(),
                    OperationCountry.ARG
                )
            )
        }

        binding.emailInput.editText?.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$").matches(text)) {
                    binding.button.isEnabled = true
                }
            }
        }

        val adapter = ArrayAdapter(this, R.layout.list_item, listOf(OperationCountry.ARG.value))

        with(binding.countryDropdown.editText as AutoCompleteTextView) {
            setAdapter(adapter)
            setOnItemClickListener { _, _, _, _ ->
                binding.menuText.setTextColor(
                    MaterialColors.getColor(
                        binding.menuText,
                        attr.colorOnSurface
                    )
                )
            }
            setText(OperationCountry.ARG.value)
        }
    }

    private fun startAppKoin() {
        val modules = listOf(MainModule.initModule())

        GlobalContext.getOrNull()?.apply {
            loadKoinModules(modules)
        } ?: startKoin {
            androidLogger()
            androidContext(this@MainActivity)

            modules(modules)
        }
    }
}