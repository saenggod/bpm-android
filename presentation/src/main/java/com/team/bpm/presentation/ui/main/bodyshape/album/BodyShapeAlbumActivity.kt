package com.team.bpm.presentation.ui.main.bodyshape.album

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.team.bpm.presentation.base.BaseActivity
import com.team.bpm.presentation.databinding.ActivityBodyshapeAlbumBinding
import com.team.bpm.presentation.ui.main.bodyshape.album.add.BodyShapeAlbumAddActivity
import com.team.bpm.presentation.ui.main.bodyshape.album.more.BodyShapeAlbumMoreBottomSheet
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
                    BodyShapeAlbumContract.Effect.ShowMoreBottomSheet -> {
                        showMoreBottomSheet()
                    }
                    BodyShapeAlbumContract.Effect.GoOutThisPage -> {
                        finish()
                    }
                    is BodyShapeAlbumContract.Effect.GoToEditAlbumDetail -> {
                        goToEditAlbumDetail(effect.albumId)
                    }
                    is BodyShapeAlbumContract.Effect.GoToAddBodyShapeDetail -> {
                        goToAddBodyShapeDetail(effect.albumId)
                    }
                    is BodyShapeAlbumContract.Effect.GoToBodyShapeDetail -> {
                        goToBodyShapeDetail(effect.albumId, effect.albumDetailId, effect.dday)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getBodyShapeAlbumInfo()
    }

    private fun showMoreBottomSheet() {
        BodyShapeAlbumMoreBottomSheet.newInstance()
            .show(supportFragmentManager, BodyShapeAlbumMoreBottomSheet::class.java.simpleName)
    }

    private fun goToAddBodyShapeDetail(albumId: Int) {
        startActivity(BodyShapeDetailPostingActivity.newIntent(this, albumId))
    }

    private fun goToEditAlbumDetail(albumId: Int) {
        startActivity(BodyShapeAlbumAddActivity.newIntent(this, albumId))
    }

    private fun goToBodyShapeDetail(albumId: Int, albumDetailId: Int, dday: Int) {
        startActivity(BodyShapeDetailActivity.newIntent(this, albumId, albumDetailId, dday))
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