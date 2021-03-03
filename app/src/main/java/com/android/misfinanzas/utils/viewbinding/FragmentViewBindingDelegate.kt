package com.android.misfinanzas.utils.viewbinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Used to implement [ViewBinding] property delegate for [Fragment].
 *
 * @param fragment the [Fragment] which owns this delegated property.
 * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from [Fragment]'s root view, eg: `T::bind` static method can be used.
 * @param viewBindingClazz if viewBindingBind is not provided, Kotlin Reflection will be used to get `T::bind` static method.
 */
class FragmentViewBindingDelegate<T : ViewBinding> private constructor(
    private val fragment: Fragment,
    viewBindingBind: ((View) -> T)? = null,
    viewBindingClazz: Class<T>? = null
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null
    private val bind = viewBindingBind ?: { view: View ->
        @Suppress("UNCHECKED_CAST")
        GetBindMethod(viewBindingClazz!!)(null, view) as T
    }

    init {
        ensureMainThread()
        require(viewBindingBind != null || viewBindingClazz != null) {
            "Both viewBindingBind and viewBindingClazz are null. Please provide at least one."
        }

        fragment.lifecycle.addObserver(FragmentLifecycleObserver())
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        ensureMainThread()

        binding?.let { return it }

        check(fragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            "Attempt to get view binding when fragment view is destroyed"
        }

        return bind(thisRef.requireView()).also { binding = it }
    }

    private inner class FragmentLifecycleObserver : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            fragment.viewLifecycleOwnerLiveData.observe(
                fragment,
                Observer { viewLifecycleOwner: LifecycleOwner? ->
                    viewLifecycleOwner ?: return@Observer

                    val viewLifecycleObserver = object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            log { "$fragment::view::onDestroy" }
                            viewLifecycleOwner.lifecycle.removeObserver(this)

                            MainHandler.post {
                                binding = null
                                log { "MainHandler.post { binding = null }" }
                            }
                        }
                    }

                    viewLifecycleOwner.lifecycle.addObserver(viewLifecycleObserver)
                }
            )

            log { "$fragment::onCreate" }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            fragment.lifecycle.removeObserver(this)
            binding = null

            log { "$fragment::onDestroy" }
        }
    }

    companion object Factory {
        /**
         * Create [FragmentViewBindingDelegate] from [viewBindingBind] lambda function.
         *
         * @param fragment the [Fragment] which owns this delegated property.
         * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from [Fragment]'s root view, eg: `T::bind` static method can be used.
         */
        fun <T : ViewBinding> from(
            fragment: Fragment,
            viewBindingBind: (View) -> T
        ): FragmentViewBindingDelegate<T> = FragmentViewBindingDelegate(
            fragment = fragment,
            viewBindingBind = viewBindingBind
        )

        /**
         * Create [FragmentViewBindingDelegate] from [ViewBinding] class.
         *
         * @param fragment the [Fragment] which owns this delegated property.
         * @param viewBindingClazz Kotlin Reflection will be used to get `T::bind` static method from this class.
         */
        fun <T : ViewBinding> from(
            fragment: Fragment,
            viewBindingClazz: Class<T>
        ): FragmentViewBindingDelegate<T> = FragmentViewBindingDelegate(
            fragment = fragment,
            viewBindingClazz = viewBindingClazz
        )
    }
}
