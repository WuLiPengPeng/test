package downloader;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;

/**
 * 界面类
 * 
 * 主要注意组件添加的顺序，其中牵涉到层级关系
 * 可能添加的组件被遮挡，导致其无法显示的情况
 * @author WLP
 *
 */
public class DownloadWindow{
	/**序列版本，验证版本一致性*/
	private static final long serialVersionUID = 1L;
	/**窗口宽度*/
	private static final int JFRAME_WIDTH = 500;
	/**窗口高度*/
	private static final int JFRAME_HEIGTH = 500;
	/**进度条*/
	protected static JProgressBar processBar;  
	/**组件容器*/
	protected static JPanel mainJpanl ;
	/**确认下载按钮*/
	protected static Button affirmDownLoadButton ;
	/**目录文件选择器*/
	private JFileChooser fileChooser ;
	/**下载地址标签*/
	private JLabel downloadLabel ;
	/**开启线程数量标签*/
	private JLabel threadNumLabel;
	/**线程数量清空按钮*/
	private Button clearNumButton;
	/**下载路径清空按钮*/
	private Button clearPathButton ;
	/**选择保存路径按钮*/
	private Button selectLocationButton;
	/**路径输入栏*/
	private JTextField locationTextField;
	/**保存路径显示栏*/
	protected static JTextField savePathTextField;
	/**警告栏*/
	protected static JTextField noticeBoardTextField;
	/**线程数量输入栏*/
	private JTextField threadNumTextField;
	//		http://download.ydstatic.cn/cidian/static/7.5/20171123/YoudaoDictSetup.exe
	public DownloadWindow(){
		JFrame mainJFrame = new JFrame("欢迎使用");
		//设置窗口大小
		mainJFrame.setSize(JFRAME_WIDTH,JFRAME_HEIGTH);
	    processBar = new JProgressBar();
		//设置进度条上的字符串显示，false则不能显示  
		processBar.setStringPainted(true);
		processBar.setBackground(Color.white);
		//浅绿色进度
		processBar.setForeground(new Color(102,205,0));
		processBar.setFont(new Font(null,1,20));
		processBar.setBounds(0, 0, 500, 50);
		//组件容器
		mainJpanl = new JPanel();
		mainJpanl.setLayout(null);
		mainJpanl.setBounds(200,200, 400, 400);
        
		//下载地址标签
		downloadLabel = new JLabel("下载文件地址:");
		downloadLabel.setLayout(null);
		downloadLabel.setBounds(0, 285, 100, 100);
		
		//开启线程标签
		threadNumLabel = new JLabel("开启线程数量:");
		threadNumLabel.setLayout(null);
		threadNumLabel.setBounds(0, 350, 200, 100);
		//警告栏
		noticeBoardTextField = new JTextField("警告!!!");
		noticeBoardTextField.setLayout(null);
		noticeBoardTextField.setEnabled(false);
		noticeBoardTextField.setBackground(new Color(255,64,64));
		noticeBoardTextField.setBounds(0, 50, 500, 25);
		//保存路径显示栏
		savePathTextField = new JTextField(DownloadThread.File_Save_Path);
		savePathTextField.setLayout(null);
		savePathTextField.setEnabled(false);
		savePathTextField.setBackground(new Color(150,150,150));
		savePathTextField.setBounds(90, 270, 350, 30);
		//下载地址输入栏
		locationTextField = new JTextField("请在此处输入下载地址");
		locationTextField.setLayout(null);
		locationTextField.setBounds(90, 320, 350, 30);
		
		//开启线程数量输入栏
		threadNumTextField = new JTextField("输入开启线程数量（请根据服务器性能适当添加）");
		threadNumTextField.setLayout(null);
		threadNumTextField.setBounds(90, 385, 350, 30);
		//清空内容按钮
		clearPathButton = new Button("清空");
		clearPathButton.setBounds(445, 320, 45, 30);
		clearPathButton.setBackground(Color.white);
		clearNumButton = new Button("清空");
		clearNumButton.setBounds(445, 385, 45, 30);
		clearNumButton.setBackground(Color.white);
		//确认下载按钮
		affirmDownLoadButton = new Button("开始下载");
		affirmDownLoadButton.setBounds(210, 420, 80, 40);
		affirmDownLoadButton.setFont(new Font(null,1,15));
		//路径选择按钮
		selectLocationButton = new Button("选择保存路径");
		selectLocationButton.setBounds(0, 270, 80, 30);
		//目录文件选择器
		fileChooser = new JFileChooser();
		fileChooser.setBounds(0,50,500,270);
		FileSystemView fsv = FileSystemView.getFileSystemView();
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle("请选择要上传的文件...");
		fileChooser.setApproveButtonText("确定");
		//选取文件夹
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		/**为路径选择按钮添加监听器*/
		selectLocationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DownloadUtil.setSavePath(fileChooser);
			}
		});
		/**为清空路径按钮添加监听器*/
		clearPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//清空对应输入栏内容
				DownloadUtil.clearTextFieldContent(locationTextField);
			}
		});
		/**为清空线程数量按钮添加监听器*/
		clearNumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//清空对应输入栏内容
				DownloadUtil.clearTextFieldContent(threadNumTextField);
			}
		});
		/**为确认下载按钮添加监听器*/
		affirmDownLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//对下载所需数据进行设置
				DownloadUtil.setDownloadPathAndThreadSize(locationTextField, threadNumTextField);
				System.out.println("点击确认下载了");
			}
		});
		/**为下载地址输入栏设置内容修改监听器，修改前景色*/
		locationTextField.getDocument().addDocumentListener(new DocumentListener() {
			//当修改内容时将颜色改回黑色
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void changedUpdate(DocumentEvent e) {
				DownloadUtil.upDateTextFieldColorToBlack(locationTextField,e.getType());
			}
		});
		/**为线程数量输入栏设置内容修改监听器，修改前景色*/
		threadNumTextField.getDocument().addDocumentListener(new DocumentListener() {
			//当修改内容时将颜色改回黑色
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			public void changedUpdate(DocumentEvent e) {
				DownloadUtil.upDateTextFieldColorToBlack(threadNumTextField, e.getType());
			}
		});
		//添加警告栏
		mainJpanl.add(noticeBoardTextField);
		//添加清空按钮
		mainJpanl.add(clearPathButton);
		mainJpanl.add(clearNumButton);
		//添加确认下载按钮
		mainJpanl.add(affirmDownLoadButton);
		//添加路径选择按钮
		mainJpanl.add(selectLocationButton);
		//添加输入栏
		mainJpanl.add(savePathTextField);
		mainJpanl.add(locationTextField);
		mainJpanl.add(threadNumTextField);
		//添加标签
		mainJpanl.add(downloadLabel);
		mainJpanl.add(threadNumLabel);
		//设置背景图片
//		ImageIcon bgImg = new ImageIcon(ClassLoader.getSystemResource("imgsa/bg.jpg"));
		
		//（图片资源和class文件同一目录才行）将图片打包到jar包，获取资源路径时，可以获取当前类的路径作为参考，再填写资源相对路径即可
		java.net.URL resource = DownloadWindow.class.getResource("images/bg.jpg");
		System.out.println(resource.toString());
		ImageIcon bgImg = new ImageIcon(resource);
		//将背景图放在标签里。  
        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(bgImg);
        imgLabel.setBounds(0,0,bgImg.getIconWidth(), bgImg.getIconHeight());
        mainJpanl.add(imgLabel);
		
		mainJFrame.add(mainJpanl);
		//设置窗口可见
		mainJFrame.setVisible(true);
		//设置窗口不可变换大小
		mainJFrame.setResizable(false);
		//设置窗口居中显示
		mainJFrame.setLocationRelativeTo(null);
		//关闭窗口时退出
		mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
