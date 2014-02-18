/*
 * Copyright 2014 Cluster Labs, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.getcluster.fluidgridsample;

import java.io.File;
import java.util.ArrayList;

import com.getcluster.adapter.FluidGridAdapter;
import com.getcluster.models.ImageData;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class DevicePhotosSampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupFluidGrid();
	}

	private void setupFluidGrid() {
		ArrayList<ImageData> imageDatas = loadDevicePhotos();
		FluidGridAdapter fluidGridAdapter = new FluidGridAdapter(this, imageDatas) {

			@Override
			protected void onSingleCellTapped(ImageData imageData) {
				Toast.makeText(DevicePhotosSampleActivity.this, "You tapped a photo! (" + imageData.getImageUrl() + ")", Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void loadImageIntoView(String photoUrl, int cellWidth, int cellHeight, ImageView imageHolder) {
				Picasso.with(DevicePhotosSampleActivity.this).load(new File(photoUrl)).resize(cellWidth, cellHeight).into(imageHolder);
			}

		};
		ListView listview = (ListView)findViewById(R.id.fluid_list);
		listview.setAdapter(fluidGridAdapter);
	}

	protected ArrayList<ImageData> loadDevicePhotos() {
		String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.HEIGHT,
				MediaStore.Images.Thumbnails.WIDTH };

		Cursor cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
		ArrayList<ImageData> imageDatas = new ArrayList<ImageData>();
		int photoHeightIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
		int photoWidthIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
		int fileLocationIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		while(cursor.moveToNext()) {

			int photoHeight = cursor.getInt(photoHeightIndex);
			int photoWidth = cursor.getInt(photoWidthIndex);
			String fileLocation = cursor.getString(fileLocationIndex);

			if(photoWidth > 0 && photoHeight > 0) {
				ImageData imageData = new ImageData(fileLocation, photoWidth, photoHeight);
				imageDatas.add(imageData);
			}
		}
		cursor.close();

		return imageDatas;
	}

}
