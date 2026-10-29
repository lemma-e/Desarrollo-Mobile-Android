package ar.edu.unicen.seminario.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object SystemBarsHelper {
    fun applyInsets(root: View, includeBottom: Boolean = true) {
        val paddingLeft = root.paddingLeft
        val paddingTop = root.paddingTop
        val paddingRight = root.paddingRight
        val paddingBottom = root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.setPadding(
                paddingLeft + insets.left,
                paddingTop + insets.top,
                paddingRight + insets.right,
                paddingBottom +
                        if (includeBottom) insets.bottom
                            else 0
            )
            windowInsets
        }
        ViewCompat.requestApplyInsets(root)
    }
}
