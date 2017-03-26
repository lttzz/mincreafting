package com.cuisanzhang.mincreafting;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ActivityListViewShowBlocks extends AppCompatActivity {

    public static String TAG = "getView";

    List<Block> blocks = null;
    // checkbox状态
    List<Boolean> checkBoxStateList = null;
    MyAdapter adapter = null;
    String table_name = null;
    int loading_of_background = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        int theme = ChangeTheme.getTheme(getApplicationContext());
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listview_of_blocks);
        initActionBar();

        checkBoxStateList = new ArrayList<Boolean>();

        DbManage dbManage = new DbManage(getApplicationContext());

         table_name = getIntent().getStringExtra(MainActivity.EXTRA_TABLE_NAME);
        String category = getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY);

        if (table_name.equals(MyDatabaseHelper.TABLE_BREWING)) {
            loading_of_background = R.drawable.loading_of_brewing;
        }
        else {
            loading_of_background = R.drawable.loading_of_blocks;
        }
        TextView listViewTitle = (TextView) findViewById(R.id.listViewTitle);
        listViewTitle.setText(category);

        blocks = dbManage.getBlocksFormTable(table_name);
        dbManage.closeDatabase();

        if (blocks == null) {
            System.out.println("getDatabase is null");
        } else {
            System.out.println("blocks.size=" + blocks.size());
        }

        ListView listView = (ListView) findViewById(R.id.listView1);
        adapter = new MyAdapter(getApplicationContext());
        listView.setAdapter(adapter);
//		if (blocks != null) {
//
//			 System.out.println("blocks.size()="+blocks.size());
//		} else {
//			// System.out.println("blocks == null");
//			//
//		}

    }

    public class MyAdapter extends BaseAdapter {

        public final class ViewHolder {
            public ImageView imageView;
            public TextView textViewName;
            public TextView textViewMaterial;
            public TextView textViewUse;
            public TextView textViewDetail;
            public TextView textViewShowBlockDetail;
            public CheckBox checkBox;
        }

        private LayoutInflater mInflater = getLayoutInflater();
        private ViewHolder holder = null;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return blocks.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final int pos = position;

            if (convertView == null) {

                holder = new ViewHolder();


                convertView = mInflater.inflate(R.layout.layout_listview_item_block, null);
                holder.textViewName = (TextView) convertView
                        .findViewById(R.id.name);
                holder.textViewMaterial = (TextView) convertView
                        .findViewById(R.id.material);
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.imageView1);
                holder.textViewUse = (TextView) convertView
                        .findViewById(R.id.use);
                holder.textViewShowBlockDetail = (TextView) convertView
                        .findViewById(R.id.textViewShowBlockDetail);
                holder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.checkBox1);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            Block block = blocks.get(position);
            holder.textViewName.setText(block.getName());
            TextView textViewBuilding = (TextView)findViewById(R.id.textViewBuilding);

            holder.textViewMaterial.setText(block.getMaterial() + block.getFileName());
//            boolean isgif = block.isgif();
            int resId = getResources().getIdentifier(block.getFileName(), "drawable",
                    getPackageName());

                Glide.with(ActivityListViewShowBlocks.this).load(resId).placeholder(loading_of_background)
                        .into(holder.imageView);

            holder.textViewUse.setText(block.getUse());
            holder.textViewShowBlockDetail.setText(block.getDetail());

            // 防止数组越界
            if (checkBoxStateList.size() <= position) {
                // 保存每个checkBoxState, 用来动态更新
                checkBoxStateList.add(false);
            }

            holder.checkBox
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            // 按照列表位置更新checkbox状态
                            checkBoxStateList.set(pos, isChecked);
                            adapter.notifyDataSetChanged();
                        }
                    });

            boolean ischecked = checkBoxStateList.get(position);
            holder.checkBox.setChecked(ischecked);
            if (ischecked) {
//				holder.textViewDetail.setVisibility(View.VISIBLE);
                holder.textViewShowBlockDetail.setVisibility(View.VISIBLE);
            } else {
//				holder.textViewDetail.setVisibility(View.GONE);
                holder.textViewShowBlockDetail.setVisibility(View.GONE);
            }

            return convertView;
        }

    }

    public void initActionBar() {
        ImageView imageViewMenu = (ImageView) findViewById(R.id.imageViewToolbar_menu);
        ImageView imageViewSaerch = (ImageView) findViewById(R.id.imageViewToolbar_search);
        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}