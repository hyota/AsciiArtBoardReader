package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.MyApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityModule.class,
        FragmentModule.class,
        ViewModelModule.class,
        DatabaseModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        StorageModule.class,
        UseCaseModule.class,
})
public interface ApplicationComponent extends AndroidInjector<MyApplication> {

    @Component.Factory
    interface Factory extends AndroidInjector.Factory<MyApplication> {
        @Override
        AndroidInjector<MyApplication> create(@BindsInstance MyApplication instance);
    }

}
