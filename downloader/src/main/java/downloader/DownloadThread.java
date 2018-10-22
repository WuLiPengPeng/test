package downloader;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.filechooser.FileSystemView;

public class DownloadThread {
	/**�ļ�����Ĭ��·��*/
	protected static String File_Save_Path = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
	/**����·��*/
	protected static String Download_File_Path = "";
	/**Ĭ���߳�����*/
	protected static int Thread_Size = 0;
	/**�����ļ���С*/
	protected  static int File_Total_Length = 0;
	/**���������ݴ�С*/
	protected static  int Download_Length = 0;
	
	/**
	 * �����ļ�
	 * @param path �ļ�·��
	 * @param threadSize �����̸߳���
 	 * @throws MalformedURLException
	 */
	public synchronized void downloadFile(String path,int threadSize) throws MalformedURLException{
		//������ʾ��·������Դ��λ��
		String regex = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
		if(!path.matches(regex)){
			DownloadWindow.mainJpanl.add(DownloadWindow.processBar);
			DownloadWindow.processBar.setBackground(Color.gray);
			DownloadWindow.processBar.setForeground(Color.gray);
			//����ʾ�������ϵ��ı�
			DownloadWindow.processBar.setStringPainted(false);
			DownloadWindow.noticeBoardTextField.setText("����ʧ��,���������Ƿ����!!!");
			//�������ذ�ť����
			DownloadWindow.affirmDownLoadButton.setEnabled(true);
			return ;
		}
		URL url = new URL(path);
		try {
			HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
			//�������ӳ�ʱʱ��
			openConnection.setConnectTimeout(5000);
			//��������ʽ
			openConnection.setRequestMethod("GET");
			//���ӳɹ�
			if(openConnection.getResponseCode() == 200 ){
				//��ȡ�����ļ���С
				int length = openConnection.getContentLength();
				DownloadThread.File_Total_Length = length;
				//��������Ŀ���ļ�
				File file = new File(DownloadThread.File_Save_Path+File.separatorChar+getFileName(path));
				//������������ļ������
				RandomAccessFile raf = new RandomAccessFile(file, "rwd");
				//����Ŀ���ļ���С
				raf.setLength(length);
				//�����ÿ���߳�ƽ�����ص����ݴ�С
				int block = length % threadSize == 0 ? length/threadSize : length/threadSize + 1;
				//����ָ���������߳�ִ�����ط���
				for (int threadId = 0 ; threadId < threadSize ;threadId++){
					//ִ������
					new Downloader(url,threadId,block,file).start();
				}
				//�ر�
				raf.close();
			}else{
				DownloadWindow.mainJpanl.add(DownloadWindow.processBar);
				DownloadWindow.processBar.setBackground(Color.gray);
				DownloadWindow.processBar.setForeground(Color.gray);
				//����ʾ�������ϵ��ı�
				DownloadWindow.processBar.setStringPainted(false);
				DownloadWindow.noticeBoardTextField.setText("����ʧ��,���������Ƿ����!!!");
				//�������ذ�ť����
				DownloadWindow.affirmDownLoadButton.setEnabled(true);
				System.out.println("����ʧ�ܣ�����");
				return ;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�ļ�����
	 * @param path ·��
	 * @return
	 */
	private String getFileName(String path) {
		String subStr = path.substring(path.lastIndexOf("/")+1);
		return subStr;
	}
	/**
	 * ��������
	 * @author wlp
	 */
	private class Downloader extends Thread{
		//����·��
		private URL url;
		//�߳�id
		private int threadId;
		//��������
		private int block;
		//���ص������ļ�
		private File file ;
		
		public Downloader(URL url, int threadId, int block, File file) {
			this.url = url;
			this.threadId = threadId;
			this.block = block;
			this.file = file;
		}
		
		public void run(){
			//��ȡ��ǰ�߳����ؿ�ʼλ��
			int start = threadId * block;
			//��ȡ��ǰ�߳����ؽ���λ��
			int end = (threadId + 1) * block -1 ;
			try {
				//������������ļ������
				RandomAccessFile raf = new RandomAccessFile(file,"rwd");
				//�ƶ�ָ�뵽д�����ݵĿ�ʼλ��
				raf.seek(start);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//�������ӳ�ʱʱ��
				conn.setConnectTimeout(5000);
				//��������ʽ
				conn.setRequestMethod("GET");
				//�����������ݷ�Χ
				conn.setRequestProperty("Range", "bytes="+start+"-"+end);
				//ע�⣺ʹ���̷ֶ߳�����ʱ�������벻��200������206���������ݣ�
				if(conn.getResponseCode() == 206){
					//��ȡ�����ӵ�����������ȡ���ݣ�
					InputStream inputStream = conn.getInputStream();
					//���ݻ�������
					byte[] buffer = new byte[1024];
					//��¼����ȡ�������ݳ���
					int len = 0;
					//ѭ����ȡ����
					while((len = inputStream.read(buffer)) != -1 && DownloadThread.File_Total_Length != 0){
						//��ʼд������
						raf.write(buffer, 0, len);
						DownloadThread.Download_Length += len;
						DownloadUtil.displayProgressBar(DownloadWindow.processBar, DownloadThread.File_Total_Length, DownloadThread.Download_Length);
					}
					//�ر���
					inputStream.close();
					//�ر�������ʶ���
					raf.close();
				}
				System.out.println("��"+(threadId + 1)+"���߳��������");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}























