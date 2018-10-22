package downloader;

import java.awt.Color;
import java.math.BigDecimal;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent.EventType;

public class DownloadUtil {
	/**�߳�����������Ĭ������*/
	protected static final String sizeStr = "��������ȷ���߳�����" ;
	/**����·��������Ĭ������*/
	protected static final String pathStr = "��������ȷ������·��" ;
	/**
	 * �����ļ�����·��
	 * @param fileChooser Ŀ¼�ļ�ѡ����
	 * @return
	 */
	protected static void setSavePath(JFileChooser fileChooser){
		String path = "";
		if (fileChooser != null){
			int result = fileChooser.showSaveDialog(null);
			//·��ȷ��
			if (JFileChooser.APPROVE_OPTION == result) {
				path = fileChooser.getSelectedFile().getPath();
				DownloadThread.File_Save_Path = path ;
				DownloadWindow.savePathTextField.setText(DownloadThread.File_Save_Path);
			}
		}
	}
	/**
	 * �������������
	 * @param TextField ����������
	 */
	protected static void clearTextFieldContent(JTextField TextField){
		if(TextField != null){
			TextField.setText("");
		}
	}
	
	protected static void setDownloadPathAndThreadSize(JTextField locationTextField,JTextField threadNumTextField){
		//��ȡ����·��
		if(locationTextField != null ){
			String path = locationTextField.getText();
			if(path != null && !path.trim().isEmpty()){
				DownloadThread.Download_File_Path = path ;
			}else{
				locationTextField.setForeground(Color.red);
				locationTextField.setText(pathStr);
			}
		}
		//��ȡ�����߳�����
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
		//�ļ������ַ��Ĭ��ֵ���˴������ص�ַ���߳��������жϾ���
		if(!DownloadThread.Download_File_Path.trim().isEmpty() && DownloadThread.Thread_Size != 0){
			try {
				//��ɾ��
				DownloadWindow.mainJpanl.remove(DownloadWindow.processBar);
				if(DownloadWindow.processBar.getBackground().equals(Color.gray)){
					DownloadWindow.processBar.setBackground(Color.white);
					DownloadWindow.processBar.setForeground(new Color(102,205,0));
				}
				DownloadWindow.processBar.setStringPainted(true);
				DownloadWindow.noticeBoardTextField.setText("����!!!");
				//��ӽ�����
				DownloadWindow.mainJpanl.add(DownloadWindow.processBar);
				//�������ذ�ť������
				DownloadWindow.affirmDownLoadButton.setEnabled(false);
				//�������ļ���С��¼���������ļ���С��¼����Ϊ0
				DownloadThread.File_Total_Length = 0;
				DownloadThread.Download_Length = 0;
				//��������
				new DownloadThread().downloadFile(DownloadThread.Download_File_Path,DownloadThread.Thread_Size);
				System.out.println("��ʼ������---");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * ����������ǰ��ɫΪ��ɫ
	 * @param TextField
	 * @param type �¼�����
	 */
	protected static void upDateTextFieldColorToBlack(JTextField TextField,EventType type){
		if(TextField == null || type == null){
			return;
		}
		System.out.println(type);
		if(TextField.getText().equals(DownloadUtil.sizeStr) || TextField.getText().equals(DownloadUtil.pathStr)){
			//���ú�ɫǰ��ɫ
			TextField.setForeground(Color.red);
		}else{
			//���ú�ɫǰ��ɫ
			TextField.setForeground(Color.black);
		}
	}
	
	/**
	 * ��ʾ����
	 * @param processBar ����������
	 * @param fileTotalLength �����ļ���С
	 * @param downloadLength �ѽ��������ݴ�С
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
			System.out.println("�ܴ�С��"+fileTotalLength+"�����ش�С"+downloadLength+"���ȣ�"+tempProgress);
			//�����߳���ʾ����  
	        new Thread(){  
	        	//���ý�������ֵ              
	            public void run(){  
	            	if(!"%100".equals(str)){
	            		tempProcessBar.setValue(tempProgress);
	            	}
	            }  
	        }.start(); //�����������߳�  
	        //������ʾ��Ϣ  
	        if(tempProcessBar.getValue() >= 100){
				//��ʾ�������ϵ��ı�
				DownloadWindow.processBar.setStringPainted(true);
				//�������ذ�ť����
				DownloadWindow.affirmDownLoadButton.setEnabled(true);
				//14���ո��2��Tab
				DownloadWindow.noticeBoardTextField.setText("\t\t                �������");
				
				System.out.println("DownloadThread.File_Total_Length:"+DownloadThread.Download_Length+"��Download_Length��"+
				DownloadThread.File_Total_Length);
			}
		}
	}
	
	
	
	
	
}























