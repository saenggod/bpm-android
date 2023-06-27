package com.team.bpm.presentation.ui.main.bodyshape.album

import androidx.activity.viewModels
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.ActivityBodyshapeAlbumBinding
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import dagger.hilt.android.AndroidEntryPoint

// TODO : 앨범 상세 페이지
@AndroidEntryPoint
class BodyShapeAlbumActivity :
    BaseActivity<ActivityBodyshapeAlbumBinding>(ActivityBodyshapeAlbumBinding::inflate) {

    override val viewModel: BodyShapeAlbumViewModel by viewModels()

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = this@BodyShapeAlbumActivity
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
//                when (effect) {
//
//                }
            }
        }
    }


    companion object {

        fun newInstance(): BodyShapeAlbumActivity {
            return BodyShapeAlbumActivity()
        }
    }
}