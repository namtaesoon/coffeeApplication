package kr.java.coffee.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
//import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.ibatis.exceptions.PersistenceException;

import kr.java.coffee.dto.Product;
import kr.java.coffee.service.ProductService;
import kr.java.swinglibrary.component.AbstractTablePanel;
import kr.java.swinglibrary.component.InputTextField;

@SuppressWarnings("serial")
public class RegProductUI extends JFrame implements ActionListener {
	private static final RegProductUI instance = new RegProductUI();

	private JPanel contentPane;
	private AbstractTablePanel pdtTable;
	private InputTextField pCode;
	private InputTextField pName;
	private InputTextField pPrice;
	private JButton btnAdd;
	
	public static RegProductUI getInstance() {
		return instance;
	}

	private RegProductUI() {
		initComponents();
	}

	private void initComponents() {
		setTitle("���� �깅�");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 271);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 10));

		pCode = new InputTextField();
		pCode.setLblValue("����肄���");
		contentPane.add(pCode);

		pName = new InputTextField();
		pName.setLblValue("����紐�");
		contentPane.add(pName);

		pPrice = new InputTextField();
		pPrice.setLblValue("�����④�");
		contentPane.add(pPrice);

		JPanel pBtn = new JPanel();
		contentPane.add(pBtn);
		pBtn.setLayout(new GridLayout(0, 2, 0, 0));

		btnAdd = new JButton("�깅�");
		btnAdd.addActionListener(this);
		pBtn.add(btnAdd);

		JButton btnCancel = new JButton("痍⑥��");
		btnCancel.addActionListener(this);
		pBtn.add(btnCancel);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("�깅�")) {
			Product product = null;
			try {
				product = getProduct();
				ProductService.getInstance().insertProduct(product);
				pdtTable.addRow(product);
			} catch(PersistenceException e1) {
				JOptionPane.showMessageDialog(null, "���� 肄��� 以�蹂�");
				return;
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null,"e1"+e1 +"\n"+ e1.getMessage());
				return;
			}
		}
		if (e.getActionCommand().equals("����")) {
			Product product = null;
			try {
				product = getProduct();
				ProductService.getInstance().updateProduct(product);
				pdtTable.updateRow(product);
				notifyObservers();
				dispose();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				return;
			}
		}
		
		if (e.getActionCommand().equals("痍⑥��")) {
			if (btnAdd.getText().equals("����")) {
				dispose();
			}
		}
		clearValue();
	}

	public void clearValue() {
		pCode.setTfValue("");
		pName.setTfValue("");
		pPrice.setTfValue("");
		btnAdd.setText("�깅�");
		setTitle("���� �깅�");
		enableCodeTf(true);
	}

	public void setProduct(Product product) {
		pCode.setTfValue(product.getCode());
		pName.setTfValue(product.getName());
		pPrice.setTfValue(String.valueOf(product.getPrice()));
		btnAdd.setText("����");
	}

	private Product getProduct() throws Exception {
		isEmptyValue();
		isValidCheck();
		String code = pCode.getTfValue();
		String name = pName.getTfValue();
		int price = Integer.parseInt(pPrice.getTfValue());
		return new Product(code, name, price);
	}

	private void isValidCheck() throws Exception {
		pCode.isValidCheck("[A-Z][0-9]{3}", "泥リ����� A-Z �レ�� 3��由щ� 媛���");
		pPrice.isValidCheck("[0-9]{3,8}", "���� 3��由� �댁�� ~ 8��由щ� 媛���");
	}

	private void isEmptyValue() throws Exception {
		pCode.isEmptyCheck();
		pName.isEmptyCheck();
		pPrice.isEmptyCheck();
	}

	public void setTable(AbstractTablePanel pdtTable) {
		this.pdtTable = pdtTable;
	}

	public void enableCodeTf(boolean isEnable) {
		pCode.setEditableTf(isEnable);
	}
	
	private List<Observer>observers = new ArrayList<Observer>() ;
	
	public void attach(Observer observer) { // �듭��踰� 利� �듬낫 ������ 異�媛���
		observers.add(observer) ;
	}	
	
	public void detach(Observer observer) { // �듭��踰� 利� �듬낫 ������ ��嫄고��
		observers.remove(observer) ;
	}
	

	public void notifyObservers() {
		for ( Observer o : observers ) o.update() ;
	} 
	
}
