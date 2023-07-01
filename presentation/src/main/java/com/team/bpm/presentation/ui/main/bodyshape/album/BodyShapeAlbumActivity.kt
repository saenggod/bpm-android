package com.team.bpm.presentation.ui.main.bodyshape.album

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.ActivityBodyshapeAlbumBinding
import com.team.bpm.presentation.ui.main.bodyshape.detail.BodyShapeDetailActivity
import com.team.bpm.presentation.ui.main.bodyshape.detail.posting.BodyShapeDetailPostingActivity
import com.team.bpm.presentation.util.repeatCallDefaultOnStarted
import com.team.bpm.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BodyShapeAlbumActivity :
    BaseActivity<ActivityBodyshapeAlbumBinding>(ActivityBodyshapeAlbumBinding::inflate) {

    override val viewModel: BodyShapeAlbumViewModel by viewModels()

    override fun initLayout() {
        bind {
            vm = viewModel
            lifecycleOwner = this@BodyShapeAlbumActivity

            list.adapter = BodyShapeAlbumAdapter { id ->
                viewModel.onClickAlbumDetail(id)
            }
        }
    }

    override fun setupCollect() {
        repeatCallDefaultOnStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is BodyShapeAlbumContract.Effect.ShowToast -> {
                        showToast(effect.text)
                    }
                    is BodyShapeAlbumContract.Effect.GoToAddBodyShapeDetail -> {
                        goToAddBodyShapeDetail(effect.albumId)
                    }
                    is BodyShapeAlbumContract.Effect.GoToBodyShapeDetail -> {
                        goToBodyShapeDetail(effect.albumId, effect.albumDetailId)
                    }
                }
            }
        }
    }

    override fun onResume() {
        viewModel.getBodyShapeAlbumInfo()

        super.onResume()
    }

    private fun goToAddBodyShapeDetail(albumId: Int) {
        startActivity(BodyShapeDetailPostingActivity.newIntent(this, albumId))
    }

    private fun goToBodyShapeDetail(albumId: Int, albumDetailId: Int) {
        startActivity(BodyShapeDetailActivity.newIntent(this, albumId, albumDetailId))
    }

    companion object {

        const val KEY_BUNDLE = "bundle"
        const val KEY_ALBUM_ID = "album_id"

        fun newIntent(context: Context, albumId: Int?): Intent {
            return Intent(context, BodyShapeAlbumActivity::class.java).apply {
                putExtra(
                    KEY_BUNDLE, bundleOf(
                        KEY_ALBUM_ID to albumId
                    )
                )
            }
        }
    }
}