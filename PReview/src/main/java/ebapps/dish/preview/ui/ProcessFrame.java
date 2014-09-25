package ebapps.dish.preview.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.configuration.Defectdata;
import ebapps.dish.preview.configuration.Reviewdata;
import ebapps.dish.preview.configuration.Values;
import ebapps.dish.preview.database.DbFunctions;
import ebapps.dish.preview.util.Tools;

public class ProcessFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private DbFunctions dbFunc;
	private Reviewdata  rData;
	private Defectdata defectData;
	private JTextField txtDefDesc;
	private JTable jtabDefects;
	private JTextField txtDefNote;
	private final JTextPane tpNewNote;
	private final JTextPane tpRevHistory;
	private final JTextPane tpDefHistory;
	private int iRow = -1;
	private int iLoad = 0;
	private boolean bDefectSelected = false;
	
	private JButton btnAddNote;
	private JButton btnAddDefect;
	private JButton btnAddDefectNote;
	private JButton btnSave;
	private JButton btnReplace;
	private JButton btnClear;
	
	private JComboBox<String> jcbDefType;
	private JComboBox<String> jcbDefSev;
	private JComboBox<String> jcbDefStatus;
	private JComboBox<String> jcbDefCond;
	private JComboBox<String> jcbRevStatus;		
	private JComboBox<String> jcbRevCond;
	private JComboBox<String> jcbAssign;
	
	/**
	 * Create the frame.
	 */
	public ProcessFrame(DbFunctions _dbFunc, Reviewdata _rData) {
		setTitle("Process Review");
		
		dbFunc = _dbFunc;
		rData  = _rData;
		iLoad = 0;

		String sReviewInfo = "Review ID: " + rData.getiId() + "   " + rData.getsArtifactType() + "-" + rData.getsArtifactId();
		String sUserInfo = "Logged in as: " + Values.getsUsername() + "  -- Privileges: " + Values.getsPrivs();
		
		setBounds(100, 100, 887, 787);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("Review Analysis Tool");
		label.setFont(new Font("Papyrus", Font.BOLD, 30));
		
		JLabel lblProcessReview = new JLabel("Process Review");
		
		JLabel lblReviewInfo = new JLabel("");
		lblReviewInfo.setText(sReviewInfo);
		
		JLabel lblUserInfo = new JLabel("");
		lblUserInfo.setText(sUserInfo);
		
		JLabel lblHistory = new JLabel("History");
		
		JTabbedPane tabMain = new JTabbedPane(JTabbedPane.TOP);
		
		tpNewNote = new JTextPane();
		
		btnAddNote = new JButton("Add Note");
		btnAddNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyledDocument sdNewNote = tpNewNote.getStyledDocument();
				try {
					
					String sNote = sdNewNote.getText(0, sdNewNote.getLength());
					
					long lReturn = dbFunc.addReviewNote(rData.getiId(), Values.getId(), sNote);
					if (lReturn > 0)
					 {
						sNote += "\n";
					 appendToHistory(sNote);
					  tpNewNote.setText("");
					 }
					
				} catch (BadLocationException e1) {
					System.out.println("DEBUG->Add Note, BadLocationException");
					e1.printStackTrace();
				}
			}
		});
		
		JLabel lblStatus_1 = new JLabel("Status");
		JLabel lblNewLabel = new JLabel("Review Condition");
		
		jcbRevStatus = new JComboBox<String>();		
		jcbRevStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iLoad==1)
				{
				if (dbFunc.isTransitionValid(Constants.S_PEER_REVIEW_STATUS_LIST,rData.getsStatus(),jcbRevStatus.getSelectedItem().toString()) < 1)
				{
					JOptionPane.showMessageDialog(null, Constants.S_INVALID_TRANSITION);
					 jcbRevStatus.setSelectedItem(rData.getsStatus());
				}
				else
				{
						rData.setsStatus(jcbRevStatus.getSelectedItem().toString());	
				}
				}			
			}
		});
		
		jcbRevCond = new JComboBox<String>();
		jcbRevCond.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sCond = jcbRevCond.getSelectedItem().toString();
				if (sCond.equals(Constants.S_PEER_REVIEW_SUCCESS_CONDITION) && iLoad==1)
				{
					//Check for open defects
					if (dbFunc.hasOpenDefects(rData.getiId()))
					{
						JOptionPane.showMessageDialog(null, Constants.S_OPEN_DEFECTS);
						jcbRevCond.setSelectedItem(Constants.S_DEFAULT_REV_CONDITION);
					}
					else
					{
						if (jcbRevStatus.getSelectedItem().toString().equals(Constants.S_TRANS_REQUIRES_GROUP) && jcbAssign.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
						{
							JOptionPane.showMessageDialog(null,Constants.S_INVALID_GROUP);
						}
						else
						{
						dbFunc.updateReview(rData.getiId(), jcbRevStatus.getSelectedItem().toString(), jcbRevCond.getSelectedItem().toString(), jcbAssign.getSelectedItem().toString());
						}
					}
				}
			}
		});
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jcbRevStatus.getSelectedItem().toString().equals(Constants.S_TRANS_REQUIRES_GROUP) && jcbAssign.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
				{
					JOptionPane.showMessageDialog(null,Constants.S_INVALID_GROUP);
				}
				else
				{
				dbFunc.updateReview(rData.getiId(),jcbRevStatus.getSelectedItem().toString(),jcbRevCond.getSelectedItem().toString(),jcbAssign.getSelectedItem().toString());
				StringBuffer sBuffer = new StringBuffer();
				sBuffer.append(Constants.S_HISTORY_BASE);
				sBuffer.append("Peer Review " + rData.getiId() + " updated");
				sBuffer.append("User: " + Values.getsUsername());
				sBuffer.append("Date: " + Tools.timeStamp());
				appendToHistory(sBuffer.toString());
				 setReviewControls();
				 setDefectControls();
				}
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JLabel lblAssignTo = new JLabel("Assign To");
		
		jcbAssign = new JComboBox<String>();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(37)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(8)
											.addComponent(tpNewNote, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(btnAddNote))
												.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
													.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
													.addComponent(btnSave)
													.addGap(11))))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(lblStatus_1)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(jcbRevStatus, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(lblAssignTo)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(jcbAssign, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(27)
											.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 365, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblNewLabel)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(jcbRevCond, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblHistory))))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblReviewInfo, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
									.addGap(73)
									.addComponent(lblUserInfo, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(30)
							.addComponent(tabMain, GroupLayout.PREFERRED_SIZE, 812, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(163)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(281)
							.addComponent(lblProcessReview)))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblProcessReview)
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReviewInfo)
						.addComponent(lblUserInfo))
					.addGap(7)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblStatus_1)
								.addComponent(jcbRevStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAssignTo)
								.addComponent(jcbAssign, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(jcbRevCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel))
							.addGap(17))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblHistory)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnAddNote)
							.addGap(49)
							.addComponent(btnSave))
						.addComponent(tpNewNote, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
					.addGap(28)
					.addComponent(tabMain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		tpRevHistory = new JTextPane();
		scrollPane_1.setViewportView(tpRevHistory);
		tpRevHistory.setForeground(Color.BLACK);
		tpRevHistory.setBackground(Color.LIGHT_GRAY);
		tpRevHistory.setEditable(false);
		
		JPanel panelDefects = new JPanel();
		tabMain.addTab("Defects", null, panelDefects, null);
		
		JLabel lblDefectType = new JLabel("Defect Type");
		
    	JLabel lblSeverity = new JLabel("Severity");
			
		JLabel lblStatus = new JLabel("Status");
		
		JLabel lblCondition = new JLabel("Condition");

		JLabel lblDescription = new JLabel("Description");
		
		jcbDefType = new JComboBox<String>();
		jcbDefSev = new JComboBox<String>();
		jcbDefStatus = new JComboBox<String>();
		jcbDefCond = new JComboBox<String>();
		jcbDefCond.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jcbDefCond.getSelectedItem().toString().equals(Constants.S_DEFECT_CLOSED_VALUE))
				{
					String sDefStatus = jcbDefStatus.getSelectedItem().toString();
					for (int i=0;i<Constants.ARR_DEFECT_OPEN_STATUS_VALUES.length;i++)
					{
						if (sDefStatus.equals(Constants.ARR_DEFECT_OPEN_STATUS_VALUES[i]))
						{
							JOptionPane.showMessageDialog(null,Constants.S_DEFECT_INVALID_STATUS);
							jcbDefCond.setSelectedItem(Constants.S_DEFECT_OPEN_VALUE);
							break;
						}
					}
				}
				
			}
		});
		
		txtDefDesc = new JTextField();
		txtDefDesc.setColumns(10);
		
		txtDefNote = new JTextField();
		txtDefNote.setEnabled(false);
		txtDefNote.setColumns(10);	
		
		btnAddDefect = new JButton("Add Defect");
		btnAddDefect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isDefectFormValid())
				{
					Defectdata defectData = new Defectdata();
					defectData.setiPeerReviewId(rData.getiId());
					defectData.setiSeverity(Integer.parseInt(jcbDefSev.getSelectedItem().toString()));
					defectData.setsCond(jcbDefCond.getSelectedItem().toString());
					defectData.setsDefectDesc(txtDefDesc.getText().toString());
					defectData.setsDefectType(jcbDefType.getSelectedItem().toString());
					defectData.setsStatus(jcbDefStatus.getSelectedItem().toString());
					 long lReturn = dbFunc.addReviewDefect(defectData);
					  if (lReturn>0)
					  {
						  defectData.setiDefectId((int)lReturn);
						  JOptionPane.showMessageDialog(null, Constants.S_DEFECT_INSERTED + lReturn);
						   addToDefectTable(defectData);  
					  }
				}
			}
		});
		btnAddDefectNote = new JButton("Add Defect Note");
		btnAddDefectNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (defectData.getiDefectId() > 0)
				{
				try {
				if (dbFunc.addDefectNotes(defectData.getiDefectId(),txtDefNote.getText()) > 0)
				{
					appendToDefectHistory(txtDefNote.getText());
				}
				else
				{
					System.out.println("DEBUG->Add Defect Note, 1");
					JOptionPane.showMessageDialog(null,Constants.S_MISSING_DEFECT_NOTE);
				}
				}
				catch (NullPointerException npe)
				{
					System.out.println("DEBUG->Add Defect Note, 2");
					JOptionPane.showMessageDialog(null,Constants.S_MISSING_DEFECT_NOTE);
				}
				}
				else
				{
					JOptionPane.showMessageDialog(null,Constants.S_MISSING_DEFECT);
				}
			}
		});
		btnAddDefectNote.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblDefectNote = new JLabel("Defect Note");
			
		JLabel lblDefectHistory = new JLabel("Defect History");
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearDefectFields();
			}
		});
		
		btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				StringBuffer sBuffer = new StringBuffer();
				if (!jcbDefType.getSelectedItem().toString().equals(defectData.getsDefectType()))
				{
				  sBuffer.append("Defect Type changed, Previous Value [" + defectData.getsDefectType()  + "], New Value [" + jcbDefType.getSelectedItem().toString() + "]");
				  defectData.setsDefectType(jcbDefType.getSelectedItem().toString());
				}
				
				if (!jcbDefStatus.getSelectedItem().toString().equals(defectData.getsStatus()))
				{
					sBuffer.append("Defect Status changed, Previous Value [" + defectData.getsStatus()  + "], New Value [" + jcbDefStatus.getSelectedItem().toString() + "]");
					defectData.setsStatus(jcbDefStatus.getSelectedItem().toString());
				}
				
				if (!jcbDefCond.getSelectedItem().toString().equals(defectData.getsCond()))
				{
					sBuffer.append("Defect Condition changed, Previous Value [" + defectData.getsCond()  + "], New Value [" + jcbDefCond.getSelectedItem().toString() + "]");
				    defectData.setsCond(jcbDefCond.getSelectedItem().toString());
				}
				
				if (Integer.parseInt(jcbDefSev.getSelectedItem().toString()) != defectData.getiSeverity())
				{
					sBuffer.append("Defect Severity changed, Previous Value [" + defectData.getiSeverity()  + "], New Value [" + jcbDefSev.getSelectedItem().toString() + "]");
				    defectData.setiSeverity(Integer.parseInt(jcbDefSev.getSelectedItem().toString()));
				}
				
				long lReturn = dbFunc.addDefectNotes(defectData.getiDefectId(),sBuffer.toString());
				if (lReturn>0)
				{
					System.out.println("DEBUG->Replace, calling updateDefectNotes");
					System.out.println("DEBUG->....status: " + defectData.getsStatus());
					System.out.println("DEBUG->....cond  : " + defectData.getsCond());
					System.out.println("DEBUG->....type  : " + defectData.getsDefectType());
					dbFunc.updateDefectNotes(defectData);
					appendToDefectHistory(sBuffer.toString());
					jtabDefects.setValueAt(jcbDefType.getSelectedItem().toString(),iRow,1);
					jtabDefects.setValueAt(jcbDefSev.getSelectedItem().toString(),iRow,3);
					jtabDefects.setValueAt(jcbDefStatus.getSelectedItem().toString(),iRow,4);
					jtabDefects.setValueAt(jcbDefCond.getSelectedItem().toString(),iRow,5);					
				}
				else
				{
					JOptionPane.showMessageDialog(null,Constants.S_DEFECT_NOTE_INSERT_FAILED);
				}
				
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		GroupLayout gl_panelDefects = new GroupLayout(panelDefects);
		gl_panelDefects.setHorizontalGroup(
			gl_panelDefects.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelDefects.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_panelDefects.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 731, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelDefects.createSequentialGroup()
							.addGroup(gl_panelDefects.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDefectHistory)
								.addComponent(lblDescription)
								.addComponent(lblDefectNote))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelDefects.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelDefects.createParallelGroup(Alignment.TRAILING)
									.addComponent(txtDefDesc, 430, 430, 430)
									.addComponent(txtDefNote, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelDefects.createParallelGroup(Alignment.LEADING)
								.addComponent(btnAddDefectNote)
								.addGroup(gl_panelDefects.createSequentialGroup()
									.addComponent(btnAddDefect)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnReplace)
									.addGap(6)
									.addComponent(btnClear)))
							.addGap(63))
						.addGroup(gl_panelDefects.createSequentialGroup()
							.addComponent(lblDefectType)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jcbDefType, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblSeverity)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(jcbDefSev, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblStatus)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(jcbDefStatus, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(15)
							.addComponent(lblCondition)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jcbDefCond, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(55))
		);
		gl_panelDefects.setVerticalGroup(
			gl_panelDefects.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelDefects.createSequentialGroup()
					.addGap(40)
					.addGroup(gl_panelDefects.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDefectType)
						.addComponent(jcbDefType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSeverity)
						.addComponent(jcbDefSev, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStatus)
						.addComponent(jcbDefStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCondition)
						.addComponent(jcbDefCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addGroup(gl_panelDefects.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDescription)
						.addComponent(txtDefDesc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddDefect)
						.addComponent(btnReplace)
						.addComponent(btnClear))
					.addGap(18)
					.addGroup(gl_panelDefects.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDefectNote)
						.addComponent(btnAddDefectNote)
						.addComponent(txtDefNote, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addGap(22)
					.addGroup(gl_panelDefects.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDefectHistory)
						.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		tpDefHistory = new JTextPane();
		tpDefHistory.setForeground(Color.BLACK);
		tpDefHistory.setBackground(Color.LIGHT_GRAY);
		tpDefHistory.setEditable(false);
		scrollPane_2.setViewportView(tpDefHistory);
		
		jtabDefects = new JTable();
		jtabDefects.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Defect ID", "Type", "Description","Severity","Status","Condition"
			}) {
			public boolean isCellEditable(int row, int column) {
				return false; 
			}}
		);
		
		scrollPane.setViewportView(jtabDefects);
		panelDefects.setLayout(gl_panelDefects);
		contentPane.setLayout(gl_contentPane);
		
		ListSelectionModel rowSM = jtabDefects.getSelectionModel();

	    //Listener for client row change;
	    rowSM.addListSelectionListener(new ListSelectionListener() {

	        /// Fill the form values when a row is selected in the JTable
			public void valueChanged(ListSelectionEvent e) {
			      ListSelectionModel lsmData = (ListSelectionModel)e.getSource();
	               if (!lsmData.isSelectionEmpty())
	               {
	                   iRow = lsmData.getMinSelectionIndex();
	                   defectData = new Defectdata();
	                   defectData.setiDefectId(Integer.parseInt(jtabDefects.getValueAt(iRow, 0).toString()));
	                   jcbDefType.setSelectedItem(jtabDefects.getValueAt(iRow, 1).toString());
	                   txtDefDesc.setText(jtabDefects.getValueAt(iRow, 2).toString());
	                   jcbDefSev.setSelectedItem(jtabDefects.getValueAt(iRow, 3).toString());
	                   jcbDefStatus.setSelectedItem(jtabDefects.getValueAt(iRow, 4).toString());
	                   jcbDefCond.setSelectedItem(jtabDefects.getValueAt(iRow, 5).toString());
	                   
	                   defectData.setsDefectType(jcbDefType.getSelectedItem().toString());
	                   defectData.setsDefectDesc(txtDefDesc.getText().toString());
	                   defectData.setiSeverity(Integer.parseInt(jcbDefSev.getSelectedItem().toString()));
	                   defectData.setsStatus(jcbDefStatus.getSelectedItem().toString());
	                   defectData.setsCond(jcbDefCond.getSelectedItem().toString());
	                   bDefectSelected = true;
	            	   setReviewControls();
	            	   setDefectControls();
	            	   loadDefectHistory(defectData.getiDefectId());
	            	   btnAddDefectNote.setEnabled(true);
	               }
	               else
	               {
	            	   jtabDefects.clearSelection();
	            	   bDefectSelected = false;
	            	   setReviewControls();
	            	   setDefectControls();
	            	   btnAddDefectNote.setEnabled(false);
	               }
	            }
	    });
		
		loadLists();
		loadHistory();
		loadDefectsTable();
		
		setReviewControls();
		setDefectControls();
		
		iLoad = 1;
	}
	
	
	private void loadLists()
	{
		ResultSet rsData = dbFunc.getListValues(Constants.S_DEFECT_STATUS_LIST);
		jcbDefStatus.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbDefStatus.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
			  jcbDefStatus.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbStatus");
			e.printStackTrace();
		}
		
		rsData = dbFunc.getListValues(Constants.S_DEFECT_CONDITION_LIST);
		jcbDefCond.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbDefCond.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
				jcbDefCond.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbCond");
			e.printStackTrace();
		}
		
		rsData = dbFunc.getListValues(Constants.S_DEFECT_SEVERITY_LIST);
		jcbDefSev.addItem(String.valueOf(Constants.I_DEFAULT_LIST_VALUE));
		jcbDefSev.setSelectedItem(Constants.I_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
				jcbDefSev.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbDefSev");
			e.printStackTrace();
		}
		
		rsData = dbFunc.getListValues(Constants.S_DEFECT_TYPE_LIST);
		jcbDefType.addItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbDefType.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		try {
			while (rsData.next())
			{
				jcbDefType.addItem(rsData.getString(2));	
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbDefType");
			e.printStackTrace();
		}
		
		rsData = dbFunc.getListValues(Constants.S_PEER_REVIEW_STATUS_LIST);
		 try {
			while (rsData.next())
			 {
				 jcbRevStatus.addItem(rsData.getString(2));
			 }
			jcbRevStatus.setSelectedItem(rData.getsStatus());
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbRevStatus");
			e.printStackTrace();
		}
		 
		rsData = dbFunc.getListValues(Constants.S_PEER_REVIEW_CONDITION_LIST);
		 try {
			while (rsData.next())
			 {
				 jcbRevCond.addItem(rsData.getString(2));
			 }
			jcbRevCond.setSelectedItem(rData.getsCondition());
		} catch (SQLException e) {
			System.out.println("DEBUG->loadLists, SQLException, jcbRevCond");
			e.printStackTrace();
		}
		 
		 jcbAssign.addItem(Constants.S_DEFAULT_LIST_VALUE);
		 rsData = dbFunc.getGroups();
		 try {
				while (rsData.next())
				 {
					jcbAssign.addItem(rsData.getString(1));
				 }
				jcbAssign.setSelectedItem(rData.getsGroup());
			} catch (SQLException e) {
				System.out.println("DEBUG->loadLists, SQLException, jcbRevCond");
				e.printStackTrace();
			}
	}
	
	private void loadHistory()
	{
		String sResult = "";
		ResultSet rsHistory = dbFunc.getPeerReviewHistory(rData.getiId());
		try {
			if (rsHistory!=null)
			{
			while (rsHistory.next())
			{
				sResult = Constants.S_HISTORY_BASE;
				sResult += "\n" + "User: " + rsHistory.getString(1) + " (" + rsHistory.getString(2) + ") ";
				sResult += "\n" + "Date: " + rsHistory.getString(4) + "\n";
				sResult += "\n" + "Note: " + rsHistory.getString(3) + "\n";
				 appendToHistory(sResult);
			}
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadHistory SQLException");
			e.printStackTrace();
		}
	}
	
	private void appendToHistory(String _sNote)
	{
		StyledDocument sdHist = tpRevHistory.getStyledDocument();
		 try {
			sdHist.insertString(sdHist.getLength(), _sNote, null);
		} catch (BadLocationException e) {
			System.out.println("DEBUG->appendToHistory BadLocationException");
			e.printStackTrace();
		}
	}
	
	private void loadDefectHistory(int _iDefId)
	{
		String sResult = "";
	   ResultSet rsDefHistory = dbFunc.getDefectNotes(_iDefId);
		try {
			if (rsDefHistory!=null)
			{
			while (rsDefHistory.next())
			{
				sResult = Constants.S_HISTORY_BASE;
				sResult += "\n" + "User: " + rsDefHistory.getString(1) ;
				sResult += "\n" + "Date: " + rsDefHistory.getString(3) + "\n";
				sResult += "\n" + "Note: " + rsDefHistory.getString(2) + "\n";
				 appendToDefectHistory(sResult);
			}
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadDefectHistory SQLException");
			e.printStackTrace();
		}
	}
	
	private void appendToDefectHistory(String _sNote)
	{
		StyledDocument sdHist = tpDefHistory.getStyledDocument();
		 try {
			sdHist.insertString(sdHist.getLength(), _sNote, null);
		} catch (BadLocationException e) {
			System.out.println("DEBUG->appendToDefectHistory BadLocationException");
			e.printStackTrace();
		}
	}
	
	private void disableReviewControls()
	{
		btnAddNote.setEnabled(false);
	    btnSave.setEnabled(false);	
	    jcbRevCond.setEnabled(false);
		jcbRevStatus.setEnabled(false);	
		jcbAssign.setEnabled(false);
	}
	
	private void enableReviewControls()
	{
		btnAddNote.setEnabled(true);
	    btnSave.setEnabled(true);	
	    jcbRevCond.setEnabled(true);
		jcbRevStatus.setEnabled(true);	
		jcbAssign.setEnabled(true);
	}
	
	private void disableDefectControls()
	{
	   btnAddDefect.setEnabled(false);	
	   btnAddDefectNote.setEnabled(false);
	   jcbDefSev.setEnabled(false);		
		txtDefDesc.setEnabled(false);
		jcbDefType.setEnabled(false);
		txtDefNote.setEnabled(false);
		btnReplace.setEnabled(false);
		jcbDefCond.setEnabled(false);
		jcbDefStatus.setEnabled(false);
	}
	
	private void enableCreatorDefectControls()
	{	
		   btnAddDefect.setEnabled(false);
		   btnAddDefectNote.setEnabled(false);
		   jcbDefSev.setEnabled(true);
			jcbDefCond.setEnabled(true);
			txtDefDesc.setEnabled(true);
			jcbDefType.setEnabled(true);
			jcbDefStatus.setEnabled(true);
			txtDefNote.setEnabled(true);
			btnReplace.setEnabled(true);
	}
	
	private void enableGroupDefectControls()
	{
		   btnAddDefect.setEnabled(true);	
		   btnAddDefectNote.setEnabled(false);
		   jcbDefSev.setEnabled(true);
			jcbDefCond.setEnabled(true);
			txtDefDesc.setEnabled(true);
			jcbDefType.setEnabled(true);
			jcbDefStatus.setEnabled(true);
			txtDefNote.setEnabled(true);
			btnReplace.setEnabled(true);
	}
	
	private boolean isDefectFormValid()
	{
		if (txtDefDesc.getText().length() < 1)
		{
			JOptionPane.showMessageDialog(null, Constants.S_NO_DEFECT_DESCRIPTION);
			return false;
		}
		if (jcbDefType.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
		{
			JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + " Defect Type");
			return false;
		}
		
		if (jcbDefStatus.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
		{
			JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + " Defect Status");
			return false;
		}
				
		if (jcbDefSev.getSelectedItem().toString().equals(Constants.I_DEFAULT_LIST_VALUE))
		{
			JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + " Defect Severity");
			return false;
		}
		
		if (jcbDefCond.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE))
		{
			JOptionPane.showMessageDialog(null, Constants.S_DEFAULT_VALUE_MESSAGE + " Defect Condition");
			return false;
		}
		
		return true;
	}
	
	private void clearDefectFields()
	{
		jcbDefSev.setSelectedItem(Constants.I_DEFAULT_LIST_VALUE);
		jcbDefCond.setSelectedItem(Constants.S_DEFAULT_REV_CONDITION);
		txtDefDesc.setText("");
		jcbDefType.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		jcbDefStatus.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
		txtDefNote.setText("");
		jtabDefects.clearSelection();
		  setReviewControls();
		  setDefectControls();
	}
	
	private void addToDefectTable(Defectdata defectData)
	{
		DefaultTableModel jtabModel = (DefaultTableModel) jtabDefects.getModel();
	       jtabModel.addRow(new Object[]{defectData.getiDefectId(),defectData.getsDefectType(),defectData.getsDefectDesc(),defectData.getiSeverity(),defectData.getsStatus(),defectData.getsCond()});
	          clearDefectFields();
	}
	
	private void loadDefectsTable()
	{
        DefaultTableModel jtabModel = null;
		
		jtabDefects.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Defect ID", "Type", "Description","Severity","Status","Condition"
				}) {
				public boolean isCellEditable(int row, int column) {
					return false; 
				}}
			);	
		
        jtabModel = (DefaultTableModel) jtabDefects.getModel();
		
			ResultSet rsDefects = dbFunc.getAssociatedDefects(rData.getiId());
			try {
				if (null == rsDefects) {
					System.out.println("DEBUG->Load Defects - " + Constants.S_NO_DATA_RETURNED);
				} else

					while (rsDefects.next()) {
						jtabModel.addRow(new Object[] { rsDefects.getInt(1),
								rsDefects.getString(2), rsDefects.getString(3),
								rsDefects.getInt(4), rsDefects.getString(5),
								rsDefects.getString(6) });
					}
			} catch (SQLException e) {
				System.out.println("DEBUG->loadDefectsTable, SQLException");
				e.printStackTrace();
			}
	}
	
	private void setReviewControls()
	{
		if (isUserReviewOwner())
		{
			if (allowedToModifyReview()==1)
			{
			   enableReviewControls();	
			}
			else
			{
				disableReviewControls();
			}
		}
		else
		{
			if (dbFunc.userBelongsToGroup(Values.getsUsername(), rData.getsGroup()) ==1)
			{
				enableReviewControls();
			}
			else
			{
				disableReviewControls();
			}
		}
		
	}
	
	private int allowedToModifyReview()
	{
		int iReturn = -1;
		for (int i=0;i<Constants.ARR_REVIEW_CREATOR_ALLOWED_STATUS.length;i++)
		{
			if (rData.getsStatus().equals(Constants.ARR_REVIEW_CREATOR_ALLOWED_STATUS[i]))
			{
				iReturn = 1;
			}
		}
		return iReturn;
	}
	
	private boolean isUserReviewOwner()
	{
		if (Values.getsUsername().equals(rData.getsCreator()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void setDefectControls()
	{
		if (Values.getsUsername().equals(rData.getsCreator()))
				{
			       enableCreatorDefectControls();
				}
		else if (dbFunc.userBelongsToGroup(Values.getsUsername(), rData.getsGroup()) ==1)
			{
				enableGroupDefectControls();
			}
		else
		{
			disableDefectControls();
		}
		
		if (bDefectSelected)
		{
			btnReplace.setEnabled(true);
		}
		else
		{
			btnReplace.setEnabled(false);
		}
	}
	
}
