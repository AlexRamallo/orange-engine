package com.snakeinalake.engine.ortho.tiles;

public interface OAnimationCallback {
	/**
	 * Called every time AFTER the final frame (e.g. only called once if repeat is disabled)
	 * */
	public void onAnimationEnd();
	/**
	 * Called once when the animation begins
	 * */
	public void onAnimationStart();
	/**
	 * Called every time the frame changes
	 * @param curframe The frame that has been stepped into
	 * */
	public void onFrameStep(int curframe);
}
