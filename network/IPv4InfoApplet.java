package network;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import network.IPv4;

/**
 * Applet for using the IPv4 class to gather information about IP addresses.
 * 
 * @author Markus Kwasnicki
 *
 */
public class IPv4InfoApplet extends Applet implements ActionListener {

	private static final long serialVersionUID = -1231476755693752677L;

	private JTextField firstOctet;
	private JTextField secondOctet;
	private JTextField thirdOctet;
	private JTextField fourthOctet;
	private JTextField subnetBits;
	private JButton compute;
	
	private JLabel majorInfo[];
	private JLabel subInfo[];
	
	private IPv4 ip;
	
	public void init()
	{
		setLayout(new BorderLayout());
		setSize(625, 350);
		
		firstOctet = new JTextField("255", 3);
		firstOctet.addMouseListener(new BitFieldListener());
		firstOctet.addKeyListener(new BitSeparatorListener());
		firstOctet.setFocusTraversalKeysEnabled(false);
		secondOctet = new JTextField("255", 3);
		secondOctet.addMouseListener(new BitFieldListener());
		secondOctet.addKeyListener(new BitSeparatorListener());
		secondOctet.setFocusTraversalKeysEnabled(false);
		thirdOctet = new JTextField("255", 3);
		thirdOctet.addMouseListener(new BitFieldListener());
		thirdOctet.addKeyListener(new BitSeparatorListener());
		thirdOctet.setFocusTraversalKeysEnabled(false);
		fourthOctet = new JTextField("255", 3);
		fourthOctet.addMouseListener(new BitFieldListener());
		fourthOctet.addKeyListener(new BitSeparatorListener());
		fourthOctet.setFocusTraversalKeysEnabled(false);
		subnetBits = new JTextField("32", 2);
		subnetBits.addMouseListener(new BitFieldListener());
		subnetBits.addKeyListener(new BitSeparatorListener());
		subnetBits.setFocusTraversalKeysEnabled(false);
		compute = new JButton("Compute");
		
		this.majorInfo = new JLabel[9];
		this.subInfo = new JLabel[9];
		
		compute.addActionListener(this);
		
		JPanel ipGiven = new JPanel();
		ipGiven.setBorder(BorderFactory.createTitledBorder("IP Address"));
		ipGiven.setLayout(new FlowLayout());
		ipGiven.add(firstOctet);
		ipGiven.add(new JLabel("."));
		ipGiven.add(secondOctet);
		ipGiven.add(new JLabel("."));
		ipGiven.add(thirdOctet);
		ipGiven.add(new JLabel("."));
		ipGiven.add(fourthOctet);
		ipGiven.add(new JLabel("/"));
		ipGiven.add(subnetBits);
		ipGiven.add(compute);
		
		JPanel ipInfo = new JPanel();
		ipInfo.setLayout(new GridLayout(1, 2));

		JPanel majorNet = new JPanel();
		majorNet.setBorder(BorderFactory.createTitledBorder("Major Net"));
		majorNet.setLayout(new GridLayout(9, 2, 10, 0));
		majorNet.add(new JLabel("Class/Access type:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[0] = new JLabel());
		majorNet.add(new JLabel("Subnetmask:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[1] = new JLabel());
		majorNet.add(new JLabel("Net ID:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[2] = new JLabel());
		majorNet.add(new JLabel("Broadcast Address:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[3] = new JLabel());
		majorNet.add(new JLabel("Max. Hosts available:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[4] = new JLabel());
		majorNet.add(new JLabel("First Host Address:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[5] = new JLabel());
		majorNet.add(new JLabel("Last Host Address:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[6] = new JLabel());
		majorNet.add(new JLabel("Subnet Bits used:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[7] = new JLabel());
		majorNet.add(new JLabel("Host Bits available:", JLabel.RIGHT));
		majorNet.add(this.majorInfo[8] = new JLabel());
		
		JPanel subNet = new JPanel();
		subNet.setBorder(BorderFactory.createTitledBorder("Sub Net"));
		subNet.setLayout(new GridLayout(9, 2, 10, 0));
		subNet.add(new JLabel("Address type:", JLabel.RIGHT));
		subNet.add(this.subInfo[0] = new JLabel());
		subNet.add(new JLabel("Subnetmask:", JLabel.RIGHT));
		subNet.add(this.subInfo[1] = new JLabel());
		subNet.add(new JLabel("Net ID:", JLabel.RIGHT));
		subNet.add(this.subInfo[2] = new JLabel());
		subNet.add(new JLabel("Broadcast Address:", JLabel.RIGHT));
		subNet.add(this.subInfo[3] = new JLabel());
		subNet.add(new JLabel("Max. Hosts available:", JLabel.RIGHT));
		subNet.add(this.subInfo[4] = new JLabel());
		subNet.add(new JLabel("First Host Address:", JLabel.RIGHT));
		subNet.add(this.subInfo[5] = new JLabel());
		subNet.add(new JLabel("Last Host Address:", JLabel.RIGHT));
		subNet.add(this.subInfo[6] = new JLabel());
		subNet.add(new JLabel("Subnet Bits used:", JLabel.RIGHT));
		subNet.add(this.subInfo[7] = new JLabel());
		subNet.add(new JLabel("Host Bits available:", JLabel.RIGHT));
		subNet.add(this.subInfo[8] = new JLabel());
		
		ipInfo.add(majorNet);
		ipInfo.add(subNet);
		
		add(ipGiven, BorderLayout.NORTH);
		add(ipInfo, BorderLayout.CENTER);
	}

	//@Override
	public void actionPerformed(ActionEvent arg0) {
		
		try {
			
			this.ip = new IPv4(IPv4.getIpAsIntFromHumanReadableString(
					this.firstOctet.getText() + 
					"." + 
					this.secondOctet.getText() + 
					"." + 
					this.thirdOctet.getText() + 
					"." + 
					this.fourthOctet.getText()), 
					Integer.parseInt(this.subnetBits.getText())
			);
			
			fillIpInfo();
			
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, "Invalid IP address format!");
		}
	}
	
	private void fillIpInfo()
	{
		this.majorInfo[0].setText(this.ip.getIpClass() + ", " + this.ip.getIpAccess());
		this.majorInfo[1].setText(this.ip.getMajorNetmaskAsString());
		this.majorInfo[2].setText(this.ip.getMajorNetIdAsString());
		this.majorInfo[3].setText(this.ip.getMajorBroadcastAddressAsString());
		this.majorInfo[4].setText(Integer.toString(this.ip.getNumberOfMajorHosts()));
		this.majorInfo[5].setText(this.ip.getMajorIpMinAsString());
		this.majorInfo[6].setText(this.ip.getMajorIpMaxAsString());
		this.majorInfo[7].setText(Integer.toString(this.ip.getNumberOfMajorSubnetBits()));
		this.majorInfo[8].setText(Integer.toString(this.ip.getNumberOfMajorHostBits()));

		this.subInfo[0].setText(this.ip.getKindOfIpAddressAsStringConstant());
		this.subInfo[1].setText(this.ip.getSubnetmaskAsString());
		this.subInfo[2].setText(this.ip.getSubnetIdAsString());
		this.subInfo[3].setText(this.ip.getBroadcastAddressAsString());
		this.subInfo[4].setText(Long.toString(this.ip.getNumberOfHosts()));
		this.subInfo[5].setText(this.ip.getIpMinAsString());
		this.subInfo[6].setText(this.ip.getIpMaxAsString());
		this.subInfo[7].setText(Integer.toString(this.ip.getNumberOfSubnetBits()));
		this.subInfo[8].setText(Integer.toString(this.ip.getNumberOfHostBits()));
	}

	private class BitSeparatorListener extends KeyAdapter
	{
		public void keyTyped(KeyEvent e)
		{
			if (Character.isDigit(e.getKeyChar())) {
				int bits;
				try {
					bits = Integer.parseInt(((JTextField)e.getSource()).getText());
				} catch (Exception ex) {
					bits = 0;
				}

				if (((JTextField)e.getSource()).getSelectedText() != null) {
					if (!((JTextField)e.getSource()).getText().equals(((JTextField)e.getSource()).getSelectedText())) {
						e.consume();
					}
					else if (e.getSource().equals(subnetBits) && Character.getNumericValue(e.getKeyChar()) >= 4) {
						compute.requestFocus();
					}
				}
				else if (e.getSource().equals(subnetBits)) {
					if (Character.getNumericValue(e.getKeyChar()) >= 4 && (bits * 10 + Character.getNumericValue(e.getKeyChar())) <= 32) {
						compute.requestFocus();
					}
					else if ((bits * 10 + Character.getNumericValue(e.getKeyChar())) > 32) {
						e.consume();
					}
					else if (((JTextField)e.getSource()).getText().length() >= 1) {
						compute.requestFocus();
					}
				}
				else if ((bits * 10 + Character.getNumericValue(e.getKeyChar())) >= 26 || ((JTextField)e.getSource()).getText().length() >= 2) {
					if ((bits * 10 + Character.getNumericValue(e.getKeyChar())) > 255) {
						e.consume();
					}
					else if (e.getSource().equals(firstOctet)) {
						secondOctet.requestFocus();
						secondOctet.selectAll();
					}
					else if (e.getSource().equals(secondOctet)) {
						thirdOctet.requestFocus();
						thirdOctet.selectAll();
					}
					else if (e.getSource().equals(thirdOctet)) {
						fourthOctet.requestFocus();
						fourthOctet.selectAll();
					}
					else if (e.getSource().equals(fourthOctet)) {
						subnetBits.requestFocus();
						subnetBits.selectAll();
					}
				}
			}
			else if (KeyEvent.VK_PERIOD == e.getKeyChar() && ((JTextField)e.getSource()).getText().length() != 0) {
				if (e.getSource().equals(firstOctet)) {
					secondOctet.requestFocus();
					secondOctet.selectAll();
				}
				else if (e.getSource().equals(secondOctet)) {
					thirdOctet.requestFocus();
					thirdOctet.selectAll();
				}
				else if (e.getSource().equals(thirdOctet)) {
					fourthOctet.requestFocus();
					fourthOctet.selectAll();
				}
				
				e.consume();
			}
			else if (KeyEvent.VK_SLASH == e.getKeyChar() && e.getSource().equals(fourthOctet)) {
				subnetBits.requestFocus();
				subnetBits.selectAll();
				e.consume();
			}
			else if (KeyEvent.VK_TAB == e.getKeyChar()) {
				if (e.getSource().equals(firstOctet)) {
					secondOctet.requestFocus();
					secondOctet.selectAll();
				}
				else if (e.getSource().equals(secondOctet)) {
					thirdOctet.requestFocus();
					thirdOctet.selectAll();
				}
				else if (e.getSource().equals(thirdOctet)) {
					fourthOctet.requestFocus();
					fourthOctet.selectAll();
				}
				else if (e.getSource().equals(fourthOctet)) {
					subnetBits.requestFocus();
					subnetBits.selectAll();
				}
				else if (e.getSource().equals(subnetBits)) {
					compute.requestFocus();
				}
			}
			else {
				e.consume();
			}
		}
	}
	
	private class BitFieldListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			((JTextField)e.getSource()).selectAll();
		}
	}
}
