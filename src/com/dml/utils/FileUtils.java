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
	/*˼·��
��SD�����ڣ��͵���getExternalCacheDir()��������ȡ����·��������͵���getCacheDir()��������ȡ����·����
ǰ�߻�ȡ���ľ���/sdcard/Android/data/<application package>/cache ���·����
�����߻�ȡ������ /data/data/<application package>/cache ���·����

getCacheDir()�������ڻ�ȡ/data/data/<application package>/cacheĿ¼
getFilesDir()�������ڻ�ȡ/data/data/<application package>/filesĿ¼
ͨ��Context.getExternalFilesDir()�������Ի�ȡ�� SDCard/Android/data/���Ӧ�õİ���/files/ Ŀ¼��һ���һЩ��ʱ�䱣�������

ͨ��Context.getExternalCacheDir()�������Ի�ȡ�� SDCard/Android/data/���Ӧ�ð���/cache/Ŀ¼��һ������ʱ��������*/
	
	
	
	/**
	 * sd���ĸ�Ŀ¼
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * �ֻ��Ļ����Ŀ¼
	 */
	private static String mDataRootPath = null;
	/**
	 * ����Image��Ŀ¼��
	 */
	private final static String FOLDER_NAME = "/SouQiu";
	
	
	public FileUtils(){
		
	}
	
	public FileUtils(Context context){
		mDataRootPath = context.getCacheDir().getPath();
	}
	

	/**
	 * ��ȡ����Image��Ŀ¼
	 * @return
	 */
	private String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	
	/**
	 * ����Image�ķ�������sd���洢��sd����û�оʹ洢���ֻ�Ŀ¼
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
	 * ���ֻ�����sd����ȡBitmap
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName){
		return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
	}
	
	/**
	 * �ж��ļ��Ƿ����
	 * @param fileName
	 * @return
	 */
	public  boolean isFileExists(String fileName){
		return new File(getStorageDirectory() + File.separator + fileName).exists();
	}
	
	/**
	 * ��ȡ·�����ļ����ܴ�С
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
	 * ɾ��SD�������ֻ��Ļ���ͼƬ��Ŀ¼
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
