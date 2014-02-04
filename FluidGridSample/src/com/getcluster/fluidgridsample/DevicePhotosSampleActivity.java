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
	
	private void setupFluidGrid(){
		ArrayList<ImageData> imageDatas = loadDevicePhotos();
		FluidGridAdapter fluidGridAdapter = new FluidGridAdapter(this, imageDatas){

			@Override
			protected void onSingleCellTapped(ImageData imageData) {
				Toast.makeText(DevicePhotosSampleActivity.this, "You tapped a photo! ("+imageData.getImageUrl()+")", Toast.LENGTH_SHORT).show();
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
		String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.HEIGHT,
				MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.MIME_TYPE };
		String selection = "";

		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
				MediaStore.Images.Media.DATE_TAKEN + " DESC");

		ArrayList<ImageData> imageDatas = new ArrayList<ImageData>();
		
		int photoHeightIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
		int photoWidthIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
		int fileLocationIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		while(cursor.moveToNext()) {
			
			int photoHeight = cursor.getInt(photoHeightIndex);
			int photoWidth = cursor.getInt(photoWidthIndex);
			String fileLocation = cursor.getString(fileLocationIndex);
			
			
			if(photoWidth > 0 && photoHeight > 0) {
				float aspectRatio = (float)photoWidth / photoHeight;
				ImageData imageData = new ImageData();
				imageData.setAspectRatio(aspectRatio);
				imageData.setImageUrl(fileLocation);
				
				imageDatas.add(imageData);
			}
			
		}
		cursor.close();
		
		return imageDatas;
	}

}
