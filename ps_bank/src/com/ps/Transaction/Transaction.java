package com.ps.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Transaction {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps1 = null, ps2 = null, ps3 = null, ps4 = null;
		ResultSet rs1 = null, rs2 = null;
		;
		String Qry1 = "SELECT * FROM PS_BANK WHERE ACC=? AND PIN=?";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			System.out.println("Driver class loaded n registered");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?user=root&password=tiger");
			System.out.println("Database connected");
			con.setAutoCommit(false);
			// login module
			System.out.println("**********Login module**********");
			System.out.println("Enter the Account number:");
			int acc1 = sc.nextInt();
			System.out.println("Enter the pin");
			int pin1 = sc.nextInt();

			ps1 = con.prepareStatement(Qry1);
			ps1.setInt(1, acc1);
			ps1.setInt(2, pin1);
			rs1 = ps1.executeQuery();

			if (rs1.next()) {
				String name1;
				name1 = rs1.getString(2);
				double bal1;
				bal1 = rs1.getDouble(4);
				System.out.println("Hi " + name1 + ", your Account balance is Rs." + bal1);

				// transfer module
				System.out.println("********Transaction details*********");
				System.out.println("Enter the Beneficiary Account number");
				int acc2 = sc.nextInt();
				if (acc2 != acc1) {

					String Qry4 = "SELECT * FROM PS_BANK WHERE ACC=?";
					ps4 = con.prepareStatement(Qry4);
					ps4.setInt(1, acc2);
					rs2 = ps4.executeQuery();

					if (rs2.next()) {
						int accg2;
						accg2 = rs2.getInt(1);

						System.out.println("Do you like to continue the transaction?");
						String rply = sc.next();
						if (rply.equals("Y") || rply.equals("y")) {
							System.out.println("Enter the Amount:");
							double amt = sc.nextDouble();

							if (amt <= bal1) {

								System.out.println("Enter the Pin:");
								int pin2 = sc.nextInt();
								if (pin2 == pin1) {
									String Qry2 = "UPDATE PS_BANK SET BALANCE =BALANCE-? WHERE ACC=?";
									ps2 = con.prepareStatement(Qry2);
									ps2.setDouble(1, amt);
									ps2.setInt(2, acc1);
									ps2.executeUpdate();

									String Qry3 = "UPDATE PS_BANK SET BALANCE=BALANCE+? WHERE ACC=?";
									ps3 = con.prepareStatement(Qry3);
									ps3.setDouble(1, amt);
									ps3.setInt(2, acc2);
									ps3.executeUpdate();
									System.out.println("Amount has been transfered successfully");
									double bal3 = bal1 - amt;
									System.out.println("Your updated balance is Rs:" + bal3);

								}
							} else {
								System.out.println("Insufficient Balance");
							}

						} else {
							System.out.println("Transaction cancelled");
						}

					} else {
						System.out.println("Invalid beneficiery acc no");
					}
				}

				else {
					System.out.println("Can't transfer to yourself");
				}
			} else {
				System.out.println("Invalid Credentials");

			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (ps1 != null) {
				try {
					ps1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
