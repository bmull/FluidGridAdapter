FluidGridAdapter
================

A listview adapter that arranges images in a fluid grid. Callbacks for loading the image into the view and for when a single cell is tapped. Allows for custom cell padding, row height, and cell background. 

![](resources/SampleS.png)
<br><br>
Create a FluidGridAdapter by passing in an array of objects that have:<br>
1) an image url/tag/identifier<br>
2) an aspect ratio OR width & height
<br>
<h2>Example Usage</h2>
Here's using an example using a library such as <a href="https://github.com/squareup/Picasso.git">Picasso</a>
     
     
     //Query the MediaStore for device photos and build and array of ImageData objects
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
     
     //Pass the array of ImageData objects into the adapter
     FluidGridAdapter fluidGridAdapter = new FluidGridAdapter(this, imageDatas){

			@Override
			protected void onSingleCellTapped(ImageData imageData) {
				Log.d(TAG,"Single cell tapped");
			}

			@Override
			protected void loadImageIntoView(String photoUrl, int cellWidth, int cellHeight, ImageView imageHolder) {	
				Picasso.with(context).load(photoUrl).resize(cellWidth, cellHeight).into(imageHolder);
			}
		};
	listview.setAdapter(fluidGridAdapter);
<br>

Custom cell padding and row height
<br>
![](resources/SampleLarge.png)
<br>
Custom cell background color
<br>
![](resources/SampleLoading.png)
<br><br>
Compatible with Android 4.0 and above
