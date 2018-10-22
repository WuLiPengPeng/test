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
 * ������
 * 
 * ��Ҫע�������ӵ�˳������ǣ�浽�㼶��ϵ
 * ������ӵ�������ڵ����������޷���ʾ�����
 * @author WLP
 *
 */
public class DownloadWindow{
	/**���а汾����֤�汾һ����*/
	private static final long serialVersionUID = 1L;
	/**���ڿ��*/
	private static final int JFRAME_WIDTH = 500;
	/**���ڸ߶�*/
	private static final int JFRAME_HEIGTH = 500;
	/**������*/
	protected static JProgressBar processBar;  
	/**�������*/
	protected static JPanel mainJpanl ;
	/**ȷ�����ذ�ť*/
	protected static Button affirmDownLoadButton ;
	/**Ŀ¼�ļ�ѡ����*/
	private JFileChooser fileChooser ;
	/**���ص�ַ��ǩ*/
	private JLabel downloadLabel ;
	/**�����߳�������ǩ*/
	private JLabel threadNumLabel;
	/**�߳�������հ�ť*/
	private Button clearNumButton;
	/**����·����հ�ť*/
	private Button clearPathButton ;
	/**ѡ�񱣴�·����ť*/
	private Button selectLocationButton;
	/**·��������*/
	private JTextField locationTextField;
	/**����·����ʾ��*/
	protected static JTextField savePathTextField;
	/**������*/
	protected static JTextField noticeBoardTextField;
	/**�߳�����������*/
	private JTextField threadNumTextField;
	//		http://download.ydstatic.cn/cidian/static/7.5/20171123/YoudaoDictSetup.exe
	public DownloadWindow(){
		JFrame mainJFrame = new JFrame("��ӭʹ��");
		//���ô��ڴ�С
		mainJFrame.setSize(JFRAME_WIDTH,JFRAME_HEIGTH);
	    processBar = new JProgressBar();
		//���ý������ϵ��ַ�����ʾ��false������ʾ  
		processBar.setStringPainted(true);
		processBar.setBackground(Color.white);
		//ǳ��ɫ����
		processBar.setForeground(new Color(102,205,0));
		processBar.setFont(new Font(null,1,20));
		processBar.setBounds(0, 0, 500, 50);
		//�������
		mainJpanl = new JPanel();
		mainJpanl.setLayout(null);
		mainJpanl.setBounds(200,200, 400, 400);
        
		//���ص�ַ��ǩ
		downloadLabel = new JLabel("�����ļ���ַ:");
		downloadLabel.setLayout(null);
		downloadLabel.setBounds(0, 285, 100, 100);
		
		//�����̱߳�ǩ
		threadNumLabel = new JLabel("�����߳�����:");
		threadNumLabel.setLayout(null);
		threadNumLabel.setBounds(0, 350, 200, 100);
		//������
		noticeBoardTextField = new JTextField("����!!!");
		noticeBoardTextField.setLayout(null);
		noticeBoardTextField.setEnabled(false);
		noticeBoardTextField.setBackground(new Color(255,64,64));
		noticeBoardTextField.setBounds(0, 50, 500, 25);
		//����·����ʾ��
		savePathTextField = new JTextField(DownloadThread.File_Save_Path);
		savePathTextField.setLayout(null);
		savePathTextField.setEnabled(false);
		savePathTextField.setBackground(new Color(150,150,150));
		savePathTextField.setBounds(90, 270, 350, 30);
		//���ص�ַ������
		locationTextField = new JTextField("���ڴ˴��������ص�ַ");
		locationTextField.setLayout(null);
		locationTextField.setBounds(90, 320, 350, 30);
		
		//�����߳�����������
		threadNumTextField = new JTextField("���뿪���߳�����������ݷ����������ʵ���ӣ�");
		threadNumTextField.setLayout(null);
		threadNumTextField.setBounds(90, 385, 350, 30);
		//������ݰ�ť
		clearPathButton = new Button("���");
		clearPathButton.setBounds(445, 320, 45, 30);
		clearPathButton.setBackground(Color.white);
		clearNumButton = new Button("���");
		clearNumButton.setBounds(445, 385, 45, 30);
		clearNumButton.setBackground(Color.white);
		//ȷ�����ذ�ť
		affirmDownLoadButton = new Button("��ʼ����");
		affirmDownLoadButton.setBounds(210, 420, 80, 40);
		affirmDownLoadButton.setFont(new Font(null,1,15));
		//·��ѡ��ť
		selectLocationButton = new Button("ѡ�񱣴�·��");
		selectLocationButton.setBounds(0, 270, 80, 30);
		//Ŀ¼�ļ�ѡ����
		fileChooser = new JFileChooser();
		fileChooser.setBounds(0,50,500,270);
		FileSystemView fsv = FileSystemView.getFileSystemView();
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle("��ѡ��Ҫ�ϴ����ļ�...");
		fileChooser.setApproveButtonText("ȷ��");
		//ѡȡ�ļ���
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		/**Ϊ·��ѡ��ť��Ӽ�����*/
		selectLocationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DownloadUtil.setSavePath(fileChooser);
			}
		});
		/**Ϊ���·����ť��Ӽ�����*/
		clearPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//��ն�Ӧ����������
				DownloadUtil.clearTextFieldContent(locationTextField);
			}
		});
		/**Ϊ����߳�������ť��Ӽ�����*/
		clearNumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//��ն�Ӧ����������
				DownloadUtil.clearTextFieldContent(threadNumTextField);
			}
		});
		/**Ϊȷ�����ذ�ť��Ӽ�����*/
		affirmDownLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�������������ݽ�������
				DownloadUtil.setDownloadPathAndThreadSize(locationTextField, threadNumTextField);
				System.out.println("���ȷ��������");
			}
		});
		/**Ϊ���ص�ַ���������������޸ļ��������޸�ǰ��ɫ*/
		locationTextField.getDocument().addDocumentListener(new DocumentListener() {
			//���޸�����ʱ����ɫ�Ļغ�ɫ
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
		/**Ϊ�߳��������������������޸ļ��������޸�ǰ��ɫ*/
		threadNumTextField.getDocument().addDocumentListener(new DocumentListener() {
			//���޸�����ʱ����ɫ�Ļغ�ɫ
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
		//��Ӿ�����
		mainJpanl.add(noticeBoardTextField);
		//�����հ�ť
		mainJpanl.add(clearPathButton);
		mainJpanl.add(clearNumButton);
		//���ȷ�����ذ�ť
		mainJpanl.add(affirmDownLoadButton);
		//���·��ѡ��ť
		mainJpanl.add(selectLocationButton);
		//���������
		mainJpanl.add(savePathTextField);
		mainJpanl.add(locationTextField);
		mainJpanl.add(threadNumTextField);
		//��ӱ�ǩ
		mainJpanl.add(downloadLabel);
		mainJpanl.add(threadNumLabel);
		//���ñ���ͼƬ
//		ImageIcon bgImg = new ImageIcon(ClassLoader.getSystemResource("imgsa/bg.jpg"));
		
		//��ͼƬ��Դ��class�ļ�ͬһĿ¼���У���ͼƬ�����jar������ȡ��Դ·��ʱ�����Ի�ȡ��ǰ���·����Ϊ�ο�������д��Դ���·������
		java.net.URL resource = DownloadWindow.class.getResource("images/bg.jpg");
		System.out.println(resource.toString());
		ImageIcon bgImg = new ImageIcon(resource);
		//������ͼ���ڱ�ǩ�  
        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(bgImg);
        imgLabel.setBounds(0,0,bgImg.getIconWidth(), bgImg.getIconHeight());
        mainJpanl.add(imgLabel);
		
		mainJFrame.add(mainJpanl);
		//���ô��ڿɼ�
		mainJFrame.setVisible(true);
		//���ô��ڲ��ɱ任��С
		mainJFrame.setResizable(false);
		//���ô��ھ�����ʾ
		mainJFrame.setLocationRelativeTo(null);
		//�رմ���ʱ�˳�
		mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
