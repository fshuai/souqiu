package com.dml.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	/*思路：
当SD卡存在，就调用getExternalCacheDir()方法来获取缓存路径，否则就调用getCacheDir()方法来获取缓存路径。
前者获取到的就是/sdcard/Android/data/<application package>/cache 这个路径，
而后者获取到的是 /data/data/<application package>/cache 这个路径。

getCacheDir()方法用于获取/data/data/<application package>/cache目录
getFilesDir()方法用于获取/data/data/<application package>/files目录
通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据

通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据*/
	
	
	
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存Image的目录名
	 */
	private final static String FOLDER_NAME = "/SouQiu";
	
	
	public FileUtils(){
		
	}
	
	public FileUtils(Context context){
		mDataRootPath = context.getCacheDir().getPath();
	}
	

	/**
	 * 获取储存Image的目录
	 * @return
	 */
	private String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	
	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * @param fileName 
	 * @param bitmap   
	 * @throws IOException
	 */
	public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{
		if(bitmap == null){
			return;
		}
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdir();
		}
		File file = new File(path + File.separator + fileName);
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
	}
	
	/**
	 * 从手机或者sd卡获取Bitmap
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName){
		return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
	}
	
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public  boolean isFileExists(String fileName){
		return new File(getStorageDirectory() + File.separator + fileName).exists();
	}
	
	/**
	 * 获取路径下文件的总大小
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		long size = 0;
		File dirFile = new File(getStorageDirectory());
		if(! dirFile.exists()){
			return 0;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			
			for (int i = 0; i < children.length; i++) {
				size += new File(dirFile, children[i]).length();
			}
		}
		return size;
	}
	
	
	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if(! dirFile.exists()){
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}
		
		dirFile.delete();
	}
	
	
	public void save(int videotype, String s){
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdir();
		}
		String name = getFileName(videotype);
		try{
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path + File.separator + name, false), "utf-8");
		    osw.write(s);
	        osw.close();
		 }catch(IOException e){
		    e.printStackTrace();
		 }
	}
	
	public String read(int videotype){
		String name = getFileName(videotype);
		String path = getStorageDirectory();
		File file = new File(path + File.separator + name);
		if(file.exists()){
			try{
				FileInputStream fis = new FileInputStream(file);
				BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
				String s = null;
				StringBuilder sb = new StringBuilder();
				while((s = bf.readLine()) != null){
					sb.append(s);
				}
				fis.close();
				bf.close();
				
				return sb.toString();
				
			}catch(IOException e){
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
	
	public String getFileName(int videotype){
		String name = null;
		switch(videotype){
		case 1:
			name = "football";
			break;
		case 2:
			name = "basketball";
			break;
		case 3:
			name = "tennis";
			break;
		case 4:
			name = "pingpong";
			break;
		case 5:
			name = "badminton";
			break;
		case 6:
			name = "volleyball";
			break;
		case 7:
			name = "billiards";
			break;
		default:
			break;
		}
		return name;
	}

}
