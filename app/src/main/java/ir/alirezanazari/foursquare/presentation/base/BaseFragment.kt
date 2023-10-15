package ir.alirezanazari.foursquare.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import ir.alirezanazari.foursquare.di.util.ViewModelFactory
import javax.inject.Inject

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

abstract class BaseFragment<V : BaseViewModel, B : ViewBinding> : Fragment() {

    private var _binding: B? = null
    val binding: B get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var viewModel: V
    protected abstract fun getViewModelClass(): Class<V>
    protected open fun getViewModelScope(): ViewModelStoreOwner = this

    abstract fun initInjection()
    abstract fun bindView(inflater: LayoutInflater, parent: ViewGroup?): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjection()
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            getViewModelScope(), viewModelFactory.create(this, arguments)
        )[getViewModelClass()]
    }
}