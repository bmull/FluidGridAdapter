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
     
    FluidGridAdapter fluidGridAdapter = new FluidGridAdapter(this, imageContainers){

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
