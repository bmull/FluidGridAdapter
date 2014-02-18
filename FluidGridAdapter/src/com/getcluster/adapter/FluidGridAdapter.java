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
package com.getcluster.adapter;

import java.util.ArrayList;

import com.getcluster.fluidgridadapter.R;
import com.getcluster.models.FluidPhotoRow;
import com.getcluster.models.ImageData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FluidGridAdapter extends BaseAdapter {

	private Context context;
	private int cellPadding;
	private int screenWidth;
	private ArrayList<FluidPhotoRow> fluidPhotoRows;
	private int desiredRowHeight = 200;
	private int initialCellPadding = 5;
	private int cellBackgroundColor = 0;

	public FluidGridAdapter(Context context, ArrayList<ImageData> imageDatas) {
		this(context, imageDatas, -1);
	}

	public FluidGridAdapter(Context context, ArrayList<ImageData> imageDatas, int cellPadding) {
		this(context, imageDatas, cellPadding, 0);
	}

	public FluidGridAdapter(Context context, ArrayList<ImageData> imageDatas, int initialCellPadding, int desiredRowHeight) {
		this.context = context;
		if(initialCellPadding > -1) {
			this.initialCellPadding = initialCellPadding;
		}
		if(desiredRowHeight > 0) {
			this.desiredRowHeight = desiredRowHeight;
		}
		calculateScreenDimensions();
		this.fluidPhotoRows = buildFluidPhotoRows(imageDatas);
	}

	@Override
	public int getCount() {
		if(fluidPhotoRows != null) {
			return fluidPhotoRows.size();
		}
		return 0;
	}

	@Override
	public FluidPhotoRow getItem(int position) {
		if(fluidPhotoRows != null) {
			return fluidPhotoRows.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		RowHolder rowHolder;
		if(v == null) {
			v = LayoutInflater.from(context).inflate(R.layout.fluid_row, parent, false);
			rowHolder = new RowHolder();
			rowHolder.fluidRow = (LinearLayout)v.findViewById(R.id.fluid_row_layout);
			v.setTag(rowHolder);
		} else {
			rowHolder = (RowHolder)v.getTag();
		}
		rowHolder.fluidRow.removeAllViews();
		if(getItem(position) != null) {
			int photosSize = getItem(position).getImageDatas().size();

			int rowHeight = getItem(position).getRowHeight();

			for(int i = 0; i < photosSize; i++) {
				View singleCell = LayoutInflater.from(context).inflate(R.layout.single_image_cell, null);
				RelativeLayout imageContainer = (RelativeLayout)singleCell.findViewById(R.id.image_container);
				ImageView photo = (ImageView)singleCell.findViewById(R.id.photo);

				final ImageData imageData = getItem(position).getImageDatas().get(i);
				if(cellBackgroundColor != 0) {
					photo.setBackgroundColor(cellBackgroundColor);
				}
				float aspectRatio = imageData.getAspectRatio();
				String photoUrl = imageData.getImageUrl();

				int rowWidth = (int)Math.floor((rowHeight * aspectRatio));
				LayoutParams lp = photo.getLayoutParams();
				lp.height = rowHeight;
				lp.width = rowWidth;
				photo.setLayoutParams(lp);

				loadImageIntoView(photoUrl, rowWidth, rowHeight, photo);

				LayoutParams clp = imageContainer.getLayoutParams();
				if(clp != null) {
					clp.height = rowHeight;
					clp.width = rowWidth;
					imageContainer.setLayoutParams(clp);
				}
				singleCell.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onSingleCellTapped(imageData);
					}
				});

				singleCell.setPadding(0, 0, cellPadding, 0);
				rowHolder.fluidRow.setPadding(cellPadding, cellPadding, 0, 0);
				rowHolder.fluidRow.addView(singleCell);
			}
		}
		return v;
	}

	/*
	 * Called when the user taps on a single image cell
	 * 
	 * @param The image data of the cell being tapped on
	 */
	protected void onSingleCellTapped(ImageData imageData) {
	}

	/*
	 * Called when the adapter is ready to load the specified image into the
	 * view
	 * 
	 * @param photoUrl the url of the photo
	 * 
	 * @param imageWidth width of image cell
	 * 
	 * @param imageHeight height of image cell
	 * 
	 * @param imageHolder the view that the image should be loaded into
	 */
	protected void loadImageIntoView(String photoUrl, int cellWidth, int cellHeight, ImageView imageHolder) {
	}

	static class RowHolder {
		LinearLayout fluidRow;
	}

	/*
	 * calculate dimensions for the specific device
	 */
	@SuppressWarnings("deprecation")
	private void calculateScreenDimensions() {
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		float one_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
		cellPadding = (int)Math.ceil(initialCellPadding * one_px);
		screenWidth = size.x - cellPadding;
	}

	/*
	 * build an array of rows that have images and dimensions of that row
	 */
	private ArrayList<FluidPhotoRow> buildFluidPhotoRows(ArrayList<ImageData> imageDatas) {
		double photoRowWidth = 0;
		int i = 0;

		ArrayList<FluidPhotoRow> fluidPhotoRows = new ArrayList<FluidPhotoRow>();
		ArrayList<ImageData> subList = new ArrayList<ImageData>();

		for(ImageData imageData : imageDatas) {
			i++;
			int totalPadding = (i - 1) * cellPadding;
			float aspectRatio = imageData.getAspectRatio();
			double photoWidth = aspectRatio * desiredRowHeight;

			photoRowWidth = photoRowWidth + photoWidth;
			if(photoRowWidth < (screenWidth - totalPadding)) {
				subList.add(imageData);
				if(i == imageDatas.size()) {
					double newRowHeight = desiredRowHeight * (screenWidth / photoRowWidth);
					FluidPhotoRow photoRow = new FluidPhotoRow(subList, (int)Math.floor(newRowHeight));
					fluidPhotoRows.add(photoRow);
				}
			} else if(subList.size() == 0) {
				double newRowHeight = desiredRowHeight * (screenWidth / photoRowWidth);
				subList.add(imageData);
				FluidPhotoRow photoRow = new FluidPhotoRow(subList, (int)Math.floor(newRowHeight));
				fluidPhotoRows.add(photoRow);
				subList = new ArrayList<ImageData>();
			} else {
				photoRowWidth = photoRowWidth - photoWidth;
				double newRowHeight = desiredRowHeight * (screenWidth / photoRowWidth);

				FluidPhotoRow photoRow = new FluidPhotoRow(subList, (int)newRowHeight);
				fluidPhotoRows.add(photoRow);
				subList = new ArrayList<ImageData>();
				subList.add(imageData);
				photoRowWidth = photoWidth;

				if(i == imageDatas.size()) {
					double finalRowHeight = desiredRowHeight * (screenWidth / photoRowWidth);
					FluidPhotoRow newPhotoRow = new FluidPhotoRow(subList, (int)Math.floor(finalRowHeight));
					fluidPhotoRows.add(newPhotoRow);
				} else {
					i = 0;
				}
			}
		}

		return fluidPhotoRows;
	}

	/*
	 * set the background color of the cell shown before loading completes
	 * 
	 * @param the id of the color eg. R.color.white
	 */
	public void setCellBackgroundColor(int cellBackgroundColor) {
		this.cellBackgroundColor = cellBackgroundColor;
	}

}
