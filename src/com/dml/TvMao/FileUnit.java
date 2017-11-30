package com.dml.TvMao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.os.Environment;

/**
 * 文件操作
 * 
 * @author Loki
 * 
 */
public class FileUnit {
	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUnit() {
		// 得到当前外部存储设备的目录
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		// 如果不存在则创建
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		// 如果不存在则创建
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 判断SD卡上的文件是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			
			output = new FileOutputStream(file);
			
			byte buffer[] = new byte[4 * 1024];
			
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	// 复制文件夹
	public static boolean copyDirectiory(String sourceDir, String targetDir)
	// throws IOException
	{
		try {
            
			// 新建目标目录
			(new File(targetDir)).mkdirs();
			// 获取源文件夹当前下的文件或目录
			File[] file = (new File(sourceDir)).listFiles();                                                     ///////********
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					// 源文件
					File sourceFile = file[i];
					// 目标文件
					File targetFile = new File(
							new File(targetDir).getAbsolutePath()
									+ File.separator + file[i].getName());                                         /////找出目标文件的地址及目标文件名
					copyFile(sourceFile, targetFile);
				}
				if (file[i].isDirectory()) {
					// 准备复制的源文件夹
					String dir1 = sourceDir + "/" + file[i].getName();
					// 准备复制的目标文件夹
					String dir2 = targetDir + "/" + file[i].getName();
					copyDirectiory(dir1, dir2);                                                                //////***
				}
				
				
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 删除存储卡内的临时文件
	 * 
	 * @param strFileName
	 */
	public static void delFile(String strFileName) {
		File myFile = new File(strFileName);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public static void delFiles(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						delFiles(delFile[j].getAbsolutePath());// 递归调用delFiles方法并取得子目录路径                                                                                      //////*********
					}
					delFile[j].delete();// 删除文件
				}
			}
		}
	}

	// /**
	// * 复制目录
	// *
	// * @param srcDir
	// * 源目录
	// * @param destDir
	// * 目标目录
	// * @return 当复制成功时返回true, 否则返回false。
	// */
	// public static boolean copyDir(String srcDir, String destDir) {
	// try {
	// Log.e("CopyDir_S", srcDir);
	// Log.e("CopyDir_D", destDir);
	// FileUtils.copyDirectory(new File(srcDir), new File(destDir));
	// } catch (IOException e) {
	// e.printStackTrace();
	// return false;
	// }
	// return true;
	// }

	/**
	 * 打包文件
	 * 
	 * @param outfile
	 *            生成文件名
	 * @param infile
	 *            要打包的文件名或目录
	 * @return
	 */
	public static boolean zipFile(String outfile, String infile) {
		try {
			// 文件输出流
			FileOutputStream fout = new FileOutputStream(outfile);
			// zip文件输出流
			ZipOutputStream out = new ZipOutputStream(fout);
			// 要打包的文件
			File file = new File(infile);
			// 进行打包zip操作,第一次打包不指定文件夹,因为程序接口中指定了一级文件夹
			doMakeFileZip(out, file, "");                                              //因为是打包文件所以下级为空                      ///////??????调用
			// 关闭zip输出流
			out.close();
			// 返回成功
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("打包失败(指定的文件不存在)...");
			return false;
		} catch (Exception e) {
			System.out.println("打包失败(未知原因)...");
			return false;
		}
	}

	/**
	 * 打包文件操作
	 * 
	 * @param out
	 *            zip输出流
	 * @param file
	 *            打包文件
	 * @param dir
	 *            下一级的目录
	 * @throws IOException
	 */                                                                                                                ///////******
	public static void doMakeFileZip(ZipOutputStream out, File file, String dir)
			throws IOException {
		// 如果当前打包的是目录
		if (file.isDirectory()) {
			// 输出进度信息
			System.out.println("当前正在打包文件夹:" + file + "...");
			// 文件列表
			File[] files = file.listFiles();
			// 添加下一个打包目录文件
			out.putNextEntry(new ZipEntry(dir + "/"));
			//
			dir = dir.length() == 0 ? "" : dir + "/";
			for (int i = 0; i < files.length; i++) {
				doMakeFileZip(out, files[i], dir + files[i].getName());
			}
		}
		// 如果当前打包文件
		else {
			// 输出进度信息
			System.out.println("当前正在打包文件:" + file + "...");
			//
			// out.putNextEntry(new ZipEntry(dir + file.getName()));
			out.putNextEntry(new ZipEntry(dir));
			// Log.e("ZipFile", dir);

			// 文件输入流
			FileInputStream in = new FileInputStream(file);
			int i;
			// 进行写入
			while ((i = in.read()) != -1) {
				out.write(i);                                                 
			}
			// 关闭输入流
			in.close();
		}
	}

	/**
	 * 解压zip文件
	 * 
	 * @param zipfile
	 *            zip文件
	 * @param savedir
	 *            输出文件夹
	 * @return
	 */
	public static boolean unzipFile(String zipfile, String savedir) {
		try {
			// 文件输入流
			FileInputStream file = new FileInputStream(zipfile);
			// zip输入流
			ZipInputStream in = new ZipInputStream(file);
			ZipEntry z;
			while ((z = in.getNextEntry()) != null) {
				// ====如果是文件夹
				if (z.isDirectory()) {
					System.out.println("正在解压文件夹:" + z.getName());
					File tempfile = new File(savedir
							+ File.separator
							+ z.getName()
									.substring(0, z.getName().length() - 1));              ////////*******
					// 创建目录
					tempfile.mkdir();
					System.out.println("已经创建目录:"
							+ savedir
							+ File.separator
							+ z.getName()
									.substring(0, z.getName().length() - 1));
				}
				// ====如果是文件
				else {
					System.out.println("正在解压文件:" + z.getName());
					File tempfile = new File(savedir + File.separator
							+ z.getName());
					// 创建新文件
					tempfile.createNewFile();
					// 文件输出流
					FileOutputStream out = new FileOutputStream(tempfile);
					int i;
					while ((i = in.read()) != -1) {
						out.write(i);   
					}
					// 文件输出流关闭
					out.close();
				}
			}
			// 文件输入流关闭
			in.close();
			return true;
		} catch (Exception e) {
			System.out.println("文件" + zipfile + "解压失败...");
			return false;
		}
	}

}
