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
 * �ļ�����
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
		// �õ���ǰ�ⲿ�洢�豸��Ŀ¼
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		// ����������򴴽�
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		// ����������򴴽�
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ��Ƿ����
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputStream���������д�뵽SD����
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

	// �����ļ�
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// �½��ļ����������������л���
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// �½��ļ���������������л���
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// ��������
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// ˢ�´˻���������
		outBuff.flush();

		// �ر���
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	// �����ļ���
	public static boolean copyDirectiory(String sourceDir, String targetDir)
	// throws IOException
	{
		try {
            
			// �½�Ŀ��Ŀ¼
			(new File(targetDir)).mkdirs();
			// ��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼
			File[] file = (new File(sourceDir)).listFiles();                                                     ///////********
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					// Դ�ļ�
					File sourceFile = file[i];
					// Ŀ���ļ�
					File targetFile = new File(
							new File(targetDir).getAbsolutePath()
									+ File.separator + file[i].getName());                                         /////�ҳ�Ŀ���ļ��ĵ�ַ��Ŀ���ļ���
					copyFile(sourceFile, targetFile);
				}
				if (file[i].isDirectory()) {
					// ׼�����Ƶ�Դ�ļ���
					String dir1 = sourceDir + "/" + file[i].getName();
					// ׼�����Ƶ�Ŀ���ļ���
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
	 * ɾ���洢���ڵ���ʱ�ļ�
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
	 * ɾ���ļ����ļ���
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public static void delFiles(String filepath) throws IOException {
		File f = new File(filepath);// �����ļ�·��
		if (f.exists() && f.isDirectory()) {// �ж����ļ�����Ŀ¼
			if (f.listFiles().length == 0) {// ��Ŀ¼��û���ļ���ֱ��ɾ��
				f.delete();
			} else {// ��������ļ��Ž����飬���ж��Ƿ����¼�Ŀ¼
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						delFiles(delFile[j].getAbsolutePath());// �ݹ����delFiles������ȡ����Ŀ¼·��                                                                                      //////*********
					}
					delFile[j].delete();// ɾ���ļ�
				}
			}
		}
	}

	// /**
	// * ����Ŀ¼
	// *
	// * @param srcDir
	// * ԴĿ¼
	// * @param destDir
	// * Ŀ��Ŀ¼
	// * @return �����Ƴɹ�ʱ����true, ���򷵻�false��
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
	 * ����ļ�
	 * 
	 * @param outfile
	 *            �����ļ���
	 * @param infile
	 *            Ҫ������ļ�����Ŀ¼
	 * @return
	 */
	public static boolean zipFile(String outfile, String infile) {
		try {
			// �ļ������
			FileOutputStream fout = new FileOutputStream(outfile);
			// zip�ļ������
			ZipOutputStream out = new ZipOutputStream(fout);
			// Ҫ������ļ�
			File file = new File(infile);
			// ���д��zip����,��һ�δ����ָ���ļ���,��Ϊ����ӿ���ָ����һ���ļ���
			doMakeFileZip(out, file, "");                                              //��Ϊ�Ǵ���ļ������¼�Ϊ��                      ///////??????����
			// �ر�zip�����
			out.close();
			// ���سɹ�
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("���ʧ��(ָ�����ļ�������)...");
			return false;
		} catch (Exception e) {
			System.out.println("���ʧ��(δ֪ԭ��)...");
			return false;
		}
	}

	/**
	 * ����ļ�����
	 * 
	 * @param out
	 *            zip�����
	 * @param file
	 *            ����ļ�
	 * @param dir
	 *            ��һ����Ŀ¼
	 * @throws IOException
	 */                                                                                                                ///////******
	public static void doMakeFileZip(ZipOutputStream out, File file, String dir)
			throws IOException {
		// �����ǰ�������Ŀ¼
		if (file.isDirectory()) {
			// ���������Ϣ
			System.out.println("��ǰ���ڴ���ļ���:" + file + "...");
			// �ļ��б�
			File[] files = file.listFiles();
			// �����һ�����Ŀ¼�ļ�
			out.putNextEntry(new ZipEntry(dir + "/"));
			//
			dir = dir.length() == 0 ? "" : dir + "/";
			for (int i = 0; i < files.length; i++) {
				doMakeFileZip(out, files[i], dir + files[i].getName());
			}
		}
		// �����ǰ����ļ�
		else {
			// ���������Ϣ
			System.out.println("��ǰ���ڴ���ļ�:" + file + "...");
			//
			// out.putNextEntry(new ZipEntry(dir + file.getName()));
			out.putNextEntry(new ZipEntry(dir));
			// Log.e("ZipFile", dir);

			// �ļ�������
			FileInputStream in = new FileInputStream(file);
			int i;
			// ����д��
			while ((i = in.read()) != -1) {
				out.write(i);                                                 
			}
			// �ر�������
			in.close();
		}
	}

	/**
	 * ��ѹzip�ļ�
	 * 
	 * @param zipfile
	 *            zip�ļ�
	 * @param savedir
	 *            ����ļ���
	 * @return
	 */
	public static boolean unzipFile(String zipfile, String savedir) {
		try {
			// �ļ�������
			FileInputStream file = new FileInputStream(zipfile);
			// zip������
			ZipInputStream in = new ZipInputStream(file);
			ZipEntry z;
			while ((z = in.getNextEntry()) != null) {
				// ====������ļ���
				if (z.isDirectory()) {
					System.out.println("���ڽ�ѹ�ļ���:" + z.getName());
					File tempfile = new File(savedir
							+ File.separator
							+ z.getName()
									.substring(0, z.getName().length() - 1));              ////////*******
					// ����Ŀ¼
					tempfile.mkdir();
					System.out.println("�Ѿ�����Ŀ¼:"
							+ savedir
							+ File.separator
							+ z.getName()
									.substring(0, z.getName().length() - 1));
				}
				// ====������ļ�
				else {
					System.out.println("���ڽ�ѹ�ļ�:" + z.getName());
					File tempfile = new File(savedir + File.separator
							+ z.getName());
					// �������ļ�
					tempfile.createNewFile();
					// �ļ������
					FileOutputStream out = new FileOutputStream(tempfile);
					int i;
					while ((i = in.read()) != -1) {
						out.write(i);   
					}
					// �ļ�������ر�
					out.close();
				}
			}
			// �ļ��������ر�
			in.close();
			return true;
		} catch (Exception e) {
			System.out.println("�ļ�" + zipfile + "��ѹʧ��...");
			return false;
		}
	}

}
