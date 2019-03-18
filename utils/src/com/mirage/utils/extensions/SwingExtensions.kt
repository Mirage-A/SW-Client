package com.mirage.utils.extensions

import java.awt.Component
import java.awt.Container

operator fun Container.plus(comp: Component) : Component? = add(comp)