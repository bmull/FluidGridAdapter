package com.getcluster.models;

public class ImageData {
	private String imageUrl;
	private float aspectRatio;
	private int width;
	private int height;

	public ImageData(String imageUrl, float aspectRatio) {
		setImageUrl(imageUrl);
		setAspectRatio(aspectRatio);
	}

	public ImageData(String imageUrl, int width, int height) {
		setImageUrl(imageUrl);
		float ar = (float)width / height;
		setAspectRatio(ar);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
