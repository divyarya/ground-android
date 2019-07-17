/*
 * Copyright 2019 Google LLC
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

package com.google.android.gnd.workers;

import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

/** Enqueues file download work to be done in the background. */
public class FileDownloadWorkManager {
  private final WorkManager workManager;
  private static final Constraints CONSTRAINTS =
      new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

  public FileDownloadWorkManager() {
    this.workManager = WorkManager.getInstance();
  }

  /**
   * Enqueues a worker that downloads files when a network connection is available, returning live
   * data that contains {@link WorkInfo}. Observe the returned live data to do something with the
   * worker's result.
   */
  public LiveData<WorkInfo> enqueueFileDownloadWorker(String url, String filename) {
    return getWorkManager()
        .getWorkInfoByIdLiveData(enqueueFileDownloadWorkerInternal(url, filename).getId());
  }

  private OneTimeWorkRequest enqueueFileDownloadWorkerInternal(String url, String filename) {
    OneTimeWorkRequest request = buildWorkerRequest(url, filename);

    getWorkManager()
        .enqueueUniqueWork(FileDownloadWorker.class.getName(), ExistingWorkPolicy.APPEND, request);

    // Return the request in order to fetch the {@link WorkInfo} for the request using the request
    // id.
    return request;
  }

  private WorkManager getWorkManager() {
    return workManager;
  }

  private OneTimeWorkRequest buildWorkerRequest(String url, String filename) {
    return new OneTimeWorkRequest.Builder(FileDownloadWorker.class)
        .setConstraints(CONSTRAINTS)
        .setInputData(FileDownloadWorker.createInputData(url, filename))
        .build();
  }
}
