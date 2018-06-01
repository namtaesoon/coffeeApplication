package kr.java.coffee.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.ibatis.exceptions.PersistenceException;

import kr.java.coffee.dto.Product;
import kr.java.coffee.dto.Sale;
import kr.java.coffee.service.ProductService;
import kr.java.coffee.service.SaleService;
import kr.java.swinglibrary.component.AbstractTablePanel;
import kr.java.swinglibrary.component.InputCombo;
import kr.java.swinglibrary.component.InputTextField;

@SuppressWarnings("serial")
public class RegSaleUI extends JFrame implements ActionListener {
	private static final RegSaleUI instance = new RegSaleUI();

	public static RegSaleUI getInstance() {
		return instance;
	}

	private JPanel contentPane;
	private AbstractTablePanel saleTable;
	private JButton btnAdd;
	private InputCombo<Product> pCode;
	private InputTextField pSaleCnt;
	private InputTextField pMarginRate;
	private InputTextField pNo;

	private List<Observer>observers = new ArrayList<Observer>() ;
	
	public void attach(Observer observer) { // �듭��踰� 利� �듬낫 ������ 異�媛���
		observers.add(observer) ;
	}	
	
	public void detach(Observer observer) { // �듭��踰� 利� �듬낫 ������ ��嫄고��
		observers.remove(observer) ;
	}
	
	// �듬낫 ���� 紐⑸�, 利� observers�� 媛� �듭��踰���寃� 蹂�寃쎌�� �듬낫��
	public void notifyObservers() {
		for ( Observer o : observers ) o.update() ;
	} 

	private RegSaleUI() {
		initComponents();
	}

	private void initComponents() {
		setTitle("��留ㅽ���� �깅�");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 351);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 10));
		
		pNo = new InputTextField();
		pNo.setLblValue("踰���");
		pNo.setEditableTf(false);
		contentPane.add(pNo);

		pCode = new InputCombo<>();
		pCode.setLblValue("����肄���");
		contentPane.add(pCode);

		pSaleCnt = new InputTextField();
		pSaleCnt.setLblValue("��留ㅼ����");
		contentPane.add(pSaleCnt);

		pMarginRate = new InputTextField();
		pMarginRate.setLblValue("留�吏���");
		contentPane.add(pMarginRate);

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
			Sale sale = null;
			try {
				sale = getSale();
				SaleService.getInstance().insertSale(sale);
				saleTable.addRow(sale);
			} catch(PersistenceException e1) {
				JOptionPane.showMessageDialog(null, "���� 肄��� 以�蹂�");
				return;
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null,"e1"+e1 +"\n"+ e1.getMessage());
				return;
			}
			
		}
		if (e.getActionCommand().equals("����")) {
			Sale sale = null;
			try {
				sale = getSale();
				SaleService.getInstance().updateSale(sale);
				saleTable.updateRow(sale);
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
		notifyObservers();
	}

	public void getProductLoad() {
		List<Product> list = ProductService.getInstance().selectProductAll();
		pCode.setItems(list);
		getNextNo();
	}

	private void getNextNo() {
		int nextNo = SaleService.getInstance().selectSaleByAll().size() + 1;
		pNo.setTfValue(String.valueOf(nextNo));
	
	}
	
	public void clearValue() {
		pCode.setSelectedItem(null);
		pSaleCnt.setTfValue("");
		pMarginRate.setTfValue("");
		btnAdd.setText("�깅�");
		setTitle("��留ㅽ���� �깅�");
		getNextNo();
	}

	public void setSale(Sale sale) {
		pNo.setTfValue(String.valueOf(sale.getNo()));
		pCode.setSelectedItem(sale.getProduct());
		pSaleCnt.setTfValue(String.valueOf(sale.getSaleCnt()));
		pMarginRate.setTfValue(String.valueOf(sale.getMarginRate()));
		btnAdd.setText("����");
	}

	private Sale getSale() throws Exception {
		isEmptyValue();
		isValidCheck();
		int no = Integer.parseInt(pNo.getTfValue());
		Product product = (Product) pCode.getSelectedItem();
		int saleCnt = Integer.parseInt(pSaleCnt.getTfValue());
		int marginRate = Integer.parseInt(pMarginRate.getTfValue());
		return new Sale(no, product, saleCnt, marginRate);
	}

	private void isValidCheck() throws Exception {
		pSaleCnt.isValidCheck("[0-9]{1,8}", "���� 8��由ъ�댄��留� 媛���" );
		pMarginRate.isValidCheck("[0-9]{1,2}", "���� 2��由� �댄��留�  媛���(�� : 留�吏��⑥�� 20%�대㈃ 20 ����)");
	}

	private void isEmptyValue() throws Exception {
		pSaleCnt.isEmptyCheck();
		pMarginRate.isEmptyCheck();
	}

	public void setTable(AbstractTablePanel saleTable) {
		this.saleTable = saleTable;
	}

	public void enableCodeTf(boolean isEnable) {
		throw new UnsupportedOperationException(); 
	}
}
