package co.kyash.vtl

import android.view.ViewGroup

object VtlHelper {

    fun findValidatableViews(root: ViewGroup): ArrayList<ValidatableView> {
        return findViewsByType(root, ValidatableView::class.java)
    }

    private fun <T> findViewsByType(root: ViewGroup, clazz: Class<T>): ArrayList<T> {
        val result = ArrayList<T>()
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                result.addAll(findViewsByType(child, clazz))
            }

            if (clazz.isInstance(child)) {
                result.add(clazz.cast(child))
            }
        }
        return result
    }

}