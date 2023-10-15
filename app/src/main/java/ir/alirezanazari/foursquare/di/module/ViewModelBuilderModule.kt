package ir.alirezanazari.foursquare.di.module

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.multibindings.Multibinds
import ir.alirezanazari.foursquare.di.util.AssistedSavedStateViewModelFactory

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@Module
abstract class ViewModelBuilderModule {
    @Multibinds
    abstract fun viewModels(): Map<Class<out ViewModel>, ViewModel>

    @Multibinds
    abstract fun assistedViewModels(): Map<Class<out ViewModel>, AssistedSavedStateViewModelFactory<out ViewModel>>
}