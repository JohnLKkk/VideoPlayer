package com.example.testdemo.testModel.dagger

import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    fun providerA(): AFile = AFile()
}