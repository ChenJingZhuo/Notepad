package com.cjz.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cjz.notepad.adapter.NotepadAdapter;
import com.cjz.notepad.bean.NotepadBean;
import com.cjz.notepad.database.SQLiteHelper;
import com.cjz.notepad.utils.PerfectClickListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotepadActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    List<NotepadBean> list;
    SQLiteHelper sqLiteHelper;
    NotepadAdapter adapter;
    long exitTime = 0;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mLlTitleMenu;
    private FrameLayout mLlMenu;
    private ListView mListview;
    private ImageView mAdd;
    /**
     * 全选
     */
    private Button mAllSelect;
    /**
     * 全不选
     */
    private Button mAllNotSelect;
    /**
     * 不选
     */
    private Button mBackSelect;
    /**
     * 删除
     */
    private Button mDelete;
    private LinearLayout mToolsBar;

    private static final int REFRERSH = 0;
    private RelativeLayout mNotebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        initView();
        initDrawerLayout();
        listView = findViewById(R.id.listview);
        ImageView add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotepadActivity.this, RecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        initData();
    }

    private void initView() {
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLlTitleMenu = (FrameLayout) findViewById(R.id.ll_title_menu);
        mLlTitleMenu.setOnClickListener(this);
        mLlMenu = (FrameLayout) findViewById(R.id.ll_menu);
        mLlMenu.setOnClickListener(this);
        mListview = (ListView) findViewById(R.id.listview);
        mAdd = (ImageView) findViewById(R.id.add);
        mNavView.setOnClickListener(this);
        mDrawerLayout.setOnClickListener(this);
        mAllSelect = (Button) findViewById(R.id.all_select);
        mAllSelect.setOnClickListener(this);
        mAllNotSelect = (Button) findViewById(R.id.all_not_select);
        mAllNotSelect.setOnClickListener(this);
        mBackSelect = (Button) findViewById(R.id.back_select);
        mBackSelect.setOnClickListener(this);
        mDelete = (Button) findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mToolsBar = (LinearLayout) findViewById(R.id.tools_bar);
        mNotebook = (RelativeLayout) findViewById(R.id.notebook);
    }

    protected void initData() {
        sqLiteHelper = new SQLiteHelper(this);
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(NotepadActivity.this, RecordActivity.class);
                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotepadTime());
                intent.putExtra("content", notepadBean.getNotepadContent());
                NotepadActivity.this.startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                btnEditList();
                list.get(position).isCheck=true;
                return true;
            }
        });
    }

    /**
     * 编辑、取消编辑
     */
    public void btnEditList() {
        adapter.flag = !adapter.flag;
        if (adapter.flag){
            mToolsBar.setVisibility(View.VISIBLE);
            mAdd.setVisibility(View.GONE);
        }else {
            mToolsBar.setVisibility(View.GONE);
            mAdd.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 全选
     *
     * @param view
     */
    public void btnSelectAllList(View view) {
        if (adapter.flag) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).isCheck = true;
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 全不选
     *
     * @param view
     */
    public void btnNoList(View view) {

        if (adapter.flag) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).isCheck = false;
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 反选
     *
     * @param view
     */
    public void btnfanxuanList(View view) {
        if (adapter.flag) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCheck) {
                    list.get(i).isCheck = false;
                } else {
                    list.get(i).isCheck = true;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取选中数据
     *
     * @param view
     */
    public void btnOperateList(View view) {

        List<NotepadBean> ids = new ArrayList<>();

        if (adapter.flag) {

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCheck) {
                    ids.add(list.get(i));
                }
            }
        }
        if (ids.size()>0){
            AlertDialog dialog;
            AlertDialog.Builder builder=new AlertDialog.Builder(this)
                    .setMessage("是否删除选中的记录？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (NotepadBean nb : ids) {
                                if (sqLiteHelper.deleteData(nb.getId())) {
                                    list.remove(nb.getId());
                                }
                            }
                            Toast.makeText(NotepadActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            btnEditList();
                            showQueryData();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog=builder.show();
        }else {
            Toast.makeText(this, "您选择的记录为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void showQueryData() {
        if (list != null) {
            list.clear();
        }
        list = sqLiteHelper.query();
        adapter = new NotepadAdapter(this, list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            showQueryData();
        }
    }

    private void initDrawerLayout() {
        mNavView.inflateHeaderView(R.layout.nav_header_main);
        View headerView = mNavView.getHeaderView(0);
        ImageView iv_avatar = headerView.findViewById(R.id.iv_avatar);
//        GlideUtil.displayCircle(iv_avatar, IC_AVATAR);
        LinearLayout ll_nav_account = headerView.findViewById(R.id.ll_nav_account);
        ll_nav_account.setOnClickListener(listener);
        LinearLayout ll_nav_password = headerView.findViewById(R.id.ll_nav_password);
        ll_nav_password.setOnClickListener(listener);
        LinearLayout ll_nav_feedback = headerView.findViewById(R.id.ll_nav_feedback);
        ll_nav_feedback.setOnClickListener(listener);
        LinearLayout ll_nav_version_update = headerView.findViewById(R.id.ll_nav_version_update);
        ll_nav_version_update.setOnClickListener(listener);
        LinearLayout ll_nav_score = headerView.findViewById(R.id.ll_nav_score);
        ll_nav_score.setOnClickListener(listener);
        LinearLayout ll_nav_account_switch = headerView.findViewById(R.id.ll_nav_account_switch);
        ll_nav_account_switch.setOnClickListener(listener);

        LinearLayout ll_nav_logout = headerView.findViewById(R.id.ll_nav_logout);
        ll_nav_logout.setOnClickListener(this);

    }

    private PerfectClickListener listener = new PerfectClickListener() {

        @Override
        protected void onNoDoubleClick(final View v) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.postDelayed(() -> {
                switch (v.getId()) {
                    case R.id.ll_nav_account:
                        Toast.makeText(NotepadActivity.this, "个人中心", Toast.LENGTH_SHORT).show();
                        Intent accountIntent = new Intent(NotepadActivity.this, TestActivity.class);
                        accountIntent.putExtra("text", "个人中心");
                        startActivity(accountIntent);
                        break;
                    case R.id.ll_nav_password:
                        Toast.makeText(NotepadActivity.this, "密码设置", Toast.LENGTH_SHORT).show();
                        Intent passwordIntent = new Intent(NotepadActivity.this, TestActivity.class);
                        passwordIntent.putExtra("text", "密码设置");
                        startActivity(passwordIntent);
                        break;
                    case R.id.ll_nav_feedback:
                        Toast.makeText(NotepadActivity.this, "意见反馈", Toast.LENGTH_SHORT).show();
                        Intent feedbackIntent = new Intent(NotepadActivity.this, TestActivity.class);
                        feedbackIntent.putExtra("text", "意见反馈");
                        startActivity(feedbackIntent);
                        break;
                    case R.id.ll_nav_version_update:
                        Toast.makeText(NotepadActivity.this, "版本更新", Toast.LENGTH_SHORT).show();
                        Intent updateIntent = new Intent(NotepadActivity.this, TestActivity.class);
                        updateIntent.putExtra("text", "版本更新");
                        startActivity(updateIntent);
                        break;
                    case R.id.ll_nav_score:
                        Toast.makeText(NotepadActivity.this, "给个评分呗", Toast.LENGTH_SHORT).show();
                        Intent scoreIntent = new Intent(NotepadActivity.this, TestActivity.class);
                        scoreIntent.putExtra("text", "给个评分呗");
                        startActivity(scoreIntent);
                        break;
                    case R.id.ll_nav_account_switch:
                        Toast.makeText(NotepadActivity.this, "切换账号", Toast.LENGTH_SHORT).show();
                        Intent switchIntent = new Intent(NotepadActivity.this, TestActivity.class);
                        switchIntent.putExtra("text", "切换账号");
                        startActivity(switchIntent);
                        break;
                    default:
                        break;
                }
            }, 0);
        }
    };

    /**
     * 监听按下返回键的状态
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mToolsBar.getVisibility() == View.VISIBLE) {
                btnEditList();
            } else if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fresh:
                break;
            case R.id.search:
                break;
            case R.id.all:
                break;
            case R.id.all_back:
                break;
            case R.id.all_not:
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                // 开启抽屉式菜单
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_menu:
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.menu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
                //显示(这一行代码不要忘记了)
                popup.show();

                break;
            case R.id.ll_nav_logout:
                break;
            case R.id.nav_view:
                break;
            case R.id.drawer_layout:
                break;
            case R.id.all_select:
                btnSelectAllList(v);
                break;
            case R.id.all_not_select:
                btnNoList(v);
                break;
            case R.id.back_select:
                btnfanxuanList(v);
                break;
            case R.id.delete:
                btnOperateList(v);
                break;
        }
    }

}
