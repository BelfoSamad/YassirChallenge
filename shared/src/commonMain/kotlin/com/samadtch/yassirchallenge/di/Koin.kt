package com.samadtch.yassirchallenge.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        appModule,
        dataModule
    )
}

//To call by IOS
fun initKoin() = initKoin {}