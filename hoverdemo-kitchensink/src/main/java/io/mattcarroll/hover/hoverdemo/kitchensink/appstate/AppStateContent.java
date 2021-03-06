/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mattcarroll.hover.hoverdemo.kitchensink.appstate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.hoverdemo.kitchensink.Bus;
import io.mattcarroll.hover.hoverdemo.kitchensink.R;
import io.mattcarroll.hover.hoverdemo.kitchensink.theming.HoverTheme;
import io.mattcarroll.hover.hoverdemo.kitchensink.theming.HoverThemeManager;

/**
 * {@link Content} that displays the Activity and Service state of the app.
 */
public class AppStateContent extends FrameLayout implements Content {

    private AppStateAdapter mAppStateAdapter;

    public AppStateContent(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_app_state_content, this, true);

        mAppStateAdapter = new AppStateAdapter(HoverThemeManager.getInstance().getTheme());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAppStateAdapter);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Bus.getInstance().registerSticky(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        Bus.getInstance().unregister(this);
        super.onDetachedFromWindow();
    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {
        mAppStateAdapter.setTheme(newTheme);
    }

    public void onEventMainThread(@NonNull AppStateTracker.ActivityStackChangeEvent event) {
        mAppStateAdapter.setActivityStates(event.getActivityStack());
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        mAppStateAdapter.setServiceStates(AppStateTracker.getInstance().getServiceStates());
    }

    @Override
    public void onHidden() {

    }
}
