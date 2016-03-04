package com.example.givemepass.concurrenttest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Context mContext;
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;
    private ListView mListView;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mListView = (ListView) findViewById(R.id.list_view);

        MyData.clear();
        for(int i = 0; i < 100; i++){
            MyData.addItem("a" + i);
        }
        adapter = new MyAdapter(MyData.getData());
        mListView.setAdapter(adapter);
        ExecutorService thread = Executors.newSingleThreadExecutor();
        thread.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100000; i++){
                    if(i%2==0){
                        MyData.removeItem(i);
                    } else {
                        MyData.addItem("b" + i);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mMyRecyclerViewAdapter.notifyDataSetChanged();
//                            mRecyclerView.scrollToPosition(MyData.getData().size() - 1);
                            adapter.notifyDataSetChanged();
                            mListView.setSelection(MyData.getData().size() - 1);


                        }
                    });
                }
            }
        });
        ExecutorService thread2 = Executors.newSingleThreadExecutor();
        thread2.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100000; i++){
                    if(i%5==0){
                        MyData.removeItem(i);
                    } else {
                        MyData.addItem("b" + i);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mMyRecyclerViewAdapter.notifyDataSetChanged();
//                            mRecyclerView.scrollToPosition(MyData.getData().size() - 1);
                            adapter.notifyDataSetChanged();
                            mListView.setSelection(MyData.getData().size() - 1);
                        }
                    });
                }
            }
        });

//        mMyRecyclerViewAdapter = new MyRecyclerViewAdapter(MyData.getData());
//
//        LinearLayoutManager not_download_layoutManager = new LinearLayoutManager(mContext);
//        not_download_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(not_download_layoutManager);
//
//        mRecyclerView.setAdapter(mMyRecyclerViewAdapter);
//        mRecyclerView.getItemAnimator().setSupportsChangeAnimations(false);
    }
    public class MyAdapter extends BaseAdapter{
        private List<String> list;
        public MyAdapter(List<String> data) {
            list = data;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            if(view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
                holder = new Holder();
                holder.textView = (TextView) view.findViewById(R.id.item_text);
                view.setTag(holder);
            } else{
                holder = (Holder) view.getTag();
            }
            holder.textView.setText(list.get(position));
            return view;
        }
        class Holder{
            TextView textView;
        }
    }
    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
        private List<String> mData;

        public MyRecyclerViewAdapter(List<String> mData) {
            this.mData = mData;

        }

        @Override
        public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.textView.setText(mData.get(position));
        }
        public void setData(List<String> data){
            mData = data;
        }
        @Override
        public int getItemCount() {
            return mData.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_text);
            }
        }
    }
}
