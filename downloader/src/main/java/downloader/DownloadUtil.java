package downloader;

import java.awt.Color;
import java.math.BigDecimal;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent.EventType;

public class DownloadUtil {
	/**线程数量输入栏默认内容*/
	protected static final String sizeStr = "请输入正确的线程数量" ;
	/**下载路径输入栏默认内容*/
	protected static final String pathStr = "请输入正确的下载路径" ;
	/**
	 * 设置文件保存路径
	 * @param fileChooser 目录文件选择器
	 * @return
	 */
	protected static void setSavePath(JFileChooser fileChooser){
		String path = "";
		if (fileChooser != null){
			int result = fileChooser.showSaveDialog(null);
			//路径确认
			if (JFileChooser.APPROVE_OPTION == result) {
				path = fileChooser.getSelectedFile().getPath();
				DownloadThread.File_Save_Path = path ;
				DownloadWindow.savePathTextField.setText(DownloadThread.File_Save_Path);
			}
		}
	}
	/**
	 * 清空输入栏内容
	 * @param TextField 输入栏对象
	 */
	protected static void clearTextFieldContent(JTextField TextField){
		if(TextField != null){
			TextField.setText("");
		}
	}
	
	protected static void setDownloadPathAndThreadSize(JTextField locationTextField,JTextField threadNumTextField){
		//获取下载路径
		if(locationTextField != null ){
			String path = locationTextField.getText();
			if(path != null && !path.trim().isEmpty()){
				DownloadThread.Download_File_Path = path ;
			}else{
				locationTextField.setForeground(Color.red);
				locationTextField.setText(pathStr);
			}
		}
		//获取开启线程数量
		if(threadNumTextField != null){
			String threadSize = threadNumTextField.getText();
			if(threadSize.matches("\\d+")){
				try{
					int size = Integer.parseInt(threadSize);
					DownloadThread.Thread_Size = size;
				}catch(Exception e){
					e.printStackTrace();
					threadNumTextField.setForeground(Color.red);
					threadNumTextField.setText(sizeStr);
				}
			}else{
				threadNumTextField.setForeground(Color.red);
				threadNumTextField.setText(sizeStr);
			}
		}
		//文件保存地址有默认值，此处对下载地址和线程数进行判断就行
		if(!DownloadThread.Download_File_Path.trim().isEmpty() && DownloadThread.Thread_Size != 0){
			try {
				//先删除
				DownloadWindow.mainJpanl.remove(DownloadWindow.processBar);
				if(DownloadWindow.processBar.getBackground().equals(Color.gray)){
					DownloadWindow.processBar.setBackground(Color.white);
					DownloadWindow.processBar.setForeground(new Color(102,205,0));
				}
				DownloadWindow.processBar.setStringPainted(true);
				DownloadWindow.noticeBoardTextField.setText("警告!!!");
				//添加进度条
				DownloadWindow.mainJpanl.add(DownloadWindow.processBar);
				//设置下载按钮不可用
				DownloadWindow.affirmDownLoadButton.setEnabled(false);
				//将下载文件大小记录和已下载文件大小记录设置为0
				DownloadThread.File_Total_Length = 0;
				DownloadThread.Download_Length = 0;
				//调用下载
				new DownloadThread().downloadFile(DownloadThread.Download_File_Path,DownloadThread.Thread_Size);
				System.out.println("开始下载了---");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 设置输入栏前景色为黑色
	 * @param TextField
	 * @param type 事件类型
	 */
	protected static void upDateTextFieldColorToBlack(JTextField TextField,EventType type){
		if(TextField == null || type == null){
			return;
		}
		System.out.println(type);
		if(TextField.getText().equals(DownloadUtil.sizeStr) || TextField.getText().equals(DownloadUtil.pathStr)){
			//设置红色前景色
			TextField.setForeground(Color.red);
		}else{
			//设置黑色前景色
			TextField.setForeground(Color.black);
		}
	}
	
	/**
	 * 显示进度
	 * @param processBar 进度条对象
	 * @param fileTotalLength 下载文件大小
	 * @param downloadLength 已近下载数据大小
	 */
	public static void displayProgressBar(JProgressBar processBar,int fileTotalLength,int downloadLength){
		System.out.println("fileTotalLength:--="+fileTotalLength);
		if(processBar == null || fileTotalLength == 0){
			return ;
		}
		synchronized (processBar) {
			double result =  new BigDecimal((float)downloadLength/fileTotalLength).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			int progressNum = (int)(result * 100);
			final int  tempProgress = progressNum;
			final JProgressBar tempProcessBar;
			final String str = processBar.getString();
			tempProcessBar = processBar ;
			System.out.println("总大小："+fileTotalLength+"已下载大小"+downloadLength+"进度："+tempProgress);
			//创建线程显示进度  
	        new Thread(){  
	        	//设置进度条数值              
	            public void run(){  
	            	if(!"%100".equals(str)){
	            		tempProcessBar.setValue(tempProgress);
	            	}
	            }  
	        }.start(); //启动进度条线程  
	        //设置提示信息  
	        if(tempProcessBar.getValue() >= 100){
				//显示进度条上的文本
				DownloadWindow.processBar.setStringPainted(true);
				//设置下载按钮可用
				DownloadWindow.affirmDownLoadButton.setEnabled(true);
				//14个空格加2个Tab
				DownloadWindow.noticeBoardTextField.setText("\t\t                下载完成");
				
				System.out.println("DownloadThread.File_Total_Length:"+DownloadThread.Download_Length+"和Download_Length："+
				DownloadThread.File_Total_Length);
			}
		}
	}
	
	
	
	
	
}























