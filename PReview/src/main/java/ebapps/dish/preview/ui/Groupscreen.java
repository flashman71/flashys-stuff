package ebapps.dish.preview.ui;

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
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ebapps.dish.preview.configuration.Constants;
import ebapps.dish.preview.database.DbFunctions;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;

import java.awt.Component;

public class Groupscreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3689156319205452802L;
	private JPanel contentPane;
	private JTextField txtGroupName;
    private DbFunctions dbFunc;
    private long lGroupId = 0;
    private JTable jtabAvail;
    private JTable jtabAsg;
    private JComboBox<String> jcbGroups;
    
    private JButton btnAssign;
    private JButton btnRemove;
    
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public Groupscreen(DbFunctions _dbFunc) {
		setTitle("Group Maintenance");
		dbFunc = _dbFunc;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 513, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("Review Analysis Tool");
		label.setFont(new Font("Dialog", Font.BOLD, 30));
		
		JLabel lblGroupMaintenance = new JLabel("Group Maintenance");
		
		JLabel lblGroupName = new JLabel("Group Name");
		
		txtGroupName = new JTextField();
		txtGroupName.setColumns(10);
		
		JLabel lblAvailableUsers = new JLabel("Available Users");
		
		JLabel lblNewLabel = new JLabel("Assigned Users");
		
		btnAssign = new JButton("Assign");
		btnRemove = new JButton("Remove");
		
		JButton btnAddGroup = new JButton("Add Group");
		btnAddGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtGroupName.getText().toString().length() > 0)
				{
				  lGroupId = dbFunc.addGroup(txtGroupName.getText().toString());
					if (lGroupId > 0)
					{
						jcbGroups.addItem(txtGroupName.getText().toString());
						txtGroupName.setText("");
					}
				}
			}
		});
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JLabel lblSelectGroup = new JLabel("Select Group");
		
		jcbGroups = new JComboBox<String>();
		jcbGroups.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!jcbGroups.getSelectedItem().toString().equals(Constants.S_DEFAULT_LIST_VALUE)){
					loadTables();
					disableAssign();
					disableRemove();
				}
				else
				{
					disableControls();
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnAssign, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
								.addComponent(btnRemove, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addContainerGap()
									.addComponent(lblGroupName)
									.addGap(18))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addContainerGap()
									.addComponent(lblSelectGroup)
									.addGap(16)))
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(txtGroupName, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
								.addComponent(jcbGroups, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))))
					.addGap(6)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnAddGroup)
							.addGap(26)
							.addComponent(btnExit))
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
					.addGap(232))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(115)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblGroupMaintenance)
							.addGap(105)))
					.addContainerGap(220, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(38)
					.addComponent(lblAvailableUsers)
					.addGap(175)
					.addComponent(lblNewLabel)
					.addGap(282))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblGroupMaintenance)
					.addGap(47)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtGroupName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGroupName)
						.addComponent(btnAddGroup)
						.addComponent(btnExit))
					.addGap(12)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectGroup)
						.addComponent(jcbGroups, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(31)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblAvailableUsers))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(75)
							.addComponent(btnAssign)
							.addGap(18)
							.addComponent(btnRemove)))
					.addContainerGap(39, Short.MAX_VALUE))
		);
		gl_contentPane.linkSize(SwingConstants.VERTICAL, new Component[] {scrollPane, scrollPane_1});
		gl_contentPane.linkSize(SwingConstants.HORIZONTAL, new Component[] {scrollPane, scrollPane_1});
		
		jtabAsg = new JTable();
		jtabAsg.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jtabAsg.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Username"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane_1.setViewportView(jtabAsg);
		
		jtabAvail = new JTable();
		jtabAvail.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jtabAvail.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Username"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(jtabAvail);
		contentPane.setLayout(gl_contentPane);
		
		// Available Users table
		ListSelectionModel rowSM = jtabAvail.getSelectionModel();

	    //Listener for client row change;
	    rowSM.addListSelectionListener(new ListSelectionListener() {

	        /// Fill the form values when a row is selected in the JTable
			public void valueChanged(ListSelectionEvent e) {
			      ListSelectionModel lsmData = (ListSelectionModel)e.getSource();
	               if (!lsmData.isSelectionEmpty())
	               {
	                    enableAssign();
	                    disableRemove();
	               }   
	            }
	    });
		

	    // Assigned Users table
		ListSelectionModel rowAsg = jtabAsg.getSelectionModel();

	    //Listener for client row change;
	    rowAsg.addListSelectionListener(new ListSelectionListener() {

	        /// Fill the form values when a row is selected in the JTable
			public void valueChanged(ListSelectionEvent e) {
			      ListSelectionModel lsmAsg = (ListSelectionModel)e.getSource();
	               if (!lsmAsg.isSelectionEmpty())
	               {
                        enableRemove();
	                    disableAssign();
	               }   
	            }
	    });

		// Assign users, move to Assigned Users table, remove from Available Users	    
		btnAssign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] iRows = jtabAvail.getSelectedRows();
				DefaultTableModel jtabAvailModel = (DefaultTableModel) jtabAvail.getModel();
				
				//Add to the assigned users table
				for (int i=0; i<iRows.length;i++)
			  {  
					iRows[i] = jtabAvail.convertRowIndexToModel(iRows[i]);
					addToAssignTable(jtabAvail.getValueAt(iRows[i], 0).toString());
					if (!dbFunc.addUserToGroups(jtabAvail.getValueAt(iRows[i], 0).toString(), jcbGroups.getSelectedItem().toString()).equals(Constants.S_PROC_SUCCESS))
							{
						      JOptionPane.showMessageDialog(null, Constants.S_GROUP_ADD_FAILED_MESSAGE);
							}
			  }
				
				//Remove from the available users table
				for (int j=iRows.length-1;j>=0;j--)
				{
					iRows[j] = jtabAvail.convertRowIndexToModel(iRows[j]);
					jtabAvailModel.removeRow(iRows[j]);
				}
				 disableAssign();
				 disableRemove();
			}
		});
	    
		// Unassign users, move to Available Users table, remove from Assigned Users
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] iRows = jtabAsg.getSelectedRows();
				DefaultTableModel jtabAsgModel = (DefaultTableModel) jtabAsg.getModel();
			
				//Add to the assigned users table
				for (int i=0; i<iRows.length;i++)
			  {  
					iRows[i] = jtabAsg.convertRowIndexToModel(iRows[i]);
					addToAvailableTable(jtabAsg.getValueAt(iRows[i], 0).toString());
					if (!dbFunc.removeUserFromGroups(jtabAsg.getValueAt(iRows[i], 0).toString(), jcbGroups.getSelectedItem().toString()).equals(Constants.S_PROC_SUCCESS))
							{
						      JOptionPane.showMessageDialog(null, Constants.S_GROUP_DELETE_FAILED_MESSAGE);
							}
			  }
				
				//Remove from the available users table
				for (int j=iRows.length-1;j>=0;j--)
				{
					iRows[j] = jtabAvail.convertRowIndexToModel(iRows[j]);
					jtabAsgModel.removeRow(iRows[j]);
				}
				 disableAssign();
				 disableRemove();
			}
		});
		
		loadGroups();
		disableControls();
	}
	
	private void enableAssign()
	{
		btnAssign.setEnabled(true);
	}
	
	private void disableAssign()
	{
		btnAssign.setEnabled(false);
	}
	
	private void enableRemove()
	{
		btnRemove.setEnabled(true);
	}

	private void disableRemove()
	{
		btnRemove.setEnabled(false);
	}
	
	@SuppressWarnings("serial")
	private void disableControls()
	{
	   btnAssign.setEnabled(false);
	   btnRemove.setEnabled(false);
		
		jtabAvail.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Username"
				}) {
				public boolean isCellEditable(int row, int column) {
					return false; 
				}}
			);
		
		jtabAvail.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Username"
				}) {
				public boolean isCellEditable(int row, int column) {
					return false; 
				}}
			);
	}
	
	private void loadGroups()
	{
	    ResultSet rsData = dbFunc.getGroups();
	    jcbGroups.addItem(Constants.S_DEFAULT_LIST_VALUE);
	    jcbGroups.setSelectedItem(Constants.S_DEFAULT_LIST_VALUE);
	    try {
	    	while (rsData.next())
	    	{
	    		jcbGroups.addItem(rsData.getString(1));
	    	}
	    }
	    catch (SQLException e)
	    {
	    	System.out.println("DEBUG->loadGroups, SQLException(3)");
	    	e.printStackTrace();
	    }
	}
	
	@SuppressWarnings("serial")
	private void loadTables()
	{
		
	//Available Users table
       DefaultTableModel jtabAvailModel = null;
		
		jtabAvail.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Username"
				}) {
				public boolean isCellEditable(int row, int column) {
					return false; 
				}}
			);	
		
		jtabAvailModel = (DefaultTableModel) jtabAvail.getModel();
		
		ResultSet rsData = dbFunc.getAvailableUsers(Constants.S_AVAILABLE_USERS_TYPE,jcbGroups.getSelectedItem().toString(),Constants.S_YES);
		try {
			while (rsData.next())
			{
				String sValue = rsData.getString(1);
				if (null!=sValue&&sValue.length()>0)
				{
				  jtabAvailModel.addRow(new Object[]{sValue});
				}
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadTables, SQLException(1)");
			e.printStackTrace();
		}
		
	     
		//Assigned Users table
		DefaultTableModel jtabAsgModel = null;
			
	       jtabAsg.setModel(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
							"Username"
					}) {
					public boolean isCellEditable(int row, int column) {
						return false; 
					}}
				);	

			jtabAsgModel = (DefaultTableModel) jtabAsg.getModel();
		
		rsData = dbFunc.getAvailableUsers(Constants.S_ASSIGNED_USERS_TYPE, jcbGroups.getSelectedItem().toString(),Constants.S_YES);
		try {
			while (rsData.next())
			{
				String sValue = rsData.getString(1);
				if (null!=sValue&&sValue.length()>0)
				{
				  jtabAsgModel.addRow(new Object[]{sValue});
				}
			}
		} catch (SQLException e) {
			System.out.println("DEBUG->loadTables, SQLException(2)");
			e.printStackTrace();
		}
		
	}
	
	private void addToAssignTable(String _sUsername)
	{
		DefaultTableModel jtabModel = (DefaultTableModel) jtabAsg.getModel();
	       jtabModel.addRow(new Object[]{_sUsername});
	}
	
	private void addToAvailableTable(String _sUsername)
	{
		DefaultTableModel jtabModel = (DefaultTableModel) jtabAvail.getModel();
	       jtabModel.addRow(new Object[]{_sUsername});
	}
}
