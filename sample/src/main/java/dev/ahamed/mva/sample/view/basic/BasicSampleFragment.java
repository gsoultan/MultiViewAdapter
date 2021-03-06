/*
 * Copyright 2017 Riyaz Ahamed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.ahamed.mva.sample.view.basic;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import dev.ahamed.mva.sample.R;
import dev.ahamed.mva.sample.data.model.NumberItem;
import dev.ahamed.mva.sample.view.common.BaseFragment;
import java.util.List;
import mva2.adapter.ListSection;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class BasicSampleFragment extends BaseFragment {

  private BasicConfig basicConfig = new BasicConfig();

  private Spinner spinnerLayoutManager;
  private Spinner spinnerOrientation;
  private Spinner spinnerReverseLayout;
  private Spinner spinnerSpanCount;

  public BasicSampleFragment() {
  }

  @Override public void initViews(View view) {
    spinnerSpanCount = view.findViewById(R.id.spinner_span_count);
    spinnerLayoutManager = view.findViewById(R.id.spinner_layout_manager);
    spinnerOrientation = view.findViewById(R.id.spinner_orientation);
    spinnerReverseLayout = view.findViewById(R.id.spinner_reverse_layout);

    updateConfiguration();
  }

  @Override public int layoutId() {
    return R.layout.fragment_basic;
  }

  @Override public void resetConfiguration() {
    spinnerSpanCount.setSelection(0);
    spinnerLayoutManager.setSelection(0);
    spinnerOrientation.setSelection(0);
    spinnerReverseLayout.setSelection(0);
    updateConfiguration();
  }

  @Override public void updateConfiguration() {
    basicConfig.layoutManager = spinnerLayoutManager.getSelectedItemPosition();
    basicConfig.orientation =
        spinnerOrientation.getSelectedItemPosition() == 0 ? VERTICAL : HORIZONTAL;
    basicConfig.reverseLayout = spinnerReverseLayout.getSelectedItemPosition() == 1;
    basicConfig.spanCount = (spinnerSpanCount.getSelectedItemPosition() + 2);
    setUpAdapter();
  }

  private void setUpAdapter() {
    recyclerView.setAdapter(adapter);
    LinearLayoutManager layoutManager;
    if (basicConfig.layoutManager == 0) {
      layoutManager = new CustomLayoutManager(getContext());
    } else {
      layoutManager = new GridLayoutManager(getContext(), basicConfig.spanCount);
      adapter.setSpanCount(basicConfig.spanCount);
      ((GridLayoutManager) layoutManager).setSpanSizeLookup(adapter.getSpanSizeLookup());
    }
    layoutManager.setOrientation(
        basicConfig.orientation == VERTICAL ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL);
    layoutManager.setReverseLayout(basicConfig.reverseLayout);

    recyclerView.setLayoutManager(layoutManager);

    if (recyclerView.getItemDecorationCount() != 0) {
      recyclerView.removeItemDecorationAt(0);
    }
    recyclerView.addItemDecoration(adapter.getItemDecoration());

    adapter.removeAllSections();
    ListSection<NumberItem> listSection = new ListSection<>();
    adapter.addSection(listSection);
    adapter.registerItemBinders(new NumberItemBinder(new BasicDividerDecorator(adapter)));

    List<NumberItem> list = dataManager.getNumberItems(10000);
    listSection.addAll(list);
  }

  static class BasicConfig {

    // 0 - Linear | 1 - Grid
    int layoutManager = 0;
    int spanCount = 2;
    int orientation = VERTICAL;
    boolean reverseLayout = false;
  }
}
