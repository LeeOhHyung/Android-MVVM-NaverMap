package com.example.geom_databinding.recyclerview;

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geom_databinding.R
import com.example.geom_databinding.base.BaseActivity
import com.example.geom_databinding.databinding.ActivityRecyclerviewBinding
import com.example.geom_databinding.model.Point

class RecyclerViewActivity : BaseActivity() {

    private val TAG = javaClass.name

    // ViewModel, DataBinding
    private lateinit var mViewModel: RecyclerViewModel
    private lateinit var mBinding: ActivityRecyclerviewBinding

    // List
    private lateinit var points: List<Point>
    private var actionBar: ActionBar? = null

    override fun before() {
        setupActionBar() // 뒤로가기 버튼 설정
        setupRecyclerViewBindings() // 데이터바인딩 셋팅(초기화)
        setupDataOnRecyclerView()  // 리싸이클러뷰 셋팅
    }

    override fun setupObserving() {
        mViewModel.posMessageLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun after() {
    }

    override fun initToolbar() {
    }

    private fun setupActionBar(){
        if(actionBar == null){
            actionBar = supportActionBar
        }
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            (android.R.id.home)->
                //Intent intent = new Intent(this, MainActivity.class)
                //startActivity(intent)
                onBackPressed()
            else -> Exception("???")
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerViewBindings(){
        mViewModel = ViewModelProviders.of(this).get(RecyclerViewModel::class.java)
        mViewModel.init()

        // 데이터바인딩
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview)
        mBinding.viewmodel = mViewModel
        mBinding.lifecycleOwner = this
    }

    private fun setupDataOnRecyclerView(){
        val intent = intent
        points = intent.getSerializableExtra("LIST") as List<Point>

        if(!points.isNullOrEmpty()){

            // 어답터에 리스트 연결
            mViewModel.setRecyclerViewAdapter(points)
        }
    }
}
