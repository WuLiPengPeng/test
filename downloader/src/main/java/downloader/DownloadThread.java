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
	/**文件保存默认路径*/
	protected static String File_Save_Path = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
	/**下载路径*/
	protected static String Download_File_Path = "";
	/**默认线程数量*/
	protected static int Thread_Size = 0;
	/**下载文件大小*/
	protected  static int File_Total_Length = 0;
	/**已下载数据大小*/
	protected static  int Download_Length = 0;
	
	/**
	 * 下载文件
	 * @param path 文件路径
	 * @param threadSize 开启线程个数
 	 * @throws MalformedURLException
	 */
	public synchronized void downloadFile(String path,int threadSize) throws MalformedURLException{
		//创建表示该路径的资源定位符
		String regex = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
		if(!path.matches(regex)){
			DownloadWindow.mainJpanl.add(DownloadWindow.processBar);
			DownloadWindow.processBar.setBackground(Color.gray);
			DownloadWindow.processBar.setForeground(Color.gray);
			//不显示进度条上的文本
			DownloadWindow.processBar.setStringPainted(false);
			DownloadWindow.noticeBoardTextField.setText("下载失败,请检查数据是否可用!!!");
			//设置下载按钮可用
			DownloadWindow.affirmDownLoadButton.setEnabled(true);
			return ;
		}
		URL url = new URL(path);
		try {
			HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
			//设置连接超时时间
			openConnection.setConnectTimeout(5000);
			//设置请求方式
			openConnection.setRequestMethod("GET");
			//连接成功
			if(openConnection.getResponseCode() == 200 ){
				//获取网络文件大小
				int length = openConnection.getContentLength();
				DownloadThread.File_Total_Length = length;
				//创建下载目标文件
				File file = new File(DownloadThread.File_Save_Path+File.separatorChar+getFileName(path));
				//创建随机访问文件类对象
				RandomAccessFile raf = new RandomAccessFile(file, "rwd");
				//设置目标文件大小
				raf.setLength(length);
				//计算出每个线程平均下载的数据大小
				int block = length % threadSize == 0 ? length/threadSize : length/threadSize + 1;
				//开启指定数量的线程执行下载方法
				for (int threadId = 0 ; threadId < threadSize ;threadId++){
					//执行下载
					new Downloader(url,threadId,block,file).start();
				}
				//关闭
				raf.close();
			}else{
				DownloadWindow.mainJpanl.add(DownloadWindow.processBar);
				DownloadWindow.processBar.setBackground(Color.gray);
				DownloadWindow.processBar.setForeground(Color.gray);
				//不显示进度条上的文本
				DownloadWindow.processBar.setStringPainted(false);
				DownloadWindow.noticeBoardTextField.setText("下载失败,请检查数据是否可用!!!");
				//设置下载按钮可用
				DownloadWindow.affirmDownLoadButton.setEnabled(true);
				System.out.println("下载失败！！！");
				return ;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件名称
	 * @param path 路径
	 * @return
	 */
	private String getFileName(String path) {
		String subStr = path.substring(path.lastIndexOf("/")+1);
		return subStr;
	}
	/**
	 * 下载器类
	 * @author wlp
	 */
	private class Downloader extends Thread{
		//下载路径
		private URL url;
		//线程id
		private int threadId;
		//下载区块
		private int block;
		//下载到本地文件
		private File file ;
		
		public Downloader(URL url, int threadId, int block, File file) {
			this.url = url;
			this.threadId = threadId;
			this.block = block;
			this.file = file;
		}
		
		public void run(){
			//获取当前线程下载开始位置
			int start = threadId * block;
			//获取当前线程下载结束位置
			int end = (threadId + 1) * block -1 ;
			try {
				//创建随机访问文件类对象
				RandomAccessFile raf = new RandomAccessFile(file,"rwd");
				//移动指针到写入数据的开始位置
				raf.seek(start);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//设置连接超时时间
				conn.setConnectTimeout(5000);
				//设置请求方式
				conn.setRequestMethod("GET");
				//设置请求数据范围
				conn.setRequestProperty("Range", "bytes="+start+"-"+end);
				//注意：使用线程分段下载时，返回码不是200，而是206（部分内容）
				if(conn.getResponseCode() == 206){
					//获取该链接的输入流（读取数据）
					InputStream inputStream = conn.getInputStream();
					//数据缓存数组
					byte[] buffer = new byte[1024];
					//记录当读取到的数据长度
					int len = 0;
					//循环读取数据
					while((len = inputStream.read(buffer)) != -1 && DownloadThread.File_Total_Length != 0){
						//开始写入数据
						raf.write(buffer, 0, len);
						DownloadThread.Download_Length += len;
						DownloadUtil.displayProgressBar(DownloadWindow.processBar, DownloadThread.File_Total_Length, DownloadThread.Download_Length);
					}
					//关闭流
					inputStream.close();
					//关闭随机访问对象
					raf.close();
				}
				System.out.println("第"+(threadId + 1)+"条线程下载完毕");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}























