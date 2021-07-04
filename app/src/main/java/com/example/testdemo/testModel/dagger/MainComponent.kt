package com.example.testdemo.testModel.dagger

import dagger.Component

@Component(modules = [MainModule::class])
interface MainComponent {
    fun inject(activity: DaggerLearnActivity?)
}