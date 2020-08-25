package com.example.filmsapp.player

import com.example.filmsapp.R
import com.example.filmsapp.databinding.PlayerFragmentBinding
import com.example.filmsapp.base.BaseFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : BaseFragment<PlayerViewModel, PlayerFragmentBinding>() {

    override val viewModel: PlayerViewModel by viewModel()
    override val layoutRes: Int = R.layout.player_fragment

    override fun init() {
        with(binding) {
            lifecycle.addObserver(youtubePlayerView)

            youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    PlayerFragmentArgs.fromBundle(requireArguments()).run {
                        youTubePlayer.loadOrCueVideo(lifecycle, videoId, viewModel.lastStoppedTime)
                    }
                }
            })

            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    viewModel.lastStoppedTime = second
                }
            })
        }
    }
}
