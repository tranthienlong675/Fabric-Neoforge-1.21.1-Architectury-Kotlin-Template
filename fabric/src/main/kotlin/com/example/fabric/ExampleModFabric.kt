package com.example.fabric

import com.example.ExampleMod
import net.fabricmc.api.ModInitializer

class ExampleModFabric: ModInitializer {
    override fun onInitialize() {
        ExampleMod.init()
    }
}