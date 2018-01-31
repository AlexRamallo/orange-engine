package com.snakeinalake.engine.config;

import com.snakeinalake.engine.core.vector2df;

public class OEngineSettings{
	private static OEngineSettings pInstance = null;	
	private settings Settings;
	/**
	 * Get instance of OEngineSettings
	 * */
	public static OEngineSettings getInstance(){
		if(pInstance == null){
			pInstance = new OEngineSettings();
			pInstance.Settings = null;
		}
		return pInstance;
	}
	public enum gfxQuality{
		/**
		 * Highest possible quality, low performance
		 * */
		gfx_HIGH,
		/**
		 * good balance between performance and quality
		 * */
		gfX_MEDIUM,
		/**
		 * low quality, high performance
		 * */
		gfx_LOW,
		/**
		 * For REALLY shitty phones. Attempts to apply the least functionality possible to improve performance.\n\n
		 * Not gauranteed to work
		 * */
		gfx_COMPATABILITY,
		/**
		 * Let developer choose what to enable or disable
		 * */
		gfx_CUSTOM
	};
	
	public void setSettings(settings set){
		Settings = set;
	}
	public settings getSettings(){
		return Settings;
	}
	public class settings{
		public boolean
		/**
		 * Mesh Caching. Enabling this GREATLY reduces load time\n
		 * default: ON
		 * */
		meshCaching,
		/**
		 * Anti Aliasing on GUI text
		 * */
		textAA,
		/**
		 * Whether to enable Wireframe mode for all nodes by default.\n can be manually enabled/disabled per-node
		 * */
		allWireframe,
		/**
		 * Whether to enable Lighting for all nodes by default.\n can be manually enabled/disabled per-node
		 * */
		allLighting;
		/**
		 * Resolution at which to render 
		 * */
		public vector2df resolution;
		/**
		 * Preset quality standards
		 * */
		public gfxQuality quality;
	}
}
