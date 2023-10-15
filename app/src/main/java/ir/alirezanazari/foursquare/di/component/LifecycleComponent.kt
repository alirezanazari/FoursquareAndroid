package ir.alirezanazari.foursquare.di.component

import androidx.lifecycle.ViewModel
import dagger.Component
import ir.alirezanazari.foursquare.di.AssistedSavedStateViewModelFactory
import ir.alirezanazari.foursquare.di.module.ViewModelBuilderModule
import ir.alirezanazari.foursquare.di.scope.LifecycleScope

// Written by Alireza Nazari (a.alirezanazari@gmail.com) / (@ali_rezaNazari)

@LifecycleScope
@Component(modules = [ViewModelBuilderModule::class])
interface LifecycleComponent {

    @Component.Factory
    interface Factory {
        fun create(): LifecycleComponent
    }

    fun assistedViewModels(): Map<Class<out ViewModel>, AssistedSavedStateViewModelFactory<out ViewModel>>
}
