/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gnd.ui.home.mapcontainer;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.google.android.gnd.model.feature.Point;
import com.google.android.gnd.rx.Nil;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class FeatureRepositionViewModelTest {

  private static final Point TEST_POINT =
      Point.newBuilder().setLatitude(0.0).setLongitude(0.0).build();

  @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  private FeatureRepositionViewModel viewModel;

  @Before
  public void setUp() {
    viewModel = new FeatureRepositionViewModel();
  }

  @Test
  public void testConfirmButtonClicks_notReplayed() {
    viewModel.onCameraMoved(TEST_POINT);

    viewModel.onConfirmButtonClick();

    viewModel.getConfirmButtonClicks().test().assertNoValues().assertNoErrors().assertNotComplete();
  }

  @Test
  public void testConfirmButtonClicks() {
    viewModel.onCameraMoved(TEST_POINT);
    TestObserver<Point> testObserver = viewModel.getConfirmButtonClicks().test();

    viewModel.onConfirmButtonClick();

    testObserver.assertValue(TEST_POINT).assertNoErrors().assertNotComplete();
  }

  @Test
  public void testCancelButtonClicks_notReplayed() {
    viewModel.onCancelButtonClick();

    viewModel.getCancelButtonClicks().test().assertNoValues().assertNoErrors().assertNotComplete();
  }

  @Test
  public void testCancelButtonClicks() {
    TestObserver<Nil> testObserver = viewModel.getCancelButtonClicks().test();

    viewModel.onCancelButtonClick();

    testObserver.assertValue(Nil.NIL).assertNoErrors().assertNotComplete();
  }
}
