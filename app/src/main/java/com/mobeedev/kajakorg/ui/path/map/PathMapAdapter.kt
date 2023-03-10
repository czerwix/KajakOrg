package com.mobeedev.kajakorg.ui.path.map

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.model.CameraPosition
import com.mobeedev.kajakorg.ui.model.PathMapItem

class PathMapAdapter(var path: PathMapItem, fragmentManager: FragmentManager)
//    MapViewPager.Adapter(fragmentManager) {
//    override fun getCount() = path.points.size
//
//    override fun getItem(position: Int): Fragment {
//        TODO("Not yet implemented")
//    }
//
//    override fun getCameraPosition(position: Int) =
//        CameraPosition.fromLatLngZoom(path.points[position].position, 18f)
//}