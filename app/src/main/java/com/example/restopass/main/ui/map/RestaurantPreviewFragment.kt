package com.example.restopass.main.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.restopass.R
import kotlinx.android.synthetic.main.restaurant_preview_item.*


class RestaurantPreviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.restaurant_preview_item, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoPreviewCard.visibility = View.GONE
    }

    private fun fillRestaurantPreview() {

    }
}
