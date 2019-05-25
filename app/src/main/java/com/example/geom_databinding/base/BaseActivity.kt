package com.example.geom_databinding.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(){

    abstract fun before()

    abstract fun setupObserving()

    abstract fun after()

    abstract fun initToolbar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        before()
        setupObserving()
        after()
        initToolbar()
    }
}