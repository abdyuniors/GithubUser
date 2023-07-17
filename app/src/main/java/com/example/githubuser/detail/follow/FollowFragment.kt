package com.example.githubuser.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.UserAdapter
import com.example.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowViewModel
    private lateinit var adapter: UserAdapter

    companion object {
        const val TYPE_FOLLOWER = 1
        const val TYPE_FOLLOWING = 2
        internal const val ARG_USERNAME = "username"
        internal const val ARG_TYPE = "type"

        fun newInstance(type: Int, username: String?): FollowFragment {
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            bundle.putInt(ARG_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvListFollow.setHasFixedSize(true)
        binding.rvListFollow.layoutManager = LinearLayoutManager(activity)
        binding.rvListFollow.adapter = adapter

        val username = arguments?.getString(ARG_USERNAME)
        val type = arguments?.getInt(ARG_TYPE, 0)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowViewModel::class.java]
        if (username != null) {
            if (type == TYPE_FOLLOWER) {
                viewModel.setListFollowers(username)
            } else if (type == TYPE_FOLLOWING) {
                viewModel.setListFollowing(username)
            }
        }
        showLoading(true)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
        viewModel.getListFollowing().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}