package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xiao.common.app.BaseActivity;
import com.xiao.common.widget.PortraitView;
import com.xiao.factory.persisitence.Account;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.main.ActiveFragment;
import com.xiao.mineim.fragment.main.ContactFragment;
import com.xiao.mineim.fragment.main.GroupFragment;
import com.xiao.mineim.helper.NavigationHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationHelper.OnTabChangedListener<Integer> {

    private static final String TAG = "MAIN_ACTIVITY";

    private NavigationHelper<Integer> mNavigationHelper;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.main_portrait)
    PortraitView mPortrait;

    @BindView(R.id.main_text_title)
    TextView mTitle;

    @BindView(R.id.main_button_action)
    FloatActionButton mFloatAction;

    @BindView(R.id.main_navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getContentLayoutID() {

        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mNavigationHelper = new NavigationHelper<>(MainActivity.this,
                R.id.main_layout_container, getSupportFragmentManager(), this);

        mNavigation.setOnNavigationItemSelectedListener(this);

        mNavigationHelper.addTab(R.id.action_home, new NavigationHelper.Tab<Integer>(ActiveFragment.class, R.string.title_home))
                .addTab(R.id.action_contact, new NavigationHelper.Tab<Integer>(ContactFragment.class, R.string.title_contact))
                .addTab(R.id.action_group, new NavigationHelper.Tab<Integer>(GroupFragment.class, R.string.title_group));

        Glide.with(MainActivity.this)
                .load(R.mipmap.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<AppBarLayout, GlideDrawable>(mAppBarLayout) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                        this.view.setBackground(resource);

                    }
                });

//        PermissionFragment.hasAllPermissions(this, getSupportFragmentManager());

    }

    @Override
    protected void initData() {
        super.initData();

//        mNavigationHelper.performClickMenu(R.id.action_home);

        //首次进入默认选中
        Menu menu = mNavigation.getMenu();

        menu.performIdentifierAction(R.id.action_home, 0);

        mPortrait.setup(Glide.with(this), R.mipmap.default_portrait, Account.getCurrentUser().getPortrait());
    }

    @OnClick(R.id.main_portrait)
    void onPortraitClick() {

        PersonalActivity.show(this, Account.getUserId());
    }

    @Override
    protected boolean initArgs(Bundle extras) {
        if (Account.userInfoIsCompleted()) {

            return super.initArgs(extras);

        } else {

            UserActivity.show(this);

            return false;
        }
    }

    @OnClick(R.id.main_image_search)
    void onSearchClick() {

        int type = Objects.equals(mNavigationHelper.getCurrentTab().mExtra, R.string.title_group) ?
                SearchActivity.TYPE_GROUP : SearchActivity.TYPE_CONTACT;

        SearchActivity.show(this, type);

    }


    @OnClick(R.id.main_button_action)
    void onActionClick() {
        if (Objects.equals(mNavigationHelper.getCurrentTab().mExtra, R.string.title_group)) {

            GroupCreateActivity.show(this);
        } else {

            SearchActivity.show(this, SearchActivity.TYPE_CONTACT);
        }


    }

    /**
     * 底部导航栏选中回调
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        mNavigationHelper.performClickMenu(item.getItemId());

        return true;
    }

    @Override
    public void onTabChanged(NavigationHelper.Tab<Integer> newTab, NavigationHelper.Tab<Integer> oldTab) {

        mTitle.setText(newTab.mExtra);

        float translateY = 0;
        float rotation = 0;

        if (Objects.equals(newTab.mExtra, R.string.title_home)) {

            translateY = Ui.dipToPx(getResources(), 76);
        } else {

            if (Objects.equals(newTab.mExtra, R.string.title_contact)) {

                mFloatAction.setImageResource(R.drawable.ic_contact_add);
                rotation = -360;

            } else if (Objects.equals(newTab.mExtra, R.string.title_group)) {

                mFloatAction.setImageResource(R.drawable.ic_group_add);
                rotation = 360;

            }

        }

        mFloatAction.animate()
                .rotation(rotation)
                .translationY(translateY)
                .setInterpolator(new AnticipateOvershootInterpolator(1.0f))
                .setDuration(480)
                .start();


    }

    public static void show(Context context) {


        context.startActivity(new Intent(context, MainActivity.class));
    }
}
