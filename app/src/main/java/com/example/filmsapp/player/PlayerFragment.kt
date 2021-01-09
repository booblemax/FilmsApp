package com.example.filmsapp.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.databinding.PlayerFragmentBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalArgumentException

@ExperimentalCoroutinesApi
class PlayerFragment :
    BaseFragment<PlayerViewModel, PlayerFragmentBinding, PlayerState, PlayerIntents>() {

    override val viewModel: PlayerViewModel by viewModel()
    override val layoutRes: Int = R.layout.player_fragment

    private val videoId: String get() = requireArguments().getString(VIDEO_ID_ARG) ?:
        throw IllegalArgumentException("No such argument exception")

    override fun render(state: PlayerState) {
        with(state) {
            videoId?.let { loadVideo(it) }
        }
    }

    override fun init() {
        with(binding) {
            lifecycle.addObserver(youtubePlayerView)

            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    viewModel.lastStoppedTime = second
                }
            })

            viewModel.pushIntent(PlayerIntents.Initial(videoId))
        }
    }

    private fun loadVideo(videoId: String) {
        binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadOrCueVideo(lifecycle, videoId, viewModel.lastStoppedTime)
            }
        })
    }

    companion object {

        const val TAG = "PlayerFragment"

        private const val VIDEO_ID_ARG = "VIDEO_ID_ARG"

        fun newInstance(videoId: String): Fragment = Fragment().apply {
            arguments = Bundle().apply {
                putString(VIDEO_ID_ARG, videoId)
            }
        }
    }
}
